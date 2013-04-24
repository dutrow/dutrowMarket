/**
 * 
 */
package dutrow.sales.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.BuyerMgmt;
import dutrow.sales.bl.BuyerMgmtException;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.bo.BidResult;
import dutrow.sales.bo.Image;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.BidResultDTO;
import dutrow.sales.dto.DTOConversionUtil;
import dutrow.sales.dto.ImageDTO;

/**
 * @author dutroda1
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BuyerMgmtEJB implements BuyerMgmtLocal, BuyerMgmtRemote,
		javax.ejb.SessionSynchronization {
	private static final Log log = LogFactory.getLog(BuyerMgmtEJB.class);

	@Inject
	BuyerMgmt buyerMgmt;

	@Resource
	protected SessionContext ctx;

	@PostConstruct
	public void init() {
		try {
			log.debug("**** init ****");
			log.debug("init complete, buyerManager=" + buyerMgmt);
		} catch (Throwable ex) {
			log.warn("error in init", ex);
			throw new EJBException("error in init" + ex);
		}
	}

	@PreDestroy
	public void close() {
		log.debug("*** close() ***");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionSynchronization#afterBegin()
	 */
	@Override
	public void afterBegin() throws EJBException, RemoteException {
		log.debug("* Transaction Started *");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionSynchronization#afterCompletion(boolean)
	 */
	@Override
	public void afterCompletion(boolean arg0) throws EJBException,
			RemoteException {
		log.debug("* Transaction Completed *");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionSynchronization#beforeCompletion()
	 */
	@Override
	public void beforeCompletion() throws EJBException, RemoteException {
		log.debug("* Transaction about to complete * ");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.BuyerMgmtRemote#listOpenAuctions()
	 */
	@Override
	public Collection<AuctionDTO> getOpenAuctions() {
		log.debug("*** listOpenAuctions() *** ");
		Collection<AuctionItem> oas = buyerMgmt.getOpenAuctions();
		Collection<AuctionDTO> openAuctions = new ArrayList<AuctionDTO>();
		for (AuctionItem oa : oas) {
			openAuctions.add(DTOConversionUtil.convertAuctionItem(oa));
		}
		return openAuctions;
	}

	@Override
	public AuctionItem getAuction(long auction) throws BuyerMgmtException {
		log.debug("*** getAccount() *** ");

		try {
			return buyerMgmt.getAuction(auction);
		} catch (Throwable ex) {
			log.error(ex);
			ctx.setRollbackOnly();
			throw new BuyerMgmtException(ex.toString());
		}
	}

	@Override
	public AuctionDTO getAuctionDTO(long auction) throws BuyerMgmtException {
		log.debug("*** getAuctionDTO ***");
		return DTOConversionUtil.convertAuctionItem(getAuction(auction));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.BuyerMgmtRemote#getAuctionImages(java.lang.String)
	 */
	@Override
	public Collection<ImageDTO> getAuctionImages(long auctionId) {
		log.debug("*** getAuctionImages() ***");
		Collection<ImageDTO> imageBytes = new ArrayList<ImageDTO>();

		AuctionItem ai = buyerMgmt.getAuction(auctionId);
		Collection<Image> images = ai.getImages();

		for (Image i : images) {
			imageBytes.add(new ImageDTO(i.getImage()));
		}

		return imageBytes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.BuyerMgmtLocal#listMyBids(java.lang.String)
	 */
	@Override
	public Collection<BidDTO> listMyBids(String userId) {
		log.debug("*** listMyBids() ***");
		Collection<BidDTO> bids = new ArrayList<BidDTO>();
		Collection<Bid> listedBids = buyerMgmt.listBids(userId);

		for (Bid b : listedBids) {
			BidDTO bd = DTOConversionUtil.convertBid(b);

			bids.add(bd);
		}

		return bids;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.BuyerMgmtLocal#listMyOpenBids(java.lang.String)
	 */
	@Override
	public Collection<BidDTO> listMyOpenBids(String userId) {
		log.debug("*** listMyOpenBids() ***");
		Collection<BidDTO> bids = new ArrayList<BidDTO>();
		Collection<Bid> listedBids = buyerMgmt.listOpenBids(userId);

		for (Bid b : listedBids)
			bids.add(DTOConversionUtil.convertBid(b));

		return bids;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.BuyerMgmtRemote#placeBid(java.lang.String, long,
	 * float)
	 */
	@Override
	public BidResultDTO placeBid(String userId, long auctionId, float bidValue) {
		log.debug("*** placeBid() ***");

		BidResult bidResult = buyerMgmt.placeBid(userId, auctionId, bidValue);

		if (bidResult.getBid() == null) {
			ctx.setRollbackOnly();
			throw new EJBException(bidResult.getResult());
		}

		return DTOConversionUtil.convertBidResult(bidResult);
	}

	@Override
	public BidResultDTO placeBidLocal(String userId, long auctionId,
			float bidValue) {
		log.debug("*** placeBidLocal() ***");

		BidResult bidResult = buyerMgmt.placeBid(userId, auctionId, bidValue);

		return DTOConversionUtil.convertBidResult(bidResult);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.BuyerMgmtRemote#placeMultiBid(java.lang.String,
	 * long, float[])
	 */
	@Override
	public boolean placeMultiBid(String userId, long auctionId,
			float[] bidValues) throws BuyerMgmtException {
		log.debug("*** placeMultiBid() ***");

		for (float bidValue : bidValues) {
			BidResult bidResult = buyerMgmt.placeBid(userId, auctionId,
					bidValue);

			if (bidResult.getBid() == null) {
				ctx.setRollbackOnly();
				throw new EJBException(bidResult.getResult());
			}
		}
		return true;
	}

}
