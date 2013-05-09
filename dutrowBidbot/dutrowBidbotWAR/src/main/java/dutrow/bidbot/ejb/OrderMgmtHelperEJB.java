package dutrow.bidbot.ejb;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.cdi.BidbotOrderManager;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemoteException;
import dutrow.sales.ejb.SellerMgmtRemoteException;

/**
 * @author dutroda1
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RunAs("esales-trusted")
public class OrderMgmtHelperEJB {
	private static final Log log = LogFactory.getLog(OrderMgmtHelperEJB.class);

	final String mName = "ejb:dutrowSalesEAR/dutrowSalesEJB/BuyerMgmtEJB!dutrow.sales.ejb.BuyerMgmtRemote";
	@EJB(mappedName = mName)
	private BuyerMgmtRemote buyerManager;
	
	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory connFactory;
	@Resource(mappedName = "java:/topic/ejava/projects/emarket/esales-action", type = Topic.class)
	private Destination sellTopic;

	@Inject
	@BidbotOrderManager
	private OrderMgmt orderMgmt;


	public BidResultDTO placeBid(long auctionId, BidAccount bidder,
			float bidAmount) throws BuyerMgmtRemoteException {
		return buyerManager.placeBid(auctionId, bidder.getSalesAccount(), bidder.getSalesPassword(), bidAmount);
	}
	
	public int checkAuction() throws SellerMgmtRemoteException {
		log.info("bidbot checking auctions");
		Connection connection = null;
		Session session = null;
		int index = 0;
		try {
			connection = connFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Collection<AuctionDTO> items = null;

			items = buyerManager.getOpenAuctions();
			log.info("bidbot sees #items: " + items.size());

			Calendar now = Calendar.getInstance();

			for (AuctionDTO item : items) {
				log.info(item.title + " : " + item.id + " is open");
				processAuctionItem(item);
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
	
	protected void processAuctionItem(AuctionDTO ai) {
		List<BidOrder> orders = null;

		orders = orderMgmt.getOrdersForItem(ai.id);
		for (BidOrder order : orders) {
			processOrder(order, ai);
		}

	}

	protected void processOrder(BidOrder order, AuctionDTO item) {
		log.debug("processing order:" + order);
		long auctionId = order.getAuctionId();

		BidDTO highestBid = item.bids.last();
		String bidderAcct = order.getBidder().getSalesAccount();
		String bidderPasswd = order.getBidder().getSalesPassword();

		if (highestBid == null) {
			if (item.askingPrice < order.getMaxBid()) {

				buyerManager.placeBid(item.id, bidderAcct, bidderPasswd,
						item.askingPrice);
				log.debug("placed initial bid for order:" + order);
			}
		} else if (highestBid.amount < order.getMaxBid()
		// add don't bid against ourself
				&& !highestBid.poc.equals(order.getBidder().getSalesAccount())) {
			float bidAmount = Math
					.min(order.getMaxBid(), highestBid.amount + 1);
			buyerManager.placeBid(item.id, bidderAcct, bidderPasswd, bidAmount);
			log.debug("placed new bid for order:" + order);
		}
	}



}
