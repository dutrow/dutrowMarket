package dutrow.bidbot.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bl.OrderMgmt;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.cdi.BidbotOrderManager;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.ejb.BuyerMgmtRemote;

/**
 * This class will listen for market events and cause further bidding to occur.
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationTopic", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/ejava/projects/emarket/esales-action"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "JMSType in ('closed, saleUpdate')"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class BidbotMDB implements MessageListener {
	private static final Log log = LogFactory.getLog(BidbotMDB.class);

	final String mName = "ejb:dutrowSalesEAR/dutrowSalesEJB/BuyerMgmtEJB!dutrow.sales.ejb.BuyerMgmtRemote";
	@EJB(mappedName = mName)
	private BuyerMgmtRemote buyerManager;

	@Inject
	@BidbotOrderManager
	private OrderMgmt orderMgmt;

	@Resource
	private MessageDrivenContext ctx;

	@EJB
	OrderMgmtHelperEJB orderMgmtHelper;

	@PostConstruct
	public void init() {
		log.info("*** BuyerMDB init() ***");
		log.debug("ctx=" + ctx);
		log.debug("buyer=" + buyerManager);
		log.debug("orderMgmt=" + orderMgmt);
	}

	public void onMessage(Message message) {
		try {
			log.debug("onMessage:" + message.getJMSMessageID());
			if (message instanceof ObjectMessage) {
				ObjectMessage om = (ObjectMessage) message;
				Object o = om.getObject();
				if (o instanceof AuctionDTO) {
					AuctionDTO dto = (AuctionDTO) o;
					if (message.getJMSType().equalsIgnoreCase("saleUpdate")) {
						orderMgmtHelper.processAuctionItem(dto);
					} else if (message.getJMSType().equalsIgnoreCase("closed")) {
						orderMgmt.endOrder(dto.id);
					}
				}
			}

		} catch (Exception ex) {
			log.error("error processing message", ex);
		}
	}

}
