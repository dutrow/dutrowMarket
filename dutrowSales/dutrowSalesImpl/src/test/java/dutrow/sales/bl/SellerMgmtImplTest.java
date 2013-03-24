/**
 * 
 */
package dutrow.sales.bl;

import static org.junit.Assert.fail;

import java.util.Collection;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.bo.Account;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.dao.JPATestBase;

/**
 * @author dutroda1
 * 
 */
public class SellerMgmtImplTest extends JPATestBase {
	private static Log log = LogFactory.getLog(SellerMgmtImplTest.class);

	Account seller ;
	AuctionItem myItem;

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.JPATestBase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		seller = testSupport.createSellerExample();
		seller = this.accountDao.createAccount(seller);
		myItem = testSupport.createAuctionItem("gold coin", seller);
		sellerManager.createAuction(myItem);
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.bl.impl.SellerMgmtImpl#createAuction(dutrow.sales.bo.AuctionItem)}
	 * .
	 */
	@Test
	public void testCreateAuction() {
		Assert.assertTrue("Entity Manager does not contain auction", em.contains(myItem));
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.bl.impl.SellerMgmtImpl#getUserAuctions(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetUserAuctions() {
		Collection<AuctionItem> c = sellerManager.getUserAuctions(seller.getUserId());
		Assert.assertNotNull("User Auctions are Null", c);
		for (AuctionItem i : c){
			log.info("Open User Auction:" + i);
		}		
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.bl.impl.SellerMgmtImpl#getOpenUserAuctions(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetOpenUserAuctions() {
		Collection<AuctionItem> c = sellerManager.getOpenUserAuctions(seller.getUserId());
		for (AuctionItem i : c){
			log.info("Open User Auction:" + i);
			Assert.assertTrue("Auction Must be Open", i.isOpen());
		}
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.bl.impl.SellerMgmtImpl#getAuction(long)}.
	 */
	@Test
	public void testGetAuction() {
		AuctionItem ai = sellerManager.getAuction(myItem.getId());
		Assert.assertNotNull("Could not find Auction", ai);
		log.info("Found Auction: " + ai.toString());
	}

}
