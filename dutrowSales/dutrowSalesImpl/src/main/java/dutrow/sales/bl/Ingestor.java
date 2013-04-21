/**
 * 
 */
package dutrow.sales.bl;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import dutrow.sales.dao.AccountDAO;
import dutrow.sales.dao.AuctionDAO;

/**
 * @author dutroda1
 * 
 */
public interface Ingestor {
	public boolean ingest() throws JAXBException, XMLStreamException;

	public void createImage(ejava.projects.esales.dto.Image object);
	public void createAuction(ejava.projects.esales.dto.Auction object) ;
	public void createAccount(ejava.projects.esales.dto.Account accountDTO) ;
}
