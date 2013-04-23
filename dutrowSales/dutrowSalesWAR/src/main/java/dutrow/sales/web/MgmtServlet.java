package dutrow.sales.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.AccountMgmt;
import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.ParserRemote;
import dutrow.sales.ejb.SellerMgmtRemote;
import dutrow.sales.ejb.SupportRemote;

@SuppressWarnings("serial")
public class MgmtServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(MgmtServlet.class);

	private Map<String, Handler> handlers = new HashMap<String, Handler>();

	/**
	 * This will get automatically inject when running within the application
	 * server.
	 */
	@Inject
	private BuyerMgmtRemote injectedBuyerMgmt;
	@Inject
	private AccountMgmt injectedAccountMgmt;
	@Inject
	private SellerMgmtRemote injectedSellerMgmt;
	@Inject
	private ParserRemote injectedParser;
	@Inject
	private SupportRemote injectedSupport;

	/**
	 * Init verify the reference to the EJB logic is in place and initializes
	 * the proper handler for the assigned role supplied in the servlet init
	 * parameters.
	 */
	public void init() throws ServletException {
		log.debug("init() called; injectedBuyerMgmt=" + injectedBuyerMgmt);
		log.debug("init() called; injectedAccountMgmt=" + injectedAccountMgmt);
		log.debug("init() called; injectedSellerMgmt=" + injectedSellerMgmt);
		log.debug("init() called; injectedParser=" + injectedParser);
		log.debug("init() called; injectedSupport=" + injectedSupport);

		try {
			ServletConfig config = getServletConfig();
			// build a list of handlers for individual commands
			if (Strings.ADMIN_TYPE.equals(config
					.getInitParameter(Strings.HANDLER_TYPE_KEY))) {

				handlers.put(Strings.LIST_OPEN_AUCTIONS, new ListOpenAuctions());
				handlers.put(Strings.GET_AUCTION, new GetAuction());
				handlers.put(Strings.CREATE_ACCOUNT, new CreateAccount());
				handlers.put(Strings.RESET_ALL, new ResetAll());
				handlers.put(Strings.POPULATE, new Populate());
				handlers.put(Strings.CREATE_AUCTION, new CreateAuction());
				handlers.put(Strings.GET_USER_AUCTIONS, new GetUserAuctions());
				handlers.put(Strings.PLACE_BID, new PlaceBid());
			}
		} catch (Exception ex) {
			log.fatal("error initializing handler", ex);
			throw new ServletException("error initializing handler", ex);
		}
	}

	/**
	 * This is the main dispatch method for the servlet. It expects to find a
	 * command keyed by an argument in the request parameters.
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.debug("doGet() called");

		String command = request.getParameter(Strings.COMMAND_PARAM);
		log.debug("command=" + command);

		InitialContext jndi = null;
		BuyerMgmtRemote buyerMgmt = injectedBuyerMgmt; // assign to what was
														// injected
		AccountMgmt accountMgmt = injectedAccountMgmt;
		SellerMgmtRemote sellerMgmt = injectedSellerMgmt;
		ParserRemote parser = injectedParser;
		SupportRemote support = injectedSupport;

		try {
			if (buyerMgmt == null || accountMgmt == null || sellerMgmt == null
					|| parser == null || support == null) { // not injected --
															// manually lookup
				ServletConfig config = getServletConfig();
	            String ctxFactory = config.getServletContext()
	                                      .getInitParameter(Context.INITIAL_CONTEXT_FACTORY);
	            log.debug(Context.INITIAL_CONTEXT_FACTORY + "=" + ctxFactory);
	            if (ctxFactory!=null) {
	                    Properties env = new Properties();
	                    env.put(Context.INITIAL_CONTEXT_FACTORY, ctxFactory);
	                    jndi = new InitialContext(env);
	            }
	            else {
	                    jndi = new InitialContext();
	            }

				String jndiName;

				jndiName = config.getServletContext().getInitParameter(
						"dutrow.buyermgmt.remote");
				log.info("JNDI Name: " + jndiName);
				buyerMgmt = (BuyerMgmtRemote) jndi.lookup(jndiName);

				jndiName = config.getServletContext().getInitParameter(
						"dutrow.accountmgmt.remote");
				log.info("JNDI Name: " + jndiName);
				accountMgmt = (AccountMgmt) jndi.lookup(jndiName);

				jndiName = config.getServletContext().getInitParameter(
						"dutrow.sellermgmt.remote");
				log.info("JNDI Name: " + jndiName);
				sellerMgmt = (SellerMgmtRemote) jndi.lookup(jndiName);

				jndiName = config.getServletContext().getInitParameter(
						"dutrow.parser.remote");
				log.info("JNDI Name: " + jndiName);
				parser = (ParserRemote) jndi.lookup(jndiName);

				jndiName = config.getServletContext().getInitParameter(
						"dutrow.support.remote");
				log.info("JNDI Name: " + jndiName);
				support = (SupportRemote) jndi.lookup(jndiName);

			}
			if (command != null) {
				Handler handler = handlers.get(command);
				if (handler != null) {
					handler.handle(request, response, getServletContext(),
							buyerMgmt, accountMgmt, sellerMgmt, parser, support);
				} else {
					RequestDispatcher rd = getServletContext()
							.getRequestDispatcher(Strings.UNKNOWN_COMMAND_URL);

					rd.forward(request, response);
				}
			} else {
				throw new Exception("no " + Strings.COMMAND_PARAM
						+ " supplied.");
			}
		} catch (Exception ex) {
			request.setAttribute(Strings.EXCEPTION_PARAM, ex);

			Set<String> ss = handlers.keySet();
			for (String string : ss) {
				log.info("Handler: " + string);
			}
			log.warn("Exception: ", ex);

			RequestDispatcher rd = getServletContext().getRequestDispatcher(
					Strings.UNKNOWN_COMMAND_URL);
			rd.forward(request, response);
		} finally {
			if (jndi != null) {
				try {
					jndi.close();
				} catch (Exception ex) {
				}
			}
		}
	}

	/**
	 * Since this is a toy, we don't really care whether they call get or post.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.debug("doPost() called, calling doGet()");
		doGet(request, response);
	}

	public void destroy() {
		log.debug("destroy() called");
	}

}
