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
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.AccountMgmt;
import dutrow.sales.bl.SellerMgmt;
import dutrow.sales.bo.Account;
import dutrow.sales.bo.Address;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.bo.POC;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.DTOConversionUtil;

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
			
			AuctionDTO dto = DTOConversionUtil.convertAuctionItem(item);
			ObjectMessage message = session.createObjectMessage(dto);
			
			message.setJMSType(jmsType);
			
			message.setStringProperty("category", item.getCategory().prettyName);
			message.setBooleanProperty("open", item.isOpen());
			producer.send(message);
			log.debug("sent auction[" + message + "] = " + dto.toString());
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
	 * @param b 
	 */
	public void publishBid(Bid b, String jmsType ) {
		Connection connection = null;
		Session session = null;
		
		BidDTO dto = DTOConversionUtil.convertBid(b);
		
		try {
			connection = connFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			publishBid(session, dto, jmsType);
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
	private void publishBid(Session session, BidDTO dto,
			String jmsType) throws JMSException {
		MessageProducer producer = null;
		try {
			producer = session.createProducer(sellTopic);
			ObjectMessage message = session.createObjectMessage(dto);
			message.setJMSType(jmsType);
			producer.send(message);
			log.debug("sent bid[" + message + "] = " + dto.toString());
		} finally {
			if (producer != null) {
				producer.close();
			}
		}
		
	}

}
