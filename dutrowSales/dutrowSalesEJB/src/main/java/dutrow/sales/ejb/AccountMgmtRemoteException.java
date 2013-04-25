/**
 * 
 */
package dutrow.sales.ejb;

/**
 * @author dutroda1
 *
 */
public class AccountMgmtRemoteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6208070642624903834L;
	
	public AccountMgmtRemoteException() {
	}

	public AccountMgmtRemoteException(Throwable ex) {
		super(ex);
	}

	public AccountMgmtRemoteException(String msg, Throwable ex) {
		super(msg, ex);
	}

	public AccountMgmtRemoteException(String msg) {
		super(msg);
	}

}
