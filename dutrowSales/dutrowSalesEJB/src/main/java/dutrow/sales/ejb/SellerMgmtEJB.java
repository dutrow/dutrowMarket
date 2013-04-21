/**
 * 
 */
package dutrow.sales.ejb;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.SellerMgmt;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.ImageDTO;

/**
 * @author dutroda1
 *
 */
public class SellerMgmtEJB implements SellerMgmtLocal, SellerMgmtRemote {
	private static final Log log = LogFactory.getLog(SellerMgmtEJB.class);
	
	@Inject
	SellerMgmt sellerMgmt;
	
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

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.SellerMgmtRemote#createAuction(dutrow.sales.dto.AuctionDTO)
	 */
	@Override
	public long createAuction(AuctionDTO auction) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.SellerMgmtRemote#listMyAuctions(java.lang.String)
	 */
	@Override
	public Collection<AuctionDTO> listMyAuctions(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.SellerMgmtRemote#listMyOpenAuctions(java.lang.String)
	 */
	@Override
	public Collection<AuctionDTO> listMyOpenAuctions(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.SellerMgmtRemote#getAuction(long)
	 */
	@Override
	public AuctionDTO getAuction(long auctionId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.SellerMgmtRemote#getAuctionImages(long)
	 */
	@Override
	public Collection<ImageDTO> getAuctionImages(long auctionId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.SellerMgmtLocal#getUserAuctions(java.lang.String)
	 */
	@Override
	public Collection<AuctionDTO> getUserAuctions(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dutrow.sales.ejb.SellerMgmtLocal#getOpenUserAuctions(java.lang.String)
	 */
	@Override
	public Collection<AuctionDTO> getOpenUserAuctions(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
}
