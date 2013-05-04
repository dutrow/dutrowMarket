/**
 * 
 */
package dutrow.bidbot.ejbclient;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import dutrow.bidbot.ejb.BidbotUtilRemote;

/**
 * @author dutroda1
 * 
 */
public class Support {
	private static final Log log = LogFactory.getLog(Support.class);
	protected InitialContext jndi;

	protected static final String utilJNDI = System.getProperty(
			"jndi.name.registrar",
			"dutrowBidbot/BidbotUtilEJB!dutrow.bidbot.ejb.BidbotUtilRemote");
			//"dutrowBidbotWAR/BidbotUtilEJB!dutrow.bidbot.ejb.BidbotUtilRemote");

	protected BidbotUtilRemote testSupport;

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

	protected static final String admin2User = System.getProperty(
			"admin2.user", "admin2");
	protected static final String admin2Password = System.getProperty(
			"admin2.password", "password");

//	protected static final String syssales1User = System.getProperty(
//			"syssales1.user", "syssales1");
//	protected static final String syssales1Password = System.getProperty(
//			"syssales1.password", "password");
//	protected static final String sysbidbot1User = System.getProperty(
//			"sysbidbot1.user", "sysbidbot1");
//	protected static final String sysbidbot1Password = System.getProperty(
//			"sysbidbot1.password", "password");

//	protected static final String user1User = System.getProperty("user1.user",
//			"user1");
//	protected static final String user1Password = System.getProperty(
//			"user1.password", "password");
//	protected static final String user2User = System.getProperty("user2.user",
//			"user2");
//	protected static final String user2Password = System.getProperty(
//			"user2.password", "password");
	protected static final String user3User = System.getProperty("user3.user",
			"user3");
	protected static final String user3Password = System.getProperty(
			"user3.password", "password");

	/**
	 * @throws NamingException 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws NamingException {
		log.debug("Set up for SupportIT");

		assertNotNull("jndi.name.registrar not supplied", utilJNDI);

		log.debug("getting jndi initial context");
		jndi = new InitialContext();
		log.debug("jndi=" + jndi.getEnvironment());
		jndi.lookup("/"); // do a quick comms check of JNDI

		log.debug("jndi name:" + utilJNDI);
		try {
			testSupport = (BidbotUtilRemote) jndi.lookup(utilJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("testSupport=" + testSupport);

		runAs(admin2User, admin2Password);
		testSupport.reset();
		log.debug("reset complete");
		runAs(knownUser, knownPassword);
	}

	@After
	public void tearDown() throws NamingException {
		if (jndi != null) {
			jndi.close();
		}

	}
	
	protected Context runAs(String username, String password) throws NamingException {
        if (jndi!=null) {
        	jndi.close();
        }
        Properties env = new Properties();
        if (username != null) {
            env.put(Context.SECURITY_PRINCIPAL, username);
            env.put(Context.SECURITY_CREDENTIALS, password);
        }
        log.debug(String.format("%s env=%s", username==null?"anonymous":username, env));
        jndi=new InitialContext(env);
        return jndi;
    }
}
