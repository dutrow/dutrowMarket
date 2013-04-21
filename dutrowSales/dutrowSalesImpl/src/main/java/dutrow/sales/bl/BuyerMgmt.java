/**
 * 
 */
package dutrow.sales.bl;

import java.util.Collection;

import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.bo.BidResult;
import dutrow.sales.bo.Image;

/**
 * @author dutroda1
 *
 */
public interface BuyerMgmt {
	Collection<AuctionItem> getOpenAuctions();
	AuctionItem getAuction(long auctionId);
	BidResult placeBid(String userId, long auctionId, float amount);
	Collection<Image> getAuctionImages(long auctionId);
	Collection<Bid> listBids(String userId);
	Collection<Bid> listOpenBids(String userId);
}
