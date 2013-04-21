/**
 * 
 */
package dutrow.sales.bl;

/**
 * @author dutroda1
 *
 */
public class BuyerMgmtException extends Exception {
	
	private static final long serialVersionUID = 3369576692810812583L;
	public BuyerMgmtException() {}
    public BuyerMgmtException(Throwable ex) { super(ex); }
    public BuyerMgmtException(String msg, Throwable ex) { super(msg, ex); }
    public BuyerMgmtException(String msg) { super(msg); }
	
}
