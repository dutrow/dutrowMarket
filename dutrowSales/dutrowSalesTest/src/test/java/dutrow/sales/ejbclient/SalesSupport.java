/**
 * 
 */
package dutrow.sales.ejbclient;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.ParserRemote;
import dutrow.sales.ejb.SellerMgmtRemote;
import dutrow.sales.ejb.SalesSupportRemote;
import ejava.util.ejb.EJBClient;

/**
 * @author dutroda1
 * 
 */
public class SalesSupport {
	private static final Log log = LogFactory.getLog(SalesSupport.class);
	protected InitialContext jndi;

	private static final String sellerJNDI = System
			.getProperty("jndi.name.seller",
					"dutrowSalesEAR/dutrowSalesEJB/SellerMgmtEJB!dutrow.sales.ejb.SellerMgmtRemote");
	protected SellerMgmtRemote sellerManager;

	private static final String buyerJNDI = System
			.getProperty("jndi.name.buyer",
					"dutrowSalesEAR/dutrowSalesEJB/BuyerMgmtEJB!dutrow.sales.ejb.BuyerMgmtRemote");
	protected BuyerMgmtRemote buyerManager;

	private static final String accountJNDI = System
			.getProperty(
					"jndi.name.account",
					"dutrowSalesEAR/dutrowSalesEJB/AccountMgmtEJB!dutrow.sales.ejb.AccountMgmtRemote");
	protected AccountMgmtRemote accountManager;

	private static final String parserJNDI = System
			.getProperty("jndi.name.parser",
					"dutrowSalesEAR/dutrowSalesEJB/ParserEJB!dutrow.sales.ejb.ParserRemote");
	protected static ParserRemote parser;

	private static final String testJNDI = System
			.getProperty("jndi.name.test",
					"dutrowSalesEAR/dutrowSalesEJB/SalesSupportEJB!dutrow.sales.ejb.SalesSupportRemote");
	protected SalesSupportRemote testSupport;

	final public void configureJndi() {

		log.debug("seller jndi name:" + sellerJNDI);
		log.debug("account jndi name:" + accountJNDI);
		log.debug("buyer jndi name:" + buyerJNDI);
		log.debug("parser jndi name: " + parserJNDI);
		log.debug("test jndi name:" + testJNDI);

		assertNotNull("jndi.name.seller not supplied", sellerJNDI);
		assertNotNull("jndi.name.buyer not supplied", buyerJNDI);
		assertNotNull("jndi.name.account not supplied", accountJNDI);
		assertNotNull("jndi.name.parser not supplied", parserJNDI);
		assertNotNull("jndi.name.test not supplied", testJNDI);

		try {
			sellerManager = (SellerMgmtRemote) jndi.lookup(sellerJNDI);
			accountManager = (AccountMgmtRemote) jndi.lookup(accountJNDI);
			buyerManager = (BuyerMgmtRemote) jndi.lookup(buyerJNDI);
			parser = (ParserRemote) jndi.lookup(parserJNDI);
			testSupport = (SalesSupportRemote) jndi.lookup(testJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("sellerManager=" + sellerManager);
		log.debug("accountManager=" + accountManager);
		log.debug("buyerManager=" + buyerManager);
		log.debug("parser=" + parser);
		log.debug("testSupport=" + testSupport);

	}

	// known (no roles)
	// admin1 esales-admin
	// admin2 ebidbot-admin
	// syssales1 esales-sys
	// sysbidbot1 esales-trusted
	// user1 esales-user
	// user2 esales-user
	// user3 esales-user,ebidbot-user

	protected static final String knownUser = System.getProperty("known.user",
			"known");
	protected static final String knownPassword = System.getProperty(
			"known.password", "password");

	protected static final String admin1User = System.getProperty(
			"admin1.user", "admin1");
	protected static final String admin1Password = System.getProperty(
			"admin1.password", "password");
	protected static final String admin2User = System.getProperty(
			"admin2.user", "admin2");
	protected static final String admin2Password = System.getProperty(
			"admin2.password", "password");

	protected static final String syssales1User = System.getProperty(
			"syssales1.user", "syssales1");
	protected static final String syssales1Password = System.getProperty(
			"syssales1.password", "password");
	protected static final String sysbidbot1User = System.getProperty(
			"sysbidbot1.user", "sysbidbot1");
	protected static final String sysbidbot1Password = System.getProperty(
			"sysbidbot1.password", "password");

	protected static final String user1User = System.getProperty("user1.user",
			"user1");
	protected static final String user1Password = System.getProperty(
			"user1.password", "password");
	protected static final String user2User = System.getProperty("user2.user",
			"user2");
	protected static final String user2Password = System.getProperty(
			"user2.password", "password");
	protected static final String user3User = System.getProperty("user3.user",
			"user3");
	protected static final String user3Password = System.getProperty(
			"user3.password", "password");

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log.debug(" **** Set up for SalesSupport **** ");

		assertNotNull("jndi.name.registrar not supplied", testJNDI);

		log.debug("getting jndi initial context");
		jndi = new InitialContext();
		log.debug("jndi=" + jndi.getEnvironment());
		jndi.lookup("/"); // do a quick comms check of JNDI

		configureJndi();

		runAs(admin1User, admin1Password);
		testSupport.resetAll();
		log.debug("reset complete");

		sellerManager.cancelTimers();

		runAs(knownUser, knownPassword);
	}


	protected Context runAs(String username, String password)
			throws NamingException {
		if (jndi != null) {
			jndi.close();
		}
		Properties env = new Properties();
		if (username != null) {
			env.put(Context.SECURITY_PRINCIPAL, username);
			env.put(Context.SECURITY_CREDENTIALS, password);
		}
		log.debug(String.format("%s env=%s", username == null ? "anonymous"
				: username, env));
		jndi = new InitialContext(env);
		return jndi;
	}

	@After
	public void tearDown() throws NamingException {
		
		if (jndi != null) {
			jndi.close();
		}

	}
}
