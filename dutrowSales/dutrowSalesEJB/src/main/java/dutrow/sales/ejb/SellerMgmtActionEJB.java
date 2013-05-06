package dutrow.sales.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class contains some demo methods to perform example actions on 
 * behalf of the AuctionMgmtEJB.
 */
@Stateless
public class SellerMgmtActionEJB {
	private static Log log = LogFactory.getLog(SellerMgmtActionEJB.class);
	
    /**
     * Perform action synchronously while caller waits.
     */
	public Date doWorkSync(long delay) {
		DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
		log.debug(String.format("sync method %d starting %d delay at %s", Thread.currentThread().getId(), delay, df.format(new Date())));
		try { Thread.sleep(delay); }
		catch (Exception ex) {
			log.error("unexpected error during sleep:", ex);
		}
		Date now = new Date();
		log.debug(String.format("sync method %d completed %d delay at %s", Thread.currentThread().getId(), delay, df.format(now)));
		
		return now;
	}    

	/**
	 * Perform action async from caller.
	 */
	@Asynchronous
	public Future<Date> doWorkAsync(long delay) {
		DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
		log.debug(String.format("async method %d starting %d delay at %s", Thread.currentThread().getId(), delay, df.format(new Date())));
		try { Thread.sleep(delay); }
		catch (Exception ex) {
			log.error("unexpected error during sleep:", ex);
		}
		Date now = new Date();
		log.debug(String.format("async method %d completed %d delay at %s", Thread.currentThread().getId(), delay, df.format(now)));
		
		return new AsyncResult<Date>(now);
	}    

}
