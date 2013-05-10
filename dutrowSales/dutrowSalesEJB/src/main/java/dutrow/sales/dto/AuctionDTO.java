/**
 * 
 */
package dutrow.sales.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author dutroda1
 * 
 */
public class AuctionDTO implements Serializable {
	private static final long serialVersionUID = 7300123732092489492L;
	public long id;
	public String title;
	public String category;
	public String description;
	public Date startTime;
	public Date endTime;
	public String seller;
	public String seller_email;
	public String buyer;
	public String buyer_email;
	public float askingPrice;
	public float purchasePrice;
	public boolean isOpen;

	public AddressDTO shipTo;

	public SortedSet<BidDTO> bids;

	public Collection<ImageDTO> images;
	public boolean isExpired;

	/**
	 * 
	 */
	public AuctionDTO() {
		super();
	}

	public AuctionDTO(String titleIn, String categoryIn, String descriptionIn,
			Date startTimeIn, Date endTimeIn, float askingPriceIn,
			String sellerIn, String sellerEmailIn, boolean isOpen) {
		this.title = titleIn;
		this.category = categoryIn;
		this.description = descriptionIn;
		this.startTime = startTimeIn;
		this.endTime = endTimeIn;
		this.askingPrice = askingPriceIn;
		this.seller = sellerIn;
		this.seller_email = sellerEmailIn;
		this.isOpen = isOpen;
		this.images = new ArrayList<ImageDTO>();
		this.bids = new TreeSet<BidDTO>();
		this.isExpired = false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AuctionDTO [id=" + id + ", title=" + title + ", category="
				+ category + ", description=" + description + ", startTime="
				+ startTime + ", endTime=" + endTime + ", seller=" + seller
				+ ", seller_email=" + seller_email + ", buyer=" + buyer
				+ ", buyer_email=" + buyer_email + ", askingPrice="
				+ askingPrice + ", purchasePrice=" + purchasePrice
				+ ", isOpen=" + isOpen + ", shipTo=" + shipTo + ", bids="
				+ bids + ", images=" + images + ", isExpired=" + isExpired
				+ "]";
	}

	/**
	 * @return
	 */
	public float highestBid() {
		float bid = -1; 
		if (this.bids != null && !this.bids.isEmpty()){
			bid = this.bids.last().amount;
		}
		
		return bid;
	}

}
