/**
 * 
 */
package dutrow.sales.bl.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.TestSupport;
import dutrow.sales.bo.Account;
import dutrow.sales.bo.Address;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.bo.Image;
import dutrow.sales.bo.POC;
import dutrow.sales.dao.AccountDAO;
import dutrow.sales.dao.AuctionDAO;
import dutrow.sales.dao.DAOException;

/**
 * @author dutroda1
 *
 */
public class TestSupportImpl implements TestSupport {
	private static Log log = LogFactory.getLog(TestSupportImpl.class);
	private AccountDAO accountDao;
	private AuctionDAO auctionDao;
	private EntityManager em;
	
	@SuppressWarnings("unused")
	private TestSupportImpl(){}
	public TestSupportImpl(EntityManager entityManager, AccountDAO accounts, AuctionDAO auctions){
		em = entityManager;
		accountDao = accounts;
		auctionDao = auctions;
	}
	

	/* (non-Javadoc)
	 * @see dutrow.sales.bl.TestSupport#resetAll()
	 */
	@Override
	public boolean resetAll() {
		boolean cleanReset = true;
		try {
			cleanup(em);
		} catch (DAOException e) {
			log.warn(e.getLocalizedMessage());
			e.printStackTrace();
			cleanReset = false;
		}
		return cleanReset;
	}
	
	private void deleteByJPA(EntityManager em) throws DAOException {
		log.debug("Delete By JPA");

		boolean useCascades = true;
		
		// Delete the auctions first because the auction item
		// has a link to the seller accounts
		
		// delete auction bids link
		// delete auction images link
		// delete images
		// delete bids
		// delete auction items
		Collection<AuctionItem> auctions = auctionDao.getAuctions();
		for (AuctionItem a : auctions){
			
			if (useCascades == false){
				for (Image i : a.getImages()){
					em.remove(i);
				}				
				for (Bid b : a.getBids()){
					em.remove(b);
				}
				em.remove(a);
			}
			else{
				auctionDao.removeAuction(a.getId());
			}
		}		
		
		// Delete account address links
		// delete addresses
		// delete POCs
		// delete accounts
		Collection<Account> accounts = accountDao.getAccounts();
		for (Account a : accounts) {
			if (useCascades == false){
				for (Map.Entry<String, Address> addr : a.getAddresses().entrySet()){
					em.remove(addr.getValue());
				}
				em.remove(a.getPoc());
				em.remove(a);
			}
			else{
				accountDao.removeAccount(a.getUserId());
			}
		}		
	}
	
	@SuppressWarnings("unused")
	private void deleteByQuery(EntityManager em) throws RuntimeException {

		log.debug("Delete By Query");
		
		int rows = 0;
		Query query;
		
		em.getTransaction().begin();
		query = em.createNativeQuery("delete from DUTROW_SALES_ACCT_ADDRESS_LINK");
		rows += query.executeUpdate();		
		query = em.createNativeQuery("delete from DUTROW_SALES_AUCTIONITEM_DUTROW_SALES_BID");
		rows += query.executeUpdate();
		query = em.createNativeQuery("delete from DUTROW_SALES_BID");
		rows += query.executeUpdate();
		query = em.createNativeQuery("delete from DUTROW_SALES_ITEM_IMAGE_LINK");
		rows += query.executeUpdate();
		query = em.createNativeQuery("delete from DUTROW_SALES_IMAGE");
		rows += query.executeUpdate();
		query = em.createNativeQuery("delete from DUTROW_SALES_ADDRESS");
		rows += query.executeUpdate();
		query = em.createNativeQuery("delete from DUTROW_SALES_AUCTIONITEM");
		rows += query.executeUpdate();		
		query = em.createNativeQuery("delete from DUTROW_SALES_ACCT");
		rows += query.executeUpdate();
		query = em.createNativeQuery("delete from DUTROW_SALES_POC");
		rows += query.executeUpdate();
		
		//query = em.createNativeQuery("delete from DUTROW_SALES_");
		//rows += query.executeUpdate();
		
		
		em.getTransaction().commit();
		log.debug("removed " + rows + " rows");
	}
	
