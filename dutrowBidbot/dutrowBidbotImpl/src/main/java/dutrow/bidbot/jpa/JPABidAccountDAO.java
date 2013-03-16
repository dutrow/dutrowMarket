/**
 * 
 */
package dutrow.bidbot.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.dao.BidAccountDAO;
import dutrow.bidbot.dao.DAOException;

/**
 * @author dutroda1
 * 
 */
public class JPABidAccountDAO implements BidAccountDAO {
	private static Log log = LogFactory.getLog(JPABidAccountDAO.class);

	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unused")
	private JPABidAccountDAO() {
	} // force EntityManager constructor

	public JPABidAccountDAO(EntityManager emIn) {
		setEntityManager(emIn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dutrow.bidbot.dao.BidAccountDAO#createAccount(dutrow.bidbot.bo.BidAccount
	 * )
	 */
	@Override
	public BidAccount createAccount(BidAccount accountDetails) {
		try {
			em.persist(accountDetails);
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
		return accountDetails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.dao.BidAccountDAO#getAccountById(java.lang.String)
	 */
	@Override
	public BidAccount getAccountById(String userId) {
		try {
			return em.find(BidAccount.class, userId);
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dutrow.bidbot.dao.BidAccountDAO#updateAccount(dutrow.bidbot.bo.BidAccount
	 * )
	 */
	@Override
	public boolean updateAccount(BidAccount accountDetails) {
		try {
			em.persist(accountDetails);
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
	 * @see dutrow.bidbot.dao.BidAccountDAO#removeAccount(java.lang.String)
	 */
	@Override
	public boolean removeAccount(String userId) {

		try {
			BidAccount accountToRemove = em.find(BidAccount.class, userId);
			em.remove(accountToRemove);
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}

		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.dao.BidAccountDAO#getAccounts()
	 */
	@Override
	public List<BidAccount> getAccounts() {
		try {
			return em.createQuery("select a from BidAccount a",
					BidAccount.class).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
	}

}