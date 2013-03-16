/**
 * 
 */
package dutrow.bidbot.jpa;

import java.util.Collection;

import dutrow.bidbot.bo.Account;
import dutrow.bidbot.dao.BidAccountDAO;

/**
 * @author dutroda1
 *
 */
public class JPABidAccountDAO implements BidAccountDAO {

	/* (non-Javadoc)
	 * @see dutrow.sales.dao.BidAccountDAO#createAccount(dutrow.sales.bo.Account)
	 */
	@Override
	public boolean createAccount(Account accountDetails) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.dao.BidAccountDAO#getAccountById(java.lang.String)
	 */
	@Override
	public Account getAccountById(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.dao.BidAccountDAO#updateAccount(dutrow.sales.bo.Account)
	 */
	@Override
	public boolean updateAccount(Account accountDetails) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.dao.BidAccountDAO#removeAccount(java.lang.String)
	 */
	@Override
	public boolean removeAccount(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.dao.BidAccountDAO#getAccounts()
	 */
	@Override
	public Collection<Account> getAccounts() {
		// TODO Auto-generated method stub
		return null;
	}
}