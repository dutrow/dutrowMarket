package dutrow.sales.ejb;

import dutrow.sales.dto.AddressDTO;
import ejava.projects.esales.dto.Account;

import ejava.projects.esales.dto.Address;
import ejava.projects.esales.dto.Auction;
import ejava.projects.esales.dto.Bid;
import ejava.projects.esales.dto.ESales;

import java.io.InputStream;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ejava.projects.esales.xml.ESalesParser;

@Stateless
public class ParserTestEJB implements ParserTestRemote {
	@Resource(name="vals/xmlFile")
	private static String xmlFile;
	
	private static final Log log = LogFactory.getLog(ParserTestEJB.class);
	
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
			Object object=null;
			do {
		        object = parser.getObject(
		        		"Address", "Account", "Auction", "Bid");
		        if (object instanceof Address) {
		        	Address in = (Address) object;
		        	
		            log.debug("found address");
 
		        }
		        else if (object instanceof Account) {
		        	Account in = (Account) object;
		        	
		            log.debug("found Account");
		        }
		        else if (object instanceof Auction) {
		        	Auction in = (Auction) object;
		        	log.debug("found Auction");
		        }
		        else if (object instanceof Bid) {
		        	Bid in = (Bid) object;
		        	
		        	log.debug("found Bid");
		        }
		        else if (object != null) {
		        	log.warn("object of unknown type:" + object);
		        }
			} while (object != null);
		}
		catch (Throwable ex) {
			log.error("error parsing doc",ex);
			throw new EJBException("error parsing doc:" + ex);
		}
		finally {
			if (is != null) is.close();
		}
	}

}
