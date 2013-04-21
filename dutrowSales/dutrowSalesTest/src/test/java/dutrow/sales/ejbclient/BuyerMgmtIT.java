/**
 * 
 */
package dutrow.sales.ejbclient;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Collection;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.bl.BuyerMgmtException;
import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;

/**
 * @author dutroda1
 * 
 */
public class BuyerMgmtIT extends Support {
	private static final Log log = LogFactory.getLog(AccountMgmtIT.class);

	private static final String registrarJNDI = System
			.getProperty("jndi.name.registrar",
					"dutrowSalesEAR/dutrowSalesEJB/BuyerMgmtEJB!dutrow.sales.ejb.BuyerMgmtRemote");
	private BuyerMgmtRemote buyerManager;

	private AccountDTO seller;
	private AccountDTO bidder;
	private AuctionDTO auction;
	long auctionId;

	public void configureJndi(){
		assertNotNull("jndi.name.registrar not supplied", registrarJNDI);

		log.debug("jndi name:" + registrarJNDI);
		try {
			buyerManager = (BuyerMgmtRemote) jndi.lookup(registrarJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("buyerManager=" + buyerManager);
	}
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		configureJndi();
		
		log.debug("*** Set up for BuyerMgmtIT ***");
		log.debug("testSupport=" + testSupport);
		seller = new AccountDTO("seller", "John", "s", "Hopkins",
				"seller@jhu.edu");
		testSupport.createAccount(seller);
		bidder = new AccountDTO("bidder", "Alexander", "X", "Kossiakoff",
				"kossi@jhuapl.edu");
		testSupport.createAccount(bidder);
		auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", Calendar
						.getInstance().getTime(), 18.00f, seller);
		auction.id = testSupport.createAuction(auction);
		
		log.debug("bidder.userId: " + bidder.userId + " seller.userId: " + seller.userId + " auction.id: " + auction.id);
		
	}

	@Test
	public void testListOpenAuctions() {
		log.debug("*** testListOpenAuctions() *** ");
		Collection<AuctionDTO> openAuctions = buyerManager.listOpenAuctions();
		Assert.assertNotSame("No Open Auctions", 0, openAuctions.size());
		AuctionDTO anOpenAuction = openAuctions.iterator().next();
		Assert.assertNotNull("An Open Auction is null", anOpenAuction);
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.bl.impl.BuyerMgmtImpl#getAuction(long)}.
	 * 
	 * @throws BuyerMgmtException
	 */
	@Test
	public void testGetAuction() throws BuyerMgmtException {
		log.debug("*** testGetAuction() *** ");
		AuctionDTO theOpenAuction = buyerManager.getAuctionDTO(auction.id);
		Assert.assertNotNull("The Open Auction is null", theOpenAuction);
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.bl.impl.BuyerMgmtImpl#placeBid(dutrow.sales.bo.POC, long, float)}
	 * .
	 */
	@Test
	public void testPlaceBid() {
		log.debug("*** testPlaceBid() *** ");
		BidResultDTO one = buyerManager.placeBid(bidder.userId, auction.id,
				2.00f);
		BidResultDTO two = buyerManager.placeBid(bidder.userId, auction.id,
				1.00f);
		BidResultDTO three = buyerManager.placeBid(bidder.userId, auction.id,
				3.00f);

		Assert.assertNotNull("First Bid invalid: " + one.result, one.bid);
		Assert.assertNull(
				"Second Bid should have been rejected: " + two.result, two.bid);
		Assert.assertNotNull("Third Bid should have been accepted: "
				+ three.result, three.bid);
	}

}
