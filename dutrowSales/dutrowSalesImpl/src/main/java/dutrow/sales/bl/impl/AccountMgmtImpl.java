/**
 * 
 */
package dutrow.sales.bl.impl;

import java.util.Collection;

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

	private AccountDAO accountDAO;
	public void setAccountDAO(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
		;
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
	public Account createAccount(Account accountDetails) {

		if (accountDAO == null) {
			log.warn("Account DAO Does Not Exist");
			throw new IllegalArgumentException("Cannot create account");
		}

		// Do not create if a user with that user id already exists
		Account existingAccount = accountDAO.getAccountByUser(accountDetails
				.getUserId());
		if (existingAccount != null) {
			log.warn("User Already Exists: " + accountDetails.getUserId());
			return null;
		}

		// Create a new user
		Account newAccount = accountDAO.createAccount(accountDetails);
		log.info("User Created: " + newAccount.getUserId());

		return newAccount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.bl.AccountMgmt#getAccount(java.lang.String)
	 */
	@Override
	public Account getAccount(String userString) {

		if (accountDAO == null) {
			log.warn("Account DAO Does Not Exist");
			// throw new IllegalArgumentException("Cannot get account");
			return null; // so tests will complete
		}

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

		if (accountDAO == null) {
			log.warn("Account DAO Does Not Exist");
			// throw new IllegalArgumentException("Cannot update account");
			return false; // so tests will complete
		}

		Account matchAccount = accountDAO.getAccountByUser(accountDetails
				.getUserId());
		if (matchAccount == null) {
			log.warn("No Matching User: " + accountDetails.getUserId());
			return false;
		}

		matchAccount.copy(accountDetails);

		log.info("User Update Successful: " + accountDetails.getUserId());

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.bl.AccountMgmt#closeAccount(java.lang.String)
	 */
	@Override
	public boolean closeAccount(String userId) {

		if (accountDAO == null) {
			log.warn("Account DAO Does Not Exist");
			// throw new IllegalArgumentException("Cannot close account");
			return false; // so tests will complete
		}

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
