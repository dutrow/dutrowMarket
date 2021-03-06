/**
 * 
 */
package dutrow.sales.cdi;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dutrow.sales.bl.AccountMgmt;
import dutrow.sales.bl.BuyerMgmt;
import dutrow.sales.bl.Ingestor;
import dutrow.sales.bl.SellerMgmt;
import dutrow.sales.bl.TestSupport;
import dutrow.sales.bl.impl.AccountMgmtImpl;
import dutrow.sales.bl.impl.BuyerMgmtImpl;
import dutrow.sales.bl.impl.IngestorImpl;
import dutrow.sales.bl.impl.SellerMgmtImpl;
import dutrow.sales.bl.impl.TestSupportImpl;
import dutrow.sales.dao.AccountDAO;
import dutrow.sales.dao.AuctionDAO;
import dutrow.sales.dao.jpa.JPAAccountDAO;
import dutrow.sales.dao.jpa.JPAAuctionDAO;

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
	@SalesEntityManager
	public EntityManager getEntityManager() {
		return em;
	}

	// TEST UTIL
	@Produces
	public TestSupport getTestUtil(
			@SalesEntityManager final EntityManager emgr,
			final AccountDAO accountDao, final AuctionDAO auctionDao) {
		return new TestSupportImpl(emgr, accountDao, auctionDao);
	}

	// MANAGERS
	@Produces
	public AccountMgmt getAccountManager(final AccountDAO accountDao) {
		return new AccountMgmtImpl(accountDao);
	}


	@Produces
	public SellerMgmt getSellerManager(final AuctionDAO auctionDao) {
		return new SellerMgmtImpl(auctionDao);
	}

	@Produces
	public BuyerMgmt getBuyerManager(final AccountDAO accountDao,
			final AuctionDAO auctionDao) {
		return new BuyerMgmtImpl(accountDao, auctionDao);
	}

	@Produces
	public AuctionDAO getAuctionDAO(
			@SalesEntityManager final EntityManager emgr) {
		return new JPAAuctionDAO(emgr);

	}

	@Produces
	public AccountDAO getAccountDAO(
			@SalesEntityManager final EntityManager emgr) {
		return new JPAAccountDAO(emgr);
	}
	
	// INGESTOR
	@Produces
	public Ingestor getIngestor(final AccountDAO accountDao,
			final AuctionDAO auctionDao) {
		return new IngestorImpl(accountDao, auctionDao);
	}
}
