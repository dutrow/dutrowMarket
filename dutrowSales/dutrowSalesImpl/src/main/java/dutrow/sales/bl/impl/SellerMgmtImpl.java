/**
 * 
 */
package dutrow.sales.bl.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.SellerMgmt;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.dao.AuctionDAO;

/**
 * @author dutroda1
 *
 */
public class SellerMgmtImpl implements SellerMgmt {
	private static Log log = LogFactory.getLog(SellerMgmtImpl.class);

	private AuctionDAO auctions;
	
	public void setAuctionDAO(AuctionDAO accountDAO) {
		this.auctions = accountDAO;;
	}
	
	@SuppressWarnings("unused")
	private SellerMgmtImpl(){} // force use of DAO constructor
	
	public SellerMgmtImpl(AuctionDAO auctionDao){
		setAuctionDAO(auctionDao);
	}
	
	/* (non-Javadoc)
	 * @see dutrow.sales.bl.SellerMgmt#createAuction(dutrow.sales.bo.AuctionItem)
	 */
	@Override
	public AuctionItem createAuction(AuctionItem auctionDetails) {
		return auctions.createAuction(auctionDetails);
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.bl.SellerMgmt#getUserAuctions(java.lang.String)
	 */
	@Override
	public Collection<AuctionItem> getUserAuctions(String userId) {
		return auctions.getUserAuctions(userId);
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.bl.SellerMgmt#getOpenUserAuctions(java.lang.String)
	 */
	@Override
	public Collection<AuctionItem> getOpenUserAuctions(String userId) {
		Collection<AuctionItem> allUserAuctions = auctions.getUserAuctions(userId);
		Collection<AuctionItem> openAuctions = new ArrayList<AuctionItem>();
		
		for (AuctionItem i : allUserAuctions){
			if (i.isOpen())
				openAuctions.add(i);
		}
		
		return openAuctions;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.bl.SellerMgmt#getAuction(long)
	 */
	@Override
	public AuctionItem getAuction(long auctionId) {
		return auctions.getAuctionById(auctionId);
	}

}
