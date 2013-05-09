/**
 * 
 */
package dutrow.bidbot.ejb;

import java.beans.Transient;
import java.util.Collection;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.cdi.BidbotOrderManager;
import dutrow.bidbot.dao.BidAccountDAO;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemoteException;

/**
 * @author dutroda1
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({ "ebidbot-admin", "ebidbot-user" })
// ebidbot-admin these users will be able to perform management and test
// functions on eBidbot.
// ebidbot-user these users can and manage their orders.
public class OrderMgmtEJB implements OrderMgmtRemote {
	private static final Log log = LogFactory.getLog(OrderMgmtEJB.class);

	@Resource
	protected SessionContext ctx;

	@Inject
	private BidAccountDAO dao;

	@Inject
	@BidbotOrderManager
	private OrderMgmt orderMgmt;

	final String mName = "ejb:dutrowSalesEAR/dutrowSalesEJB/BuyerMgmtEJB!dutrow.sales.ejb.BuyerMgmtRemote";
	@EJB(mappedName = mName)
	private BuyerMgmtRemote buyerManager;
	
	@EJB
	OrderMgmtHelperEJB orderMgmtHelper;
	
	@Resource
	private TimerService timerService;

	// not injected
	long checkAuctionInterval;
	
	@PostConstruct
	public void init() {
		try {
			log.info("**** OrderMgmtEJB init ****");
			log.info("timerService=" + timerService);
			
			log.info("orderManager=" + orderMgmt);
			log.info("buyerManager=" + buyerManager);
			
			
			checkAuctionInterval = 10000;
			log.info("setting checkAuctionInterval=" + checkAuctionInterval);
			
			
		} catch (Throwable ex) {
			log.warn("error in init", ex);
			throw new EJBException("error in init" + ex);
		}
	}
	

	@Override
	public void cancelTimers() {
		log.info("bidbot canceling timers");
		for (Timer timer : (Collection<Timer>) timerService.getTimers()) {
			timer.cancel();
		}
	}
	
	public void initTimers(long delay) {
		cancelTimers();
		log.info("initializing bidbot btimers, checkAuctionInterval=" + delay);
		timerService.createTimer(0, delay, "checkAuctionTimer");
	}

	public void initTimers(ScheduleExpression schedule) {
		cancelTimers();
		log.info("initializing bidbot timers, schedule=" + schedule);
		timerService.createCalendarTimer(schedule);
	}

	@Timeout
	@Transient
	@Schedule(second = "*/2", minute = "*", hour = "*", dayOfMonth = "*", month = "*", year = "*")
	public void execute(Timer timer) {
		log.info("timer fired:" + timer);
		try {
			orderMgmtHelper.checkAuction();
		} catch (Exception ex) {
			log.error("error checking auction", ex);
		}
	}

	
	protected BidResultDTO executeOrder(BidOrder order)
			throws BuyerMgmtRemoteException, NamingException {
		log.info(" **** executeOrder ****");

		if (order == null) {
			log.warn("BidOrder is null");
			return new BidResultDTO(null, "BidOrder is null");
		}

		AuctionDTO auctionDTO = buyerManager
				.getAuctionDTO(order.getAuctionId());

		if (auctionDTO == null) {
			String msg = "AuctionDTO id:" + order.getAuctionId()
					+ " returns null";
			log.warn(msg);
			return new BidResultDTO(null, msg);
		}

		if (auctionDTO.bids == null) {
			auctionDTO.bids = new TreeSet<BidDTO>();
		}

		float bidAmount = 0;
		if (auctionDTO.bids.isEmpty()) {
			bidAmount = order.getStartBid();
			if (auctionDTO.askingPrice > bidAmount
					&& auctionDTO.askingPrice <= order.getMaxBid()) {
				bidAmount = auctionDTO.askingPrice;
			}
		} else {
			BidDTO lastBid = auctionDTO.bids.last();
			if (lastBid.amount < order.getMaxBid()) {
				// place more than the previous bid
				bidAmount = Math.max(order.getStartBid(), lastBid.amount + 1);
				// place no more than your max bid
				bidAmount = Math.min(bidAmount, order.getMaxBid());
			}
		}

		BidResultDTO result = orderMgmtHelper.placeBid(order.getAuctionId(), order.getBidder(),
				bidAmount);

		return result;

	}


	@Override
	public BidResultDTO placeBid(long auctionId, BidAccount bidder,
			float bidAmount) throws BuyerMgmtRemoteException {

		BidResultDTO result = null;
		result = orderMgmtHelper.placeBid(auctionId, bidder, bidAmount);
		return result;

	}


	public long createOrder(BidOrder order) throws BuyerMgmtRemoteException,
			NamingException {
		log.info(" **** createOrder ****");
		String bidderId = order.getBidder().getUserId();
		String callerId = ctx.getCallerPrincipal().getName();
		if (bidderId.equalsIgnoreCase(callerId) == false) {
			throw new IllegalArgumentException(
					"Logged in user does not match bid order");
		}

		boolean b = orderMgmt.createOrder(order);

		if (b) {
			BidResultDTO result = executeOrder(order);
		}

		return order.getBidOrderId();
	}

	public BidOrder getOrder(long bidOrderId) {
		log.info(" **** getOrder ****");
		return orderMgmt.getOrder(bidOrderId);
	}

	public boolean endOrder(long bidOrderId) {
		log.info(" **** endOrder ****");
		return orderMgmt.endOrder(bidOrderId);
	} // complete order processing once auction has closed and note if won.

	public boolean getOrderStatus(long bidOrderId) {
		log.info(" **** getOrderStatus ****");
		return orderMgmt.getOrderStatus(bidOrderId);
	} // did user win or not

	@RolesAllowed({ "ebidbot-admin" })
	public boolean createAccount(BidAccount ba) {
		log.info(" **** createAccount(BidAccount) ****");
		return orderMgmt.createAccount(ba);
	}

	@RolesAllowed({ "ebidbot-admin" })
	public BidAccount createAccount(String userId, String accountId,
			String passwd) {
		log.info(" **** createAccount(String,String,String) ****");
		return orderMgmt.createAccount(userId, accountId, passwd);
	}

	public BidAccount getAccount(String userId) {
		log.info(" **** getAccount ****");
		return orderMgmt.getAccount(userId);
	}


	/* (non-Javadoc)
	 * @see dutrow.bidbot.ejb.OrderMgmtRemote#createOrder(long, float, float)
	 */
	@Override
	public long createOrder(long auctionid, float firstBid, float maxBid) throws BuyerMgmtRemoteException, NamingException {
		String callerId = ctx.getCallerPrincipal().getName();
		BidAccount account = orderMgmt.getAccount(callerId);
		BidOrder bo = new BidOrder(auctionid, firstBid, maxBid, account);
		return createOrder(bo);
		
	}

}
