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
	public String createAccount(BidAccount accountDetails) {
		try {
			em.persist(accountDetails);
		} catch (RuntimeException ex) {
			log.warn("error creating account " + accountDetails, ex);
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
		return accountDetails.getUserId();
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
			log.warn("error on getAccountById: " + userId, ex);
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

			if (order.getBidOrderId() != 0){
				BidOrder existingOrder = em.find(BidOrder.class,
						order.getBidOrderId());
				if (existingOrder != null) {
					log.warn("BidOrder already exists, not creating");
					return false;
				}
			}

			BidAccount bidder = order.getBidder();
			if (bidder == null) {
				log.warn("BidOrder not assigned to BidAccount, not creating");
				return false;
			}

			em.persist(order);
			log.info("BidOrder created id: " + order.getBidOrderId());

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
	public List<BidOrder> getBidOrders() {
		try {
			return (List<BidOrder>) em.createQuery(
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dutrow.bidbot.dao.BidAccountDAO#updateOrder(dutrow.bidbot.bo.BidOrder)
	 */
	@Override
	public boolean updateOrder(BidOrder bo) {
		try {
			BidOrder existingOrder = em
					.find(BidOrder.class, bo.getBidOrderId());
			if (existingOrder == null)
				return false;

			em.persist(bo);

		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
		return true;
	}

}