/**
 * 
 */
package dutrow.sales.ejb;

import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.AccountMgmt;
import dutrow.sales.bl.SellerMgmt;
import dutrow.sales.bo.Account;
import dutrow.sales.bo.Address;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.POC;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RunAs("esales-sys")
//esales-sys	role required to perform internal auction actions like JMS publishing.
public class SellerMgmtHelper {
	private static final Log log = LogFactory.getLog(SellerMgmtHelper.class);

	@Inject
	SellerMgmt sellerMgmt;
	@Inject
	AccountMgmt acctMgmt;
	
	@Resource
	protected SessionContext ctx;

	private @EJB
	SellerMgmtActionEJB actions;

	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory connFactory;
	@Resource(mappedName = "java:/topic/ejava/projects/emarket/esales-action", type = Topic.class)
	private Destination sellTopic;

	public int checkAuction() throws SellerMgmtRemoteException {
		log.info("checking auctions");
		Connection connection = null;
		Session session = null;
		int index = 0;
		try {
			connection = connFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Collection<AuctionItem> items = null;

			items = sellerMgmt.getOpenAuctions();
			log.info("#items: " + items.size());

			Calendar now = Calendar.getInstance();
			
			for (AuctionItem item : items) {
				log.info(item.getTitle() + " ends: " + item.getEndTime());
				if (now.after(item.getEndTime())) {
					closeBidding(item);
				} else {
					publishAuctionItem(session, item, "saleUpdate");
				}
			}

			log.debug("processed " + index + " active items");
			return index;
		} catch (JMSException ex) {
			log.error("error publishing auction item updates", ex);
			return index;
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (JMSException ignored) {
			}
		}
	}
	
	public void publishAuctionItem(Session session, AuctionItem item,
			String jmsType) throws JMSException {
		MessageProducer producer = null;
		try {
			producer = session.createProducer(sellTopic);
			MapMessage message = session.createMapMessage();
			message.setJMSType(jmsType);
			message.setLong("id", item.getId());
			message.setString("title", item.getTitle());
			message.setString("category", item.getCategory().prettyName);			
			message.setString("seller", item.getSeller().getUserId());
			message.setLong("startTime", item.getStartTime().getTime());
			message.setLong("endTime", item.getEndTime().getTime());
			message.setFloat("askingPrice", item.getAskingPrice());
			message.setFloat("bids", item.getBids().size());
			message.setFloat("highestBid",
					(item.getHighestBid() == null ? 0.00f : item.getHighestBid()
							.getAmount()));
			message.setBoolean("open", item.isOpen());
			producer.send(message);
			log.debug("sent=" + message);
		} finally {
			if (producer != null) {
				producer.close();
			}
		}
	}
	

	public void closeBidding(AuctionItem item) throws SellerMgmtRemoteException {

		try {

			item.closeAuction();
			POC buyer = item.getBuyer();
			if (buyer != null) {
				Account acct = acctMgmt.getAccount(buyer.getUserId());
				Map<String, Address> addressMap = acct.getAddresses();
				if (addressMap != null && !addressMap.isEmpty()) {
					Address shippingAddr = acct.getAddresses().get("shipping");
					if (shippingAddr != null) {
						item.setShipTo(acct.getAddresses().get("shipping"));
					} else {
						item.setShipTo(acct.getAddresses().values().iterator()
								.next());
					}
				}
			}
			log.debug("closed bidding for item:" + item);
		} catch (Exception ex) {
			log.error("error closing bid", ex);
			throw new SellerMgmtRemoteException("error closing bid:" + ex);
		}
	}


	public void endAuction(long itemId) throws SellerMgmtRemoteException {
		Connection connection = null;
		Session session = null;
		try {
			AuctionItem item = sellerMgmt.getAuction(itemId);
			if (item != null) {
				item.closeAuction();
				log.info("ending auction for:" + item);

				connection = connFactory.createConnection();
				session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				publishAuctionItem(session, item, "sold");
			}
		} catch (JMSException jex) {
			log.error("error publishing jms message:", jex);
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ignored) {
			}
		}
	}


}
