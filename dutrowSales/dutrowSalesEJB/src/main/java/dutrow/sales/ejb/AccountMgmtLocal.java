/**
 * 
 */
package dutrow.sales.ejb;

import javax.ejb.Local;

import dutrow.sales.bl.AccountMgmtException;
import dutrow.sales.bo.Account;

/**
 * @author dutroda1
 *
 */
@Local
public interface AccountMgmtLocal {
	Account createAccount(Account accountDetails) throws AccountMgmtException;
	Account getAccount(String userString) throws AccountMgmtException;
    boolean updateAccount(Account accountDetails) throws AccountMgmtException;
	boolean closeAccount(String userId) throws AccountMgmtException;
	
	
	

    
}