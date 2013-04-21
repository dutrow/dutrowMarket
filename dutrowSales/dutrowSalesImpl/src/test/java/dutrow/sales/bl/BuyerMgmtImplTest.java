/**
 * 
 */
package dutrow.sales.bl;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import dutrow.sales.bo.Account;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.BidResult;
import dutrow.sales.dao.JPATestBase;

/**
 * @author dutroda1
 *
 */
public class BuyerMgmtImplTest extends JPATestBase {

	private Account seller;
	private Account buyer1;
	private long auctionId;
	
	/* (non-Javadoc)
	 * @see dutrow.sales.dao.JPATestBase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		seller = testSupport.createSellerExample();
		this.accountDao.createAccount(seller);
		buyer1 = testSupport.createDan();
		this.accountDao.createAccount(buyer1);
		auctionId = testSupport.persistAuctionItemExample(seller);
	}
	

	/**
	 * Test method for {@link dutrow.sales.bl.impl.BuyerMgmtImpl#getOpenAuctions()}.
	 */
	@Test
	public void testGetOpenAuctions() {
		Collection<AuctionItem> openAuctions = buyerManager.getOpenAuctions();
		Assert.assertNotSame("No Open Auctions", 0, openAuctions.size());
		AuctionItem anOpenAuction = openAuctions.iterator().next();
		Assert.assertNotNull("An Open Auction is null", anOpenAuction);
	}

	/**
	 * Test method for {@link dutrow.sales.bl.impl.BuyerMgmtImpl#getAuction(long)}.
	 */
	@Test
	public void testGetAuction() {
		AuctionItem theOpenAuction = buyerManager.getAuction(auctionId);
		Assert.assertNotNull("The Open Auction is null", theOpenAuction);
	}


	/**
	 * Test method for {@link dutrow.sales.bl.impl.BuyerMgmtImpl#placeBid(dutrow.sales.bo.POC, long, float)}.
	 */
	@Test
	public void testPlaceBid() {
		BidResult one = buyerManager.placeBid(buyer1.getPoc().getUserId(), auctionId, 2.00f);
		BidResult two = buyerManager.placeBid(buyer1.getPoc().getUserId(), auctionId, 1.00f);
		BidResult three = buyerManager.placeBid(buyer1.getPoc().getUserId(), auctionId, 3.00f);
		
		Assert.assertNotNull("First Bid invalid: " + one.getResult(), one.getBid());
		Assert.assertNull("Second Bid should have been rejected: " + two.getResult(), two.getBid());
		Assert.assertNotNull("Third Bid should have been accepted: " + three.getResult(), three.getBid());
	}

}
