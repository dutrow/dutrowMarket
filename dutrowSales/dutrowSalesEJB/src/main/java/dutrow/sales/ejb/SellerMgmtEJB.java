/**
 * 
 */
package dutrow.sales.ejb;

import java.beans.Transient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.persistence.NoResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.AccountMgmt;
import dutrow.sales.bl.SellerMgmt;
import dutrow.sales.bo.Account;
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
@RolesAllowed({ "esales-user", "esales-admin" })
// esales-admin these users will be able to perform management and test
// functions on eSales.
// esales-user these users can create and auction, and bid on auctions. This
// role is also required to subscribe to JMS auction events.
public class SellerMgmtEJB implements SellerMgmtLocal, SellerMgmtRemote {
	private static final Log log = LogFactory.getLog(SellerMgmtEJB.class);

	@Inject
	SellerMgmt sellerMgmt;
	@Inject
	AccountMgmt acctMgmt;
	
	@EJB
	SalesHelperEJB esalessys;

	@Resource
	protected SessionContext ctx;

	private @EJB // TODO: Remove, I think this serves the same function as my SalesHelperEJB
	SellerMgmtActionEJB actions;

	@Resource
	private TimerService timerService;
	

	// injected
	long checkAuctionInterval;

	@PostConstruct
	public void init() {
		try {
			log.info("**** SellerMgmtEJB init ****");
			log.info("timerService=" + timerService);
			log.info("checkAuctionInterval=" + checkAuctionInterval);
			log.info("sellerManager=" + sellerMgmt);
			log.info("accountManager=" + acctMgmt);
		} catch (Throwable ex) {
			log.warn("error in init", ex);
			throw new EJBException("error in init" + ex);
		}
	}

	@Override
	public void cancelTimers() {
		log.debug("sales canceling timers");
		for (Timer timer : (Collection<Timer>) timerService.getTimers()) {
			timer.cancel();
		}
	}

	public void initTimers(long delay) {
		cancelTimers();
		log.debug("initializing timers, checkAuctionInterval=" + delay);
		timerService.createTimer(0, delay, "checkAuctionTimer");
	}

	public void initTimers(ScheduleExpression schedule) {
		cancelTimers();
		log.debug("initializing timers, schedule=" + schedule);
		timerService.createCalendarTimer(schedule);
	}

	@Timeout
	@Transient
	@Schedule(second = "*/10", minute = "*", hour = "*", dayOfMonth = "*", month = "*", year = "*")
	public void execute(Timer timer) {
		log.info("timer fired:" + timer);
		try {
			esalessys.checkAuction();
		} catch (Exception ex) {
			log.error("error checking auction", ex);
		}
	}

	/**
	 * Perform action synchronously while caller waits.
	 */
	@Override
	public void workSync(int count, long delay) {
		DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			log.info(String.format("%s issuing sync request, delay=%d",
					df.format(new Date()), delay));
			@SuppressWarnings("unused")
			Date date = actions.doWorkSync(delay);
			log.info(String.format("sync waitTime=%d msecs",
					System.currentTimeMillis() - startTime));
		}
		long syncTime = System.currentTimeMillis() - startTime;
		log.info(String.format("workSync time=%d msecs", syncTime));
	}

	/**
	 * Perform action async from caller.
	 */
	@Override
	public void workAsync(int count, long delay) {
		DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");

		long startTime = System.currentTimeMillis();
		List<Future<Date>> results = new ArrayList<Future<Date>>();
		for (int i = 0; i < count; i++) {
			log.info(String.format("%s issuing async request, delay=%d",
					df.format(new Date()), delay));
			Future<Date> date = actions.doWorkAsync(delay);
			results.add(date);
			log.info(String.format("async waitTime=%d msecs",
					System.currentTimeMillis() - startTime));
		}
		for (Future<Date> f : results) {
			log.info(String.format("%s getting async response",
					df.format(new Date())));
			try {
				@SuppressWarnings("unused")
				Date date = f.get();
			} catch (Exception ex) {
				log.error("unexpected error on future.get()", ex);
				throw new EJBException("unexpected error during future.get():"
						+ ex);
			}
			log.info(String.format("%s got async response",
					df.format(new Date())));
		}
		long asyncTime = System.currentTimeMillis() - startTime;
		log.info(String.format("workAsync time=%d msecs", asyncTime));
	}

	
	

	@Override
	@RolesAllowed({ "esales-user" })
	public long createAuction(AuctionDTO auction)
			throws SellerMgmtRemoteException {
		log.debug("createAuction");

		AuctionItem auctionitem = DTOConversionUtil.convertAuctionDTO(auction);
		sellerMgmt.createAuction(auctionitem);

		Account seller = null;
		try {
			esalessys.publishAuctionItem(auctionitem, "forSale");
			timerService.createTimer(auctionitem.getEndTime(), new Long(
					auctionitem.getId()));
			return auctionitem.getId();
		} catch (NoResultException ex) {
			log.error("error locating information for sale, seller=" + seller,
					ex);
			ctx.setRollbackOnly();
			throw new SellerMgmtRemoteException(
					"error locating information for sale, " + "seller="
							+ seller + ":" + ex);
		} catch (Exception ex) {
			log.error("error creating auction:", ex);
			ctx.setRollbackOnly();
			throw new SellerMgmtRemoteException("error creating auction:" + ex);
		} 
	}

	@PreDestroy
	public void close() {
		log.debug("*** close() ***");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.ejb.SellerMgmtRemote#getUserAuctions(java.lang.String)
	 */
	@Override
	@RolesAllowed({ "esales-user" })
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
	@RolesAllowed({ "esales-user" })
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
	@RolesAllowed({ "esales-user" })
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
	@RolesAllowed({ "esales-user" })
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
