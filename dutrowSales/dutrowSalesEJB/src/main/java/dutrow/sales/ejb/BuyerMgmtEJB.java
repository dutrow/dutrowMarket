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
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.BuyerMgmt;
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
@RolesAllowed({"esales-user", "esales-trusted", "esales-admin"})
//esales-admin		these users will be able to perform management and test functions on eSales.
//esales-trusted	these users can bid on auctions on behalf of a specified user.
//esales-user		these users can create and auction, and bid on auctions. This role is also required to subscribe to JMS auction events.
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
	@PermitAll
	public Collection<AuctionDTO> getOpenAuctions() {
		log.debug("*** listOpenAuctions() *** ");
		log.debug("caller=" + ctx.getCallerPrincipal().getName());
		Collection<AuctionItem> oas = buyerMgmt.getOpenAuctions();
		Collection<AuctionDTO> openAuctions = new ArrayList<AuctionDTO>();
		for (AuctionItem oa : oas) {
			openAuctions.add(DTOConversionUtil.convertAuctionItem(oa));
		}
		return openAuctions;
	}

	@Override
	public AuctionItem getAuction(long auction) throws BuyerMgmtRemoteException {
		log.debug("*** getAccount() *** ");
		log.debug("caller=" + ctx.getCallerPrincipal().getName());
		try {
			return buyerMgmt.getAuction(auction);
		} catch (Throwable ex) {
			log.error(ex);
			ctx.setRollbackOnly();
			throw new BuyerMgmtRemoteException(ex.toString());
		}
	}

	@Override
	public AuctionDTO getAuctionDTO(long auction) throws BuyerMgmtRemoteException {
		log.debug("*** getAuctionDTO ***");
		log.debug("caller=" + ctx.getCallerPrincipal().getName());
		return DTOConversionUtil.convertAuctionItem(getAuction(auction));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.BuyerMgmtRemote#getAuctionImages(java.lang.String)
	 */
	@Override
	@RolesAllowed({"esales-user"})
	public Collection<ImageDTO> getAuctionImages(long auctionId) {
		log.debug("*** getAuctionImages() ***");
		log.debug("caller=" + ctx.getCallerPrincipal().getName());
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
	public Collection<BidDTO> listMyBids() {
		log.debug("*** listMyBids() ***");
		log.debug("caller=" + ctx.getCallerPrincipal().getName());
		Collection<BidDTO> bids = new ArrayList<BidDTO>();
		Collection<Bid> listedBids = buyerMgmt.listBids(ctx.getCallerPrincipal().getName());

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
	public Collection<BidDTO> listMyOpenBids() {
		log.debug("*** listMyOpenBids() ***");
		log.debug("caller=" + ctx.getCallerPrincipal().getName());
		Collection<BidDTO> bids = new ArrayList<BidDTO>();
		Collection<Bid> listedBids = buyerMgmt.listOpenBids(ctx.getCallerPrincipal().getName());

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
	public BidResultDTO placeBid(long auctionId, float bidValue) {
		log.debug("*** placeBid() ***");
		log.debug("caller=" + ctx.getCallerPrincipal().getName());
		BidResult bidResult = buyerMgmt.placeBid(ctx.getCallerPrincipal().getName(), auctionId, bidValue);

		if (bidResult.getBid() == null) {
			ctx.setRollbackOnly();
			throw new EJBException(bidResult.getResult());
		}

		return DTOConversionUtil.convertBidResult(bidResult);
	}

	@Override
	public BidResultDTO placeBidLocal(long auctionId,
			float bidValue) {
		log.debug("*** placeBidLocal() ***");
		log.debug("caller=" + ctx.getCallerPrincipal().getName());
		BidResult bidResult = buyerMgmt.placeBid(ctx.getCallerPrincipal().getName(), auctionId, bidValue);

		return DTOConversionUtil.convertBidResult(bidResult);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.BuyerMgmtRemote#placeMultiBid(java.lang.String,
	 * long, float[])
	 */
	@Override
	public boolean placeMultiBid(long auctionId,
			float[] bidValues) throws BuyerMgmtRemoteException {
		log.debug("*** placeMultiBid() ***");
		log.debug("caller=" + ctx.getCallerPrincipal().getName());
		for (float bidValue : bidValues) {
			BidResult bidResult = buyerMgmt.placeBid(ctx.getCallerPrincipal().getName(), auctionId,
					bidValue);

			if (bidResult.getBid() == null) {
				ctx.setRollbackOnly();
				throw new EJBException(bidResult.getResult());
			}
		}
		return true;
	}

}
