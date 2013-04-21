/**
 * 
 */
package dutrow.sales.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.ImageDTO;

/**
 * @author dutroda1
 *
 */
@Remote
public interface SellerMgmtRemote {
	long createAuction(AuctionDTO auction);
	Collection<AuctionDTO> listMyAuctions(String userId);
	Collection<AuctionDTO> listMyOpenAuctions(String userId);
	AuctionDTO getAuction(long auctionId);
	Collection<ImageDTO> getAuctionImages(long auctionId);
	

	
}