	/**
	 * 
	 */
	private void cleanup(EntityManager em) throws RuntimeException {
		log.debug("JPAAccountDAOTest::cleanup");
		
		//deleteByQuery(em);
		deleteByJPA(em);
		
	}
	
	
	/* (non-Javadoc)
	 * @see dutrow.sales.bl.TestSupport#getAccounts()
	 */
	@Override
	public Collection<Account> getAccounts() {
		return accountDao.getAccounts();
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.bl.TestSupport#removeAccount(java.lang.String)
	 */
	@Override
	public boolean removeAccount(String userId) {
		return accountDao.removeAccount(userId);
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.bl.TestSupport#getAuctions()
	 */
	@Override
	public Collection<AuctionItem> getAuctions() {
		return auctionDao.getAuctions();
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.bl.TestSupport#removeAuction(java.lang.String)
	 */
	@Override
	public boolean removeAuction(long id) {
		return auctionDao.removeAuction(id);
	}


	@Override
	public AuctionItem createAuctionItem(String itemName, Account seller){
		
		Date startTime = createTime(5);
		Date endTime = createTime(8);
		AuctionItem item =  new AuctionItem(
				itemName, null, "A beautiful " + itemName, 
				startTime, endTime, 10, seller.getPoc());
		
		return item;
	}
	
	@Override
	public void createAndPersistAccountExamples(){
		log.info("Create Account: " + accountDao.createAccount(createDan()));
		log.info("Create Account: " + accountDao.createAccount(createJim()));
		log.info("Create Account: " + accountDao.createAccount(createSellerExample()));
	}

	@Override
	public Account createDan(){
		String dansId = "dutrow"; 
		Map<String, Address> dansAddresses = new HashMap<String,Address>();
		dansAddresses.put("work", new Address("shipping", "Dan's Colleague", "11100 Johns Hopkins Rd.", "Laurel", "MD", "21128"));
		POC dansPoint = new POC(dansId, "dan.dutrow@gmail.com");
		Date dansStartDate = createDate(2);
		Account dansAccount = new Account(dansId, "Dan", "A", "Dutrow", dansStartDate, dansAddresses, dansPoint);
		return dansAccount;
	}

	@Override
	public Account createJim(){
		String jimsId = "jcs";
		Map<String, Address> jimsAddresses = new HashMap<String,Address>();
		jimsAddresses.put("work", new Address("shipping", "Jim's Grader", "11100 Johns Hopkins Rd.", "Laurel", "MD", "21128"));
		POC jimsPoint = new POC(jimsId, "ejava_class@yahoogroups.com");
		Date jimsStartDate = createDate(3);
		Account jimsAccount = new Account(jimsId, "Jim", "C", "Stafford", jimsStartDate, jimsAddresses, jimsPoint);
		return jimsAccount;
	}

	@Override
	public Account createSellerExample(){
		String sellerID = "seller";
		Map<String, Address> sellerAddress = new HashMap<String,Address>();
		sellerAddress.put("work", new Address("distributor", "Kossiakoff Center", "11100 Johns Hopkins Rd.", "Laurel", "MD", "21128"));
		POC sellerPOC = sellerPOC(sellerID);
		Date sellerStartDate = createDate(1);
		Account sellerAccount = new Account(sellerID, "Kossi", "-", "Mart", sellerStartDate, sellerAddress, sellerPOC);
		return sellerAccount;
	}

	@Override
	public Date createTime(int secondsOffset){
		Calendar nowPlus = GregorianCalendar.getInstance();
		nowPlus.add(Calendar.SECOND, secondsOffset);
		return nowPlus.getTime();
	}
	
	@Override
	public Date createDate(int secondsOffset){
		return createTime(secondsOffset);
	}

	@Override
	public POC sellerPOC(String sellerID){
		POC sellerPOC = new POC(sellerID, "webmaster@www.apl.jhu.edu");
		return sellerPOC;
	}
	@Override
	public long persistAuctionItemExample(Account seller){
		return auctionDao.createAuction(createAuctionItem("fashionable gear", seller));
	}

	/**
	 * @return the accountDao
	 */
	@Override
	public AccountDAO getAccountDao() {
		return accountDao;
	}

	/**
	 * @param accountDao the accountDao to set
	 */
	@Override
	public void setAccountDao(AccountDAO accountDao) {
		this.accountDao = accountDao;
	}

	/**
	 * @return the auctionDao
	 */
	@Override
	public AuctionDAO getAuctionDao() {
		return auctionDao;
	}

	/**
	 * @param auctionDao the auctionDao to set
	 */
	@Override
	public void setAuctionDao(AuctionDAO auctionDao) {
		this.auctionDao = auctionDao;
	}
	/* (non-Javadoc)
	 * @see dutrow.sales.bl.TestSupport#persistAccount(dutrow.sales.bo.Account)
	 */
	@Override
	public void persistAccount(Account account) {
		accountDao.createAccount(account);
		
	}
	/* (non-Javadoc)
	 * @see dutrow.sales.bl.TestSupport#persistAuction(dutrow.sales.bo.AuctionItem)
	 */
	@Override
	public long persistAuction(AuctionItem auction) {
		auctionDao.createAuction(auction);
		return auction.getId();
	}
	/* (non-Javadoc)
	 * @see dutrow.sales.bl.TestSupport#getAccount(java.lang.String)
	 */
	@Override
	public Account getAccount(String id) {
		return accountDao.getAccountByUser(id);
	}

	


}
