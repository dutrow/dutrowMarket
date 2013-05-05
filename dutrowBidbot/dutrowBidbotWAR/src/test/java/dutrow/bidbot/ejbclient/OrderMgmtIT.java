/**
 * 
 */
package dutrow.bidbot.ejbclient;

import java.util.Calendar;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.BuyerMgmtRemoteException;

/**
 * @author dutroda1
 * 
 */
public class OrderMgmtIT extends Support {
	private static Log log = LogFactory.getLog(OrderMgmtIT.class);

	private BidAccount bidAccount;

	private AuctionDTO auction;

	private AccountDTO seller;

	private AccountDTO buyer1;

	/**
	 * @throws NamingException
	 */
	@Before
	public void setUp() throws NamingException {
		super.setUp();
		log.debug("Set up for OrderMgmtIT");
		configureJndi();

		runAs(admin1User, admin1Password);
		seller = new AccountDTO("user1", "John", "s", "Hopkins",
				"seller@jhu.edu");
		testSupportSales.createAccount(seller);
		buyer1 = new AccountDTO("user2", "Alexander", "X", "Kossiakoff",
				"kossi@jhuapl.edu");
		testSupportSales.createAccount(buyer1);
		auction = new AuctionDTO("VT Fuse", "Science & Toys",
				"detonates an explosive device automatically", Calendar
						.getInstance().getTime(), 18.00f, seller.userId,
				seller.email, true);
		auction.id = testSupportSales.createAuction(auction);

		log.debug("bidder.userId: " + buyer1.userId + " seller.userId: "
				+ seller.userId + " auction.id: " + auction.id);

		runAs(admin2User, admin2Password);
		bidAccount = testSupport.createBidder();
		Assert.assertNotNull("BidAccount is null", bidAccount);
		orderManager.createAccount(bidAccount);
		Assert.assertNotNull("Created account null", bidAccount);

		runAs(knownUser, knownPassword);

	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.ejb.OrderMgmtEJB#createOrder(dutrow.bidbot.bo.BidOrder)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws BuyerMgmtRemoteException
	 */
	@Test
	public void testCreateOrder() throws NamingException,
			BuyerMgmtRemoteException {

		runAs(user3User, user3Password);
		BidOrder bo1 = new BidOrder(auction.id, 4.5f, 7.5f, bidAccount);
		bo1.setBidOrderId(orderManager.createOrder(bo1));
		long bid1 = bo1.getBidOrderId();
		Assert.assertNotSame("Create order problem", 0, bid1);
		BidOrder bo2 = orderManager.getOrder(bid1);
		Assert.assertNotNull("returned BidOrder is null", bo2);
		Assert.assertEquals("Auction Id Test", bo1.getAuctionId(),
				bo2.getAuctionId());
		Assert.assertEquals("Bidder Sales Account Test", bo1.getBidder()
				.getSalesAccount(), bo2.getBidder().getSalesAccount());
		Assert.assertEquals("Start Bid Test", 0,
				Double.compare(bo1.getStartBid(), bo2.getStartBid()));
		Assert.assertEquals("Result Test", bo1.getResult(), bo2.getResult());

	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.ejb.OrderMgmtEJB#placeBid(dutrow.bidbot.bo.BidOrder, float)}
	 * .
	 * 
	 * @throws NamingException
	 * @throws BuyerMgmtRemoteException
	 */
	@Test
	public void testPlaceBid() throws NamingException, BuyerMgmtRemoteException {

		runAs(user3User, user3Password);
		BidOrder bo1 = new BidOrder(auction.id, 4.5f, 7.5f, bidAccount);
		bo1.setBidOrderId(orderManager.createOrder(bo1));
		long bid1 = bo1.getBidOrderId();
		BidOrder bo2 = orderManager.getOrder(bid1);

		runAs(user3User, user3Password);
		orderManager.createOrder(bo1);
		BidResultDTO resultGood = orderManager.placeBid(auction.id, bidAccount, 5f);
		Assert.assertNotNull(resultGood.result);
	}

	/**
	 * Test method for {@link dutrow.bidbot.ejb.OrderMgmtEJB#endOrder(long)}.
	 * 
	 * @throws NamingException
	 * @throws BuyerMgmtRemoteException
	 */
	@Test
	public void testEndOrder() throws NamingException, BuyerMgmtRemoteException {

		runAs(user3User, user3Password);
		BidOrder bo = new BidOrder(auction.id, 4.5f, 7.5f, bidAccount);
		bo.setBidOrderId(orderManager.createOrder(bo));
		Assert.assertNotSame("Create Order", 0, bo.getBidOrderId());

		boolean endorder = orderManager.endOrder(bo.getBidOrderId());
		Assert.assertTrue("Order not properly ended.", endorder);
	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.ejb.OrderMgmtEJB#getOrderStatus(long)}.
	 * 
	 * @throws NamingException
	 * @throws BuyerMgmtRemoteException
	 */
	@Test
	public void testGetOrderStatus() throws NamingException,
			BuyerMgmtRemoteException {
		runAs(admin2User, admin2Password);
		BidOrder bo = testSupport.createOrder(auction.id, bidAccount);

		runAs(user3User, user3Password);
		bo.setBidOrderId(orderManager.createOrder(bo));
		boolean orderStatus = orderManager.getOrderStatus(bo.getBidOrderId());
		Assert.assertFalse(orderStatus);

	}

	@Test
	public void testCreateAccount() throws NamingException {

		try {
			runAs(admin2User, admin2Password);
			orderManager.createAccount(user3User, "user2", "password");
			Assert.fail("BidAccount should not have been created -- it already exists");
		} catch (javax.ejb.EJBException ejbe){
			log.info("Expected exception because account already exists");
		} catch (IllegalArgumentException iae) {
			log.info("Expected exception because account already exists");
		}

		runAs(user3User, user3Password);

		BidAccount ba2 = orderManager.getAccount(user3User);

		Assert.assertNotNull("Retrieved account null", ba2);
		Assert.assertEquals(bidAccount.getSalesAccount(), ba2.getSalesAccount());
		Assert.assertEquals(bidAccount.getSalesPassword(),
				ba2.getSalesPassword());

	}

}
