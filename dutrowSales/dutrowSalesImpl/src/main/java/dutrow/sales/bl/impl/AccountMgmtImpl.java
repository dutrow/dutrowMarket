/**
 * 
 */
package dutrow.sales.bl.impl;

import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.AccountMgmt;
import dutrow.sales.bo.Account;
import dutrow.sales.dao.AccountDAO;

/**
 * @author dutroda1
 * 
 */
public class AccountMgmtImpl implements AccountMgmt {
	private static Log log = LogFactory.getLog(AccountMgmtImpl.class);

	protected AccountDAO accountDAO;

	@Inject
	public void setAccountDAO(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	@SuppressWarnings("unused")
	private AccountMgmtImpl() {
	} // force use of DAO constructor

	public AccountMgmtImpl(AccountDAO accountDao) {
		setAccountDAO(accountDao);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.bl.AccountMgmt#createAccount(dutrow.sales.bo.Account)
	 */
	@Override
	public String createAccount(Account accountDetails) {

		// Do not create if a user with that user id already exists
		Account existingAccount = accountDAO.getAccountByUser(accountDetails
				.getUserId());
		if (existingAccount != null) {
			log.warn("User Already Exists: " + accountDetails.getUserId());
			return null;
		}

		// Create a new user
		String newAccount = accountDAO.createAccount(accountDetails);
		log.info("User Created: " + newAccount);

		return newAccount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.bl.AccountMgmt#getAccount(java.lang.String)
	 */
	@Override
	public Account getAccount(String userString) {

		Account account = accountDAO.getAccountByUser(userString);
		if (account != null) {
			log.info("Found User: " + account.getUserId());
			return account;
		}

		account = accountDAO.getAccountByEmail(userString);

		if (account != null) {
			log.info("Found User: " + account.getUserId());
			return account;
		}

		log.info("Could Not Find User: " + userString);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.bl.AccountMgmt#updateAccount(dutrow.sales.bo.Account)
	 */
	@Override
	public boolean updateAccount(Account accountDetails) {

		Account matchAccount = accountDAO.getAccountByUser(accountDetails
				.getUserId());
		if (matchAccount == null) {
			log.warn("No Matching User: " + accountDetails.getUserId());
			return false;
		}

		matchAccount.copy(accountDetails);
		accountDAO.updateAccount(matchAccount);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.bl.AccountMgmt#closeAccount(java.lang.String)
	 */
	@Override
	public boolean closeAccount(String userId) {

		Account matchAccount = accountDAO.getAccountByUser(userId);
		if (matchAccount == null) {
			log.warn("No Matching User: " + userId);
			return false;
		}

		accountDAO.removeAccount(userId);

		log.info("Removed User: " + userId);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.bl.AccountMgmt#getAccounts(int, int)
	 */
	@Override
	public Collection<Account> getAccounts(int offset, int limit) {
		return accountDAO.getAccounts(offset, limit);

	}
}
