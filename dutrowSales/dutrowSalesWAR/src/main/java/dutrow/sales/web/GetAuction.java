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

import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.ParserRemote;
import dutrow.sales.ejb.SellerMgmtRemote;
import dutrow.sales.ejb.SalesSupportRemote;

/**
 * @author dutroda1
 * 
 */
public class GetAuction extends Handler {
	private static final Log log = LogFactory.getLog(GetAuction.class);

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
			String auctionIdStr = (String) request
					.getParameter(Strings.ID_PARAM);
			if (auctionIdStr != null && !auctionIdStr.isEmpty()) {
				int auctionId = Integer.parseInt(auctionIdStr);
				AuctionDTO auction = buyerMgmt.getAuctionDTO(auctionId);
				request.setAttribute(Strings.AUCTION_PARAM, auction);
			}
			RequestDispatcher rd = context
					.getRequestDispatcher(Strings.DISPLAY_AUCTION_URL);
			rd.forward(request, response);
		} catch (Exception ex) {
			log.fatal("error getting auction:" + ex, ex);
			request.setAttribute(Strings.EXCEPTION_PARAM, ex);
			RequestDispatcher rd = context
					.getRequestDispatcher(Strings.DISPLAY_EXCEPTION);
			rd.forward(request, response);
		}
	}

}
