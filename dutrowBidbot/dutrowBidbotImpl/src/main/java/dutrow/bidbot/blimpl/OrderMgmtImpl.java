/**
 * 
 */
package dutrow.bidbot.blimpl;

import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.dao.BidAccountDAO;

/**
 * @author dutroda1
 *
 */
public class OrderMgmtImpl implements OrderMgmt {

	private BidAccountDAO accountDAO;
	public void setAccountDAO(BidAccountDAO accountDAO) {
		this.accountDAO = accountDAO;
		;
	}

	@SuppressWarnings("unused")
	private OrderMgmtImpl() {
	} // force use of DAO constructor

	public OrderMgmtImpl(BidAccountDAO accountDao) {
		setAccountDAO(accountDao);
	}

	
	/* (non-Javadoc)
	 * @see dutrow.bidbot.bl.BidAccountMgmt#createAccount(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public BidAccount createAccount(String userId, String accountId,
			String passwd) {

		BidAccount ba = new BidAccount();
		ba.setUserId(userId);
		ba.setSalesAccount(accountId);
		ba.setSalesPassword(passwd);
		
		if (accountDAO.getAccountById(userId) == null){
			return accountDAO.createAccount(ba);
		}
		else return null;
		
	}

	/* (non-Javadoc)
	 * @see dutrow.bidbot.bl.OrderMgmt#createOrder(dutrow.bidbot.bo.BidOrder)
	 */
	@Override
	public boolean createOrder(BidOrder order) {
		return accountDAO.createOrder(order);
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
}
