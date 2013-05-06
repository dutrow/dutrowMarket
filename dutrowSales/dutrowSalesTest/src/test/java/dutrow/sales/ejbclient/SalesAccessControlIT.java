/**
 * 
 */
package dutrow.sales.ejbclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Collection;

import javax.ejb.EJBAccessException;
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
public class SalesAccessControlIT extends SalesSupport {
	private static final Log log = LogFactory.getLog(SalesAccessControlIT.class);


	@Test
	public void accessControlTest() throws BuyerMgmtRemoteException,
			NamingException {
		log.debug(" **** accessControlTest **** ");
		// not runAs(admin1User, admin1Password);
		log.info("reset databases");
		boolean isReset = false;
		try {
			isReset = testSupport.resetAll();
		} catch (EJBAccessException ae) {
			log.info("Caught EJBAccessException: good!");
		}
		Assert.assertFalse(isReset);

		// createAccount for seller, buyer1, and buyer2 in eSales
		AccountDTO seller = new AccountDTO(user1User, "John", "s", "Hopkins",
				"seller@jhu.edu");
		AccountDTO buyer1 = new AccountDTO(user2User, "Alexander", "X",
				"Kossiakoff", "kossi@jhuapl.edu");
		AccountDTO buyer2 = new AccountDTO(user3User, "Ralph", "D.", "Semmel",
				"Ralph.Semmel@jhuapl.edu");

		// not runAs(knownUser, knownPassword);

		log.info("createAccount for buyer2 in eBidbot::TODO");
		// TODO: orderManager.createAccountDTO(buyer2);

		log.info("createAuction for seller");
		// not runAs(user1User, user1Password);
		AuctionDTO auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", Calendar
						.getInstance().getTime(), 18.00f, seller.userId,
				seller.email, true);
		try {
			auction.id = sellerManager.createAuction(auction);
			Assert.fail("EJB Access Should Fail");
		} catch (EJBAccessException ae) {
			log.info("Caught EJBAccessException: good!");
		}

		log.info("getUserAuctions for seller");
		try {
			Collection<AuctionDTO> userAuctions = sellerManager
					.getUserAuctions();
			for (AuctionDTO auctionDTO : userAuctions) {
				log.info("Auction: " + auctionDTO.toString());
			}
			Assert.fail("EJB Access Should Fail");
		} catch (EJBAccessException ae) {
			log.info("Caught EJBAccessException: good!");
		}

		log.info("getAuction for the one created in earlier step");
		// not runAs(user2User, user2Password);
		AuctionDTO gotAuction = null;
		try {
			gotAuction = buyerManager.getAuctionDTO(auction.id);
		} catch (EJBAccessException ae) {
			Assert.fail("EJB Access Should be PermitAll");
		}
		

		try {
			log.info("getAuctions for buyer1");
			Collection<BidDTO> gotBids = buyerManager.listMyOpenBids();
			Assert.fail("EJB Access Should Fail");
		} catch (EJBAccessException ae) {
			log.info("Caught EJBAccessException: good!");
		}

		try {
			log.info("placeOrder for buyer2 in eBidbot (stimulate a bid)");
			// TODO: runAs(user3User, user3Password);
			log.info("TODO: orderManager.placeOrder(buyer2.userId)");
			// TODO: Assert.fail("EJB Access Should Fail");
		} catch (EJBAccessException ae) {
			log.info("Caught EJBAccessException: good!");
		}

		try {
			log.info("getAuction to verify bids were placed for buyer1 and buyer2");
			// not runAs(user1User, user1Password);
			gotAuction = sellerManager.getAuction(auction.id);
			Assert.assertNotNull("Auction item came back null", gotAuction);
			Assert.assertTrue("Buyer1's bid not entered",
					gotAuction.bids.size() >= 1);
			Assert.fail("EJB Access Should Fail");
		} catch (EJBAccessException ae) {
			log.info("Caught EJBAccessException: good!");
		}

		try {
			log.info("TODO: Assert.assertTrue(\"Buyer2's bids not entered\", gotAuction.bids.size() >= 2);");
		//TODO:	Assert.fail("EJB Access Should Fail");
		} catch (EJBAccessException ae) {
			log.info("Caught EJBAccessException: good!");
		}

	}
}
