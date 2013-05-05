/**
 * 
 */
package dutrow.sales.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dutroda1
 *
 */
public class BidDTO implements Serializable, Comparable<BidDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7442043374154805375L;
	public float amount;
	public Date timestamp;
	public String poc;
	public String poc_email;
	
	public long auctionItem;
	
	@Override
	public int compareTo(BidDTO other) {
		return Float.compare(this.amount, other.amount);
	}
}
