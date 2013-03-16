/**
 * 
 */
package dutrow.sales.bo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author dutroda1
 * 
 */
@Entity
@Table(name = "DUTROW_SALES_BID")
public class Bid implements Comparable<Bid> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	float amount;
	Date timestamp;

	@ManyToOne
	@JoinColumn
	POC bidder;

	@ManyToOne
	@JoinColumn
	AuctionItem auction;

	// JPA Requires no-arg constructor
	public Bid() {
	}

	/**
	 * @param id
	 * @param amount
	 * @param timestamp
	 * @param bidder
	 * @param auction
	 */
	public Bid(float amount, Date timestamp, POC bidder, AuctionItem auction) {
		super();
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
	public float getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the bidder
	 */
	public POC getBidder() {
		return bidder;
	}

	/**
	 * @param bidder
	 *            the bidder to set
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
	 * @param auction
	 *            the auction to set
	 */
	public void setAuction(AuctionItem auction) {
		this.auction = auction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Bid other) {
		return Float.compare(this.amount, other.amount);
	}
}
