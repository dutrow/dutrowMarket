/**
 * 
 */
package dutrow.bidbot.ejb;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.cdi.BidbotOrderManager;
import dutrow.bidbot.cdi.BidbotOrderMgmtHelper;
import dutrow.bidbot.dao.BidAccountDAO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.BuyerMgmtRemoteException;

/**
 * @author dutroda1
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"ebidbot-admin", "ebidbot-user"})
//ebidbot-admin	these users will be able to perform management and test functions on eBidbot.
//ebidbot-user	these users can and manage their orders.
public class OrderMgmtEJB implements OrderMgmtRemote {
	private static final Log log = LogFactory.getLog(OrderMgmtEJB.class);

	@Resource
	protected SessionContext ctx;
	
	@Inject
	private BidAccountDAO dao;

	@Inject
	@BidbotOrderManager
	private OrderMgmt orderMgmt;
	
	@Inject
	@BidbotOrderMgmtHelper
	OrderMgmtHelper sysbidbot1;
	
//	@Inject
//	BuyerMgmtRemote buyerManager;

	public long createOrder(BidOrder order) throws BuyerMgmtRemoteException {
		log.debug(" **** createOrder ****");
		String bidderId = order.getBidder().getUserId();
		String callerId = ctx.getCallerPrincipal().getName();
		if (bidderId.equalsIgnoreCase(callerId) == false){
			throw new IllegalArgumentException("Logged in user does not match bid order");
		}
		
		boolean b = orderMgmt.createOrder(order);
		
		if (b){
			executeOrder(order);
		}
		
		return order.getBidOrderId();
	}
	
	
	public BidResultDTO executeOrder(BidOrder order) throws BuyerMgmtRemoteException{
		log.debug(" **** executeOrder ****");
	
		/*
		AuctionDTO auctionDTO = buyerManager.getAuctionDTO(order.getAuctionId());
		
		float bidAmount = 0;
		if (auctionDTO.bids.isEmpty()){
			bidAmount = order.getStartBid();
			if (auctionDTO.askingPrice > bidAmount && auctionDTO.askingPrice <= order.getMaxBid()){
				bidAmount = auctionDTO.askingPrice;
			}
		}
		else{
			BidDTO lastBid = auctionDTO.bids.last();
			if (lastBid.amount < order.getMaxBid()){
				bidAmount = Math.max(order.getStartBid(), lastBid.amount + 1); // place more than the previous bid
				bidAmount = Math.min(bidAmount, order.getMaxBid()); // place no more than your max bid
			}
		}
		*/
		
		return new BidResultDTO(null, "TODO"); // sysbidbot1.placeBid(order.getAuctionId(), order.getBidder(), bidAmount);
	}


	public BidOrder getOrder(long bidOrderId) {
		log.debug(" **** getOrder ****");
		return orderMgmt.getOrder(bidOrderId);
	}

	public boolean placeBid(BidOrder order, float bid) {
		log.debug(" **** placeBid ****");
		return orderMgmt.placeBid(order, bid);
	}

	public boolean endOrder(long bidOrderId) {
		log.debug(" **** endOrder ****");
		return orderMgmt.endOrder(bidOrderId);
	} // complete order processing once auction has closed and note if won.

	public boolean getOrderStatus(long bidOrderId) {
		log.debug(" **** getOrderStatus ****");
		return orderMgmt.getOrderStatus(bidOrderId);
	} // did user win or not

	@RolesAllowed({"ebidbot-admin"})
	public boolean createAccount(BidAccount ba) {
		log.debug(" **** createAccount(BidAccount) ****");
		return orderMgmt.createAccount(ba);
	}

	@RolesAllowed({"ebidbot-admin"})
	public BidAccount createAccount(String userId, String accountId,
			String passwd) {
		log.debug(" **** createAccount(String,String,String) ****");
		return orderMgmt.createAccount(userId, accountId, passwd);
	}

	public BidAccount getAccount(String userId) {
		log.debug(" **** getAccount ****");
		return orderMgmt.getAccount(userId);
	}

}
