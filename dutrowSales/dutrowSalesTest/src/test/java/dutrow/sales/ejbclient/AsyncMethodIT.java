package dutrow.sales.ejbclient;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.ejb.SellerMgmtRemote;

/**
 * This testcase provides a demonstration of asynchronous methods on session 
 * beans.
 */
public class AsyncMethodIT extends SalesSupport {
    Log log = LogFactory.getLog(AsyncMethodIT.class);


	private AccountDTO seller;
	private AccountDTO bidder;
	private AuctionDTO auction;
	long auctionId;



	@Before
	public void setUp() throws Exception {
		super.setUp();
		runAs(admin1User, admin1Password);
		configureJndi();

		log.debug(" **** Set up for AsyncMethodIT **** ");
		log.debug("testSupport=" + testSupport);
		seller = new AccountDTO("seller", "John", "s", "Hopkins",
				"seller@jhu.edu");
		testSupport.createAccount(seller);
		bidder = new AccountDTO("bidder", "Alexander", "X", "Kossiakoff",
				"kossi@jhuapl.edu");
		testSupport.createAccount(bidder	);
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

		seller = new AccountDTO("seller", "John", "s", "Hopkins",
				"seller@jhu.edu");
		testSupport.createAccount(seller);
		runAs(knownUser, knownPassword);

	}

   
    
    @Test
    public void testAsync() throws Exception {
        log.info("*** testAsync ***");
        int count=3;
        long delay=3000;
        
        long startTime = System.currentTimeMillis();
        runAs(admin1User, admin1Password);
        sellerManager.workSync(count, delay);
    	long syncTime = System.currentTimeMillis() - startTime;

        
        startTime = System.currentTimeMillis();
        sellerManager.workAsync(count, delay);
    	long asyncTime = System.currentTimeMillis() - startTime;

    	log.info(String.format("count=%d, delay=%d, syncTime=%d msecs", count, delay, syncTime));
    	log.info(String.format("count=%d, delay=%d, asyncTime=%d msecs", count, delay, asyncTime));
    }
}
