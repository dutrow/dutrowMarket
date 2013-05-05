package dutrow.sales.ejb;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bl.Ingestor;
import ejava.projects.esales.dto.Account;
import ejava.projects.esales.dto.Address;
import ejava.projects.esales.dto.Auction;
import ejava.projects.esales.dto.Bid;
import ejava.projects.esales.dto.ESales;
import ejava.projects.esales.dto.Image;
import ejava.projects.esales.xml.ESalesParser;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"esales-admin", "ebidbot-admin"})
//esales-admin		these users will be able to perform management and test functions on eSales.
//ebidbot-admin	these users will be able to perform management and test functions on eBidbot.
public class ParserEJB implements ParserRemote {
	@Resource(name = "vals/xmlFile")
	private static String xmlFile;

	private static final Log log = LogFactory.getLog(ParserEJB.class);

	@Inject
	Ingestor ingestor;
	
	@PostConstruct
	public void init() {
		log.debug("*** ParserEJB ***");
		log.debug("xmlFile=" + xmlFile);
	}

	public void ingest() throws IOException{
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
						"Bid", "Image");
				if (object instanceof Address) {
					log.debug("found address");
					Address in = (Address) object;
					// ignore: this will be parsed with the account
					
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
					// ignore: this will be parsed with the auction
					
				} else if (object instanceof Image) { 
					log.debug("found Image");
					Image in = (Image) object;
					ingestor.createImage(in);
					
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
