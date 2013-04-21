/**
 * 
 */
package dutrow.sales.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


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
	public AccountDTO seller;
	public float askingPrice;
	public float purchasePrice;
	public boolean isOpen;
	
	public AddressDTO shipTo;
	
	public Collection<BidDTO> bids;
	
	Collection<ImageDTO> images;
	public boolean isExpired;

	/**
	 * 
	 */
	public AuctionDTO() {
		super();
	}
	
	public AuctionDTO(String titleIn, String categoryIn, String descriptionIn, Date startTimeIn, float askingPriceIn, AccountDTO sellerIn, boolean isOpen){
		this.title = titleIn;
		this.category = categoryIn;
		this.description = descriptionIn;
		this.startTime = startTimeIn;
		this.askingPrice = askingPriceIn;
		this.seller = sellerIn;
		this.isOpen = isOpen;
		this.images = new ArrayList<ImageDTO>();
		this.bids = new ArrayList<BidDTO>();
		this.isExpired = false;
		
	}

	
	
}
