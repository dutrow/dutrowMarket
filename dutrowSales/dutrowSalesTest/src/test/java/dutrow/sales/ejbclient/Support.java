/**
 * 
 */
package dutrow.sales.ejbclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.dto.AccountDTO;
import dutrow.sales.ejb.TestSupportRemote;

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
					"dutrowSalesEAR/dutrowSalesEJB/TestSupportEJB!dutrow.sales.ejb.TestSupportRemote");
	protected TestSupportRemote testSupport;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log.debug("Set up for SupportIT");

		assertNotNull("jndi.name.registrar not supplied", testJNDI);

		log.debug("getting jndi initial context");
		jndi = new InitialContext();
		log.debug("jndi=" + jndi.getEnvironment());
		jndi.lookup("/"); // do a quick comms check of JNDI

		log.debug("jndi name:" + testJNDI);
		try {
			testSupport = (TestSupportRemote) jndi.lookup(testJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("testSupport=" + testSupport);

		testSupport.resetAll();
		log.debug("reset complete");
	}
}
