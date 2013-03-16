/**
 * 
 */
package dutrow.sales.bl;

import java.util.Collection;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.bo.Account;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.dao.JPATestBase;

/**
 * @author dutroda1
 *
 */
public class BusinessLogicTest extends JPATestBase{
	private static Log log = LogFactory.getLog(BusinessLogicTest.class);
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log.trace("Set up for BusinessLogicTest");
		super.setUp();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		log.trace("Tear down for BusinessLogicTest");
		super.tearDown();
	}

	
	@Test
	public void testAccountManager(){
		log.info("testAccountManager");
		//testSupport.createAndPersistAccountExamples();
		Account dan = testSupport.createDan();
		accountManager.createAccount(dan);
		
		Collection<Account> accounts = testSupport.getAccounts();
		for (Account a : accounts){
			log.info("Found account: " + a.toString() );
		}
		
		log.info("testAccountManager::getAccount");
		Account retrieved = accountManager.getAccount("dutrow");
		Assert.assertNotNull("Could not retrieve account", retrieved);
		if (retrieved != null)
			retrieved.setMiddleName("Allan Lundahl");
		log.info("testAccountManager::updateAccount");
		Assert.assertTrue("Could not update account", accountManager.updateAccount(retrieved));
		log.info("testAccountManager::closeAccount");
		Assert.assertTrue("Could not close account", accountManager.closeAccount("dutrow"));
	}
	
	
	@Test
	public void testSellerManager(){
		log.info("testSellerManager");
		Account seller = testSupport.createSellerExample();
		Account persistedSeller = this.accountDao.createAccount(seller);
		
		AuctionItem myItem = testSupport.createAuctionItem("gold coin", persistedSeller);
		log.info("myItem starts as: " + myItem.getId());
		sellerManager.createAuction(myItem);
		log.info("myItem updated to: " + myItem.getId());
		
		AuctionItem ai = sellerManager.getAuction(myItem.getId());
		log.info("Found Auction: " + ai.toString());
		
		Collection<AuctionItem> c = sellerManager.getOpenUserAuctions(seller.getUserId());
		for (AuctionItem i : c){
			log.info("Open User Auction:" + i);
		}
	}
	
	@Test
	public void testBuyerManager(){
		log.info("testBuyerManager");
		Account seller = testSupport.createSellerExample();
		this.accountDao.createAccount(seller);
		
		Account bidder = testSupport.createDan();
		this.accountDao.createAccount(bidder);
		
		AuctionItem auction = testSupport.createAuctionItemExample(seller);
		
		this.auctionDao.createAuction(auction);
		
		Collection<AuctionItem> openAuctions = buyerManager.getOpenAuctions();
		Assert.assertNotSame("No Open Auctions", 0, openAuctions.size());
		AuctionItem anOpenAuction = openAuctions.iterator().next();
		Assert.assertNotNull("An Open Auction is null", anOpenAuction);
		
		AuctionItem theOpenAuction = buyerManager.getAuction(auction.getId());
		Assert.assertNotNull("The Open Auction is null", theOpenAuction);
		
		Bid one = buyerManager.placeBid(bidder.getPoc(), auction.getId(), 2.00f);
		Bid two = buyerManager.placeBid(bidder.getPoc(), auction.getId(), 1.00f);
		Bid three = buyerManager.placeBid(bidder.getPoc(), auction.getId(), 3.00f);
		
		Assert.assertNotNull("First Bid invalid", one);
		Assert.assertNull("Second Bid should have been rejected", two);
		Assert.assertNotNull("Third Bid should have been accepted", three);
		
	}

}
