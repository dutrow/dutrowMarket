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
	@PersistenceContext(unitName = "dutrowBidbot")
	public EntityManager em;

	// this is a second option for an EntityManager to create an ambiguity
	// when selecting on type alone
	@Produces
	@DutrowEntityManager
	public EntityManager getEntityManager() {
		return em;
	}

	// TEST UTIL
	@Produces
	public BidbotTestUtil getTestUtil(
			@DutrowEntityManager final EntityManager emgr,
			final BidAccountDAO accountDao) {
		return new BidbotTestUtilImpl(emgr, accountDao);
	}

	// MANAGERS
	@Produces
	public OrderMgmt getAccountManager(final BidAccountDAO accountDao) {
		return new OrderMgmtImpl(accountDao);
	}

	@Produces
	public BidAccountDAO getAccountDAO(
			@DutrowEntityManager final EntityManager emgr) {
		return new JPABidAccountDAO(emgr);
	}
	
}
