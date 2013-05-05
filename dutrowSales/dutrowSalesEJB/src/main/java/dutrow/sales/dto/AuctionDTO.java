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
public class AuctionDTO implements Serializable{
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
	
	public AuctionDTO(String titleIn, String categoryIn, String descriptionIn, Date startTimeIn, float askingPriceIn, String sellerIn, String sellerEmailIn, boolean isOpen){
		this.title = titleIn;
		this.category = categoryIn;
		this.description = descriptionIn;
		this.startTime = startTimeIn;
		this.askingPrice = askingPriceIn;
		this.seller = sellerIn;
		this.seller_email = sellerEmailIn;
		this.isOpen = isOpen;
		this.images = new ArrayList<ImageDTO>();
		this.bids = new TreeSet<BidDTO>();
		this.isExpired = false;
		
		
	}

	
	
}
