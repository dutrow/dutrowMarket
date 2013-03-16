/**
 * 
 */
package dutrow.sales.bl.impl;

import java.util.Calendar;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.AuctionMgmt;
import dutrow.sales.bo.Account;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.bo.POC;
import dutrow.sales.dao.AccountDAO;
import dutrow.sales.dao.AuctionDAO;

/**
 * @author dutroda1
 *
 */
public class AuctionMgmtImpl implements AuctionMgmt {
	private static Log log = LogFactory.getLog(AuctionMgmtImpl.class);

	private AuctionDAO auctions;
	public void setAuctionDAO(AuctionDAO auctionDAO) {
		this.auctions = auctionDAO;
	}
	
	private AccountDAO accounts;
	public void setAccountDAO(AccountDAO accountDAO) {
		this.accounts = accountDAO;;
	}
	
	@SuppressWarnings("unused")
	private AuctionMgmtImpl(){} // force use of DAO constructor
	
	public AuctionMgmtImpl(AccountDAO accountDao, AuctionDAO auctionDao){
		setAuctionDAO(auctionDao);
		setAccountDAO(accountDao);
	}
	
	/* (non-Javadoc)
	 * @see dutrow.sales.bl.AuctionMgmt#closeAuction()
	 */
	@Override
	public void closeAuction(AuctionItem item) {

		Bid winningBid = null;
		SortedSet<Bid> bids = item.getBids(); 
		
		if (!bids.isEmpty())
			winningBid = bids.last();
		
		if (winningBid != null){
			log.info("Winner! " + winningBid.getBidder().getUserId());
			log.info(String.format(" bought %s for $%.2f", item.getTitle(),winningBid.getAmount()));
			POC winner = winningBid.getBidder();
			winner.getPurchases().add(item);
			Account accountDetails = accounts.getAccountByUser(winner.getUserId());
			accounts.updateAccount(accountDetails);
			item.setBuyer(winner);
			item.setPurchasePrice(winningBid.getAmount());
			//TODO: when should we choose which shipping address the user prefers for this order? 
			//Address shippingAddress = accounts.getAccountByUser(winningUser).getAddresses().values();
			//item.setShipTo(shippingAddress);
		}
		else {
			log.info("Nobody bought " + item.getTitle());
		}
		
		
		item.setEndTime(Calendar.getInstance().getTime());
		
		auctions.updateAuction(item);
	}
	
}
