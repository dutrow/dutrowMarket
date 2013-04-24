package dutrow.sales.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import dutrow.sales.bl.AccountMgmtException;
import dutrow.sales.dto.AccountDTO;

/**
 * @author dutroda1
 * 
 */
@Remote
public interface AccountMgmtRemote {
	boolean createAccountDTO(AccountDTO accountDetails) throws AccountMgmtException;
	AccountDTO getAccountDTO(String userString) throws AccountMgmtException;
    boolean updateAccountDTO(AccountDTO accountDetails) throws AccountMgmtException;
	boolean closeAccountDTO(String userId) throws AccountMgmtException;
	Collection<AccountDTO> getAccounts(int index, int count) throws AccountMgmtException;
    
}
