/**
 * 
 */
package dutrow.sales.dao.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.dao.AuctionDAO;
import dutrow.sales.dao.DAOException;

/**
 * @author dutroda1
 * 
 */
public class JPAAuctionDAO implements AuctionDAO {
	private static Log log = LogFactory.getLog(JPAAuctionDAO.class);
	private EntityManager em;

	/**
	 * @param emIn
	 */
	public JPAAuctionDAO(EntityManager emIn) {
		setEntityManager(emIn);
	}

	/**
	 * @param entityManagerIn
	 */
	public void setEntityManager(EntityManager entityManagerIn) {
		try {
			em = entityManagerIn;
		} catch (RuntimeException ex) {
			log.error("JPAAuctionDAO::setEntityManager " + ex.getMessage());
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dutrow.sales.dao.AuctionDAO#createAuction(dutrow.sales.bo.AuctionItem)
	 */
	@Override
	public AuctionItem createAuction(AuctionItem auctionDetails) {

		try {
			em.persist(auctionDetails);
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
		return auctionDetails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dutrow.sales.dao.AuctionDAO#updateAuction(dutrow.sales.bo.AuctionItem)
	 */
	@Override
	public boolean updateAuction(AuctionItem ai) {
		// I expect that persisting will just do an overwrite

		boolean useCascade = true;
		try {
			if (useCascade == false) {
				for (Bid b : ai.getBids()) {
					em.persist(b);
				}
			}
			em.persist(ai);
		} catch (RuntimeException ex) {
			log.warn("RuntimeException: " + ex.getMessage());
			ex.printStackTrace();
			throw new DAOException("troubles: " + ex.toString(), ex);
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.AuctionDAO#getAuctions()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<AuctionItem> getAuctions() {
		try {
			return (Collection<AuctionItem>) em.createQuery(
					"select a from AuctionItem a").getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.AuctionDAO#getAuctionById(java.lang.String)
	 */
	@Override
	public AuctionItem getAuctionById(long id) {
		try {
			return em.find(AuctionItem.class, id);
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.AuctionDAO#removeAuction(java.lang.String)
	 */
	@Override
	public boolean removeAuction(long l) {
		try {
			AuctionItem ai = em.find(AuctionItem.class, l);
			em.remove(ai);
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
		return true;
	}

}
