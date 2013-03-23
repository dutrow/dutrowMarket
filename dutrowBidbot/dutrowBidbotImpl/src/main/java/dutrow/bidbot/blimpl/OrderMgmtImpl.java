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

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.bl.BidAccountMgmt#createAccount(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public BidAccount createAccount(String userId, String accountId,
			String passwd) {

		BidAccount ba = new BidAccount();
		ba.setUserId(userId);
		ba.setSalesAccount(accountId);
		ba.setSalesPassword(passwd);

		if (accountDAO.getAccountById(userId) == null) {
			return accountDAO.createAccount(ba);
		} else
			return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dutrow.bidbot.bl.OrderMgmt#createAccount(dutrow.bidbot.bo.BidAccount)
	 */
	@Override
	public boolean createAccount(BidAccount ba) {
		if (accountDAO.getAccountById(ba.getUserId()) == null) {
			accountDAO.createAccount(ba);
			return true;
		} else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.bl.OrderMgmt#createOrder(dutrow.bidbot.bo.BidOrder)
	 */
	@Override
	public boolean createOrder(BidOrder order) {
		return accountDAO.createOrder(order);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.bl.OrderMgmt#endOrder()
	 */
	@Override
	public boolean endOrder(long bidOrder) {
		BidOrder bo = accountDAO.getOrderById(bidOrder);
		// TODO: complete order processing once auction has closed and note if
		// won.
		bo.setComplete(true);
		accountDAO.updateOrder(bo);
		return bo.isComplete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.bl.OrderMgmt#getOrderStatus()
	 */
	@Override
	public boolean getOrderStatus(long boId) {
		BidOrder boResult = accountDAO.getOrderById(boId);
		return boResult.getResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.bl.OrderMgmt#placeBid(float)
	 */
	@Override
	public boolean placeBid(BidOrder order, float bid) {
		if (order.isComplete())
			return false;

		if (bid > order.getMaxBid())
			return false;

		// TODO: place a bid that is higher than the current bid for an open
		// auction

		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.bl.OrderMgmt#getAccount(java.lang.String)
	 */
	@Override
	public BidAccount getAccount(String userId) {
		return accountDAO.getAccountById(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.bl.OrderMgmt#getOrder(long)
	 */
	@Override
	public BidOrder getOrder(long bidOrderId) {
		return accountDAO.getOrderById(bidOrderId);
	}

}
