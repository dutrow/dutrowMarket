/**
 * 
 */
package dutrow.sales.ejb;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.TestSupport;
import dutrow.sales.bo.Account;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.ejb.DTOConversionUtil;

/**
 * @author dutroda1
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TestSupportEJB implements TestSupportRemote {
	private static final Log log = LogFactory.getLog(TestSupportEJB.class);
	
	@Inject
	TestSupport testUtil;
	
	@PostConstruct
	public void init() {
		try {
			log.debug("**** init ****");
			log.debug("init complete, accountManager=" + testUtil);
		} catch (Throwable ex) {
			log.warn("error in init", ex);
			throw new EJBException("error in init" + ex);
		}
	}

	@PreDestroy
	public void close() {
		log.debug("*** close() ***");
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.TestSupportRemote#getAccounts()
	 */
	@Override
	public Collection<AccountDTO> getAccounts() {
		Collection<Account> allaccounts = testUtil.getAccounts();
		Collection<AccountDTO> accounts = new ArrayList<AccountDTO>(); 
		for (Account a : allaccounts){
			accounts.add(DTOConversionUtil.convertAccount(a));
		}
		
		return accounts;
		
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.TestSupportRemote#removeAccount()
	 */
	@Override
	public boolean removeAccount(String userId) {
		return testUtil.removeAccount(userId);
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.TestSupportRemote#getAuctions()
	 */
	@Override
	public Collection<AuctionDTO> getAuctions() {
		Collection<AuctionItem> allauctions = testUtil.getAuctions();
		Collection<AuctionDTO> auctions = new ArrayList<AuctionDTO>();
		for (AuctionItem ai : allauctions){
			auctions.add(DTOConversionUtil.convertAuctionItem(ai));
		}
		return auctions;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.TestSupportRemote#removeAuction()
	 */
	@Override
	public boolean removeAuction(long auctionId) {
		return testUtil.removeAuction(auctionId);
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.TestSupportRemote#resetAll()
	 */
	@Override
	public boolean resetAll() {
		return testUtil.resetAll();
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.TestSupportRemote#createAccount(dutrow.sales.dto.AccountDTO)
	 */
	@Override
	public void createAccount(AccountDTO seller) {
		
		testUtil.persistAccount(DTOConversionUtil.convertAccountDTO(seller));
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.TestSupportRemote#createAuction(dutrow.sales.dto.AuctionDTO)
	 */
	@Override
	public long createAuction(AuctionDTO auction) {
		log.info("createAuction");
		return testUtil.persistAuction(DTOConversionUtil.convertAuctionDTO(auction));
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.TestSupportRemote#getAccount(long)
	 */
	@Override
	public AccountDTO getAccount(String id) {
		Account a = testUtil.getAccount(id);
		return DTOConversionUtil.convertAccount(a);
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.TestSupportRemote#getAuction(long)
	 */
	@Override
	public AuctionDTO getAuction(long id) {
		AuctionItem ai = testUtil.getAuctionDao().getAuctionById(id);
		return DTOConversionUtil.convertAuctionItem(ai);
	}

}
