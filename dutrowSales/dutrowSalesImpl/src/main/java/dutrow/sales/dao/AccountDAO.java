/**
 * 
 */
package dutrow.sales.dao;

import java.util.Collection;

import dutrow.sales.bo.Account;

/**
 * @author dutroda1
 *
 */
public interface AccountDAO {
	String createAccount(Account accountDetails);
	Account getAccountByUser(String userId);
	Account getAccountByEmail(String email);
	boolean updateAccount(Account accountDetails);
	boolean removeAccount(String userId);
	Collection<Account> getAccounts();
	Collection<Account> getAccounts(int offset, int limit);
}
