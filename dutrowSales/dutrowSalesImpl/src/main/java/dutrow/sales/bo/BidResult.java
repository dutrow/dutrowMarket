/**
 * 
 */
package dutrow.sales.bo;

/**
 * @author dutroda1
 *
 */
public class BidResult {

	Bid bid;
	String result;
	/**
	 * @param bid
	 * @param result
	 */
	public BidResult(Bid bid, String result) {
		super();
		this.bid = bid;
		this.result = result;
	}
	/**
	 * @return the bid
	 */
	public Bid getBid() {
		return bid;
	}
	/**
	 * @param bid the bid to set
	 */
	public void setBid(Bid bid) {
		this.bid = bid;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	
}
