/**
 * 
 */
package dutrow.sales.ejbclient;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Collection;

import javax.ejb.EJBException;
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
import dutrow.sales.dto.ImageDTO;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemoteException;

/**
 * @author dutroda1
 * 
 */
public class BuyerMgmtIT extends Support {
	private static final Log log = LogFactory.getLog(BuyerMgmtIT.class);

	private static final String registrarJNDI = System
			.getProperty("jndi.name.registrar",
					"dutrowSalesEAR/dutrowSalesEJB/BuyerMgmtEJB!dutrow.sales.ejb.BuyerMgmtRemote");
	private BuyerMgmtRemote buyerManager;

	private AccountDTO seller;
	private AccountDTO bidder;
	private AuctionDTO auction;
	long auctionId;

	public void configureJndi() {
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
		seller = new AccountDTO(user1User, "John", "s", "Hopkins",
				"seller@jhu.edu");
		testSupport.createAccount(seller);
		bidder = new AccountDTO(user2User, "Alexander", "X", "Kossiakoff",
				"kossi@jhuapl.edu");
		testSupport.createAccount(bidder);
		auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", Calendar
						.getInstance().getTime(), 18.00f, seller.userId, seller.email, true);
		auction.id = testSupport.createAuction(auction);

		log.debug("bidder.userId: " + bidder.userId + " seller.userId: "
				+ seller.userId + " auction.id: " + auction.id);

		Assert.assertNotSame("Auction not created", 0, auction.id);
	}

	@Test
	public void testListOpenAuctions() {
		log.debug("*** testListOpenAuctions() *** ");
		Collection<AuctionDTO> openAuctions = buyerManager.getOpenAuctions();
		Assert.assertNotSame("No Open Auctions", 0, openAuctions.size());
		AuctionDTO anOpenAuction = openAuctions.iterator().next();
		Assert.assertNotNull("An Open Auction is null", anOpenAuction);
	}

	@Test
	public void testGetAuction() throws BuyerMgmtRemoteException, NamingException {
		log.debug("*** testGetAuction() *** ");
		runAs(user2User, user2Password);
		AuctionDTO theOpenAuction = buyerManager.getAuctionDTO(auction.id);
		Assert.assertNotNull("The Open Auction is null", theOpenAuction);
	}

	@Test
	public void testGetAuctionImages() throws BuyerMgmtRemoteException, NamingException {
		log.debug("*** testGetAuctionImages() *** ");
		runAs(user2User, user2Password);
		// TODO: add an image to an auction
		Collection<ImageDTO> images = buyerManager.getAuctionImages(auction.id);
		Assert.assertNotNull("Failure to getAuctionImages for auction: "
				+ auctionId, images);

	}

	@Test
	public void testListMyBids() throws BuyerMgmtRemoteException, NamingException {
		log.info("*** testListMyBids() *** ");
		runAs(user2User, user2Password);
		Collection<BidDTO> bids = buyerManager.listMyBids();
		Assert.assertNotNull("listMyBids returned null", bids);
		for (BidDTO bidDTO : bids) {
			log.info(bidDTO);
		}
	}

	@Test
	public void testListMyOpenBids() throws BuyerMgmtRemoteException, NamingException {
		log.debug("*** testListMyOpenBids() *** ");
		runAs(user2User, user2Password);
		Collection<BidDTO> bids = buyerManager.listMyOpenBids();
		Assert.assertNotNull("listMyBids returned null", bids);
		for (BidDTO bidDTO : bids) {
			log.info(bidDTO);
			AuctionDTO a = testSupport.getAuction(bidDTO.auctionItem);
			Assert.assertTrue("Bid must be open", a.isOpen);
		}
	}

	@Test
	public void testPlaceBid() throws BuyerMgmtRemoteException, NamingException {
		log.debug("*** testPlaceBid() *** ");
		runAs(user1User, user1Password);
		BidResultDTO one = buyerManager.placeBid(auction.id,
				2.00f);
		BidResultDTO two = new BidResultDTO();
		try {
			two = buyerManager.placeBid(auction.id, 1.00f);
		} catch (EJBException e) {
			log.info("Expected EJB Exception: " + e.getMessage());
		}
		BidResultDTO three = buyerManager.placeBid(auction.id,
				3.00f);

		Assert.assertNotNull("First Bid invalid: " + one.result, one.bid);
		Assert.assertNull(
				"Second Bid should have been rejected: " + two.result, two.bid);
		Assert.assertNotNull("Third Bid should have been accepted: "
				+ three.result, three.bid);
	}

}
