/**
 * 
 */
package dutrow.bidbot.ejb;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import dutrow.bidbot.bl.BidbotTestUtil;
import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.cdi.BidbotOrderManager;
import dutrow.bidbot.cdi.BidbotTest;
import dutrow.bidbot.dao.BidAccountDAO;

/**
 * @author dutroda1
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class BidbotUtilEJB implements BidbotUtilRemote {
	
	@Inject
	private BidAccountDAO dao;

	@Inject @BidbotOrderManager
	private OrderMgmt orderMgmt;

	@Inject @BidbotTest
	private BidbotTestUtil testUtil;

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrSow.bidbot.ejb.BidbotUtilRemote#reset()
	 */
	@Override
	public boolean reset() {
		return testUtil.reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.ejb.BidbotUtilRemote#createOrder()
	 */
	@Override
	public BidOrder createOrder() {
		return testUtil.createOrder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.ejb.BidbotUtilRemote#createBidder()
	 */
	@Override
	public BidAccount createBidder() {
		// TODO Auto-generated method stub
		return null;
	}

}
