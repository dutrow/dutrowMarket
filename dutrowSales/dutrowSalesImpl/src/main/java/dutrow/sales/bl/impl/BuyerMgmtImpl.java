/**
 * 
 */
package dutrow.sales.bl.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import dutrow.sales.bl.BuyerMgmt;
import dutrow.sales.bo.Account;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.bo.BidResult;
import dutrow.sales.bo.Image;
import dutrow.sales.bo.POC;
import dutrow.sales.dao.AccountDAO;
import dutrow.sales.dao.AuctionDAO;

/**
 * @author dutroda1
 *
 */
public class BuyerMgmtImpl implements BuyerMgmt {

	private AccountDAO accounts;
	private AuctionDAO auctions;
	
	public void setAccountDAO(AccountDAO accountDAO) {
		this.accounts = accountDAO;;
	}
	
	public void setAuctionDAO(AuctionDAO auctionDAO) {
		this.auctions = auctionDAO;;
	}
	
	@SuppressWarnings("unused")
	private BuyerMgmtImpl(){} // force use of DAO constructor
	
	public BuyerMgmtImpl(AccountDAO accountDao, AuctionDAO auctionDao){
		setAuctionDAO(auctionDao);
		setAccountDAO(accountDao);
	}
	
	/* (non-Javadoc)
	 * @see dutrow.sales.bl.BuyerMgmt#getOpenAuctions()
	 */
	@Override
	public Collection<AuctionItem> getOpenAuctions() {
		return auctions.getAuctions();
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.bl.BuyerMgmt#getAuction(java.lang.String)
	 */
	@Override
	public AuctionItem getAuction(long auctionId) {
		return auctions.getAuctionById(auctionId);
	}


	
	/* (non-Javadoc)
	 * @see dutrow.sales.bl.BuyerMgmt#placeBid(dutrow.sales.bo.Account, dutrow.sales.bo.AuctionItem, float)
	 */
	@Override
	public BidResult placeBid(String bidder, long auctionId, float amount) {
		
		Date now = GregorianCalendar.getInstance().getTime();
		
		// the auction must exist
		AuctionItem verifiedAuction = this.auctions.getAuctionById(auctionId);
		if (verifiedAuction == null)
			return new BidResult(null, "auction " + auctionId + " does not exist");
		
		// the auction must be open
		if (verifiedAuction.isExpired())
			return new BidResult(null, "auction " + auctionId + " is expired");
		
		if (!verifiedAuction.isOpen())
			return new BidResult(null, "auction " + auctionId + " is not open");
		
		// the user must exist
		Account verifiedBidder = this.accounts.getAccountByUser(bidder);
		if (verifiedBidder == null)
			return new BidResult(null, "bidder " + bidder +  " does not exist");
		
		// the bid amount must be greater than any pre-existing bid
		Bid highestBid = verifiedAuction.getHighestBid();
		if (highestBid != null && highestBid.getAmount() > amount){
			return new BidResult(null, "bid is not higher than previous bid");
		}
		
		// the bid is valid
		Bid successfulBid = new Bid(amount, now, verifiedBidder.getPoc(), verifiedAuction);
		
		// place the bid on the auction and save
		verifiedAuction.addBids(successfulBid);
		this.auctions.updateAuction(verifiedAuction);
		
		return new BidResult(successfulBid, "bid was successful");
	}



	/* (non-Javadoc)
	 * @see dutrow.sales.bl.BuyerMgmt#listBids(java.lang.String)
	 */
	@Override
	public Collection<Bid> listBids(String userId) {
		Account user = accounts.getAccountByUser(userId);
		
		return user.getPoc().getBids();
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.bl.BuyerMgmt#listOpenBids(java.lang.String)
	 */
	@Override
	public Collection<Bid> listOpenBids(String userId) {
		Account user = accounts.getAccountByUser(userId);
		Collection<Bid> allBids = user.getPoc().getBids();
		Collection<Bid> openBids = new ArrayList<Bid>();
		
		for (Bid b : allBids ){
			if (b.getAuction().isOpen())
				openBids.add(b);
		}
		
		return openBids;
	}

}
