/**
 * 
 */
package dutrow.bidbot.ejb;

import javax.ejb.Remote;
import javax.naming.NamingException;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.BuyerMgmtRemoteException;

/**
 * @author dutroda1
 *
 */
@Remote
public interface OrderMgmtRemote {
	long createOrder(BidOrder order) throws BuyerMgmtRemoteException, NamingException;
	BidOrder getOrder(long bidOrderId);
	boolean endOrder(long bidOrderId); // complete order processing once auction has closed and note if won.
	boolean getOrderStatus(long bidOrderId); // did user win or not
	boolean createAccount(BidAccount ba);
	BidAccount createAccount(String userId, String accountId, String passwd);
	BidAccount getAccount(String userId);
	
	BidResultDTO placeBid(long auctionId, BidAccount bidder, float bidAmount)
			throws BuyerMgmtRemoteException;
	
	void cancelTimers();
}
