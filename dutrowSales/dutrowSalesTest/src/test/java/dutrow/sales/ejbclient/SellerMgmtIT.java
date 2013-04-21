/**
 * 
 */
package dutrow.sales.ejbclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Collection;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.ImageDTO;
import dutrow.sales.ejb.SellerMgmtRemote;

/**
 * @author dutroda1
 * 
 */
public class SellerMgmtIT extends Support {
	private static final Log log = LogFactory.getLog(SellerMgmtIT.class);

	private static final String registrarJNDI = System
			.getProperty("jndi.name.registrar",
					"dutrowSalesEAR/dutrowSalesEJB/SellerMgmtEJB!dutrow.sales.ejb.SellerMgmtRemote");
	private SellerMgmtRemote sellerManager;

	private AccountDTO seller;
	private AccountDTO bidder;
	private AuctionDTO auction;
	long auctionId;

	public void configureJndi() {
		assertNotNull("jndi.name.registrar not supplied", registrarJNDI);

		log.debug("jndi name:" + registrarJNDI);
		try {
			sellerManager = (SellerMgmtRemote) jndi.lookup(registrarJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("sellerManager=" + sellerManager);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();

		configureJndi();

		log.debug("*** Set up for SellerMgmtIT ***");
		log.debug("testSupport=" + testSupport);
		seller = new AccountDTO("seller", "John", "s", "Hopkins",
				"seller@jhu.edu");
		testSupport.createAccount(seller);
		bidder = new AccountDTO("bidder", "Alexander", "X", "Kossiakoff",
				"kossi@jhuapl.edu");
		testSupport.createAccount(bidder);
		auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", Calendar
						.getInstance().getTime(), 18.00f, seller, true);
		auction.id = testSupport.createAuction(auction);

		log.debug("bidder.userId: " + bidder.userId + " seller.userId: "
				+ seller.userId + " auction.id: " + auction.id);

		seller = new AccountDTO("seller", "John", "s", "Hopkins",
				"seller@jhu.edu");
		testSupport.createAccount(seller);

	}

	/**
	 * Test method for
	 * {@link dutrow.sales.ejb.SellerMgmtEJB#createAuction(dutrow.sales.dto.AuctionDTO)}
	 * .
	 */
	@Test
	public void testCreateAuction() {
		auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", Calendar
						.getInstance().getTime(), 18.00f, seller, true);
		auction.id = sellerManager.createAuction(auction);

		Assert.assertNotSame("Auction not created", 0, auction.id);
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.ejb.SellerMgmtEJB#listMyAuctions(java.lang.String)}.
	 */
	@Test
	public void testListMyAuctions() {
		log.info("*** testListMyAuctions() *** ");
		Collection<AuctionDTO> myA = sellerManager
				.listMyAuctions(seller.userId);
		Assert.assertNotNull("My auctions list is null", myA);
		for (AuctionDTO auctionDTO : myA) {
			log.info(auctionDTO);
		}

	}

	/**
	 * Test method for
	 * {@link dutrow.sales.ejb.SellerMgmtEJB#listMyOpenAuctions(java.lang.String)}
	 * .
	 */
	@Test
	public void testListMyOpenAuctions() {
		log.debug("*** testListMyOpenAuctions() *** ");
		Collection<AuctionDTO> myOA = sellerManager
				.listMyOpenAuctions(seller.userId);
		Assert.assertNotNull("My auctions list is null", myOA);
		for (AuctionDTO auctionDTO : myOA) {
			log.info(auctionDTO);
		}
	}

	/**
	 * Test method for {@link dutrow.sales.ejb.SellerMgmtEJB#getAuction(long)}.
	 */
	@Test
	public void testGetAuction() {
		log.debug("*** testGetAuction() *** ");
		AuctionDTO ana = sellerManager.getAuction(auction.id);
		Assert.assertNotNull("Failure to getAuction for auction: "
				+ auctionId, ana);
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.ejb.SellerMgmtEJB#getAuctionImages(long)}.
	 */
	@Test
	public void testGetAuctionImages() {
		log.debug("*** testGetAuctionImages() *** ");
		// TODO: add an image to an auction
		Collection<ImageDTO> images = sellerManager.getAuctionImages(auction.id);
		Assert.assertNotNull("Failure to getAuctionImages for auction: "
				+ auctionId, images);
	}


}
