/**
 * 
 */
package dutrow.bidbot.ejbclient;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.BuyerMgmtRemoteException;
import dutrow.sales.ejb.SellerMgmtRemoteException;

/**
 * @author dutroda1
 * 
 */
public class EndToEndProject2IT extends BidbotSupport {
	private static final Log log = LogFactory.getLog(EndToEndProject2IT.class);



	@Before
	public void setUp() throws NamingException {
		log.debug("*** Set up for EndToEndIT ***");
		super.setUp();
	}

	@Test
	public void endToEnd() throws BuyerMgmtRemoteException, NamingException {
		log.debug(" **** endToEnd **** ");
		
		runAs(admin1User, admin1Password);
		log.info("reset sales databases");
		boolean isReset = testSupportSales.resetAll();
		Assert.assertTrue(isReset);
		
		runAs(admin2User, admin2Password);
		log.info("reset bidbot databases");
		isReset = super.testSupport.reset();
		Assert.assertTrue(isReset);

		log.info("ingest data");
		try {
			runAs(admin2User, admin2Password);
			parser.ingest();
		} catch (Exception e) {
			log.error("Parser ingest failed", e);
			Assert.fail("Parser ingest failed: " + e.getMessage());
		}
		runAs(knownUser, knownPassword);

		// createAccount for seller, buyer1, and buyer2 in eSales
		AccountDTO seller = new AccountDTO(user1User, "John", "s", "Hopkins",
				"seller@jhu.edu");
		AccountDTO buyer1 = new AccountDTO(user2User, "Alexander", "X",
				"Kossiakoff", "kossi@jhuapl.edu");
		AccountDTO buyer2 = new AccountDTO(user3User, "Ralph", "D.", "Semmel",
				"Ralph.Semmel@jhuapl.edu");

		// The Injestor already creates user1 (my seller)
		// try {
		// accountManager.createAccountDTO(seller);
		// } catch (AccountMgmtRemoteException e) {
		// Assert.fail("Create seller failed");
		// }
		// The Injestor already creates user2 (my buyer1)
		// try{
		// accountManager.createAccountDTO(buyer1);
		// } catch (AccountMgmtRemoteException f) {
		// Assert.fail("Create buyer1 failed");
		// }
		// The Injestor already creates user3 (my buyer2)
		// try {
		// accountManager.createAccountDTO(buyer2);
		// } catch (AccountMgmtRemoteException g) {
		// Assert.fail("Create buyer2 failed");
		// }

		log.info("createAccount for buyer2 in eBidbot::TODO");
		// TODO: orderManager.createAccountDTO(buyer2);

		log.info("createAuction for seller");
		runAs(user1User, user1Password);
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		cal.add(Calendar.SECOND, 10);
		Date end = cal.getTime();
		AuctionDTO auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", now , end, 18.00f, seller.userId,
				seller.email, true);
		try {
			auction.id = sellerManager.createAuction(auction);
		} catch (SellerMgmtRemoteException e) {
			log.warn("Create Auction Failed", e);
			Assert.fail("Create Auction Failed");
		}

		log.info("getUserAuctions for seller");
		Collection<AuctionDTO> userAuctions = sellerManager.getUserAuctions();

		Assert.assertNotNull("User auctions came back null", userAuctions);
		for (AuctionDTO auctionDTO : userAuctions) {
			log.info("Auction: " + auctionDTO.toString());
		}
		Assert.assertEquals("There should have been two auctions, one parsed and one manually entered", 2,
				userAuctions.size());

		log.info("getAuction for the one created in earlier step");
		runAs(user2User, user2Password);
		AuctionDTO gotAuction = null;
		try {
			gotAuction = buyerManager.getAuctionDTO(auction.id);
		} catch (BuyerMgmtRemoteException h) {
			Assert.fail("Buyer manager threw exception on getAuctionDTO");
		}
		Assert.assertEquals("requested " + auction.id + " and retreived "
				+ gotAuction.id + " auction id was different", auction.id,
				gotAuction.id);

		log.info("getOpenAuctions");
		Collection<AuctionDTO> gotOpenAuctions = buyerManager.getOpenAuctions();
		Assert.assertNotSame("Auctions not open", 0, gotOpenAuctions.size());
		// placeBid for buyer1
		BidResultDTO bidResult = buyerManager.placeBid(gotAuction.id, 1f);
		Assert.assertNotNull("Bid invalid: " + bidResult.result, bidResult.bid);

		log.info("getAuctions for buyer1");
		// :: NOTE :: This isn't a defined interface in the buyerManager,
		// so I'm going to assume that this meant to get bids for buyer1
		Collection<BidDTO> gotBids = buyerManager.listMyOpenBids();
		Assert.assertNotNull("Bids came back null", gotBids);

		log.info("placeOrder for buyer2 in eBidbot (stimulate a bid)");
		// TODO: runAs(user3User, user3Password);
		log.info("TODO: orderManager.placeOrder(buyer2.userId)");

		log.info("getAuction to verify bids were placed for buyer1 and buyer2");
		runAs(user1User, user1Password);
		gotAuction = sellerManager.getAuction(auction.id);
		Assert.assertNotNull("Auction item came back null", gotAuction);
		Assert.assertTrue("Buyer1's bid not entered",
				gotAuction.bids.size() >= 1);

		log.info("TODO: Assert.assertTrue(\"Buyer2's bids not entered\", gotAuction.bids.size() >= 2);");

	}
}
