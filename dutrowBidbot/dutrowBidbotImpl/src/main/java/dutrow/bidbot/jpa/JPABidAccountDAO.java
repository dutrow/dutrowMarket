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
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.dao.BidAccountDAO;
import dutrow.bidbot.dao.DAOException;

/**
 * @author dutroda1
 * 
 */
public class JPABidAccountDAO extends JPADAO implements BidAccountDAO {
	private static Log log = LogFactory.getLog(JPABidAccountDAO.class);

	public JPABidAccountDAO(EntityManager emIn) {
		super(emIn);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.dao.OrderDAO#createOrder(dutrow.bidbot.bo.BidOrder)
	 */
	@Override
	public boolean createOrder(BidOrder order) {
	
		try {
			
			BidOrder existingOrder = em.find(BidOrder.class,
					order.getBidOrderId());
			if (existingOrder != null){
				log.warn("BidOrder already exists, not creating");
				return false;
			}

			BidAccount bidder = order.getBidder();
			if (bidder == null){
				log.warn("BidOrder not assigned to BidAccount, not creating");
				return false;
			}
			
			em.persist(bidder);
			
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.dao.OrderDAO#getBidOrders()
	 */
	@Override
	public Collection<BidOrder> getBidOrders() {
		try {
			return (Collection<BidOrder>) em.createQuery(
					"select a.orders from BidAccount a").getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.dao.OrderDAO#getOrderById(long)
	 */
	@Override
	public BidOrder getOrderById(long bidOrderId) {
		try {
			BidOrder existingOrder = em.find(BidOrder.class, bidOrderId);
			return existingOrder;
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
	}

}