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
import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.AccountMgmtRemoteException;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemoteException;
import dutrow.sales.ejb.ParserRemote;
import dutrow.sales.ejb.SellerMgmtRemote;
import ejava.util.ejb.EJBClient;

/**
 * @author dutroda1
 * 
 */
public class EndToEndIT extends Support {
	private static final Log log = LogFactory.getLog(EndToEndIT.class);

	private static final String sellerJNDI = System
			.getProperty("jndi.name.registrar",
					"dutrowSalesEAR/dutrowSalesEJB/SellerMgmtEJB!dutrow.sales.ejb.SellerMgmtRemote");
	private SellerMgmtRemote sellerManager;

	private static final String buyerJNDI = System
			.getProperty("jndi.name.registrar",
					"dutrowSalesEAR/dutrowSalesEJB/BuyerMgmtEJB!dutrow.sales.ejb.BuyerMgmtRemote");
	private BuyerMgmtRemote buyerManager;

	private static final String accountJNDI = System
			.getProperty(
					"jndi.name.registrar",
					"dutrowSalesEAR/dutrowSalesEJB/AccountMgmtEJB!dutrow.sales.ejb.AccountMgmtRemote");
	private AccountMgmtRemote accountManager;

	public static final String parserJNDI = System.getProperty("jndi.name",
			EJBClient.getRemoteLookupName("dutrowSalesEAR", "dutrowSalesEJB",
					"ParserEJB", ParserRemote.class.getName()));

	private static ParserRemote parser;

	public void configureJndi() {
		assertNotNull("jndi.name.registrar not supplied", sellerJNDI);
		assertNotNull("jndi.name.registrar not supplied", buyerJNDI);
		assertNotNull("jndi.name.registrar not supplied", accountJNDI);

		log.debug("seller jndi name:" + sellerJNDI);
		log.debug("account jndi name:" + accountJNDI);
		log.debug("buyer jndi name:" + buyerJNDI);
		log.debug("parser jndi name: " + parserJNDI);

		try {
			sellerManager = (SellerMgmtRemote) jndi.lookup(sellerJNDI);
			accountManager = (AccountMgmtRemote) jndi.lookup(accountJNDI);
			buyerManager = (BuyerMgmtRemote) jndi.lookup(buyerJNDI);
			parser = (ParserRemote) jndi.lookup(parserJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("sellerManager=" + sellerManager);
		log.debug("accountManager=" + accountManager);
		log.debug("buyerManager=" + buyerManager);
		log.debug("parser=" + parser);

	}

	@Before
	public void setUp() throws Exception {
		log.debug("*** Set up for EndToEndIT ***");
		super.setUp();

		configureJndi();

		log.debug("testSupport=" + testSupport);
	}

	@Test
	public void endToEnd() throws BuyerMgmtRemoteException, NamingException {
		log.debug(" **** endToEnd **** ");
		runAs(admin1User, admin1Password);
		log.info("reset databases");
		boolean isReset = testSupport.resetAll();
		Assert.assertTrue(isReset);

		log.info("ingest data");
		try {
			parser.ingest();
		} catch (Exception e) {
			Assert.fail("Parser ingest failed");
		}

		// createAccount for seller, buyer1, and buyer2 in eSales
		AccountDTO seller = new AccountDTO(user1User, "John", "s", "Hopkins",
				"seller@jhu.edu");
		AccountDTO buyer1 = new AccountDTO(user2User, "Alexander", "X",
				"Kossiakoff", "kossi@jhuapl.edu");
		AccountDTO buyer2 = new AccountDTO(user3User, "Ralph", "D.", "Semmel",
				"Ralph.Semmel@jhuapl.edu");

		runAs(knownUser, knownPassword);
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
		AuctionDTO auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", Calendar
						.getInstance().getTime(), 18.00f, seller.userId,
				seller.email, true);
		auction.id = sellerManager.createAuction(auction);

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
