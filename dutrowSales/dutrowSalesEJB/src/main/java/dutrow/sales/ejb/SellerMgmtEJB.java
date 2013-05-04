/**
 * 
 */
package dutrow.sales.ejb;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.SellerMgmt;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Image;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.DTOConversionUtil;
import dutrow.sales.dto.ImageDTO;

/**
 * @author dutroda1
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"esales-user", "esales-admin"})
// esales-admin		these users will be able to perform management and test functions on eSales.
// esales-user		these users can create and auction, and bid on auctions. This role is also required to subscribe to JMS auction events.
public class SellerMgmtEJB implements SellerMgmtLocal, SellerMgmtRemote {
	private static final Log log = LogFactory.getLog(SellerMgmtEJB.class);

	@Inject
	SellerMgmt sellerMgmt;
	
	@Resource
	protected SessionContext ctx;

	@PostConstruct
	public void init() {
		try {
			log.debug("**** init ****");
			log.debug("init complete, sellerManager=" + sellerMgmt);
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
	 * @see
	 * dutrow.sales.ejb.SellerMgmtRemote#createAuction(dutrow.sales.dto.AuctionDTO
	 * )
	 */
	@Override
	@RolesAllowed({"esales-user"})
	public long createAuction(AuctionDTO auction) {
		log.debug("createAuction");
		return sellerMgmt.createAuction(DTOConversionUtil
				.convertAuctionDTO(auction));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.SellerMgmtRemote#getUserAuctions(java.lang.String)
	 */
	@Override
	@RolesAllowed({"esales-user"})
	public Collection<AuctionDTO> getUserAuctions() {
		String userId = ctx.getCallerPrincipal().getName();
		Collection<AuctionItem> auctions = sellerMgmt.getUserAuctions(userId);

		Collection<AuctionDTO> auctionDTOs = new ArrayList<AuctionDTO>();
		for (AuctionItem oa : auctions) {
			auctionDTOs.add(DTOConversionUtil.convertAuctionItem(oa));
		}
		return auctionDTOs;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dutrow.sales.ejb.SellerMgmtLocal#getOpenUserAuctions(java.lang.String)
	 */
	@Override
	@RolesAllowed({"esales-user"})
	public Collection<AuctionDTO> getOpenUserAuctions() {
		String userId = ctx.getCallerPrincipal().getName();
		Collection<AuctionItem> auctions = sellerMgmt
				.getOpenUserAuctions(userId);

		Collection<AuctionDTO> openAuctions = new ArrayList<AuctionDTO>();
		for (AuctionItem oa : auctions) {
			openAuctions.add(DTOConversionUtil.convertAuctionItem(oa));
		}
		return openAuctions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.SellerMgmtRemote#getAuction(long)
	 */
	@Override
	@RolesAllowed({"esales-user"})
	public AuctionDTO getAuction(long auctionId) {
		AuctionItem ai = sellerMgmt.getAuction(auctionId);
		return DTOConversionUtil.convertAuctionItem(ai);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.SellerMgmtRemote#getAuctionImages(long)
	 */
	@Override
	@RolesAllowed({"esales-user"})
	public Collection<ImageDTO> getAuctionImages(long auctionId) {
		log.debug("*** getAuctionImages() ***");
		Collection<ImageDTO> imageBytes = new ArrayList<ImageDTO>();

		AuctionItem a = sellerMgmt.getAuction(auctionId);

		Collection<Image> images = a.getImages();

		for (Image i : images) {
			imageBytes.add(new ImageDTO(i.getImage()));
		}

		return imageBytes;
	}

	
}
