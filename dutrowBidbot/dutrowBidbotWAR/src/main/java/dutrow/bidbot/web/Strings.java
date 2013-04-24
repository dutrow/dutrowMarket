/**
 * 
 */
package dutrow.bidbot.web;

/**
 * @author dutroda1
 *
 */
public final class Strings {
	public static final String COMMAND_PARAM = "command";
	public static final String EXCEPTION_PARAM = "exception";
	public static final String HANDLER_TYPE_KEY = "type";
	public static final String ADMIN_TYPE = "admin";
	public static final String USER_TYPE = "user";
	public static final String ANON_TYPE = "anon";

	// COMMANDS
	public static final String GET_ACCOUNTS = "Get Accounts";
	public static final String GET_ACCOUNT = "Get Account";
	public static final String LIST_OPEN_AUCTIONS = "List Open Auctions";
	public static final String GET_AUCTION = "Get Auction";
	public static final String CREATE_ACCOUNT = "Create Account";
	public static final String RESET_ALL = "Reset All";
	public static final String POPULATE = "Populate";
	public static final String CREATE_AUCTION = "Create Auction";
	public static final String GET_USER_AUCTIONS = "Get User Auctions";
	public static final String PLACE_BID = "Place Bid";
	public static final String PLACE_ORDER = "Place Order";

	public static final String UNKNOWN_COMMAND_URL = "/WEB-INF/content/UnknownCommand.jsp";
	
	protected static final String MAIN_PAGE = "/index.jsp";
	protected static final String DISPLAY_EXCEPTION = "/WEB-INF/content/DisplayException.jsp";
	
	// PARAMS
	protected static final String ACCT_NUM_PARAM = "accountNumber";
	protected static final String AMOUNT_PARAM = "amount";
	protected static final String INDEX_PARAM = "index";
	protected static final String ID_PARAM = "id";
	protected static final String NEXT_INDEX_PARAM = "nextIndex";
	protected static final String COUNT_PARAM = "count";
	protected static final String ACCOUNT_PARAM = "account";
	protected static final String ACCOUNTS_PARAM = "accounts";
	protected static final String AUCTION_PARAM = "auction";
	protected static final String AUCTIONS_PARAM = "auctions";
	protected static final String ORDER_PARAM = "order";
	
	// URLS
	protected static final String DISPLAY_ACCOUNT_URL = "/WEB-INF/content/DisplayAccount.jsp";
	protected static final String DISPLAY_ORDER_URL = "/WEB-INF/content/DisplayOrder.jsp";
	
	
	


}
