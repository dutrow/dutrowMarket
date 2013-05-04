/**
 * 
 */
package dutrow.bidbot.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import dutrow.bidbot.bl.BidbotTestUtil;
import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.cdi.BidbotOrderManager;
import dutrow.bidbot.cdi.BidbotTest;
import dutrow.bidbot.dao.BidAccountDAO;
import dutrow.sales.ejb.AccountMgmtRemote;

/**
 * @author dutroda1
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"ebidbot-admin"})
//ebidbot-admin	these users will be able to perform management and test functions on eBidbot.
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
	public BidOrder createOrder(BidAccount ba) {
		return testUtil.createOrder(ba);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.bidbot.ejb.BidbotUtilRemote#createBidder()
	 */
	@Override
	public BidAccount createBidder() {
		return testUtil.createBidder();
	}

}
