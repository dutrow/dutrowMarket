/**
 * 
 */
package dutrow.bidbot.dao;

import java.util.Collection;

import dutrow.bidbot.bo.Account;

/**
 * @author dutroda1
 *
 */
public interface BidAccountDAO {
	boolean createAccount(Account accountDetails);
	Account getAccountById(String userId);
	boolean updateAccount(Account accountDetails);
	boolean removeAccount(String userId);
	Collection<Account> getAccounts();
}
