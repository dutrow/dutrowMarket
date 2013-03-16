/**
 * 
 */
package dutrow.bidbot.bl;

import java.util.Calendar;
import dutrow.bidbot.bo.*;

/**
 * @author dutroda1
 *
 */
public interface BidbotTestUtil {
	boolean reset(); // - reset the bidbot database to an initial starting state.

	/**
	 * @param sellerID
	 * @return
	 */
	POC sellerPOC(String sellerID);

	/**
	 * @return
	 */
	Account createSeller();

	/**
	 * @param secondsOffset
	 * @return
	 */
	Calendar createTime(int secondsOffset);

	/**
	 * @return
	 */
	Account createDan();

	/**
	 * @return
	 */
	Account createJim();

	/**
	 * @param itemName
	 * @param seller
	 * @return
	 */
	AuctionItem createAuctionItem(String itemName, Account seller);

	/**
	 * @return
	 */
	BidAccount createBidAccount();

	/**
	 * @return
	 */
	Order createOrder();

}
