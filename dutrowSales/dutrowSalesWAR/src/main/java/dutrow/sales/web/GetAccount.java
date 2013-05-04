/**
 * 
 */
package dutrow.sales.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.dto.AccountDTO;
import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.ParserRemote;
import dutrow.sales.ejb.SellerMgmtRemote;
import dutrow.sales.ejb.SupportRemote;

/**
 * @author dutroda1
 * 
 */
public class GetAccount extends Handler {
	private static final Log log = LogFactory.getLog(GetAccount.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dutrow.sales.web.Handler#handle(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, javax.servlet.ServletContext,
	 * dutrow.sales.ejb.BuyerMgmtRemote, dutrow.sales.ejb.AccountMgmtRemote,
	 * dutrow.sales.ejb.SellerMgmtRemote, dutrow.sales.ejb.ParserRemote,
	 * dutrow.sales.ejb.SupportRemote)
	 */
	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			BuyerMgmtRemote buyerMgmt, AccountMgmtRemote accountMgmt,
			SellerMgmtRemote sellerMgmt, ParserRemote parser,
			SupportRemote support) throws ServletException, IOException {

		try {
			String idStr = (String) request
					.getParameter(Strings.ID_PARAM);
			if (idStr != null && !idStr.isEmpty()) {
				AccountDTO accountDTO = accountMgmt.getAccountDTO(idStr);
				request.setAttribute(Strings.ACCOUNT_PARAM, accountDTO);
			}
			
			RequestDispatcher rd = context
					.getRequestDispatcher(Strings.DISPLAY_ACCOUNT_URL);
			rd.forward(request, response);
		} catch (Exception ex) {
			log.fatal("error getting account:" + ex, ex);
			request.setAttribute(Strings.EXCEPTION_PARAM, ex);
			RequestDispatcher rd = context.getRequestDispatcher(
					Strings.DISPLAY_EXCEPTION);
			rd.forward(request, response);
		}

	}

}
