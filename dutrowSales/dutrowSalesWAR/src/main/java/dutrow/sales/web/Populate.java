/**
 * 
 */
package dutrow.sales.web;

import java.io.IOException;
import java.util.Collection;

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
import dutrow.sales.ejb.SalesSupportRemote;

/**
 * @author dutroda1
 * 
 */
public class Populate extends Handler {
	private static final Log log = LogFactory.getLog(Populate.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dutrow.sales.web.Handler#handle(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, dutrow.sales.ejb.BuyerMgmtRemote,
	 * dutrow.sales.ejb.AccountMgmtRemote, dutrow.sales.ejb.SellerMgmtRemote,
	 * dutrow.sales.ejb.ParserRemote, dutrow.sales.ejb.SupportRemote)
	 */
	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			BuyerMgmtRemote buyerMgmt, AccountMgmtRemote accountMgmt,
			SellerMgmtRemote sellerMgmt, ParserRemote parser,
			SalesSupportRemote support) throws ServletException, IOException {

		try {
			parser.ingest();
			
            int index = 0;
            int count = 25;
			Collection<AccountDTO> accounts = accountMgmt.getAccounts(index, count);
            
            int nextIndex = (accounts.size()==0) ? 
                    index : index + accounts.size();
            
            request.setAttribute(Strings.ACCOUNTS_PARAM, accounts);
            request.setAttribute(Strings.INDEX_PARAM, index);
            request.setAttribute(Strings.COUNT_PARAM, count);
            request.setAttribute(Strings.NEXT_INDEX_PARAM, nextIndex);

			RequestDispatcher rd = context
					.getRequestDispatcher(Strings.DISPLAY_ACCOUNTS_URL);
			rd.forward(request, response);
			
		} catch (Exception ex) {
			log.fatal("error getting accounts:" + ex, ex);
			request.setAttribute(Strings.EXCEPTION_PARAM, ex);
			RequestDispatcher rd = context.getRequestDispatcher(
					Strings.DISPLAY_EXCEPTION);
			rd.forward(request, response);
		}
	}

}
