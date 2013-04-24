/**
 * 
 */
package dutrow.sales.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.ParserRemote;
import dutrow.sales.ejb.SellerMgmtRemote;
import dutrow.sales.ejb.SupportRemote;

/**
 * @author dutroda1
 * 
 */
public class CreateAuction extends Handler {
	private static final Log log = LogFactory.getLog(CreateAuction.class);

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

		String title = request.getParameter("title");
		String category = request.getParameter("category");
		String description = request.getParameter("description");
		String stString = request.getParameter("startTime");
		String etString = request.getParameter("endTime");
		String seller = request.getParameter("seller");
		String price = request.getParameter("price");
		String open = request.getParameter("open");

		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd kk:mm:ss.S");

		AccountDTO sellerAccount;
		
		try {
			AuctionDTO auction = new AuctionDTO();
			auction.title = title;
			auction.category = category;
			auction.description = description;

			try {
				auction.startTime = dateFormatter.parse(stString);
			} catch (ParseException e) {
				auction.startTime = Calendar.getInstance().getTime();
			}
			try {
				auction.endTime = dateFormatter.parse(etString);
			} catch (ParseException e) {
				auction.endTime = Calendar.getInstance().getTime();
			}
			
			sellerAccount = accountMgmt.getAccountDTO(seller);			
			auction.seller = seller;
			auction.seller_email = sellerAccount.email;
			
			auction.askingPrice = Float.parseFloat(price);
			
			auction.isOpen = open != null;
			
			
			auction.id = sellerMgmt.createAuction(auction);

			if (auction.id != 0) {
				request.setAttribute(Strings.AUCTION_PARAM, auction);
			}

			RequestDispatcher rd = context
					.getRequestDispatcher(Strings.DISPLAY_AUCTION_URL);
			rd.forward(request, response);
		} catch (Exception ex) {
			log.fatal("error creating auction:" + ex, ex);
			request.setAttribute(Strings.EXCEPTION_PARAM, ex);
			RequestDispatcher rd = context
					.getRequestDispatcher(Strings.DISPLAY_EXCEPTION);
			rd.forward(request, response);
		}

	}

}
