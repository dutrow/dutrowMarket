/**
 * 
 */
package dutrow.sales.bl.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.sales.bo.Account;
import dutrow.sales.bo.Address;
import dutrow.sales.bo.AuctionItem;
import dutrow.sales.bo.Category;
import dutrow.sales.bo.Image;
import dutrow.sales.bo.POC;
import dutrow.sales.dao.AccountDAO;
import dutrow.sales.dao.AuctionDAO;
import dutrow.sales.dao.jpa.JPAAccountDAO;
import dutrow.sales.dao.jpa.JPAAuctionDAO;
import ejava.projects.esales.dto.ESales;
import ejava.projects.esales.xml.ESalesParser;

/**
 * @author dutroda1
 * 
 */
public class Ingestor {
	private static Log log = LogFactory.getLog(Ingestor.class);

	private AccountDAO accountDAO;
	private AuctionDAO auctionDAO;
	private InputStream inputStream;
	private ESalesParser parser;

	/**
	 * @param is
	 * @param accountDao2
	 */
	public Ingestor(InputStream is, JPAAccountDAO accountDaoIn,
			JPAAuctionDAO auctionDaoIn) throws IllegalArgumentException {
		this.inputStream = is;
		this.accountDAO = accountDaoIn;
		this.auctionDAO = auctionDaoIn;
		try {
			parser = new ESalesParser(ESales.class, inputStream);
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getMessage());
		} catch (XMLStreamException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * This method will ingest the input data by reading in external DTOs in
	 * from the parser, instantiating project business objects, and inserting
	 * into database. Note that the XML Schema is organized such that object
	 * references are fully resolved. Therefore, there is no specific need to
	 * process the addresses as they come in. They can be stored once we get the
	 * accounts they are related to.
	 * 
	 * @throws JAXBException
	 * @throws XMLStreamException
	 */
	public boolean ingest() throws JAXBException, XMLStreamException {

		log.info("Ingesting");

		Object object = parser.getObject("address", "account", "auction",
				"image");
		while (object != null) {
			if (object instanceof ejava.projects.esales.dto.Account) {
				createAccount((ejava.projects.esales.dto.Account) object);
			} else if (object instanceof ejava.projects.esales.dto.Auction) {
				createAuction((ejava.projects.esales.dto.Auction) object);
			} else if (object instanceof ejava.projects.esales.dto.Image) {
				createImage((ejava.projects.esales.dto.Image) object);
			}

			object = parser.getObject("address", "account", "auction", "image");
		}

		return true;
	}

	/**
	 * @param object
	 */
	private void createImage(ejava.projects.esales.dto.Image object) {

		ejava.projects.esales.dto.Auction auctionObject = (ejava.projects.esales.dto.Auction) object
				.getAuctionRef();
		AuctionItem ai = auctionDAO.getAuctionById(auctionObject.getId());

		Image i = new Image();
		i.setImage(object.getImage());
		if (ai != null) {
			ai.getImages().add(i);

			auctionDAO.updateAuction(ai);
			log.info("updated auction: " + ai);
		}
		else log.warn("AuctionItem does not exist with id: " + auctionObject.getId());
	}

	/**
	 * @param object
	 */
	private void createAuction(ejava.projects.esales.dto.Auction object) {
		AuctionItem ai = new AuctionItem();
		ai.setId(object.getId());
		ai.setTitle(object.getTitle());
		ai.setCategory(Category.getCategory(object.getCategory()));
		ai.setDescription(object.getDescription());
		ai.setStartTime(object.getStartTime());
		ai.setEndTime(object.getEndTime());
		ai.setAskingPrice(object.getAskingPrice());

		ejava.projects.esales.dto.Account s = (ejava.projects.esales.dto.Account) object
				.getSeller();
		Account a = accountDAO.getAccountByUser(s.getLogin());
		ai.setSeller(a.getPoc());

		auctionDAO.createAuction(ai);
		log.info("created auction item: " + ai);
	}

	/**
	 * This method is called by the main ingest processing loop. The JAXB/StAX
	 * parser will already have the Account populated with Address information.
	 * 
	 * @param accountDTO
	 */
	private void createAccount(ejava.projects.esales.dto.Account accountDTO) {

		Map<String, Address> addresses = new HashMap<String, Address>();
		POC poc = new POC();

		poc.setUserId(accountDTO.getLogin());
		poc.setEmail(accountDTO.getEmail());

		Account accountBO = new Account(accountDTO.getLogin(),
				accountDTO.getFirstName(), accountDTO.getMiddleName(),
				accountDTO.getLastName(), accountDTO.getStartDate(), addresses,
				poc);

		accountBO.setFirstName(accountDTO.getFirstName());
		for (Object o : accountDTO.getAddress()) {
			ejava.projects.esales.dto.Address addressDTO = (ejava.projects.esales.dto.Address) o;

			Address addressBO = new Address(addressDTO.getName(),
					addressDTO.getAddressee(), addressDTO.getStreet(),
					addressDTO.getCity(), addressDTO.getState(),
					addressDTO.getZip());

			accountBO.getAddresses().put(addressDTO.getName(), addressBO);

		}
		accountDAO.createAccount(accountBO);
		log.debug("created account:" + accountBO);
	}
}
