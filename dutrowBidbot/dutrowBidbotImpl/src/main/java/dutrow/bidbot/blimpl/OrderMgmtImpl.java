/**
 * 
 */
package dutrow.bidbot.blimpl;

import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.dao.BidAccountDAO;
import dutrow.bidbot.jpa.JPABidAccountDAO;

/**
 * @author dutroda1
 *
 */
public class OrderMgmtImpl implements OrderMgmt {

	BidAccountDAO accountDao;
	
	/**
	 * @param accountDao
	 */
	public OrderMgmtImpl(JPABidAccountDAO accountDao) {
		this.accountDao = accountDao;
	}

	/* (non-Javadoc)
	 * @see dutrow.bidbot.bl.OrderMgmt#createOrder(dutrow.bidbot.bo.BidOrder)
	 */
	@Override
	public void createOrder(BidOrder order) {
		// TODO Auto-generated method stub		
	}

	/* (non-Javadoc)
	 * @see dutrow.bidbot.bl.OrderMgmt#placeBid(float)
	 */
	@Override
	public boolean placeBid(BidOrder order, float bid) {
		if (order.isComplete())
			return false;
		
		if (bid > order.getMaxBid())
			return false;
		
		//TODO: place a bid that is higher than the current bid for an open auction
		
		
		return true;
		
	}

	/* (non-Javadoc)
	 * @see dutrow.bidbot.bl.OrderMgmt#endOrder()
	 */
	@Override
	public boolean endOrder() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see dutrow.bidbot.bl.OrderMgmt#getOrderStatus()
	 */
	@Override
	public boolean getOrderStatus() {
		// TODO Auto-generated method stub
		return false;
	}

}
