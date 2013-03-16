/**
 * 
 */
package dutrow.sales.bl.impl;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import dutrow.sales.bl.BuyerMgmt;
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
	 * @see dutrow.sales.bl.BuyerMgmt#placeBid(dutrow.sales.bo.Bid)
	 */
	public boolean placeBid(Bid bidDetails) {
		
		// TODO: Evaluate if this is efficient 
		// or choose to just get the AuctionItem from the Bid object
		AuctionItem ai = auctions.getAuctionById(bidDetails.getAuction().getId());
		//AuctionItem ai = bidDetails.getAuction();
		boolean auctionIsOpen = ai.getEndTime().after(bidDetails.getTimestamp());
		if (auctionIsOpen)
		{
			ai.getBids().add(bidDetails);
			auctions.updateAuction(ai);
			return true;
			
			/*
			double largestBid = 0;
			if (!ai.getBids().isEmpty())
				largestBid = ai.getBids().get(0).getAmount();
			
			
			//Boolean userExists = bid
			boolean bidIsValid = bidDetails.getAmount() > largestBid;
			
			// also check that the user exists
			if (auctionIsOpen && bidIsValid){
				ai.getBids().add(bidDetails);
				auctions.updateAuction(ai);
				return true;
			}
			*/
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.bl.BuyerMgmt#placeBid(dutrow.sales.bo.Account, dutrow.sales.bo.AuctionItem, float)
	 */
	@Override
	public Bid placeBid(POC bidder, long auctionId, float amount) {
		
		Date now = GregorianCalendar.getInstance().getTime();
		
		// the auction must exist
		AuctionItem verifiedAuction = this.auctions.getAuctionById(auctionId);
		if (verifiedAuction == null)
			return null;
		
		// the auction must be open
		if (verifiedAuction.getEndTime().before(now))
			return null;
		
		// the user must exist
		Account verifiedBidder = this.accounts.getAccountByUser(bidder.getUserId());
		if (verifiedBidder == null)
			return null;
		
		// the bid amount must be greater than any pre-existing bid
		Bid highestBid = verifiedAuction.getHighestBid();
		if (highestBid != null && highestBid.getAmount() > amount){
			return null;
		}
		
		// the bid is valid
		Bid successfulBid = new Bid(amount, now, bidder, verifiedAuction);
		
		// place the bid on the auction and save
		verifiedAuction.addBids(successfulBid);
		this.auctions.updateAuction(verifiedAuction);
		
		return successfulBid;
	}

}
