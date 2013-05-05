package dutrow.bidbot.ejb;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemoteException;

/**
 * @author dutroda1
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RunAs("esales-trusted")
public class OrderMgmtHelper {
	private static final Log log = LogFactory.getLog(OrderMgmtHelper.class);

	final String mName = "ejb:dutrowSalesEAR/dutrowSalesEJB/BuyerMgmtEJB!dutrow.sales.ejb.BuyerMgmtRemote";
	@EJB(mappedName = mName)
	private BuyerMgmtRemote buyerManager;

	public BidResultDTO placeBid(long auctionId, BidAccount bidder,
			float bidAmount) throws BuyerMgmtRemoteException {
		return buyerManager.placeBid(auctionId, bidder.getSalesAccount(), bidder.getSalesPassword(), bidAmount);

	}

}
