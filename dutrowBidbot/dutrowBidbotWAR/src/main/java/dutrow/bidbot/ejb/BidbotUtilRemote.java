/**
 * 
 */
package dutrow.bidbot.ejb;

import javax.ejb.Remote;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;

/**
 * @author dutroda1
 *
 */
@Remote
public interface BidbotUtilRemote {
	boolean reset(); // - reset the bidbot database to an initial starting state.

	BidOrder createOrder(); // also creates bidder
	BidAccount createBidder();
}
