package dutrow.bidbot.ejb;

import java.util.Properties;

import javax.annotation.security.RunAs;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemoteException;

/**
 * @author dutroda1
 * 
 */
//@Stateless
//@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//@RunAs("esales-trusted")
public class OrderMgmtHelper {
	private static final Log log = LogFactory.getLog(OrderMgmtHelper.class);

	private BuyerMgmtRemote buyerManager;
	
	
	//public OrderMgmtHelper(BuyerMgmtRemote buyerManager) {
	//	this.buyerManager = buyerManager;
	//	log.info("buyerManager=" + buyerManager);
	//}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public BidResultDTO executeOrder(BidOrder order)
			throws BuyerMgmtRemoteException {
		log.debug(" **** executeOrder ****");

		if (order == null) {
			log.warn("BidOrder is null");
			return new BidResultDTO(null, "BidOrder is null");
		}

		AuctionDTO auctionDTO = buyerManager
				.getAuctionDTO(order.getAuctionId());

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

		return placeBid(order.getAuctionId(), order.getBidder(), bidAmount);
	}

	public BidResultDTO placeBid(long auctionId, BidAccount bidder,
			float bidAmount) throws BuyerMgmtRemoteException {
		try {
			runAs(bidder.getSalesAccount(), bidder.getSalesPassword());
			return buyerManager.placeBid(auctionId, bidAmount);
		} catch (NamingException e) {
			log.warn(
					"Could not authenticate as user: "
							+ bidder.getSalesAccount(), e);
			return new BidResultDTO(null, "Could not authenticate as user: "
					+ bidder.getSalesAccount());
		}

	}

	protected Context runAs(String username, String password)
			throws NamingException {
		Properties env = new Properties();
		if (username != null) {
			env.put(Context.SECURITY_PRINCIPAL, username);
			env.put(Context.SECURITY_CREDENTIALS, password);
		}
		log.debug(String.format("%s env=%s", username == null ? "anonymous"
				: username, env));
		InitialContext jndi = new InitialContext(env);
		return jndi;
	}

}
