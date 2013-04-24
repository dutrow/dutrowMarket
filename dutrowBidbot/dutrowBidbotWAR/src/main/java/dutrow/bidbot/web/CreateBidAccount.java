/**
 * 
 */
package dutrow.bidbot.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.ejb.BidbotUtilRemote;
import dutrow.bidbot.ejb.OrderMgmtRemote;

/**
 * @author dutroda1
 *
 */
public class CreateBidAccount extends Handler {
	private static final Log log = LogFactory.getLog(CreateBidAccount.class);
	
	/* (non-Javadoc)
	 * @see dutrow.bidbot.web.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletContext, dutrow.bidbot.ejb.OrderMgmtRemote, dutrow.bidbot.ejb.BidbotUtilRemote)
	 */
	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			OrderMgmtRemote orderMgmt, BidbotUtilRemote support)
			throws ServletException, IOException {
		
		try{
			String userId = request.getParameter("userId");
			String accountId = request.getParameter("accountId");
			String passwd = request.getParameter("password");
			BidAccount ba = orderMgmt.createAccount(userId, accountId, passwd);
			
			request.setAttribute(Strings.ACCOUNT_PARAM, ba);
			RequestDispatcher rd = context
					.getRequestDispatcher(Strings.DISPLAY_ACCOUNT_URL);
			rd.forward(request, response);
		} catch (Exception ex) {
			log.fatal("error creating account:" + ex, ex);
			request.setAttribute(Strings.EXCEPTION_PARAM, ex);
			RequestDispatcher rd = context
					.getRequestDispatcher(Strings.DISPLAY_EXCEPTION);
			rd.forward(request, response);
		}
		
	}

}
