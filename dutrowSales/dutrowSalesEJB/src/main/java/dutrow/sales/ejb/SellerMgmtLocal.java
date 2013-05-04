/**
 * 
 */
package dutrow.sales.ejb;

import java.util.Collection;

import javax.ejb.Local;

import dutrow.sales.dto.AuctionDTO;

/**
 * @author dutroda1
 *
 */
@Local
public interface SellerMgmtLocal {
	Collection<AuctionDTO> getUserAuctions();
	Collection<AuctionDTO> getOpenUserAuctions();
}
