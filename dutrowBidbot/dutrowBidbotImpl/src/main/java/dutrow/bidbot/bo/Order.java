/**
 * 
 */
package dutrow.bidbot.bo;

/**
 * @author dutroda1
 *
 */
public class Order {
	double id;
	double auctionId;
	double startBid;
	double maxBid;
	boolean complete; // is auction complete for this order
	double finalBid; // know purchase price for winner. The buyer will owe this amount if the result says he won.
	String userId;
	
}
