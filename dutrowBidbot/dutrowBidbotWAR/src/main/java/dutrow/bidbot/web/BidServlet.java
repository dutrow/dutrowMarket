package dutrow.bidbot.web;

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

import dutrow.bidbot.ejb.BidbotUtilRemote;
import dutrow.bidbot.ejb.OrderMgmtRemote;

@SuppressWarnings("serial")
public class BidServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(BidServlet.class);

	private Map<String, Handler> handlers = new HashMap<String, Handler>();

	/**
	 * This will get automatically inject when running within the application
	 * server.
	 */
	//TODO: DUTROW @Inject
	private OrderMgmtRemote injectedOrderMgmt;

	//TODO: DUTROW @Inject
	private BidbotUtilRemote injectedBidbotUtil;

	/**
	 * Init verify the reference to the EJB logic is in place and initializes
	 * the proper handler for the assigned role supplied in the servlet init
	 * parameters.
	 */
	public void init() throws ServletException {
		log.debug("init() called; injectedBuyerMgmt=" + injectedOrderMgmt);
		log.debug("init() called; injectedBidbotUtil=" + injectedBidbotUtil);

		try {
			ServletConfig config = getServletConfig();
			// build a list of handlers for individual commands
			log.info("HANDLER_TYPE "
					+ config.getInitParameter(Strings.HANDLER_TYPE_KEY));

			if (Strings.ADMIN_TYPE.equals(config
					.getInitParameter(Strings.HANDLER_TYPE_KEY))) {
				handlers.put(Strings.CREATE_ACCOUNT, new CreateBidAccount());

			} else if (Strings.USER_TYPE.equals(config
					.getInitParameter(Strings.HANDLER_TYPE_KEY))) {
				handlers.put(Strings.PLACE_ORDER, new PlaceOrder());
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

		String command = ""; // TODO request.getParameter(Strings.COMMAND_PARAM);
		log.debug("command=" + command);

		InitialContext jndi = null;
		OrderMgmtRemote orderMgmt = injectedOrderMgmt;
		BidbotUtilRemote util = injectedBidbotUtil;

		try {
			if (orderMgmt == null || util == null ) {
				 // not injected -- manually lookup
				ServletConfig config = getServletConfig();
				String ctxFactory = config.getServletContext()
						.getInitParameter(Context.INITIAL_CONTEXT_FACTORY);
				log.debug(Context.INITIAL_CONTEXT_FACTORY + "=" + ctxFactory);
				if (ctxFactory != null) {
					Properties env = new Properties();
					env.put(Context.INITIAL_CONTEXT_FACTORY, ctxFactory);
					jndi = new InitialContext(env);
				} else {
					jndi = new InitialContext();
				}

				String jndiName;

				jndiName = config.getServletContext().getInitParameter(
						"dutrow.ordermgmt.remote");
				log.info("JNDI Name: " + jndiName);
				orderMgmt = (OrderMgmtRemote) jndi.lookup(jndiName);

				jndiName = config.getServletContext().getInitParameter(
						"dutrow.bidbotutil.remote");
				log.info("JNDI Name: " + jndiName);
				util = (BidbotUtilRemote) jndi.lookup(jndiName);

			}
			if (command != null) {
				Handler handler = handlers.get(command);
				if (handler != null) {
					handler.handle(request, response, getServletContext(),
							orderMgmt, util);
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
