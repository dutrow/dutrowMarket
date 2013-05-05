/**
 * 
 */
package dutrow.sales.dto;

import java.io.Serializable;


/**
 * @author dutroda1
 * 
 */
public class BidResultDTO implements Serializable{


	private static final long serialVersionUID = 6748294577367615014L;
	public BidDTO bid;
	public String result;
	
	/**
	 * @param bid
	 * @param result
	 */
	public BidResultDTO(BidDTO bid, String result) {
		super();
		this.bid = bid;
		this.result = result;
	}
	

}
