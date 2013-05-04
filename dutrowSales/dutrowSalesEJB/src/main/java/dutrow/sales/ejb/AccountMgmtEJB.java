/**
 * 
 */
package dutrow.sales.ejb;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.AccountMgmt;
import dutrow.sales.bl.AccountMgmtException;
import dutrow.sales.bo.Account;
import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.DTOConversionUtil;

/**
 * @author dutroda1
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"esales-admin"})
//esales-admin		these users will be able to perform management and test functions on eSales.
public class AccountMgmtEJB implements AccountMgmtLocal, AccountMgmtRemote {
	private static final Log log = LogFactory.getLog(AccountMgmtEJB.class);

	@Inject
	private AccountMgmt accountManager;
	
	@Resource
	protected SessionContext ctx;

	@PostConstruct
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void init() {
		try {
			log.debug("**** init ****");
			log.debug("init complete, accountManager=" + accountManager);
		} catch (Throwable ex) {
			log.warn("error in init", ex);
			throw new EJBException("error in init" + ex);
		}
	}

	@PreDestroy
	public void close() {
		log.debug("*** close() ***");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.AccountMgmtLocal#createAccount()
	 */
	@Override
	@PermitAll
	public Account createAccount(Account accountDetails)
			throws AccountMgmtException {
		log.debug("*** createAccount() *** ");

		try {
			accountManager.createAccount(accountDetails);
		} catch (Throwable ex) {
			log.error(ex);
			throw new AccountMgmtException(ex.toString());
		}
		return accountDetails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.AccountMgmtLocal#getAccount()
	 */
	@Override
	@PermitAll
	public Account getAccount(String userString) throws AccountMgmtException {
		log.debug("*** getAccount() *** ");

		try {
			return accountManager.getAccount(userString);
		} catch (Throwable ex) {
			log.error(ex);
			throw new AccountMgmtException(ex.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.AccountMgmtLocal#updateAccount()
	 */
	@Override
	@RolesAllowed({"esales-user","esales-admin"})
	public boolean updateAccount(Account accountDetails)
			throws AccountMgmtException {
		log.debug("*** updateAccount() *** ");

		try {
			return accountManager.updateAccount(accountDetails);
		} catch (Throwable ex) {
			log.error(ex);
			throw new AccountMgmtException(ex.toString());
		}
	}

	// user can close own account
	@RolesAllowed({"esales-user","esales-admin"})
	public boolean closeAccount() throws AccountMgmtException {
		log.debug("*** closeAccount() *** ");

		String userId = ctx.getCallerPrincipal().getName();
		return closeAccount(userId);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.AccountMgmtLocal#closeAccount()
	 */
	@Override
	public boolean closeAccount(String userId) throws AccountMgmtException {
		log.debug("*** closeAccount(userId) *** ");
		try {
			return accountManager.closeAccount(userId);
		} catch (Throwable ex) {
			log.error(ex);
			throw new AccountMgmtException(ex.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dutrow.sales.ejb.AccountMgmtRemote#createAccountDTO(dutrow.sales.dto.
	 * AccountDTO)
	 */
	@Override
	@PermitAll
	public boolean createAccountDTO(AccountDTO accountDetails)
			throws AccountMgmtRemoteException {
		log.debug("*** createAccountDTO() *** ");

		Account acct = DTOConversionUtil.convertAccountDTO(accountDetails);

		try {
			createAccount(acct);
		} catch (AccountMgmtException e) {
			throw new AccountMgmtRemoteException(e);
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.AccountMgmtRemote#getAccountDTO(java.lang.String)
	 */
	@Override
	@PermitAll
	public AccountDTO getAccountDTO(String userString)
			throws AccountMgmtRemoteException {
		log.debug("*** getAccountDTO() *** ");

		Account acct;
		try {
			acct = getAccount(userString);
		} catch (AccountMgmtException e) {
			throw new AccountMgmtRemoteException(e);
		}
		AccountDTO adto = DTOConversionUtil.convertAccount(acct);

		return adto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dutrow.sales.ejb.AccountMgmtRemote#updateAccountDTO(dutrow.sales.dto.
	 * AccountDTO)
	 */
	@Override
	@RolesAllowed({"esales-user","esales-admin"})
	public boolean updateAccountDTO(AccountDTO accountDetails)
			throws AccountMgmtRemoteException {
		log.debug("*** updateAccountDTO() *** ");

		Account acct = DTOConversionUtil.convertAccountDTO(accountDetails);

		try {
			updateAccount(acct);
		} catch (AccountMgmtException e) {
			throw new AccountMgmtRemoteException(e);
		}

		return true;
	}

	
	@Override
	// user can close own account
	@RolesAllowed({"esales-user","esales-admin"})
	public boolean closeAccountDTO() throws AccountMgmtRemoteException {
		log.debug("*** closeAccount() *** ");

		String userId = ctx.getCallerPrincipal().getName();
		return closeAccountDTO(userId);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.AccountMgmtRemote#closeAccountDTO(java.lang.String)
	 */
	public boolean closeAccountDTO(String userId)
			throws AccountMgmtRemoteException {
		try {
			return this.closeAccount(userId);
		} catch (AccountMgmtException e) {
			throw new AccountMgmtRemoteException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.AccountMgmtRemote#getAccounts(int, int)
	 */
	@Override
	@PermitAll
	public Collection<AccountDTO> getAccounts(int index, int count)
			throws AccountMgmtRemoteException {
		log.debug("*** getAccounts() *** ");

		Collection<AccountDTO> returnedDTOs = new ArrayList<AccountDTO>(count);

		try {
			Collection<Account> accounts = accountManager.getAccounts(index,
					count);

			for (Account account : accounts) {
				returnedDTOs.add(DTOConversionUtil.convertAccount(account));
			}

		} catch (Throwable ex) {
			log.error(ex);
			throw new AccountMgmtRemoteException(ex.toString());
		}

		return returnedDTOs;
	}

}
