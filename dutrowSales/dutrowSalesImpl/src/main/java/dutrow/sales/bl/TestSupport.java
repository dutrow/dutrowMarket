/**
 * 
 */
package dutrow.sales.bl;

import java.util.Collection;
import java.util.Date;

import dutrow.sales.bo.Account;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.POC;
import dutrow.sales.dao.AccountDAO;
import dutrow.sales.dao.AuctionDAO;

/**
 * @author dutroda1
 *
 */
public interface TestSupport {
	Collection<Account> getAccounts();
	boolean removeAccount(String userId);
	Collection<AuctionItem> getAuctions();
	boolean removeAuction(long auctionId);
	boolean resetAll();
	/**
	 * @param itemName
	 * @param seller
	 * @return
	 */
	AuctionItem createAuctionItem(String itemName, Account seller);
	/**
	 * @return
	 */
	Account createDan();
	/**
	 * @return
	 */
	Account createJim();
	/**
	 * @return
	 */
	Account createSellerExample();
	/**
	 * @param secondsOffset
	 * @return
	 */
	Date createTime(int secondsOffset);
	/**
	 * @param sellerID
	 * @return
	 */
	POC sellerPOC(String sellerID);
	
	/**
	 * @return
	 */
	AccountDAO getAccountDao();
	/**
	 * @param accountDao
	 */
	void setAccountDao(AccountDAO accountDao);
	/**
	 * @return
	 */
	AuctionDAO getAuctionDao();
	/**
	 * @param auctionDao
	 */
	void setAuctionDao(AuctionDAO auctionDao);
	/**
	 * 
	 */
	void createAndPersistAccountExamples();
	/**
	 * @return
	 */
	AuctionItem persistAuctionItemExample(Account seller);
	
	/**
	 * @param secondsOffset
	 * @return
	 */
	Date createDate(int secondsOffset);
	/**
	 * @param convertAccountDTO
	 */
	void persistAccount(Account convertAccountDTO);
	/**
	 * @param convertAuctionDTO
	 * @return 
	 */
	long persistAuction(AuctionItem convertAuctionDTO);
	/**
	 * @param id
	 * @return
	 */
	Account getAccount(String id);

	
}
