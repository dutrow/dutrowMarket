/**
 * 
 */
package dutrow.sales.dao.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bo.Account;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.bo.Image;
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
	public long createAuction(AuctionItem auctionDetails) {

		try {
			AuctionItem existingAuction = em.find(AuctionItem.class,
					auctionDetails.getId());
			if (existingAuction != null) {
				log.warn("AuctionItem already exists with id: "
						+ auctionDetails.getId());
				return existingAuction.getId();
			}

			em.persist(auctionDetails);
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
		return auctionDetails.getId();
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

		boolean useCascade = false;
		try {
			if (useCascade == false) {
				if (ai.getBids() != null)
					for (Bid b : ai.getBids()) {
						em.persist(b);
					}
				if (ai.getImages() != null)
					for (Image i : ai.getImages()) {
						em.persist(i);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.AuctionDAO#getUserAuctions(java.lang.String)
	 */
	@Override
	public Collection<AuctionItem> getUserAuctions(String userId) {
		try {
			
			TypedQuery<AuctionItem> auctionQuery = em
					.createQuery(
					"select a from AuctionItem a where a.seller.userId=:userId",
					AuctionItem.class);
			auctionQuery.setParameter("userId", userId);

			return auctionQuery.getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("troubles: " + ex.toString(), ex);
		}
	}

}
