/**
 * 
 */
package dutrow.sales.bl;

/**
 * @author dutroda1
 * 
 */
public class AccountMgmtException extends Exception {

	private static final long serialVersionUID = -1393667688078466795L;

	public AccountMgmtException() {
	}

	public AccountMgmtException(Throwable ex) {
		super(ex);
	}

	public AccountMgmtException(String msg, Throwable ex) {
		super(msg, ex);
	}

	public AccountMgmtException(String msg) {
		super(msg);
	}

}
