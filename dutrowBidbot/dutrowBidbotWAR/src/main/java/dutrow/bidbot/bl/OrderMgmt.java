/**
 * 
 */
package dutrow.bidbot.bl;

import java.util.List;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;

/**
 * @author dutroda1
 *
 */
public interface OrderMgmt {
	boolean createOrder(BidOrder order);
	BidOrder getOrder(long bidOrderId);
	boolean placeBid(BidOrder order, float bid);
	boolean endOrder(long bidOrderId); // complete order processing once auction has closed and note if won.
	boolean getOrderStatus(long bidOrderId); // did user win or not
	boolean createAccount(BidAccount ba);
	BidAccount createAccount(String userId, String accountId, String passwd);
	BidAccount getAccount(String userId);
	
	List<BidOrder> getOrdersforItem(long itemId);

	

	
}
