/**
 * 
 */
package dutrow.sales.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;



/**
 * @author dutroda1
 * 
 */
@Entity
@Table(name = "DUTROW_SALES_AUCTIONITEM")
public class AuctionItem {
	@Id
	// If you would like the parser to to set the id, then remove this generated value.
	// The instructor/grader would rather I not allow something else to generate the primary keys.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AUCTION_ITEM_ID")
	long id;
	@Column(nullable=false)
	String title;
	Category category;
	@Column(nullable=false)
	String description;
	@Column(nullable=false)
	Date startTime;
	Date endTime;
	@Column(nullable=false)
	float askingPrice;
	float purchasePrice;
	@Column(nullable=false)
	boolean open;

	@OneToMany(cascade = { CascadeType.ALL })
	@Sort(type = SortType.NATURAL)
	SortedSet<Bid> bids;
	// @OrderBy("amount")
	// Set<Bid> bids;

	@ManyToOne(optional = true, cascade = { CascadeType.PERSIST })
	@JoinColumn
	POC buyer;

	@OneToOne(cascade = { CascadeType.PERSIST })
	@JoinColumn
	Address shipTo;

	@ManyToOne
	@JoinColumn(nullable = false)
	POC seller;

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "DUTROW_SALES_ITEM_IMAGE_LINK")
	List<Image> images;

	// JPA Requires no-arg constructor
	public AuctionItem() {
		images = new ArrayList<Image>();
		bids = new TreeSet<Bid>();
	}

	/**
	 * @param title
	 * @param category
	 * @param description
	 * @param startTime
	 * @param endTime
	 * @param askingPrice
	 * @param purchasePrice
	 * @param bids
	 * @param buyer
	 * @param shipTo
	 * @param seller
	 * @param images2
	 */
	public AuctionItem(String title, Category category, String description,
			Date startTime, Date endTime, float askingPrice, POC seller) {
		this.title = title;
		this.category = category;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.askingPrice = askingPrice;
		this.purchasePrice = -1;
		this.bids = new TreeSet<Bid>();
		this.buyer = null;
		this.shipTo = null;
		this.seller = seller;
		this.images = new ArrayList<Image>();
		
		this.open = true;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public AuctionItem setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public AuctionItem setCategory(Category category) {
		this.category = category;
		return this;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public AuctionItem setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param date
	 *            the startTime to set
	 */
	public AuctionItem setStartTime(Date date) {
		this.startTime = date;
		return this;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param date
	 *            the endTime to set
	 */
	public AuctionItem setEndTime(Date date) {
		this.endTime = date;
		return this;
	}

	/**
	 * @return the askingPrice
	 */
	public float getAskingPrice() {
		return askingPrice;
	}

	/**
	 * @param askingPrice
	 *            the askingPrice to set
	 */
	public AuctionItem setAskingPrice(float askingPrice) {
		this.askingPrice = askingPrice;
		return this;
	}

	/**
	 * @return the purchasePrice
	 */
	public float getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @param purchasePrice
	 *            the purchasePrice to set
	 */
	public AuctionItem setPurchasePrice(float purchasePrice) {
		this.purchasePrice = purchasePrice;
		return this;
	}

	/**
	 * @return the bids
	 */
	public SortedSet<Bid> getBids() {
		return bids;
	}

	/**
	 * @param bids
	 *            the bids to set
	 */
	public AuctionItem addBids(Bid bid) {
		this.bids.add(bid);
		return this;
	}

	/**
	 * @return the buyer
	 */
	public POC getBuyer() {
		return buyer;
	}

	/**
	 * @param buyer
	 *            the buyer to set
	 */
	public AuctionItem setBuyer(POC buyer) {
		this.buyer = buyer;
		return this;
	}

	/**
	 * @return the shipTo
	 */
	public Address getShipTo() {
		return shipTo;
	}

	/**
	 * @param shipTo
	 *            the shipTo to set
	 */
	public AuctionItem setShipTo(Address shipTo) {
		this.shipTo = shipTo;
		return this;
	}

	/**
	 * @return the seller
	 */
	public POC getSeller() {
		return seller;
	}

	/**
	 * @param seller
	 *            the seller to set
	 */
	public AuctionItem setSeller(POC seller) {
		this.seller = seller;
		return this;
	}

	/**
	 * @return the images
	 */
	public List<Image> getImages() {
		if (images == null)
			images = new ArrayList<Image>();

		return images;
	}

	/**
	 * @param images
	 *            the images to set
	 */
	public AuctionItem setImages(List<Image> images) {
		this.images = images;
		return this;
	}

	/**
	 * @return
	 */
	public Bid getHighestBid() {
		if (bids == null || bids.size() == 0)
			return null;

		return bids.last();
	}

	@Override
	public String toString() {

		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"E, y-M-d 'at' h:m:s a z");
		StringBuilder builder = new StringBuilder();
		builder.append("id=")
				.append(this.id)
				.append(", title=")
				.append(this.title)
				.append(", category=")
				.append(this.category == null ? "" : this.category.prettyName)
				.append(", description=")
				.append(this.description)
				.append(", askingPrice=")
				.append(this.askingPrice)
				.append(", start=")
				.append(dateFormatter.format(this.getStartTime()))
				.append(", end=")
				.append(this.getEndTime() != null ? dateFormatter.format(this
						.getEndTime()) : "").append(", seller=")
				.append(this.seller.getUserId()).append(", #images=")
				.append(this.images == null ? 0 : this.images.size());
		return builder.toString();
	}

	/**
	 * @return the open
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}

	/**
	 * @return the expired
	 */
	public boolean isExpired() {
		if (this.endTime == null)
			return false;
			
		return this.endTime.before(Calendar.getInstance().getTime());
	}

	/**
	 * 
	 */
	public void closeAuction() {
		this.open = false;
		Bid highestBid = getHighestBid();
		if (highestBid != null){
			this.setBuyer(highestBid.getBidder());
			this.setPurchasePrice(highestBid.getAmount());
		}
		
	}

}
