/**
 * 
 */
package dutrow.bidbot.bo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author dutroda1
 * 
 */
@SuppressWarnings("serial")
@Entity @Table(name = "DUTROW_BIDBOT_ORDER")
public class BidOrder implements Serializable {
	/**
	 * 
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	long bidId;
	long auctionId;
	float startBid;
	float maxBid;
	boolean complete;
	boolean result;
	float finalBid;
	
	@ManyToOne
	BidAccount bidder;
	
	@Override
	public String toString() {

		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"E, y-M-d 'at' h:m:s a z");
		StringBuilder builder = new StringBuilder();
		builder.append("id=")
				.append(this.bidId)
				.append(", auctionId=")
				.append(this.auctionId)
				.append(", startBid=")
				.append(this.startBid)
				.append(", maxBid=")
				.append(this.maxBid)
				.append(", complete=")
				.append(this.complete)
				.append(", result=")
				.append(this.result)
				.append(", finalBid=")
				.append(this.finalBid)
				;
		return builder.toString();
	}

	/**
	 * @return the bidId
	 */
	public long getBidId() {
		return bidId;
	}

	/**
	 * @param bidId the bidId to set
	 */
	public void setBidId(long bidId) {
		this.bidId = bidId;
	}

	/**
	 * @return the auctionId
	 */
	public long getAuctionId() {
		return auctionId;
	}

	/**
	 * @param auctionId the auctionId to set
	 */
	public void setAuctionId(long auctionId) {
		this.auctionId = auctionId;
	}

	/**
	 * @return the startBid
	 */
	public float getStartBid() {
		return startBid;
	}

	/**
	 * @param startBid the startBid to set
	 */
	public void setStartBid(float startBid) {
		this.startBid = startBid;
	}

	/**
	 * @return the maxBid
	 */
	public float getMaxBid() {
		return maxBid;
	}

	/**
	 * @param maxBid the maxBid to set
	 */
	public void setMaxBid(float maxBid) {
		this.maxBid = maxBid;
	}

	/**
	 * @return the complete
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * @param complete the complete to set
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	/**
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @return the finalBid
	 */
	public float getFinalBid() {
		return finalBid;
	}

	/**
	 * @param finalBid the finalBid to set
	 */
	public void setFinalBid(float finalBid) {
		this.finalBid = finalBid;
	}

	/**
	 * @return the bidder
	 */
	public BidAccount getBidder() {
		return bidder;
	}

	/**
	 * @param bidder the bidder to set
	 */
	public void setBidder(BidAccount bidder) {
		this.bidder = bidder;
	}
}