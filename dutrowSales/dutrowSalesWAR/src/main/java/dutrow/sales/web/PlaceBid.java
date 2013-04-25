/**
 * 
 */
package dutrow.sales.web;

import java.io.IOException;

import javax.ejb.EJBException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemoteException;
import dutrow.sales.ejb.ParserRemote;
import dutrow.sales.ejb.SellerMgmtRemote;
import dutrow.sales.ejb.SupportRemote;

/**
 * @author dutroda1
 * 
 */
public class PlaceBid extends Handler {
	private static final Log log = LogFactory.getLog(PlaceBid.class);

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
			SupportRemote support) throws ServletException, IOException {

		/*
		 * auction: <input type="text" name="auction" size="25"/><p/> bidder :
		 * <input type="text" name="bidder" size="25"/><p/> amount : <input
		 * type="number" name="amount" size="25"/><p/>
		 */

		String auctionIdStr = request.getParameter("auction");
		String bidderIdStr = request.getParameter("bidder");
		String bidAmountStr = request.getParameter("amount");
		
		long auctionId = Integer.parseInt(auctionIdStr);
		float bidValue = Float.parseFloat(bidAmountStr);
		try {
			BidResultDTO result = buyerMgmt.placeBid(bidderIdStr, auctionId, bidValue);
			
			if (result.bid != null){
				AuctionDTO auction = buyerMgmt.getAuctionDTO(auctionId);
				request.setAttribute(Strings.AUCTION_PARAM, auction); 
				RequestDispatcher rd = context
						.getRequestDispatcher(Strings.DISPLAY_AUCTION_URL);
				rd.forward(request, response);
			}
			
		
			
		} catch (BuyerMgmtRemoteException ex) {
			log.fatal("error making bid:" + ex, ex);
			request.setAttribute(Strings.EXCEPTION_PARAM, ex);
			RequestDispatcher rd = context
					.getRequestDispatcher(Strings.DISPLAY_EXCEPTION);
			rd.forward(request, response);
		} catch (EJBException eex) {
			log.fatal("error making bid:" + eex, eex);
			request.setAttribute(Strings.EXCEPTION_PARAM, eex);
			RequestDispatcher rd = context
					.getRequestDispatcher(Strings.DISPLAY_EXCEPTION);
			rd.forward(request, response);
		}

	}

}
