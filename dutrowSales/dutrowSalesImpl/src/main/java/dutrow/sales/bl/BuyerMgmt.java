/**
 * 
 */
package dutrow.sales.bl;

import java.util.Collection;

import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.bo.POC;

/**
 * @author dutroda1
 *
 */
public interface BuyerMgmt {
	Collection<AuctionItem> getOpenAuctions();
	AuctionItem getAuction(long auctionId);
	Bid placeBid(POC bidder, long auctionId, float amount);
}
