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

	AuctionDTO getAuctionDTO(long auction) throws BuyerMgmtRemoteException;
	Collection<ImageDTO> getAuctionImages(long auctionId) throws BuyerMgmtRemoteException;
	Collection<BidDTO> listMyBids() throws BuyerMgmtRemoteException;
	Collection<BidDTO> listMyOpenBids() throws BuyerMgmtRemoteException;

	BidResultDTO placeBid(long auctionId, float bidValue) throws BuyerMgmtRemoteException;
	boolean placeMultiBid(long auctionId, float [] bidValues) throws BuyerMgmtRemoteException;
	
	AuctionItem getAuction(long auction) throws BuyerMgmtRemoteException;

}
