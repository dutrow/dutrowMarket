/**
 * 
 */
package dutrow.sales.ejb;

import java.util.Collection;

import javax.ejb.Local;

import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.BidResultDTO;

/**
 * @author dutroda1
 *
 */
@Local
public interface BuyerMgmtLocal {
	
	BidResultDTO placeBidLocal(String userId, long auctionId, float bidValue);
	Collection<BidDTO> listMyBids(String userId);
	Collection<BidDTO> listMyOpenBids(String userId);
}
