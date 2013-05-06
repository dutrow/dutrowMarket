/**
 * 
 */
package dutrow.sales.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.persistence.NoResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.AccountMgmt;
import dutrow.sales.bl.SellerMgmt;
import dutrow.sales.bo.Account;
import dutrow.sales.bo.Address;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Image;
import dutrow.sales.bo.POC;
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

	@Resource
	protected SessionContext ctx;

	private @EJB
	SellerMgmtActionEJB actions;

	@Resource
	private TimerService timerService;
	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory connFactory;
	@Resource(mappedName = "java:/topic/ejava/projects/emarket/esales-action", type = Topic.class)
	private Destination sellTopic;

	// injected
	long checkItemInterval;

	@PostConstruct
	public void init() {
		try {
			log.debug("**** init ****");
			log.debug("timerService=" + timerService);
			log.debug("checkAuctionInterval=" + checkItemInterval);
			log.debug("connFactory=" + connFactory);
			log.debug("sellTopic=" + sellTopic);
			log.debug("sellerManager=" + sellerMgmt);
			log.debug("accountManager=" + acctMgmt);
		} catch (Throwable ex) {
			log.warn("error in init", ex);
			throw new EJBException("error in init" + ex);
		}
	}

	@Override
	public void cancelTimers() {
		log.debug("canceling timers");
		for (Timer timer : (Collection<Timer>) timerService.getTimers()) {
			timer.cancel();
		}
	}

	public void initTimers(long delay) {
		cancelTimers();
		log.debug("initializing timers, checkItemInterval=" + delay);
		timerService.createTimer(0, delay, "checkAuctionTimer");
	}

	public void initTimers(ScheduleExpression schedule) {
		cancelTimers();
		log.debug("initializing timers, schedule=" + schedule);
		timerService.createCalendarTimer(schedule);
	}

	@Timeout
	@Schedule(second = "*/10", minute = "*", hour = "*", dayOfMonth = "*", month = "*", year = "*")
	public void execute(Timer timer) {
		log.info("timer fired:" + timer);
		try {
			checkAuction();
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

	public int checkAuction() {
		log.info("checking auctions");
		Connection connection = null;
		Session session = null;
		int index = 0;
		try {
			connection = connFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Collection<AuctionItem> items = null;

			String userId = ctx.getCallerPrincipal().getName();
			items = sellerMgmt.getOpenUserAuctions(userId);

			for (AuctionItem item : items) {
				publishAuctionItem(session, item, "saleUpdate");
			}

			log.debug("processed " + index + " active items");
			return index;
		} catch (JMSException ex) {
			log.error("error publishing auction item updates", ex);
			return index;
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (JMSException ignored) {
			}
		}
	}

	protected void publishAuctionItem(Session session, AuctionItem item,
			String jmsType) throws JMSException {
		MessageProducer producer = null;
		try {
			producer = session.createProducer(sellTopic);
			MapMessage message = session.createMapMessage();
			message.setJMSType(jmsType);
			message.setLong("id", item.getId());
			message.setString("title", item.getTitle());
			message.setString("seller", item.getSeller().getUserId());
			message.setLong("startTime", item.getStartTime().getTime());
			message.setLong("endTime", item.getEndTime().getTime());
			message.setDouble("bids", item.getBids().size());
			message.setDouble("highestBid",
					(item.getHighestBid() == null ? 0.00 : item.getHighestBid()
							.getAmount()));
			producer.send(message);
			log.debug("sent=" + message);
		} finally {
			if (producer != null) {
				producer.close();
			}
		}
	}

	public void closeBidding(long itemId) throws SellerMgmtRemoteException {
		AuctionItem item = sellerMgmt.getAuction(itemId);

		if (item == null) {
			throw new SellerMgmtRemoteException("itemId not found:" + itemId);
		}

		try {

			item.closeAuction();
			POC buyer = item.getBuyer();
			if (buyer != null) {
				Account acct = acctMgmt.getAccount(buyer.getUserId());
				Map<String, Address> addressMap = acct.getAddresses();
				if (addressMap != null && !addressMap.isEmpty()) {
					Address shippingAddr = acct.getAddresses().get("shipping");
					if (shippingAddr != null) {
						item.setShipTo(acct.getAddresses().get("shipping"));
					} else {
						item.setShipTo(acct.getAddresses().values().iterator()
								.next());
					}
				}
			}
			log.debug("closed bidding for item:" + item);
		} catch (Exception ex) {
			log.error("error closing bid", ex);
			throw new SellerMgmtRemoteException("error closing bid:" + ex);
		}
	}

	public void endAuction(long itemId) throws SellerMgmtRemoteException {
		Connection connection = null;
		Session session = null;
		try {
			AuctionItem item = sellerMgmt.getAuction(itemId);
			if (item != null) {
				item.closeAuction();
				log.info("ending auction for:" + item);

				connection = connFactory.createConnection();
				session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				publishAuctionItem(session, item, "sold");
			}
		} catch (JMSException jex) {
			log.error("error publishing jms message:", jex);
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ignored) {
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long sellProduct(String sellerId, AuctionItem item)
			throws SellerMgmtRemoteException {
		log.debug("sellProduct(sellerId=" + sellerId + ",item=" + item + ")");

		Connection connection = null;
		Session session = null;
		Account seller = null;
		try {
			connection = connFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			seller = acctMgmt.getAccount(sellerId);
			item.setSeller(seller.getPoc());
			sellerMgmt.createAuction(item);

			publishAuctionItem(session, item, "forSale");
			timerService.createTimer(item.getEndTime(), new Long(item.getId()));
			return item.getId();
		} catch (JMSException ex) {
			log.error("error publishing sell", ex);
			ctx.setRollbackOnly();
			throw new EJBException("error publishing sell");
		} catch (NoResultException ex) {
			log.error("error locating information for sale, seller=" + seller,
					ex);
			ctx.setRollbackOnly();
			throw new SellerMgmtRemoteException(
					"error locating information for sale, " + "seller="
							+ seller + ":" + ex);
		} catch (Exception ex) {
			log.error("error selling product", ex);
			ctx.setRollbackOnly();
			throw new SellerMgmtRemoteException("error selling product:" + ex);
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (JMSException ex) {
				log.error("unable to close resources", ex);
			}
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
	@RolesAllowed({ "esales-user" })
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
