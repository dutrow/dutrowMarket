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

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.AccountMgmtRemoteException;
import dutrow.sales.ejb.BuyerMgmtRemoteException;
import dutrow.sales.ejb.SellerMgmtRemoteException;

/**
 * @author dutroda1
 * 
 */
public class EndToEndIT extends BidbotSupport {
	private static final Log log = LogFactory.getLog(EndToEndIT.class);



	@Before
	public void setUp() throws NamingException {
		log.debug("*** Set up for EndToEndIT ***");
		super.setUp();
	}

	@Test
	public void endToEnd() throws BuyerMgmtRemoteException, NamingException, InterruptedException {
		log.debug(" **** endToEnd **** ");
		
//		eSales initializes the EJB Timer to check for expired auctions
//		eBidbot initializes the EJB Timer to check auctions associated with its bids.
//		* This happens automatically
		
		
//		admin1 resets all eSales tables (using the eSalesTestUtilEJB)
//		admin1 populates the eDmv tables (using the ESalesIngestor)		
		runAs(admin1User, admin1Password);
		log.info("reset sales databases");
		boolean isReset = testSupportSales.resetAll();
		Assert.assertTrue(isReset);
		log.info("ingest data for eSales");
		try {
			parser.ingest();
		} catch (Exception e) {
			log.error("Parser ingest failed", e);
			Assert.fail("Parser ingest failed: " + e.getMessage());
		}

//		admin2 resets the eBidbot tables (using the EBidbotTestUtilEJB)
		runAs(admin2User, admin2Password);
		log.info("reset bidbot databases");
		isReset = super.testSupport.reset();
		Assert.assertTrue(isReset);
		
//		admin2 populates the eBidbot tables (using the EBidTestUtilEJB) if necessary.
//		log.info("ingest data");
//		try {
//			EBidTestUtilEJB.ingest();
//		} catch (Exception e) {
//			log.error("Parser ingest failed", e);
//			Assert.fail("Parser ingest failed: " + e.getMessage());
//		}
//		Suggest adding account for user3 at this point.
//		yeah, let's just do that
		BidAccount user3 = orderManager.createAccount(user3User, user3User, user3Password);		
		Assert.assertNotNull("User 3 not created", user3);
		
		runAs(knownUser, knownPassword);

		AccountDTO seller = null;
		try {
			seller = accountManager.getAccountDTO(user1User);
		} catch (AccountMgmtRemoteException e1) {
			Assert.fail("account manager could not get user1");
		}
		
//		user1 creates auction
		log.info("createAuction for seller");
		runAs(user1User, user1Password);
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		cal.add(Calendar.SECOND, 20);
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
			log.info("Found the auction we're going to bid on: " + gotAuction.toString());
		} catch (BuyerMgmtRemoteException h) {
			Assert.fail("Buyer manager threw exception on getAuctionDTO");
		}
		Assert.assertEquals("requested " + auction.id + " and retreived "
				+ gotAuction.id + " auction id was different", auction.id,
				gotAuction.id);

//		user2 gets a list of open auctions
		log.info("getOpenAuctions");
		Collection<AuctionDTO> gotOpenAuctions = buyerManager.getOpenAuctions();
		Assert.assertNotSame("Auctions not open", 0, gotOpenAuctions.size());
//		user2 places bid on an auction
		BidResultDTO bidResult = buyerManager.placeBid(gotAuction.id, 20f);
		Assert.assertNotNull("Bid invalid: " + bidResult.result, bidResult.bid);
		log.info(bidResult.result);

//		user2 views the current status of the auction they are bidding on
		AuctionDTO currStatus = buyerManager.getAuctionDTO(gotAuction.id);
		log.info("user2 sees auction status is " + currStatus.isOpen);
		Assert.assertTrue(currStatus.isOpen);
		log.info(currStatus.toString());
		log.info("highestBid: " +  currStatus.highestBid());	
		Assert.assertEquals("Bid amount not what was expected", 20f, currStatus.highestBid());
		
		log.info("getAuctions for buyer1");
		// :: NOTE :: This isn't a defined interface in the buyerManager,
		// so I'm going to assume that this meant to get bids for buyer1
		Collection<BidDTO> gotBids = buyerManager.listMyOpenBids();
		Assert.assertNotNull("Bids came back null", gotBids);

//		user3 gets a list of open auctions
		runAs(user3User, user3Password);
		Collection<AuctionDTO> openAuctions = buyerManager.getOpenAuctions();
		for (AuctionDTO auctionDTO : openAuctions) {
			// do nothing;
		}
		
//		user3 views the current status of the auction that was bid by user2
		currStatus = buyerManager.getAuctionDTO(gotAuction.id);
		log.info("user3 sees auction status is " + currStatus.isOpen);
		Assert.assertTrue("Auction not open", currStatus.isOpen);
		
//		user3 places order with eBidbot		
		runAs(user3User, user3Password);
		long user3Order = orderManager.createOrder(gotAuction.id, 23f, 35f);
//		eBidbot EJB places bid for user3

		currStatus = buyerManager.getAuctionDTO(gotAuction.id);
		log.info(currStatus.toString());
		Assert.assertEquals("Bid amount not what was expected", 23f, currStatus.highestBid());
		
//		user2 places another bid on auction
		runAs(user2User, user2Password);
		bidResult = buyerManager.placeBid(gotAuction.id, 25f);
		Assert.assertNotNull("Bid invalid: " + bidResult.result, bidResult.bid);
		log.info(bidResult.result);
		currStatus = buyerManager.getAuctionDTO(gotAuction.id);
		log.info(currStatus.toString());
		Assert.assertEquals("Bid amount not what was expected", 25f, currStatus.highestBid());

		log.info("Sleeping so that the auction can play out");
		Thread.sleep(11000);
//		eBidbot EJB wakes up again from an EJBTimer
//		eBidbot EJB sees they have been raised and places another bid for user3
//		* This happens automatically
		currStatus = buyerManager.getAuctionDTO(gotAuction.id);
		log.info(currStatus.toString());
		Assert.assertEquals("Bid amount not what was expected", 26f, currStatus.highestBid());
		
		log.info("Sleeping so that the auction can finish");
		Thread.sleep(16000);
//		eSales EJB wakes up from an EJBTimer and closes the auction
//		eSales EJB publishes a message to the topic that informs everyone of the closing and that user3 has win.
//		* This happens automatically

//		The eBidbot MDB receives the message and updates the order.
//		* This happens automatically		

//		The stand-alone client recieves the message if it matches their JMS selector for a specific category.
//		* This happens manually: from dutrowSalesImpl/ run  $mvn clean install -DskipTests; ant -f target/test-classes/jmsNotifier-ant.xml subscriber

//		user3 checks their order with eBidbot and finds out they won
		runAs(user3User, user3Password);
		boolean stat = orderManager.getOrderStatus(user3Order);
		log.info("user3 order status: " + stat);
		Assert.assertTrue("User 3 should have won", stat);
		
		log.info("YOU WIN!");
	}
}
