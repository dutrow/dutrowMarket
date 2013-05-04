/**
 * 
 */
package dutrow.sales.ejb;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
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
import dutrow.sales.dto.DTOConversionUtil;

/**
 * @author dutroda1
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"esales-admin"})
// esales-admin		these users will be able to perform management and test functions on eSales.
public class SupportEJB implements SupportRemote {
	private static final Log log = LogFactory.getLog(SupportEJB.class);
	
	@Inject
	TestSupport testUtil;
	
	@Resource
    private SessionContext ctx;
	
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
		log.debug("getAccounts caller=" + ctx.getCallerPrincipal().getName());
		Collection<Account> allaccounts = testUtil.getAccounts();
		Collection<AccountDTO> accounts = new ArrayList<AccountDTO>(); 
		for (Account a : allaccounts){
			accounts.add(DTOConversionUtil.convertAccount(a));
		}
		
		return accounts;
		
	}

	@Override
	public boolean removeAccount(String userId) {
		log.debug("removeAccount caller=" + ctx.getCallerPrincipal().getName());
		return testUtil.removeAccount(userId);
	}

	@Override
	public Collection<AuctionDTO> getAuctions() {
		log.debug("getAuctions caller=" + ctx.getCallerPrincipal().getName());
		Collection<AuctionItem> allauctions = testUtil.getAuctions();
		Collection<AuctionDTO> auctions = new ArrayList<AuctionDTO>();
		for (AuctionItem ai : allauctions){
			auctions.add(DTOConversionUtil.convertAuctionItem(ai));
		}
		return auctions;
	}

	@Override
	public boolean removeAuction(long auctionId) {
		log.debug("removeAuction caller=" + ctx.getCallerPrincipal().getName());
		return testUtil.removeAuction(auctionId);
	}

	@Override
	public boolean resetAll() {
		log.debug("resetAll caller=" + ctx.getCallerPrincipal().getName());
		return testUtil.resetAll();
	}

	@Override
	public void createAccount(AccountDTO seller) {
		log.debug("createAccount caller=" + ctx.getCallerPrincipal().getName());
		testUtil.persistAccount(DTOConversionUtil.convertAccountDTO(seller));
	}

	@Override
	public long createAuction(AuctionDTO auction) {
		log.debug("createAuction caller=" + ctx.getCallerPrincipal().getName());
		return testUtil.persistAuction(DTOConversionUtil.convertAuctionDTO(auction));
	}

	@Override
	public AccountDTO getAccount(String id) {
		log.debug("getAccount caller=" + ctx.getCallerPrincipal().getName());
		Account a = testUtil.getAccount(id);
		return DTOConversionUtil.convertAccount(a);
	}

	@Override
	public AuctionDTO getAuction(long id) {
		log.debug("getAuction caller=" + ctx.getCallerPrincipal().getName());
		AuctionItem ai = testUtil.getAuctionDao().getAuctionById(id);
		return DTOConversionUtil.convertAuctionItem(ai);
	}

}
