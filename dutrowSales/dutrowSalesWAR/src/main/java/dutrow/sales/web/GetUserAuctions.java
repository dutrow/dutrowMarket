/**
 * 
 */
package dutrow.sales.web;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dutrow.sales.bl.AccountMgmt;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.ParserRemote;
import dutrow.sales.ejb.SellerMgmtRemote;
import dutrow.sales.ejb.SupportRemote;

/**
 * @author dutroda1
 * 
 */
public class GetUserAuctions extends Handler {

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
			BuyerMgmtRemote buyerMgmt, AccountMgmt accountMgmt,
			SellerMgmtRemote sellerMgmt, ParserRemote parser,
			SupportRemote support) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}
