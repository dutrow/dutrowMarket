package dutrow.sales.web;

import java.io.IOException;
import java.util.Properties;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.ejb.AccountMgmtRemote;

@SuppressWarnings("serial")
public class AccountMgmtServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(AccountMgmtServlet.class);
	
	@EJB private AccountMgmtRemote registrar;

	public void init() throws ServletException {
		log.debug("init() called ");
		try {
			ServletConfig config = getServletConfig();
			initRegistrar(config);
		} catch (Exception ex) {
			log.fatal("error initializing handler", ex);
			throw new ServletException("error initializing handler", ex);
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.debug("doGet() called");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.debug("doPost() called, calling doGet()");
		doGet(request, response);
	}

	public void destroy() {
		log.debug("destroy() called");
	}

	private void initRegistrar(ServletConfig config) throws Exception {
		log.debug("initRegistrar(), registrar=" + registrar);
		if (registrar == null) {
			// build an InitialContext from Servlet.init properties in web.xml
			InitialContext jndi = null;
			String ctxFactory = config.getServletContext().getInitParameter(
					Context.INITIAL_CONTEXT_FACTORY);
			log.debug(Context.INITIAL_CONTEXT_FACTORY + "=" + ctxFactory);
			if (ctxFactory != null) {
				Properties env = new Properties();
				env.put(Context.INITIAL_CONTEXT_FACTORY, ctxFactory);
				jndi = new InitialContext(env);
			} else {
				jndi = new InitialContext();
			}
			
			String jndiName = config.getServletContext().getInitParameter("dutrow.accountmgmt.remote");
            registrar = (AccountMgmtRemote)jndi.lookup(jndiName);
            log.debug("registrar initialized:" + registrar);
		}
	}
}