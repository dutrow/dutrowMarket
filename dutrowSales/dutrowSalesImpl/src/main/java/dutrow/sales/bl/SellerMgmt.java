/**
 * 
 */
package dutrow.sales.bl;

import java.util.Collection;

import dutrow.sales.bo.AuctionItem;

/**
 * @author dutroda1
 *
 */
public interface SellerMgmt {
	AuctionItem createAuction(AuctionItem auctionDetails);
	Collection<AuctionItem> getUserAuctions(String userId);
	Collection<AuctionItem> getOpenUserAuctions(String userId);
	AuctionItem getAuction(long l);
}
