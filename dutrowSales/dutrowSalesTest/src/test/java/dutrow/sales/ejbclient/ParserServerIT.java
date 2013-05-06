package dutrow.sales.ejbclient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ParserServerIT extends SalesSupport {
	private static final Log log = LogFactory.getLog(ParserServerIT.class);


    //@Test//tested in EndToEndIT
    public void testIngest() throws Exception {
        log.info(" **** testIngest **** ");
        parser.ingest();
    }
}
