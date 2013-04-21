/**
 * 
 */
package dutrow.sales.bl;

/**
 * @author dutroda1
 *
 */
public class SellerMgmtException extends Exception{
	
	private static final long serialVersionUID = 5769849335290694889L;
	public SellerMgmtException() {}
    public SellerMgmtException(Throwable ex) { super(ex); }
    public SellerMgmtException(String msg, Throwable ex) { super(msg, ex); }
    public SellerMgmtException(String msg) { super(msg); }
}
