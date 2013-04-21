/**
 * 
 */
package dutrow.sales.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
	
	public AddressDTO shipTo;
	
	public Collection<BidDTO> bids;
	
	Collection<ImageDTO> images;

	/**
	 * 
	 */
	public AuctionDTO() {
		super();
	}
	
	public AuctionDTO(String titleIn, String categoryIn, String descriptionIn, Date startTimeIn, float askingPriceIn, AccountDTO sellerIn){
		this.title = titleIn;
		this.category = categoryIn;
		this.description = descriptionIn;
		this.startTime = startTimeIn;
		this.askingPrice = askingPriceIn;
		this.seller = sellerIn;
		images = new ArrayList<ImageDTO>();
		bids = new ArrayList<BidDTO>();
	}

	
	
}
