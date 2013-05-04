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
		runAs(admin1User, admin1Password);
		configureJndi();

		log.debug(" **** Set up for SellerMgmtIT **** ");
		log.debug("testSupport=" + testSupport);
		seller = new AccountDTO("seller", "John", "s", "Hopkins",
				"seller@jhu.edu");
		testSupport.createAccount(seller);
		bidder = new AccountDTO("bidder", "Alexander", "X", "Kossiakoff",
				"kossi@jhuapl.edu");
		testSupport.createAccount(bidder);
		auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", Calendar
						.getInstance().getTime(), 18.00f, seller.userId,
				seller.email, true);
		auction.id = testSupport.createAuction(auction);

		log.debug("bidder.userId: " + bidder.userId + " seller.userId: "
				+ seller.userId + " auction.id: " + auction.id);

		seller = new AccountDTO("seller", "John", "s", "Hopkins",
				"seller@jhu.edu");
		testSupport.createAccount(seller);
		runAs(knownUser, knownPassword);

	}

	/**
	 * Test method for
	 * {@link dutrow.sales.ejb.SellerMgmtEJB#createAuction(dutrow.sales.dto.AuctionDTO)}
	 * .
	 * @throws NamingException 
	 */
	@Test
	public void testCreateAuction() throws NamingException {
		log.debug(" **** testCreateAuction() **** ");
		runAs(user1User, user1Password);
		auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", Calendar
						.getInstance().getTime(), 18.00f, seller.userId,
				seller.email, true);
		auction.id = sellerManager.createAuction(auction);

		Assert.assertNotSame("Auction not created", 0, auction.id);
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.ejb.SellerMgmtEJB#getUserAuctions(java.lang.String)}.
	 * @throws NamingException 
	 */
	@Test
	public void testListMyAuctions() throws NamingException {
		log.info(" **** testListMyAuctions() **** ");
		runAs(user1User, user1Password);
		Collection<AuctionDTO> myA = sellerManager.getUserAuctions();
		Assert.assertNotNull("My auctions list is null", myA);
		for (AuctionDTO auctionDTO : myA) {
			log.info(auctionDTO);
		}

	}

	/**
	 * Test method for
	 * {@link dutrow.sales.ejb.SellerMgmtEJB#getOpenUserAuctions(java.lang.String)}
	 * .
	 * @throws NamingException 
	 */
	@Test
	public void testListMyOpenAuctions() throws NamingException {
		log.debug(" **** testListMyOpenAuctions() **** ");
		runAs(user1User, user1Password);
		Collection<AuctionDTO> myOA = sellerManager.getOpenUserAuctions();
		Assert.assertNotNull("My auctions list is null", myOA);
		for (AuctionDTO auctionDTO : myOA) {
			log.info(auctionDTO);
		}
	}

	/**
	 * Test method for {@link dutrow.sales.ejb.SellerMgmtEJB#getAuction(long)}.
	 * @throws NamingException 
	 */
	@Test
	public void testGetAuction() throws NamingException {
		log.debug(" **** testGetAuction() **** ");
		runAs(user1User, user1Password);
		AuctionDTO ana = sellerManager.getAuction(auction.id);
		Assert.assertNotNull("Failure to getAuction for auction: " + auctionId,
				ana);
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.ejb.SellerMgmtEJB#getAuctionImages(long)}.
	 * @throws NamingException 
	 */
	@Test
	public void testGetAuctionImages() throws NamingException {
		log.debug(" **** testGetAuctionImages() **** ");
		runAs(user1User, user1Password);
		// TODO: add an image to an auction
		Collection<ImageDTO> images = sellerManager
				.getAuctionImages(auction.id);
		Assert.assertNotNull("Failure to getAuctionImages for auction: "
				+ auctionId, images);
	}

}
