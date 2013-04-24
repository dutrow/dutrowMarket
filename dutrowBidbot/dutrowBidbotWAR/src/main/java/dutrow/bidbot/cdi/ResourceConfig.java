/**
 * 
 */
package dutrow.bidbot.cdi;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dutrow.bidbot.bl.BidbotTestUtil;
import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.blimpl.BidbotTestUtilImpl;
import dutrow.bidbot.blimpl.OrderMgmtImpl;
import dutrow.bidbot.dao.BidAccountDAO;
import dutrow.bidbot.jpa.JPABidAccountDAO;

/**
 * @author dutroda1
 * 
 */
public class ResourceConfig {

	// ENTITY MANAGER
	@PersistenceContext(unitName = "dutrowSales")
	public EntityManager em;

	// this is a second option for an EntityManager to create an ambiguity
	// when selecting on type alone
	@Produces
	@BidbotEntityManager
	public EntityManager getEntityManager() {
		return em;
	}

	// TEST UTIL
	@Produces
	@BidbotTest
	public BidbotTestUtil getTestUtil(
			@BidbotEntityManager final EntityManager emgr,
			final BidAccountDAO accountDao) {
		return new BidbotTestUtilImpl(emgr, accountDao);
	}

	// MANAGERS
	@Produces
	@BidbotOrderManager
	public OrderMgmt getOrderManager(final BidAccountDAO accountDao) {
		return new OrderMgmtImpl(accountDao);
	}

	@Produces
	public BidAccountDAO getAccountDAO(
			@BidbotEntityManager final EntityManager emgr) {
		return new JPABidAccountDAO(emgr);
	}

}
