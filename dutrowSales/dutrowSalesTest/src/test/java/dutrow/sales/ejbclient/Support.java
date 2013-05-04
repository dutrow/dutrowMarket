/**
 * 
 */
package dutrow.sales.ejbclient;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import dutrow.sales.ejb.SupportRemote;

/**
 * @author dutroda1
 * 
 */
public class Support {
	private static final Log log = LogFactory.getLog(Support.class);
	protected InitialContext jndi;

	private static final String testJNDI = System
			.getProperty(
					"jndi.name.registrar",
					"dutrowSalesEAR/dutrowSalesEJB/SupportEJB!dutrow.sales.ejb.SupportRemote");
	protected SupportRemote testSupport;
	
	
//	known	(no roles)
//	admin1	esales-admin
//	admin2	ebidbot-admin
//	syssales1	esales-sys
//	sysbidbot1	esales-trusted
//	user1	esales-user
//	user2	esales-user
//	user3	esales-user,ebidbot-user
	
	protected static final String knownUser = System.getProperty("known.user", "known");
	protected static final String knownPassword = System.getProperty("known.password", "password");
	
	protected static final String admin1User = System.getProperty("admin1.user", "admin1");
	protected static final String admin1Password = System.getProperty("admin1.password", "password");
	protected static final String admin2User = System.getProperty("admin2.user", "admin2");
	protected static final String admin2Password = System.getProperty("admin2.password", "password");
	
	protected static final String syssales1User = System.getProperty("syssales1.user", "syssales1");
	protected static final String syssales1Password = System.getProperty("syssales1.password", "password");
	protected static final String sysbidbot1User = System.getProperty("sysbidbot1.user", "sysbidbot1");
	protected static final String sysbidbot1Password = System.getProperty("sysbidbot1.password", "password");
	
	protected static final String user1User = System.getProperty("user1.user","user1");
	protected static final String user1Password = System.getProperty("user1.password", "password");
	protected static final String user2User = System.getProperty("user2.user","user2");
	protected static final String user2Password = System.getProperty("user2.password", "password");
	protected static final String user3User = System.getProperty("user3.user","user3");
	protected static final String user3Password = System.getProperty("user3.password", "password");
	
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log.debug(" **** Set up for Support **** ");

		assertNotNull("jndi.name.registrar not supplied", testJNDI);

		log.debug("getting jndi initial context");
		jndi = new InitialContext();
		log.debug("jndi=" + jndi.getEnvironment());
		jndi.lookup("/"); // do a quick comms check of JNDI

		log.debug("jndi name:" + testJNDI);
		try {
			testSupport = (SupportRemote) jndi.lookup(testJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("testSupport=" + testSupport);

		runAs(admin1User, admin1Password);
		testSupport.resetAll();
		log.debug("reset complete");
		
		runAs(knownUser, knownPassword);
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

	@After
	public void tearDown() throws NamingException{
		if (jndi != null) {
			jndi.close();
		}
		
	}
}
