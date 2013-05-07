/**
 * 
 */
package dutrow.bidbot.blimpl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.dao.BidAccountDAO;

/**
 * @author dutroda1
 * 
 */
public class OrderMgmtImpl implements OrderMgmt {
	private static final Log log = LogFactory.getLog(OrderMgmtImpl.class);


	
	private BidAccountDAO accountDAO;

	public void setAccountDAO(BidAccountDAO accountDAO) {
		this.accountDAO = accountDAO;

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
			String passwd) throws IllegalArgumentException {

		log.debug("createAccount: " + userId + "-" + accountId + "-" + passwd);

		if (userId == null) {
			throw new IllegalArgumentException("No user id");
		}
		if (accountId == null) {
			throw new IllegalArgumentException("No account id");
		}
		if (passwd == null) {
			throw new IllegalArgumentException("No password");
		}

		BidAccount ba = accountDAO.getAccountById(userId);
		if (ba != null) {
			throw new IllegalArgumentException("Account already exists");
		}

		BidAccount newba = new BidAccount(userId, accountId, passwd);
		String aid = accountDAO.createAccount(newba);
		
		if (aid != null)
			return newba;

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
		if (ba == null)
			log.warn("BidAccount is null");
		
		if (accountDAO == null)
			log.error("AccountDAO is null");
		
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
		boolean orderCreated = accountDAO.createOrder(order);
			
		return orderCreated;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.bl.OrderMgmt#endOrder()
	 */
	@Override
	public boolean endOrder(long bidOrder) {
		BidOrder bo = accountDAO.getOrderById(bidOrder);
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

	/* (non-Javadoc)
	 * @see dutrow.bidbot.bl.OrderMgmt#getOrdersforItem(long)
	 */
	@Override
	public List<BidOrder> getOrdersforItem(long itemId) {
		return accountDAO.getBidOrdersByAuctionId(itemId);
	}

}
