/**
 * 
 */
package dutrow.sales.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.SellerMgmt;

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
}
