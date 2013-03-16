/**
 * 
 */
package dutrow.bidbot.bo;

import java.util.Calendar;

/**
 * @author dutroda1
 *
 */
public class Bid {
	static long id = 0;
	double amount;
	Calendar timestamp;
	POC bidder;
	AuctionItem auction;
	/**
	 * @param id
	 * @param amount
	 * @param timestamp
	 * @param bidder
	 * @param auction
	 */
	public Bid(double amount, Calendar timestamp, POC bidder,
			AuctionItem auction) {
		super();
		
		Bid.id++;
		this.amount = amount;
		this.timestamp = timestamp;
		this.bidder = bidder;
		this.auction = auction;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	/**
	 * @return the timestamp
	 */
	public Calendar getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the bidder
	 */
	public POC getBidder() {
		return bidder;
	}
	/**
	 * @param bidder the bidder to set
	 */
	public void setBidder(POC bidder) {
		this.bidder = bidder;
	}
	/**
	 * @return the auction
	 */
	public AuctionItem getAuction() {
		return auction;
	}
	/**
	 * @param auction the auction to set
	 */
	public void setAuction(AuctionItem auction) {
		this.auction = auction;
	}
}
