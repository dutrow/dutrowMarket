/**
 * 
 */
package dutrow.sales.ejb;

import java.util.Collection;

import javax.ejb.Local;

import dutrow.sales.bl.BuyerMgmtException;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.BidResultDTO;

/**
 * @author dutroda1
 *
 */
@Local
public interface BuyerMgmtLocal {
	
	Collection<BidDTO> listMyBids();
	Collection<BidDTO> listMyOpenBids();
	BidResultDTO placeBidLocal(long auctionId, float bidValue);
	AuctionItem getAuction(long auction) throws BuyerMgmtException ;
}
