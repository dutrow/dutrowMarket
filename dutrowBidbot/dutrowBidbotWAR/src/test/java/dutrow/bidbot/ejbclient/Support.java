/**
 * 
 */
package dutrow.bidbot.ejbclient;

import static org.junit.Assert.assertNotNull;

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

	protected static final String utilJNDI = System
			.getProperty(
					"jndi.name.registrar",
					"dutrowBidbotWAR/BidbotUtilEJB!dutrow.bidbot.ejb.BidbotUtilRemote");
	
	protected BidbotUtilRemote testUtil;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log.debug("Set up for SupportIT");

		assertNotNull("jndi.name.registrar not supplied", utilJNDI);

		log.debug("getting jndi initial context");
		jndi = new InitialContext();
		log.debug("jndi=" + jndi.getEnvironment());
		jndi.lookup("/"); // do a quick comms check of JNDI

		log.debug("jndi name:" + utilJNDI);
		try {
			testUtil = (BidbotUtilRemote) jndi.lookup(utilJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("testSupport=" + testUtil);

		testUtil.reset();
		log.debug("reset complete");
	}

	@After
	public void tearDown() throws NamingException{
		if (jndi != null) {
			jndi.close();
		}
		
	}
}
