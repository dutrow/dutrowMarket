/**
 * 
 */
package dutrow.bidbot.ejb;

import java.util.Properties;

import javax.annotation.security.RunAs;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bo.BidAccount;
import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.AccountMgmtRemoteException;
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
	/*
	protected InitialContext jndi;
	

	private static final String buyerJNDI = System
			.getProperty("jndi.name.registrar",
					"dutrowSalesEAR/dutrowSalesEJB/BuyerMgmtEJB!dutrow.sales.ejb.BuyerMgmtRemote");
	private BuyerMgmtRemote buyerManager;

	
	public OrderMgmtHelper() {
		log.debug("buyer jndi name:" + buyerJNDI);
		try {
			buyerManager = (BuyerMgmtRemote) jndi.lookup(buyerJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("buyerManager=" + buyerManager);
	}

	
	public BidResultDTO placeBid(long auctionId, BidAccount bidder, float bidAmount) throws BuyerMgmtRemoteException {
		try {
			runAs(bidder.getSalesAccount(), bidder.getSalesPassword());
			return buyerManager.placeBid(auctionId, bidAmount);
		} catch (NamingException e) {
			log.warn("Could not authenticate as user: " + bidder.getSalesAccount(), e);
			return new BidResultDTO(null, "Could not authenticate as user: " + bidder.getSalesAccount());
		}
		
	}
	
	protected Context runAs(String username, String password) throws NamingException {
        if (jndi!=null) {
        	jndi.close();
        }
        Properties env = new Properties();
        if (username != null) {
            env.put(Context.SECURITY_PRINCIPAL, username);
            env.put(Context.SECURITY_CREDENTIALS, password);
        }
        log.debug(String.format("%s env=%s", username==null?"anonymous":username, env));
        jndi=new InitialContext(env);
        return jndi;
    }

*/
}
