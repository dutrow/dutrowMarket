/**
 * 
 */
package dutrow.sales.ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import dutrow.sales.bo.Account;
import dutrow.sales.bo.Address;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Bid;
import dutrow.sales.bo.BidResult;
import dutrow.sales.bo.Category;
import dutrow.sales.bo.POC;
import dutrow.sales.dto.AccountDTO;
import dutrow.sales.dto.AddressDTO;
import dutrow.sales.dto.AuctionDTO;
import dutrow.sales.dto.BidDTO;
import dutrow.sales.dto.BidResultDTO;

/**
 * @author dutroda1
 * 
 */
public final class DTOConversionUtil {

	/**
	 * @param addr
	 * @return
	 */
	public static Address convertAddressDTO(AddressDTO addr) {
		if (addr == null)
			return null;

		Address value = new Address(addr.name, addr.to, addr.street, addr.city,
				addr.state, addr.zip);
		return value;
	}

	/**
	 * @param accountDetails
	 * @return
	 */
	public static Account convertAccountDTO(AccountDTO accountDetails) {
		if (accountDetails == null)
			return null;

		Map<String, Address> addresses = new TreeMap<String, Address>();
		if (accountDetails.addresses != null)
			for (AddressDTO addr : accountDetails.addresses) {
				Address value = DTOConversionUtil.convertAddressDTO(addr);
				addresses.put(addr.name, value);
			}

		POC poc = new POC();
		poc.setEmail(accountDetails.email);
		poc.setUserId(accountDetails.userId);

		Account acct = new Account(accountDetails.userId,
				accountDetails.firstName, accountDetails.middleName,
				accountDetails.lastName, Calendar.getInstance().getTime(),
				addresses, poc);

		return acct;
	}

	/**
	 * @param addr
	 * @return
	 */
	public static AddressDTO convertAddress(Address addr) {
		if (addr == null)
			return null;

		AddressDTO addrDto = new AddressDTO(addr.getName(), addr.getTo(),
				addr.getStreet(), addr.getCity(), addr.getState(),
				addr.getZip());
		return addrDto;
	}

	/**
	 * @param acct
	 * @return
	 */
	public static AccountDTO convertAccount(Account acct) {
		if (acct == null)
			return null;

		AccountDTO adto = new AccountDTO(acct.getUserId(), acct.getFirstName(),
				acct.getMiddleName(), acct.getLastName(), acct.getPoc()
						.getEmail());

		for (Entry<String, Address> addrSet : acct.getAddresses().entrySet()) {
			Address addr = addrSet.getValue();
			AddressDTO addrDto = DTOConversionUtil.convertAddress(addr);

			adto.addresses.add(addrDto);
		}

		return adto;

	}

	/**
	 * @param ai
	 * @return
	 */
	public static AuctionDTO convertAuctionItem(AuctionItem ai) {
		if (ai == null)
			return null;

		AuctionDTO adto = new AuctionDTO();
		adto.title = ai.getTitle();
		adto.category = ai.getCategory().toString();
		adto.description = ai.getDescription();
		adto.startTime = ai.getStartTime();
		adto.endTime = ai.getEndTime();
		adto.askingPrice = ai.getAskingPrice();
		adto.purchasePrice = ai.getPurchasePrice();
		adto.shipTo = DTOConversionUtil.convertAddress(ai.getShipTo());
		adto.bids = new ArrayList<BidDTO>();
		for (Bid b : ai.getBids()) {
			BidDTO biddto = DTOConversionUtil.convertBid(b);
			adto.bids.add(biddto);
		}
		return adto;
	}

	/**
	 * @param auction
	 * @return
	 */
	public static AuctionItem convertAuctionDTO(AuctionDTO auction) {
		if (auction == null)
			return null;

		AuctionItem ai = new AuctionItem();
		ai.setSeller(new POC(auction.seller.userId, auction.seller.email));
		ai.setTitle(auction.title);
		ai.setCategory(Category.getCategory(auction.category));
		ai.setDescription(auction.description);
		ai.setStartTime(auction.startTime);
		ai.setEndTime(auction.endTime);
		ai.setAskingPrice(auction.askingPrice);
		ai.setPurchasePrice(auction.purchasePrice);
		ai.setShipTo(DTOConversionUtil.convertAddressDTO(auction.shipTo));
		
		boolean isOpen;
		Calendar now = Calendar.getInstance();
		if (auction.startTime == null) {
			isOpen = false;
		} else if (auction.endTime == null) {
			isOpen = now.getTime().after(auction.startTime);
		} else {
			isOpen = now.after(auction.startTime)
					&& now.before(auction.endTime);
		}
		ai.setOpen(isOpen);

		if (auction.bids != null)
			for (BidDTO bd : auction.bids)
				ai.addBids(DTOConversionUtil.convertBidDTO(bd, ai));

		return ai;
	}

	/**
	 * @param b
	 * @return
	 */
	public static BidDTO convertBid(Bid b) {
		if (b == null)
			return null;

		BidDTO bd = new BidDTO();
		bd.amount = b.getAmount();
		bd.auctionItem = b.getAuction().getId();
		bd.poc = b.getBidder().getUserId();
		bd.poc_email = b.getBidder().getEmail();
		bd.timestamp = b.getTimestamp();

		return bd;
	}

	/**
	 * @param bd
	 * @return
	 */
	private static Bid convertBidDTO(BidDTO bd, AuctionItem ai) {
		if (bd == null)
			return null;

		Bid b = new Bid();
		b.setAmount(bd.amount);
		POC poc = new POC(bd.poc, bd.poc_email);
		b.setBidder(poc);
		b.setTimestamp(bd.timestamp);

		b.setAuction(ai);

		return b;
	}

	/**
	 * @param bidResult
	 * @return
	 */
	public static BidResultDTO convertBidResult(BidResult bidResult) {
		BidResultDTO result = new BidResultDTO();
		if (bidResult != null) {
			result.bid = convertBid(bidResult.getBid());
			result.result = bidResult.getResult();
		}
		return result;

	}

}
