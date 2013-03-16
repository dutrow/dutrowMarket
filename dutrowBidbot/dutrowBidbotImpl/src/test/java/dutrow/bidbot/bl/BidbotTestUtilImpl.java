/**
 * 
 */
package dutrow.bidbot.bl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dutrow.bidbot.bo.*;


/**
 * @author dutroda1
 *
 */
public class BidbotTestUtilImpl implements BidbotTestUtil {

	/* (non-Javadoc)
	 * @see dutrow.bidbot.bl.BidbotTestUtil#reset()
	 */
	@Override
	public boolean reset() {
		// TODO Auto-generated method stub
		return true;

	}
	
	@Override
	public POC sellerPOC(String sellerID){
		POC sellerPOC = new POC(sellerID, "webmaster@www.apl.jhu.edu");
		return sellerPOC;
	}
	
	@Override
	public Account createSeller(){
		String sellerID = "seller";
		Map<String, Address> sellerAddress = new HashMap<String,Address>();
		sellerAddress.put("work", new Address("distributor", "Kossiakoff Center", "11100 Johns Hopkins Rd.", "Laurel", "MD", "21128"));
		POC sellerPOC = sellerPOC(sellerID);
		Calendar sellerStartDate = createTime(1);
		Account sellerAccount = new Account(sellerID, "Kossi", "-", "Mart", sellerStartDate, sellerAddress, sellerPOC);
		return sellerAccount;
	}
	
	@Override
	public Calendar createTime(int secondsOffset){
		Calendar nowPlus = GregorianCalendar.getInstance();
		nowPlus.add(Calendar.SECOND, secondsOffset);
		return nowPlus;
	}

	@Override
	public Account createDan(){
		String dansId = "dutrow"; 
		Map<String, Address> dansAddresses = new HashMap<String,Address>();
		dansAddresses.put("work", new Address("shipping", "Dan's Colleague", "11100 Johns Hopkins Rd.", "Laurel", "MD", "21128"));
		POC dansPoint = new POC(dansId, "dan.dutrow@gmail.com");
		Calendar dansStartDate = createTime(2);
		Account dansAccount = new Account(dansId, "Dan", "A", "Dutrow", dansStartDate, dansAddresses, dansPoint);
		return dansAccount;
	}
	
	@Override
	public Account createJim(){
		String jimsId = "jcs";
		Map<String, Address> jimsAddresses = new HashMap<String,Address>();
		jimsAddresses.put("work", new Address("shipping", "Jim's Grader", "11100 Johns Hopkins Rd.", "Laurel", "MD", "21128"));
		POC jimsPoint = new POC(jimsId, "ejava_class@yahoogroups.com");
		Calendar jimsStartDate = createTime(3);
		Account jimsAccount = new Account(jimsId, "Jim", "C", "Stafford", jimsStartDate, jimsAddresses, jimsPoint);
		return jimsAccount;
	}
	
	@Override
	public AuctionItem createAuctionItem(String itemName, Account seller){
		
		List<Image> images = new LinkedList<Image>();
		Calendar startTime = createTime(5);
		Calendar endTime = createTime(10);
		AuctionItem item =  new AuctionItem(
				itemName, null, "A beautiful " + itemName, 
				startTime, endTime, 10, seller.getPoc(), images);
		
		return item;
	}
	
	@Override
	public BidAccount createBidAccount(){
		BidAccount ba = new BidAccount();
		
		return ba;
	}
	
	@Override
	public Order createOrder(){
		Order o = new Order();
		
		return o;
	}
}
