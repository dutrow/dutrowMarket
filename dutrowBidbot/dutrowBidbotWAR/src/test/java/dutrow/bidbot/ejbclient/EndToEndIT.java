/**
 * 
 */
package dutrow.bidbot.ejbclient;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Collection;

import javax.ejb.EJB;
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

/**
 * @author dutroda1
 * 
 */
public class EndToEndIT extends Support {
	private static final Log log = LogFactory.getLog(EndToEndIT.class);

//  runAs(admin2User, admin2Password);	
//	runAs(user3User, user3Password);
	
/*	 
				
	@EJB(lookup="ejb:dutrowSalesEAR/dutrowSalesEJB/SellerMgmtEJB!dutrow.sales.ejb.SellerMgmtRemote")
	private SellerMgmtRemote sellerManager;

	private static final String buyerJNDI = 
					"java:jboss/dutrowSalesEAR/dutrowSalesEJB/BuyerMgmtEJB!dutrow.sales.ejb.BuyerMgmtRemote";
	@EJB
	private BuyerMgmtRemote buyerManager;

	private static final String accountJNDI = 
					"java:jboss/dutrowSalesEAR/dutrowSalesEJB/AccountMgmtEJB!dutrow.sales.ejb.AccountMgmtRemote";
	@EJB
	private AccountMgmtRemote accountManager;

	public static final String parserJNDI = 
					"java:jboss/dutrowSalesEAR/dutrowSalesEJB/ParserEJB!dutrow.sales.ejb.ParserRemote";
	@EJB
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
	public void endToEnd() throws BuyerMgmtRemoteException {

		// reset databases
		boolean isReset = testSupport.reset();
		Assert.assertTrue(isReset);

		// ingest data
		try {
			parser.ingest();
		} catch (Exception e) {
			Assert.fail("Parser ingest failed");
		}

		// createAccount for seller, buyer1, and buyer2 in eSales
		AccountDTO seller = new AccountDTO("seller", "John", "s", "Hopkins",
				"seller@jhu.edu");
		AccountDTO buyer1 = new AccountDTO("buyer1", "Alexander", "X",
				"Kossiakoff", "kossi@jhuapl.edu");
		AccountDTO buyer2 = new AccountDTO("buyer2", "Ralph", "D.", "Semmel",
				"Ralph.Semmel@jhuapl.edu");
		try {
			accountManager.createAccountDTO(seller);
			accountManager.createAccountDTO(buyer1);
			accountManager.createAccountDTO(buyer2);
		} catch (AccountMgmtRemoteException e) {
			Assert.fail("Create accounts failed");
		}

		// createAccount for buyer2 in eBidbot
		// TODO: orderManager.createAccountDTO(buyer2);

		// createAuction for seller
		AuctionDTO auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", Calendar
						.getInstance().getTime(), 18.00f, seller.userId,
				seller.email, true);
		auction.id = sellerManager.createAuction(auction);

		// getUserAuctions for seller
		Collection<AuctionDTO> userAuctions = sellerManager
				.getUserAuctions(seller.userId);
		Assert.assertNotNull("User auctions came back null", userAuctions);
		Assert.assertEquals("There should have been one auction", 1,
				userAuctions.size());

		// getAuction for the one created in earlier step
		AuctionDTO gotAuction = null;
		try {
			gotAuction = buyerManager.getAuctionDTO(auction.id);
		} catch (BuyerMgmtRemoteException e) {
			Assert.fail("Buyer manager threw exception on getAuctionDTO");
		}
		Assert.assertEquals("requested " + auction.id + " and retreived "
				+ gotAuction.id + " auction id was different", auction.id,
				gotAuction.id);

		// getOpenAuctions
		Collection<AuctionDTO> gotOpenAuctions = buyerManager.getOpenAuctions();
		Assert.assertNotSame("Auctions not open", 0, gotOpenAuctions.size());
		// placeBid for buyer1
		BidResultDTO bidResult = buyerManager.placeBid(buyer1.userId,
				gotAuction.id, 1f);
		Assert.assertNotNull("Bid invalid: " + bidResult.result, bidResult.bid);

		// getAuctions for buyer1
		// :: NOTE :: This isn't a defined interface in the buyerManager,
		// so I'm going to assume that this meant to get bids for buyer1
		Collection<BidDTO> gotBids = buyerManager.listMyOpenBids(buyer1.userId);
		Assert.assertNotNull("Bids came back null", gotBids);

		// placeOrder for buyer2 in eBidbot (stimulate a bid)
		// TODO: orderManager.placeOrder(buyer2.userId)

		// getAuction to verify bids were placed for buyer1 and buyer2
		gotAuction = sellerManager.getAuction(auction.id);
		Assert.assertNotNull("Auction item came back null", gotAuction);
		Assert.assertTrue("Buyer1's bid not entered",
				gotAuction.bids.size() >= 1);
		// TODO: Assert.assertTrue("Buyer2's bids not entered",
		// gotAuction.bids.size() >= 2);

	}
	*/
}
