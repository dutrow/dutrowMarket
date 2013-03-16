/**
 * 
 */
package dutrow.bidbot.bo;

import java.util.Collection;

/**
 * @author dutroda1
 *
 */
public class POC {
	String userId;
	
	String email;
	Collection<AuctionItem> auctions;
	Collection<Bid> bids;
	Collection<AuctionItem> purchases;
	
	/**
	 * @param userId
	 * @param email
	 */
	public POC(String userId, String email) {
		super();
		this.userId = userId;
		this.email = email;
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the auctions
	 */
	public Collection<AuctionItem> getAuctions() {
		return auctions;
	}
	/**
	 * @param auctions the auctions to set
	 */
	public void setAuctions(Collection<AuctionItem> auctions) {
		this.auctions = auctions;
	}
	/**
	 * @return the bids
	 */
	public Collection<Bid> getBids() {
		return bids;
	}
	/**
	 * @param bids the bids to set
	 */
	public void setBids(Collection<Bid> bids) {
		this.bids = bids;
	}
	/**
	 * @return the purchases
	 */
	public Collection<AuctionItem> getPurchases() {
		return purchases;
	}
	/**
	 * @param purchases the purchases to set
	 */
	public void setPurchases(Collection<AuctionItem> purchases) {
		this.purchases = purchases;
	}
	
}
