/**
 * 
 */
package dutrow.bidbot.dao;

import java.util.Collection;

import dutrow.bidbot.bo.BidAccount;

/**
 * @author dutroda1
 *
 */
public interface BidAccountDAO {
	boolean createAccount(BidAccount accountDetails);
	BidAccount getAccountById(String userId);
	boolean updateAccount(BidAccount accountDetails);
	boolean removeAccount(String userId);
	Collection<BidAccount> getAccounts();
}
