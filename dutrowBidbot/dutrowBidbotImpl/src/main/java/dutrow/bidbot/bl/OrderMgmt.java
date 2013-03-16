/**
 * 
 */
package dutrow.bidbot.bl;

import dutrow.bidbot.bo.Bid;
import dutrow.bidbot.bo.Order;

/**
 * @author dutroda1
 *
 */
public interface OrderMgmt {
	void createOrder(Order order);
	void placeBid(Bid bid);
	boolean endOrder(); // complete order processing once auction has closed and note if won.
	boolean getOrderStatus();// did user win or not
}
