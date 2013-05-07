/**
 * 
 */
package dutrow.sales.ejbclient;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJBException;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemoteException;

/**
 * @author dutroda1
 * 
 */
public class TransactionDemoIT extends SalesSupport {
	private static final Log log = LogFactory
			.getLog(TransactionDemoIT.class);

	private AccountDTO seller;
	private AccountDTO bidder;
	private AuctionDTO auction;
	long auctionId;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		runAs(admin1User, admin1Password);
		configureJndi();

		log.debug("*** Set up for BuyerMgmtIT ***");
		log.debug("testSupport=" + testSupport);
		seller = new AccountDTO("seller", "John", "s", "Hopkins",
				"seller@jhu.edu");
		testSupport.createAccount(seller);
		bidder = new AccountDTO("bidder", "Alexander", "X", "Kossiakoff",
				"kossi@jhuapl.edu");
		testSupport.createAccount(bidder);
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		cal.add(Calendar.SECOND, 10);
		Date end = cal.getTime();
		auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", now, end, 18.00f, seller.userId,
				seller.email, true);
		auction.id = testSupport.createAuction(auction);

		log.debug("bidder.userId: " + bidder.userId + " seller.userId: "
				+ seller.userId + " auction.id: " + auction.id);

		Assert.assertNotSame("Auction not created", 0, auction.id);
		runAs(knownUser, knownPassword);
	}

	@Test
	public void transactionDemonstration() throws BuyerMgmtRemoteException, NamingException {
		log.debug("*** transactionDemonstration() *** ");
		runAs(user1User, user1Password);
		try {
			float[] bidarray = { 1.0f, 2.0f, 0.30f };
			buyerManager.placeMultiBid(auction.id, bidarray);
		} catch (EJBException expected) {
			log.info("encountered expected exception:" + expected.getMessage());
		}
		// ROLLBACK check
		try {

			AuctionDTO rolledBackAuction = buyerManager
					.getAuctionDTO(auction.id);
			if (rolledBackAuction.bids.size() == 0) {
				log.info("all transactions have been rolled back");
			} else {
				Assert.fail("transactions were not rolled back");
			}
		} catch (BuyerMgmtRemoteException e) {
			log.warn("error when gettingAuctionDTO", e);
		}

	}

}
