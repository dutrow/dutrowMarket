/**
 * 
 */
package dutrow.sales.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.bo.Account;
import dutrow.sales.bo.AuctionItem;

/**
 * @author dutroda1
 *
 */
public class JPAAuctionDAOTest extends JPATestBase{
	private static Log log = LogFactory.getLog(JPAAuctionDAOTest.class);

		
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log.debug("JPAAuctionDAOTest::setUp");
		super.setUp();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		log.debug("JPAAuctionDAOTest::tearDown");
		super.tearDown();
	}
	
	

	/**
	 * Test method for {@link dutrow.sales.dao.jpa.JPAAuctionDAO#createAuction(dutrow.sales.bo.AuctionItem)}.
	 */
	@Test
	public void testCreateAuction() {
		log.info("JPAAuctionDAOTest::testCreateAuction");
		Account seller = testSupport.createSellerExample();
		// must persist the seller before persisting the auction
		accountDao.createAccount(seller);
		
		AuctionItem ai = testSupport.createAuctionItem("pot o gold", seller);
		auctionDao.createAuction(ai);
		log.info("Created Auction: " + ai.getId());
	}

	/**
	 * Test method for {@link dutrow.sales.dao.jpa.JPAAuctionDAO#updateAuction(dutrow.sales.bo.AuctionItem)}.
	 */
	@Test
	public void testUpdateAuction() {
		log.info("JPAAuctionDAOTest::testUpdateAuction");
		Account seller = testSupport.createSellerExample();
		accountDao.createAccount(seller);
		
		AuctionItem ai = testSupport.createAuctionItemExample(seller);
		auctionDao.createAuction(ai);
		
		ai.setAskingPrice(40)
			.setStartTime(testSupport.createTime(0))
			.setEndTime(testSupport.createTime(10))
			.setDescription("why not look great and taste great too")
			.setTitle("Bazooka Apparrel")
		;
		auctionDao.updateAuction(ai);
	}

	/**
	 * Test method for {@link dutrow.sales.dao.jpa.JPAAuctionDAO#getAuctions()}.
	 */
	@Test
	public void testGetAuctions() {
		log.info("JPAAuctionDAOTest::testGetAuctions");
		testUpdateAuction();
		assertNotNull(auctionDao.getAuctions());
	}

	/**
	 * Test method for {@link dutrow.sales.dao.jpa.JPAAuctionDAO#getAuctionById(long)}.
	 */
	@Test
	public void testGetAuctionById() {
		Account seller = testSupport.createSellerExample();
		accountDao.createAccount(seller);
		
		AuctionItem ai = testSupport.createAuctionItemExample(seller);
		assertNotNull(auctionDao.getAuctionById(ai.getId()));
	}

	/**
	 * Test method for {@link dutrow.sales.dao.jpa.JPAAuctionDAO#removeAuction(java.lang.String)}.
	 */
	@Test
	public void testRemoveAuction() {
		log.info("JPAAuctionDAOTest::testRemoveAuction");
		Account seller = testSupport.createSellerExample();
		accountDao.createAccount(seller);
		
		AuctionItem ai = testSupport.createAuctionItemExample(seller);
		assertNotNull(auctionDao.getAuctionById(ai.getId()));
		auctionDao.removeAuction(ai.getId());
		assertNull(auctionDao.getAuctionById(ai.getId()));
	}


}
