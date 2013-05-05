/**
 * 
 */
package dutrow.bidbot.ejb;

import javax.ejb.Remote;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.sales.ejb.BuyerMgmtRemoteException;

/**
 * @author dutroda1
 *
 */
@Remote
public interface OrderMgmtRemote {
	long createOrder(BidOrder order) throws BuyerMgmtRemoteException;
	BidOrder getOrder(long bidOrderId);
	boolean placeBid(BidOrder order, float bid);
	boolean endOrder(long bidOrderId); // complete order processing once auction has closed and note if won.
	boolean getOrderStatus(long bidOrderId); // did user win or not
	boolean createAccount(BidAccount ba);
	BidAccount createAccount(String userId, String accountId, String passwd);
	BidAccount getAccount(String userId);
}
