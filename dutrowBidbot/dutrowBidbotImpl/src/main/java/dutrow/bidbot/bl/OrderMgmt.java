/**
 * 
 */
package dutrow.bidbot.bl;

import dutrow.bidbot.bo.BidOrder;

/**
 * @author dutroda1
 *
 */
public interface OrderMgmt {
	void createOrder(BidOrder order);
	void placeBid(BidOrder order, float bid);
	boolean endOrder(); // complete order processing once auction has closed and note if won.
	boolean getOrderStatus();// did user win or not
}
