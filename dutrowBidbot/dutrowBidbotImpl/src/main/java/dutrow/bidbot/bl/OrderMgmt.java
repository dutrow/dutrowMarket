/**
 * 
 */
package dutrow.bidbot.bl;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;

/**
 * @author dutroda1
 *
 */
public interface OrderMgmt {
	boolean createOrder(BidOrder order);
	boolean placeBid(BidOrder order, float bid);
	boolean endOrder(); // complete order processing once auction has closed and note if won.
	boolean getOrderStatus();// did user win or not
	BidAccount createAccount(String userId, String accountId, String passwd);
}
