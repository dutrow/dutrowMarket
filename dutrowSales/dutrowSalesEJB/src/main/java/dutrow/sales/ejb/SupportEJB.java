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
public class SupportEJB implements SupportRemote {
	private static final Log log = LogFactory.getLog(SupportEJB.class);
	
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

	@Override
	public Collection<AccountDTO> getAccounts() {
		Collection<Account> allaccounts = testUtil.getAccounts();
		Collection<AccountDTO> accounts = new ArrayList<AccountDTO>(); 
		for (Account a : allaccounts){
			accounts.add(DTOConversionUtil.convertAccount(a));
		}
		
		return accounts;
		
	}

	@Override
	public boolean removeAccount(String userId) {
		return testUtil.removeAccount(userId);
	}

	@Override
	public Collection<AuctionDTO> getAuctions() {
		Collection<AuctionItem> allauctions = testUtil.getAuctions();
		Collection<AuctionDTO> auctions = new ArrayList<AuctionDTO>();
		for (AuctionItem ai : allauctions){
			auctions.add(DTOConversionUtil.convertAuctionItem(ai));
		}
		return auctions;
	}

	@Override
	public boolean removeAuction(long auctionId) {
		return testUtil.removeAuction(auctionId);
	}

	@Override
	public boolean resetAll() {
		return testUtil.resetAll();
	}

	@Override
	public void createAccount(AccountDTO seller) {
		
		testUtil.persistAccount(DTOConversionUtil.convertAccountDTO(seller));
	}

	@Override
	public long createAuction(AuctionDTO auction) {
		log.info("createAuction");
		return testUtil.persistAuction(DTOConversionUtil.convertAuctionDTO(auction));
	}

	@Override
	public AccountDTO getAccount(String id) {
		Account a = testUtil.getAccount(id);
		return DTOConversionUtil.convertAccount(a);
	}

	@Override
	public AuctionDTO getAuction(long id) {
		AuctionItem ai = testUtil.getAuctionDao().getAuctionById(id);
		return DTOConversionUtil.convertAuctionItem(ai);
	}

}
