/**
 * 
 */
package dutrow.sales.dao;

import java.util.Collection;

import dutrow.sales.bo.AuctionItem;

/**
 * @author dutroda1
 *
 */
public interface AuctionDAO {
	long createAuction(AuctionItem auctionDetails);
	AuctionItem getAuctionById(long id);
	boolean updateAuction(AuctionItem ai);
	boolean removeAuction(long id);
	Collection<AuctionItem> getAuctions();
	Collection<AuctionItem> getOpenAuctions();
	Collection<AuctionItem> getUserAuctions(String userId);
	
}
