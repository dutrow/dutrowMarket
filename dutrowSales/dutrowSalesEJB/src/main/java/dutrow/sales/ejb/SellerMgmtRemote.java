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
	long createAuction(AuctionDTO auction) throws SellerMgmtRemoteException;
	Collection<AuctionDTO> getUserAuctions();
	Collection<AuctionDTO> getOpenUserAuctions();
	AuctionDTO getAuction(long auctionId);
	Collection<ImageDTO> getAuctionImages(long auctionId);
	
	void workSync(int count, long delay);
	void workAsync(int count, long delay);	
	void cancelTimers();
		
	
}
