/**
 * 
 */
package dutrow.bidbot.bo;

import java.util.Calendar;
import java.util.List;
import java.util.Stack;


/**
 * @author dutroda1
 *
 */

public class AuctionItem {
	static long nextAuctionID = 0;	
	long id = 0;
	String title;
	@SuppressWarnings("rawtypes")
	Enum category;
	String description;
	Calendar startTime;
	Calendar endTime;
	double askingPrice;
	double purchasePrice;
	Stack<Bid> bids;
	POC buyer;
	Address shipTo;
	POC seller;
	List<Image> images;
	
	/**
	 * @param id
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
	 * @param images
	 */
	@SuppressWarnings("rawtypes")
	public AuctionItem(String title, Enum category,
			String description, Calendar startTime, Calendar endTime,
			double askingPrice, POC seller, List<Image> images) {
		super();
		this.id = AuctionItem.nextAuctionID++;
		this.title = title;
		this.category = category;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.askingPrice = askingPrice;
		this.purchasePrice = -1;
		this.bids = new Stack<Bid>();
		this.buyer = null;
		this.shipTo = null;
		this.seller = seller;
		this.images = images;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the category
	 */
	@SuppressWarnings("rawtypes")
	public Enum getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	@SuppressWarnings("rawtypes")
	public void setCategory(Enum category) {
		this.category = category;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the startTime
	 */
	public Calendar getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Calendar getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the askingPrice
	 */
	public double getAskingPrice() {
		return askingPrice;
	}
	/**
	 * @param askingPrice the askingPrice to set
	 */
	public void setAskingPrice(double askingPrice) {
		this.askingPrice = askingPrice;
	}
	/**
	 * @return the purchasePrice
	 */
	public double getPurchasePrice() {
		return purchasePrice;
	}
	/**
	 * @param purchasePrice the purchasePrice to set
	 */
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	/**
	 * @return the bids
	 */
	public Stack<Bid> getBids() {
		return bids;
	}
	/**
	 * @param bids the bids to set
	 */
	public void addBids(Bid bid) {
		this.bids.add(bid);
	}
	/**
	 * @return the buyer
	 */
	public POC getBuyer() {
		return buyer;
	}
	/**
	 * @param buyer the buyer to set
	 */
	public void setBuyer(POC buyer) {
		this.buyer = buyer;
	}
	/**
	 * @return the shipTo
	 */
	public Address getShipTo() {
		return shipTo;
	}
	/**
	 * @param shipTo the shipTo to set
	 */
	public void setShipTo(Address shipTo) {
		this.shipTo = shipTo;
	}
	/**
	 * @return the seller
	 */
	public POC getSeller() {
		return seller;
	}
	/**
	 * @param seller the seller to set
	 */
	public void setSeller(POC seller) {
		this.seller = seller;
	}
	/**
	 * @return the images
	 */
	public List<Image> getImages() {
		return images;
	}
	/**
	 * @param images the images to set
	 */
	public void setImages(List<Image> images) {
		this.images = images;
	}
}
