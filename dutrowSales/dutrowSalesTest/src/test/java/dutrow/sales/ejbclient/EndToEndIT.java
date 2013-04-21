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
import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.SellerMgmtRemote;

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

	private AccountDTO seller;
	private AccountDTO bidder;
	private AuctionDTO auction;
	long auctionId;

	public void configureJndi() {
		assertNotNull("jndi.name.registrar not supplied", sellerJNDI);
		assertNotNull("jndi.name.registrar not supplied", buyerJNDI);
		assertNotNull("jndi.name.registrar not supplied", accountJNDI);

		log.debug("seller jndi name:" + sellerJNDI);
		try {
			sellerManager = (SellerMgmtRemote) jndi.lookup(sellerJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("sellerManager=" + sellerManager);

		log.debug("account jndi name:" + accountJNDI);
		try {
			accountManager = (AccountMgmtRemote) jndi.lookup(accountJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("accountManager=" + accountManager);

		log.debug("jndi name:" + buyerJNDI);
		try {
			buyerManager = (BuyerMgmtRemote) jndi.lookup(buyerJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("buyerManager=" + buyerManager);

	}

	@Before
	public void setUp() throws Exception {
		log.debug("*** Set up for EndToEndIT ***");
		super.setUp();

		configureJndi();

		log.debug("testSupport=" + testSupport);
	}

	@Test
	public void endToEnd() {

		// reset databases
		boolean isReset = testSupport.resetAll();
		Assert.assertTrue(isReset);
		// ingest data
		// createAccount for seller, buyer1, and buyer2 in eSales
		// createAccount for buyer2 in eBidbot
		// createAuction for seller
		// getUserAuctions for seller
		// getAuction for the one created in earlier step
		// getOpenAuctions
		// placeBid for buyer1
		// getAuctions for buyer1
		// placeOrder for buyer2 in eBidbot (stimulate a bid)
		// getAuction to verify bids were placed for buyer1 and buyer2

	}

}
