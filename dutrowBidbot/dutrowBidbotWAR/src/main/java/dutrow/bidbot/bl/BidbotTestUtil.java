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
	boolean reset(); // - reset the bidbot database to an initial starting
						// state.

	BidAccount createBidder();

	BidOrder createOrder(long auctionId, BidAccount bidder); // also creates
																// bidder

}
