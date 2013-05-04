/**
 * 
 */
package dutrow.sales.ejbclient;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

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
public class TransactionDemonstrationIT extends Support {
	private static final Log log = LogFactory
			.getLog(TransactionDemonstrationIT.class);

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

		Assert.assertNotSame("Auction not created", 0, auction.id);
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
