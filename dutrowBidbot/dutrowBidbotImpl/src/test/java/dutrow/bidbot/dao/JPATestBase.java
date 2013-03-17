/**
 * 
 */
package dutrow.bidbot.dao;

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

import dutrow.bidbot.bl.BidbotTestUtil;
import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.blimpl.BidbotTestUtilImpl;
import dutrow.bidbot.blimpl.OrderMgmtImpl;
import dutrow.bidbot.jpa.JPABidAccountDAO;

/**
 * @author dutroda1
 * 
 */
public class JPATestBase {
	private static Log log = LogFactory.getLog(JPATestBase.class);
	protected static EntityManagerFactory emf;

	protected BidbotTestUtil testSupport;
	protected OrderMgmt orderManager;
	
	protected EntityManager em;
	protected JPABidAccountDAO accountDao;
	
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
		accountDao = new JPABidAccountDAO(em);
		testSupport = new BidbotTestUtilImpl(em, accountDao);
		orderManager = new OrderMgmtImpl(accountDao);
		Assert.assertTrue("Reset Unsuccessful", testSupport.reset());
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
