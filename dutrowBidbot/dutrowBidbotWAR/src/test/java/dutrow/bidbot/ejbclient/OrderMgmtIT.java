/**
 * 
 */
package dutrow.bidbot.ejbclient;

import static org.junit.Assert.assertNotNull;

import javax.ejb.EJB;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.ejb.OrderMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemoteException;

/**
 * @author dutroda1
 * 
 */
public class OrderMgmtIT extends Support {
	private static Log log = LogFactory.getLog(OrderMgmtIT.class);

	private static final String registrarJNDI = System.getProperty(
			"jndi.name.registrar",
			"dutrowBidbot/OrderMgmtEJB!dutrow.bidbot.ejb.OrderMgmtRemote");
	// "dutrowBidbotWAR/OrderMgmtEJB!dutrow.bidbot.ejb.OrderMgmtRemote");
	@EJB
	private OrderMgmtRemote orderManager;

	public void configureJndi() {
		assertNotNull("jndi.name.registrar not supplied", registrarJNDI);

		log.debug("jndi name:" + registrarJNDI);
		try {
			orderManager = (OrderMgmtRemote) jndi.lookup(registrarJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("accountManager=" + orderManager);
	}

	/**
	 * @throws NamingException
	 */
	@Before
	public void setUp() throws NamingException {
		super.setUp();
		log.debug("Set up for OrderMgmtIT");
		configureJndi();
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
	public void testCreateOrder() throws NamingException, BuyerMgmtRemoteException {

		runAs(admin2User, admin2Password);
		BidAccount ba = testSupport.createBidder();
		Assert.assertNotNull("BidAccount is null", ba);
		orderManager.createAccount(ba);
		
		runAs(user3User, user3Password);
		BidOrder bo1 = new BidOrder(3, 4.5f, 7.5f, ba);
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

		runAs(admin2User, admin2Password);
		BidAccount ba = testSupport.createBidder();
		Assert.assertNotNull("BidAccount is null", ba);
		orderManager.createAccount(ba);
		
		runAs(user3User, user3Password);
		BidOrder bo1 = new BidOrder(3, 4.5f, 7.5f, ba);
		bo1.setBidOrderId(orderManager.createOrder(bo1));
		long bid1 = bo1.getBidOrderId();
		BidOrder bo2 = orderManager.getOrder(bid1);
		
		runAs(user3User, user3Password);
		orderManager.createOrder(bo1);
		Assert.assertTrue(orderManager.placeBid(bo1, 5f));
		Assert.assertFalse(orderManager.placeBid(bo1, 200f));
	}

	/**
	 * Test method for {@link dutrow.bidbot.ejb.OrderMgmtEJB#endOrder(long)}.
	 * 
	 * @throws NamingException
	 * @throws BuyerMgmtRemoteException 
	 */
	@Test
	public void testEndOrder() throws NamingException, BuyerMgmtRemoteException {

		runAs(admin2User, admin2Password);
		BidAccount ba = testSupport.createBidder();
		Assert.assertNotNull("BidAccount is null", ba);
		orderManager.createAccount(ba);
		
		runAs(user3User, user3Password);
		BidOrder bo = new BidOrder(3, 4.5f, 7.5f, ba);
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
	public void testGetOrderStatus() throws NamingException, BuyerMgmtRemoteException {

		runAs(admin2User, admin2Password);
		BidAccount ba = testSupport.createBidder();
		Assert.assertNotNull("BidAccount is null", ba);
		orderManager.createAccount(ba);
		BidOrder bo = testSupport.createOrder(ba);
		
		runAs(user3User, user3Password);
		bo.setBidOrderId(orderManager.createOrder(bo));		
		boolean orderStatus = orderManager.getOrderStatus(bo.getBidOrderId());
		Assert.assertFalse(orderStatus);

	}

	@Test
	public void testCreateAccount() throws NamingException {

		runAs(admin2User, admin2Password);
		BidAccount ba1 = orderManager
				.createAccount(user3User, "user2", "password");
		
		runAs(user3User, user3Password);
		BidAccount ba2 = orderManager.getAccount(user3User);
		Assert.assertNotNull("Created account null", ba1);
		Assert.assertNotNull("Retrieved account null", ba2);
		Assert.assertEquals(ba1.getSalesAccount(), ba2.getSalesAccount());
		Assert.assertEquals(ba1.getSalesPassword(), ba2.getSalesPassword());
	}

}
