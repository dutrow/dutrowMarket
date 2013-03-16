/**
 * 
 */
package dutrow.sales.bl;

import dutrow.sales.bo.AuctionItem;

/**
 * @author dutroda1
 *
 */
public interface AuctionMgmt {
	
	/**
	 * @param completedAuction
	 */
	void closeAuction(AuctionItem completedAuction);
}
