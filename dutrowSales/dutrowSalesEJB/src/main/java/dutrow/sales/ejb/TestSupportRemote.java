/**
 * 
 */
package dutrow.sales.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.AuctionDTO;

/**
 * @author dutroda1
 *
 */
@Remote
public interface TestSupportRemote {

	Collection<AccountDTO> getAccounts();
	boolean removeAccount(String userId);
	Collection<AuctionDTO> getAuctions();
	boolean removeAuction(long auctionId);
	boolean resetAll();
	
	void createAccount(AccountDTO seller);
	long createAuction(AuctionDTO auction);
	
	
	
}