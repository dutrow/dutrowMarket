/**
 * 
 */
package dutrow.sales.bl;

import java.io.InputStream;
import java.util.Collection;
import java.util.SortedSet;
import java.util.Vector;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.bl.impl.IngestorImpl;
import dutrow.sales.bo.Account;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.bo.BidResult;
import dutrow.sales.bo.Image;
import dutrow.sales.bo.POC;
import dutrow.sales.dao.JPATestBase;

/**
 * @author dutroda1
 * 
 */
public class EndToEndTest extends JPATestBase{
	private static Log log = LogFactory.getLog(EndToEndTest.class);

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
		log.trace("Tear down for EndToEndTest");
		super.tearDown();
	}
	
	@Test
	public void clear(){
		log.info("Simple way to clear the database.");
	}

	@Test
	public void endToEnd() throws InterruptedException, AccountMgmtException {
		log.info("Run End-to-End Test");
		Assert.assertNotNull("Account Manager Does Not Exist", accountManager);
		Assert.assertNotNull("Buyer Manager Does Not Exist", buyerManager);
		Assert.assertNotNull("Seller Manager Does Not Exist", sellerManager);
		Assert.assertNotNull("TestSupport Does Not Exist", testSupport);

		testSupport.resetAll();// reset to starting state // for kicks and
								// giggles since I already do this in setUp
		String fileName = "xml/eSales-1.xml";
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(fileName);
		Ingestor ingestor = new IngestorImpl(is, accountDao, auctionDao); // ingest
																		// data
		try {
			ingestor.ingest();
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new InterruptedException(e.getMessage());
		} catch (XMLStreamException e) {
			e.printStackTrace();
			throw new InterruptedException(e.getMessage());
		}

		// Create a seller
		log.info("Create Seller");
		Account seller = testSupport.createSellerExample();
		String sellerId = accountManager.createAccount(seller);
		
		// Create a couple of bidders
		Vector<POC> buyers = new Vector<POC>();
		log.info("Create Buyer 1");
		Account dan = testSupport.createDan();
		accountManager.createAccount(dan);
		buyers.add(dan.getPoc());
		log.info("Create Buyer 2");
		Account jim = testSupport.createJim();
		accountManager.createAccount(jim);
		buyers.add(jim.getPoc());
		
		// Create Auction
		
		log.info("Create Auction Item");
		AuctionItem auctionDetails = testSupport.createAuctionItem(
				"silver dollar", seller);
		Image img = new Image();
		byte [] byteArray = new byte[100]; 
		img.setImage(byteArray);
		auctionDetails.getImages().add(img);
		log.info("Create Auction");
		long auctionId = sellerManager.createAuction(auctionDetails);
		log.info("Auction ID: " + auctionId);
		
		// * get auctions for seller
		log.info("Get Auctions for seller");
		Collection<AuctionItem> sellerAuctions = sellerManager
				.getUserAuctions(seller.getPoc().getUserId());
		Assert.assertNotNull("Could not find seller auctions", sellerAuctions);

		// getAuction for seller
		log.info("Get Auction Item for seller");
		AuctionItem sellerAuction = sellerManager.getAuction(auctionId);
		Assert.assertNotNull("Could not find seller auction " + auctionId,
				sellerAuction);
		
		// get auctions for buyer
		log.info("Get Auction Item for buyers");
		Collection<AuctionItem> openAuctions = buyerManager.getOpenAuctions();
		Assert.assertNotNull("Could not find buyer auctions", openAuctions);

		log.info("Get Auction Item for buyers");
		AuctionItem buyerItem = buyerManager.getAuction(auctionId);
		Assert.assertNotNull("Could not find buyer auction item " + auctionId,
				buyerItem);

		float startingBid = buyerItem.getAskingPrice();
		float nextBid = startingBid;
		log.info("Buyers: " + buyers);
		// run through once for each bidder
		for (POC b : buyers) {
			log.info("Buyer " + b.getUserId() + " placed a bid of " + nextBid);
			// place bid for buyer
			BidResult succesfulBid = buyerManager.placeBid(b.getUserId(), auctionId, nextBid);
			log.info("placed bid: " + succesfulBid.getBid());
			log.info(succesfulBid.getResult());
			Assert.assertNotNull("Bid was rejected: ", succesfulBid.getBid());

			nextBid += 1.0f;
		}

		// * getAuction (using BuyerMgmtImpl) -- i.e., showing auction has bids
		// associated.
		AuctionItem completedAuction = buyerManager.getAuction(auctionId);
		SortedSet<Bid> completedBids = completedAuction.getBids();
		Assert.assertNotNull("Completed auction should have a bidset",
				completedBids);
		Assert.assertNotNull("Completed auction should have a bid associated",
				completedBids.last());

		// * closeAuction (using AuctionMgmtImpl) -- i.e., winner declared and
		// no more bids should be accepted.
		auctionManager.closeAuction(completedAuction);
		BidResult closedBid = buyerManager.placeBid(dan.getPoc().getUserId(), auctionId,
				nextBid + 1);
		log.info(closedBid.getResult());
		Assert.assertNull("Closed auction should not accept new bids",
				closedBid.getBid());

	}

}
