/**
 * 
 */
package dutrow.sales.bl;

import static org.junit.Assert.*;

import java.util.Collection;

import junit.framework.Assert;

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
public class BuyerMgmtImplTest extends JPATestBase {

	private Account seller;
	private Account bidder;
	private AuctionItem auction;
	
	/* (non-Javadoc)
	 * @see dutrow.sales.dao.JPATestBase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		seller = testSupport.createSellerExample();
		this.accountDao.createAccount(seller);
		bidder = testSupport.createDan();
		this.accountDao.createAccount(bidder);
		auction = testSupport.createAuctionItemExample(seller);
		
		this.auctionDao.createAuction(auction);
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
		AuctionItem theOpenAuction = buyerManager.getAuction(auction.getId());
		Assert.assertNotNull("The Open Auction is null", theOpenAuction);
	}


	/**
	 * Test method for {@link dutrow.sales.bl.impl.BuyerMgmtImpl#placeBid(dutrow.sales.bo.POC, long, float)}.
	 */
	@Test
	public void testPlaceBid() {
		Bid one = buyerManager.placeBid(bidder.getPoc(), auction.getId(), 2.00f);
		Bid two = buyerManager.placeBid(bidder.getPoc(), auction.getId(), 1.00f);
		Bid three = buyerManager.placeBid(bidder.getPoc(), auction.getId(), 3.00f);
		
		Assert.assertNotNull("First Bid invalid", one);
		Assert.assertNull("Second Bid should have been rejected", two);
		Assert.assertNotNull("Third Bid should have been accepted", three);
	}

}
