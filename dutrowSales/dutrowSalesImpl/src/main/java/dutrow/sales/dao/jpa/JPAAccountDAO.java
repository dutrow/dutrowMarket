/**
 * 
 */
package dutrow.sales.dao.jpa;

import java.util.Collection;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bo.Account;
import dutrow.sales.dao.AccountDAO;
import dutrow.sales.dao.DAOException;

/**
 * @author dutroda1
 * 
 */
public class JPAAccountDAO implements AccountDAO {
	private static Log log = LogFactory.getLog(JPAAccountDAO.class);

	private EntityManager em;

	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unused")
	private JPAAccountDAO() {
	} // force EntityManager constructor

	public JPAAccountDAO(EntityManager emIn) {
		setEntityManager(emIn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.AccountDAO#createAccount(dutrow.sales.bo.Account)
	 */
	@Override
	public String createAccount(Account accountDetails) {
		try {
			Account existingAcct = getAccountByUser(accountDetails.getUserId());
			if (existingAcct != null) {
				log.warn("User Account already exists with this id: "
						+ accountDetails.getUserId());
				return accountDetails.getUserId();
			}

			em.persist(accountDetails);
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
		return accountDetails.getUserId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.AccountDAO#getAccountByUser(java.lang.String)
	 */
	@Override
	public Account getAccountByUser(String userId) {
		try {
			return em.find(Account.class, userId);
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.AccountDAO#getAccountByEmail(java.lang.String)
	 */
	@Override
	public Account getAccountByEmail(String email) {

		// throw new
		// DAOException("JPAAccountDAO::getAccountByEmail not implemented");

		// I think I have to join to the POC table before testing email equality
		TypedQuery<Account> accountQuery = em.createQuery(
				"select a from Account a where a.poc.email=:email",
				Account.class);
		accountQuery.setParameter("email", email);

		return accountQuery.getSingleResult();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.AccountDAO#updateAccount(dutrow.sales.bo.Account)
	 */
	@Override
	public boolean updateAccount(Account accountDetails) {
		// Account managedAccount = em.find(Account.class,
		// accountDetails.getUserId());
		// managedAccount.copy(accountDetails);

		// If I just persist it, will it override automatically?
		// should I check that it doesn't exist first?
		try {
			Account update = em.find(Account.class, accountDetails.getUserId());
			update.copy(accountDetails);
			em.persist(update);
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			log.warn(ex.toString());
			throw new DAOException("troubles: " + ex.toString(), ex);
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.AccountDAO#removeAccount(dutrow.sales.bo.Account)
	 */
	@Override
	public boolean removeAccount(String accountId) {

		try {
			Account accountToRemove = em.find(Account.class, accountId);
			em.remove(accountToRemove);
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.AccountDAO#getAccounts()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Account> getAccounts() {
		try {
			return (Collection<Account>) em.createQuery(
					"select a from Account a").getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.AccountDAO#getAccounts(int, int)
	 */
	@Override
	public Collection<Account> getAccounts(int offset, int limit) {
		try {
			TypedQuery<Account> q = em.createQuery("select a from Account a",
					Account.class);
			q.setMaxResults(limit);
			q.setFirstResult(offset);

			return q.getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
	}

}
