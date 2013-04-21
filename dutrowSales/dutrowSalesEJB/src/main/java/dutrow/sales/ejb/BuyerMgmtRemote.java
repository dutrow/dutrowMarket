/**
 * 
 */
package dutrow.sales.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import dutrow.sales.bl.BuyerMgmtException;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.dto.ImageDTO;

/**
 * @author dutroda1
 * 
 */
@Remote
public interface BuyerMgmtRemote {
	Collection<AuctionDTO> getOpenAuctions();

	AuctionDTO getAuctionDTO(long auction) throws BuyerMgmtException;
	Collection<ImageDTO> getAuctionImages(long auctionId) throws BuyerMgmtException;
	Collection<BidDTO> listMyBids(String userId) throws BuyerMgmtException;
	Collection<BidDTO> listMyOpenBids(String userId) throws BuyerMgmtException;

	BidResultDTO placeBid(String userId, long auctionId, float bidValue) throws BuyerMgmtException;
	boolean placeMultiBid(String userId, long auctionId, float [] bidValues) throws BuyerMgmtException;
	
	AuctionItem getAuction(long auction) throws BuyerMgmtException;

}
