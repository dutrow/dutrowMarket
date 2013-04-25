/**
 * 
 */
package dutrow.bidbot.ejb;

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
import dutrow.bidbot.dao.BidAccountDAO;

import dutrow.bidbot.dao.BidAccountDAO;
import dutrow.bidbot.web.PlaceOrder;

/**
 * @author dutroda1
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OrderMgmtEJB implements OrderMgmtRemote {
	private static final Log log = LogFactory.getLog(OrderMgmtEJB.class);

	@Inject
	private BidAccountDAO dao;

	@Inject
	@BidbotOrderManager
	private OrderMgmt orderMgmt;

	public long createOrder(BidOrder order) {
		boolean b = orderMgmt.createOrder(order);
		return order.getBidOrderId();
	}

	public BidOrder getOrder(long bidOrderId) {
		return orderMgmt.getOrder(bidOrderId);
	}

	public boolean placeBid(BidOrder order, float bid) {
		return orderMgmt.placeBid(order, bid);
	}

	public boolean endOrder(long bidOrderId) {
		return orderMgmt.endOrder(bidOrderId);
	} // complete order processing once auction has closed and note if won.

	public boolean getOrderStatus(long bidOrderId) {
		return orderMgmt.getOrderStatus(bidOrderId);
	} // did user win or not

	public boolean createAccount(BidAccount ba) {
		return orderMgmt.createAccount(ba);
	}

	public BidAccount createAccount(String userId, String accountId,
			String passwd) {
		return orderMgmt.createAccount(userId, accountId, passwd);
	}

	public BidAccount getAccount(String userId) {
		return orderMgmt.getAccount(userId);
	}

}
