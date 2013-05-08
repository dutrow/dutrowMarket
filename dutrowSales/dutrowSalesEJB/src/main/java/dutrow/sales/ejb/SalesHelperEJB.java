/**
 * 
 */
package dutrow.sales.ejb;

import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;
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
// esales-sys role required to perform internal auction actions like JMS
// publishing.
public class SalesHelperEJB {
	private static final Log log = LogFactory.getLog(SalesHelperEJB.class);

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
	
	@PostConstruct
	void init(){
		log.info("connFactory=" + connFactory);
		log.info("sellTopic=" + sellTopic);

	}

	public void publishAuctionItem(Session session, AuctionItem item,
			String jmsType) throws JMSException {
		MessageProducer producer = null;
		try {
			producer = session.createProducer(sellTopic);
			MapMessage message = session.createMapMessage();
			message.setJMSType(jmsType);
			message.setLong("id", item.getId());
			message.setString("action", jmsType);
			message.setString("title", item.getTitle());
			message.setString("category", item.getCategory().prettyName);
			message.setStringProperty("category", item.getCategory().prettyName);
			message.setString("seller", item.getSeller().getUserId());
			message.setLong("startTime", item.getStartTime().getTime());
			message.setLong("endTime", item.getEndTime().getTime());
			message.setFloat("askingPrice", item.getAskingPrice());
			message.setFloat("bids", item.getBids().size());
			message.setFloat("highestBid",
					(item.getHighestBid() == null ? 0.00f : item
							.getHighestBid().getAmount()));
			message.setBoolean("open", item.isOpen());
			message.setBooleanProperty("open", item.isOpen());
			producer.send(message);
			log.debug("sent auction=" + message);
		} finally {
			if (producer != null) {
				producer.close();
			}
		}
	}

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
				if (now.getTime().after(item.getEndTime())) {
					log.info("we should close");
					closeBidding(session, item);
				} else {
					log.info("we should keep the sale going");
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

	public void closeBidding(Session session, AuctionItem item ) throws SellerMgmtRemoteException, JMSException {

			publishAuctionItem(session, item, "closed");
			item.closeAuction();
			POC buyer = item.getBuyer();
			if (buyer != null) {
				sellItem(session, buyer, item);
			}

			log.debug("closed bidding for item:" + item);
		
	}

	public void sellItem(Session session, POC buyer, AuctionItem item)
			throws JMSException {
		Account acct = acctMgmt.getAccount(buyer.getUserId());
		Map<String, Address> addressMap = acct.getAddresses();
		if (addressMap != null && !addressMap.isEmpty()) {
			Address shippingAddr = acct.getAddresses().get("shipping");
			if (shippingAddr != null) {
				item.setShipTo(acct.getAddresses().get("shipping"));
			} else {
				item.setShipTo(acct.getAddresses().values().iterator().next());
			}
		}
		publishAuctionItem(session, item, "sold");
	}

	/**
	 * @param auctionitem
	 * @param string
	 */
	public void publishAuctionItem(AuctionItem auctionitem, String string) {
		Connection connection = null;
		Session session = null;
		
		try {
			connection = connFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			publishAuctionItem(session, auctionitem, string);
		}catch (JMSException ex) {
			log.error("error publishing auction item updates", ex);
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

	/**
	 * @param auctionId
	 * @param bidValue
	 * @param string
	 */
	public void publishBid(long auctionId, float bidValue, String string) {
		Connection connection = null;
		Session session = null;
		
		try {
			connection = connFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			publishBid(session, auctionId, bidValue, string);
		}catch (JMSException ex) {
			log.error("error publishing bid", ex);
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

	/**
	 * @param session
	 * @param auctionId
	 * @param bidValue
	 * @param jmsType
	 * @throws JMSException 
	 */
	private void publishBid(Session session, long auctionId, float bidValue,
			String jmsType) throws JMSException {
		MessageProducer producer = null;
		try {
			producer = session.createProducer(sellTopic);
			MapMessage message = session.createMapMessage();
			message.setJMSType(jmsType);
			message.setLong("id", auctionId);
			message.setFloat("bid", bidValue);
			producer.send(message);
			log.debug("sent bid=" + message);
		} finally {
			if (producer != null) {
				producer.close();
			}
		}
		
	}

}
