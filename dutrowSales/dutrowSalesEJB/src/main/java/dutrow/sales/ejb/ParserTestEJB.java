package dutrow.sales.ejb;

import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.Ingestor;
import dutrow.sales.dao.AccountDAO;
import dutrow.sales.dao.AuctionDAO;
import ejava.projects.esales.dto.Account;
import ejava.projects.esales.dto.Address;
import ejava.projects.esales.dto.Auction;
import ejava.projects.esales.dto.Bid;
import ejava.projects.esales.dto.ESales;
import ejava.projects.esales.xml.ESalesParser;

@Stateless
public class ParserTestEJB implements ParserTestRemote {
	@Resource(name = "vals/xmlFile")
	private static String xmlFile;

	private static final Log log = LogFactory.getLog(ParserTestEJB.class);

	@Inject
	Ingestor ingestor;
	
	@PostConstruct
	public void init() {
		log.debug("*** ParserTestEJB ***");
		log.debug("xmlFile=" + xmlFile);
	}

	public void ingest() throws Exception {
		log.info("ingest");

		InputStream is = null;

		try {
			log.info("getting input file:" + xmlFile);
			is = this.getClass().getResourceAsStream(xmlFile);
			if (is == null) {
				log.warn(xmlFile + " was not found");
				throw new Exception(xmlFile + " was not found");
			}

			log.trace("creating parser");
			ESalesParser parser = new ESalesParser(ESales.class, is);

			log.trace("starting parse loop");
			Object object = null;
			do {
				object = parser.getObject("Address", "Account", "Auction",
						"Bid");
				if (object instanceof Address) {
					log.debug("found address");
					Address in = (Address) object;
					// ignore
					
				} else if (object instanceof Account) {
					log.debug("found Account");
					Account in = (Account) object;
					ingestor.createAccount(in);
				} else if (object instanceof Auction) {
					log.debug("found Auction");
					Auction in = (Auction) object;
					ingestor.createAuction(in);
				} else if (object instanceof Bid) {
					log.debug("found Bid");
					Bid in = (Bid) object;
					// ignore
					
				} else if (object != null) {
					log.warn("object of unknown type:" + object);
				}
			} while (object != null);
		} catch (Throwable ex) {
			log.error("error parsing doc", ex);
			throw new EJBException("error parsing doc:" + ex);
		} finally {
			if (is != null)
				is.close();
		}
	}

}
