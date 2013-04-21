package dutrow.sales.ejbclient;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dutrow.sales.ejb.ParserTestRemote;


import ejava.util.ejb.EJBClient;

public class ParserServerIT {
	private static final Log log = LogFactory.getLog(ParserServerIT.class);

    public static final String jndiName = System.getProperty("jndi.name",
    		EJBClient.getRemoteLookupName("dutrowSalesEAR", "dutrowSalesEJB", 
    				"ParserTestEJB", ParserTestRemote.class.getName()));
    
    private static ParserTestRemote parser;
    private InitialContext jndi;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.debug("looking up name:" + jndiName);
    }

    @Before
    public void setUp() throws Exception {
        jndi = new InitialContext();
        parser = (ParserTestRemote)jndi.lookup(jndiName);
    }
    
    @After
    public void tearDown() throws Exception {
    	if (jndi != null) {
    		jndi.close();
    	}
    }

    @Test
    public void testIngest() throws Exception {
        log.info("*** testIngest ***");
        parser.ingest();
    }
}
