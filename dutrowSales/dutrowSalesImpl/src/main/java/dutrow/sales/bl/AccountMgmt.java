/**
 * 
 */
package dutrow.sales.bl;

import java.util.Collection;

import dutrow.sales.bo.Account;

/**
 * @author dutroda1
 *
 */
public interface AccountMgmt {
	String createAccount(Account accountDetails);
	Account getAccount(String userString);
	boolean updateAccount(Account accountDetails);
	boolean closeAccount(String userId);
	
	/**
	 * @param offset
	 * @param limit
	 * @return
	 */
	Collection<Account> getAccounts(int offset, int limit);
	
}
