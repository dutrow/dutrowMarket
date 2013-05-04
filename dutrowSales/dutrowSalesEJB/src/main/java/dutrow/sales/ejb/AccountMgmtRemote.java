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
	boolean createAccountDTO(AccountDTO accountDetails) throws AccountMgmtRemoteException;
	AccountDTO getAccountDTO(String userString) throws AccountMgmtRemoteException;
    boolean updateAccountDTO(AccountDTO accountDetails) throws AccountMgmtRemoteException;
	boolean closeAccountDTO() throws AccountMgmtRemoteException;
	Collection<AccountDTO> getAccounts(int index, int count) throws AccountMgmtRemoteException;
    
}
