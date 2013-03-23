/**
 * 
 */
package dutrow.bidbot.blimpl;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bl.BidbotTestUtil;
import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.dao.BidAccountDAO;
import dutrow.bidbot.dao.DAOException;

/**
 * @author dutroda1
 * 
 */
public class BidbotTestUtilImpl implements BidbotTestUtil {
	private static Log log = LogFactory.getLog(BidbotTestUtilImpl.class);
	private BidAccountDAO accountDao;
	private EntityManager em;

	@SuppressWarnings("unused")
	private BidbotTestUtilImpl() {
	}

	public BidbotTestUtilImpl(EntityManager entityManager,
			BidAccountDAO accounts
			) {
		em = entityManager;
		accountDao = accounts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.bl.BidbotTestUtil#reset()
	 */
	@Override
	public boolean reset() {
		return resetAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.bl.TestSupport#resetAll()
	 */
	public boolean resetAll() {
		boolean cleanReset = true;
		try {
			cleanup(em);
		} catch (DAOException e) {
			log.warn(e.getLocalizedMessage());
			e.printStackTrace();
			cleanReset = false;
		}
		return cleanReset;
	}

	private void deleteByJPA(EntityManager em) throws DAOException {
		log.info("Delete By JPA");

		boolean useCascades = true;
		
		Collection<BidAccount> accounts = accountDao.getAccounts();
		for (BidAccount a : accounts) {
			if (useCascades == false) {

				for (BidOrder o : a.getOrders()) {
					em.remove(o);
				}

				em.remove(a);
			} else {
				accountDao.removeAccount(a.getUserId());
			}
		}
		
		
	}

	/**
	 * 
	 */
	private void cleanup(EntityManager em) throws RuntimeException {
		log.debug("JPAAccountDAOTest::cleanup");

		deleteByJPA(em);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.bl.BidbotTestUtil#createOrder()
	 */
	@Override
	public BidOrder createOrder() {
		BidAccount ba = createBidder();
		BidOrder bid = new BidOrder(2, 3.0f, 9.0f, ba);
		
		ba.addOrder(bid);

		return bid;

	}

	/**
	 * @return
	 */
	@Override
	public BidAccount createBidder() {
		BidAccount acct = new BidAccount();
		acct.setUserId("bidbotSellerAcct");
		acct.setSalesAccount("bidder");
		acct.setSalesPassword("bidderPwd");
		acct.setOrders(new ArrayList<BidOrder>());

		return acct;
	}

}
