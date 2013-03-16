/**
 * 
 */
package dutrow.sales.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import dutrow.sales.bl.AccountMgmt;
import dutrow.sales.bl.AuctionMgmt;
import dutrow.sales.bl.BuyerMgmt;
import dutrow.sales.bl.SellerMgmt;
import dutrow.sales.bl.TestSupport;
import dutrow.sales.bl.impl.AccountMgmtImpl;
import dutrow.sales.bl.impl.AuctionMgmtImpl;
import dutrow.sales.bl.impl.BuyerMgmtImpl;
import dutrow.sales.bl.impl.SellerMgmtImpl;
import dutrow.sales.bl.impl.TestSupportImpl;
import dutrow.sales.dao.jpa.JPAAccountDAO;
import dutrow.sales.dao.jpa.JPAAuctionDAO;

/**
 * @author dutroda1
 * 
 */
public class JPATestBase {
	private static Log log = LogFactory.getLog(JPATestBase.class);
	protected static EntityManagerFactory emf;

	protected AccountMgmt accountManager;
	protected BuyerMgmt buyerManager;
	protected SellerMgmt sellerManager;
	protected AuctionMgmt auctionManager;
	protected TestSupport testSupport;
	protected EntityManager em;
	protected JPAAccountDAO accountDao;
	protected JPAAuctionDAO auctionDao;

	@BeforeClass
	static public void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("dutrowSalesDAO-test");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log.trace("JPATestBase setUp");
		em = emf.createEntityManager();
		accountDao = new JPAAccountDAO(em);
		auctionDao = new JPAAuctionDAO(em);
		accountManager = new AccountMgmtImpl(accountDao);
		auctionManager = new AuctionMgmtImpl(accountDao, auctionDao);
		buyerManager = new BuyerMgmtImpl(accountDao, auctionDao);
		sellerManager = new SellerMgmtImpl(auctionDao);
		testSupport = new TestSupportImpl(em, accountDao, auctionDao);
		Assert.assertTrue("Reset Unsuccessful", testSupport.resetAll());
		log.info("--- BEGIN TEST ---");
		em.getTransaction().begin();

	}

	@AfterClass
	static public void tearDownAfterClass() throws Exception {
		if (emf != null) {
			emf.close();
			emf = null;
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		log.trace("JPATestBase tearDown");
		if (em != null) {
			if (em.getTransaction().isActive()) {
				EntityTransaction tx = em.getTransaction();
				if (tx.getRollbackOnly()) {
					tx.rollback();
				} else {
					tx.commit();
				}
				em.close();
				em = null;
			}
		}
	}

}
