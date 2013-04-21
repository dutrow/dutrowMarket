/**
 * 
 */
package dutrow.sales.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import dutrow.sales.bl.BuyerMgmtException;
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
	Collection<ImageDTO> getAuctionImages(long auctionId);
	Collection<BidDTO> listMyBids(String userId);
	Collection<BidDTO> listMyOpenBids(String userId);

	BidResultDTO placeBid(String userId, long auctionId, float bidValue);

}
