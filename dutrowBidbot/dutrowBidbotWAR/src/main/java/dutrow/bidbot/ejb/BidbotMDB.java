package dutrow.bidbot.ejb;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.cdi.BidbotOrderManager;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.ejb.BuyerMgmtRemote;

/**
 * This class will listen for market events and cause further bidding to occur.
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationTopic", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/ejava/projects/emarket/esales-action"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "JMSType in ('closed, saleUpdate')"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class BidbotMDB implements MessageListener {
	private static final Log log = LogFactory.getLog(BidbotMDB.class);

	final String mName = "ejb:dutrowSalesEAR/dutrowSalesEJB/BuyerMgmtEJB!dutrow.sales.ejb.BuyerMgmtRemote";
	@EJB(mappedName = mName)
	private BuyerMgmtRemote buyerManager;

	@Inject
	@BidbotOrderManager
	private OrderMgmt orderMgmt;

	@Resource
	private MessageDrivenContext ctx;

	@PostConstruct
	public void init() {
		log.info("*** BuyerMDB init() ***");
		log.debug("ctx=" + ctx);
		log.debug("buyer=" + buyerManager);
		log.debug("orderMgmt=" + orderMgmt);
	}

	public void onMessage(Message message) {
		try {
			log.debug("onMessage:" + message.getJMSMessageID());
			if (message.getJMSType().equalsIgnoreCase("saleUpdate")) {
				MapMessage auctionMsg = (MapMessage) message;
				long itemId = auctionMsg.getLong("id");
				String titleIn = auctionMsg.getString("title");
				String categoryIn = auctionMsg.getString("category");
				String descriptionIn = "";
				Date startTimeIn = null;
				Date endTimeIn = null;
				boolean isOpen = auctionMsg.getBoolean("open");
				float askingPriceIn = auctionMsg.getFloat("askingPrice");
				float numBids = auctionMsg.getFloat("bids");
				float highestBid = auctionMsg.getFloat("highestBid");
				String sellerIn = auctionMsg.getString("seller");
				AuctionDTO ai = new AuctionDTO(titleIn, categoryIn,
						descriptionIn, startTimeIn, endTimeIn, askingPriceIn,
						sellerIn, "", isOpen);

				processAuctionItem(ai);
			}
		} catch (Exception ex) {
			log.error("error processing message", ex);
		}
	}

	protected void processAuctionItem(AuctionDTO ai) {
		List<BidOrder> orders = null;

		orders = orderMgmt.getOrdersforItem(ai.id);
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
