/**
 * 
 */
package dutrow.bidbot.dao;

import java.util.Collection;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;

/**
 * @author dutroda1
 *
 */
public interface BidAccountDAO {
	BidAccount createAccount(BidAccount accountDetails);
	BidAccount getAccountById(String userId);
	boolean updateAccount(BidAccount accountDetails);
	boolean removeAccount(String userId);
	Collection<BidAccount> getAccounts();
	Collection<BidOrder> getBidOrders();
	BidOrder getOrderById(long bidOrderId);
	boolean createOrder(BidOrder order);
}
