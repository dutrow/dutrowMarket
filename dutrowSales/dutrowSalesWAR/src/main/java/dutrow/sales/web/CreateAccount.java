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
import dutrow.sales.dto.AddressDTO;
import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.ParserRemote;
import dutrow.sales.ejb.SellerMgmtRemote;
import dutrow.sales.ejb.SalesSupportRemote;

/**
 * @author dutroda1
 * 
 */
public class CreateAccount extends Handler {
	private static final Log log = LogFactory.getLog(CreateAccount.class);
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

		String id = (String) request.getParameter("id");
		String first = (String) request.getParameter("first");
		String middle = (String) request.getParameter("middle");
		String last = (String) request.getParameter("last");
		String email = (String) request.getParameter("email");
		
		String street = (String) request.getParameter("street");
		String city = (String) request.getParameter("city");
		String state = (String) request.getParameter("state");
		String zip = (String) request.getParameter("zip");
		
		AddressDTO addr = new AddressDTO("shipping", first + middle + last, street, city, state, zip);
		AccountDTO account = new AccountDTO(id, first, middle, last, email);
		account.addresses.add(addr);

		try {
			boolean good = accountMgmt.createAccountDTO(account);
			if (good) {
				request.setAttribute(Strings.ACCOUNT_PARAM, account);
				RequestDispatcher rd = context
						.getRequestDispatcher(Strings.DISPLAY_ACCOUNT_URL);
				rd.forward(request, response);
			}
		} catch (Exception ex) {
			log.fatal("error getting account:" + ex, ex);
			request.setAttribute(Strings.EXCEPTION_PARAM, ex);
			RequestDispatcher rd = context
					.getRequestDispatcher(Strings.DISPLAY_EXCEPTION);
			rd.forward(request, response);
		}
		
		IllegalArgumentException myex = new IllegalArgumentException("Could not create account. Recheck input values and try again.");
		request.setAttribute(Strings.EXCEPTION_PARAM, myex);
		RequestDispatcher rd = context
				.getRequestDispatcher(Strings.DISPLAY_EXCEPTION);
		rd.forward(request, response);
		

	}

}
