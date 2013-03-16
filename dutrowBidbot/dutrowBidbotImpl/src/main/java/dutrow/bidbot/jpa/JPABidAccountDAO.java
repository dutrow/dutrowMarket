/**
 * 
 */
package dutrow.bidbot.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.dao.BidAccountDAO;

/**
 * @author dutroda1
 * 
 */
public class JPABidAccountDAO implements BidAccountDAO {
	private static Log log = LogFactory.getLog(JPABidAccountDAO.class);

	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unused")
	private JPABidAccountDAO() {
	} // force EntityManager constructor

	public JPABidAccountDAO(EntityManager emIn) {
		setEntityManager(emIn);
	}
	
	
	/* (non-Javadoc)
	 * @see dutrow.bidbot.dao.BidAccountDAO#createAccount(dutrow.bidbot.bo.BidAccount)
	 */
	@Override
	public boolean createAccount(BidAccount accountDetails) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see dutrow.bidbot.dao.BidAccountDAO#getAccountById(java.lang.String)
	 */
	@Override
	public BidAccount getAccountById(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dutrow.bidbot.dao.BidAccountDAO#updateAccount(dutrow.bidbot.bo.BidAccount)
	 */
	@Override
	public boolean updateAccount(BidAccount accountDetails) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see dutrow.bidbot.dao.BidAccountDAO#removeAccount(java.lang.String)
	 */
	@Override
	public boolean removeAccount(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see dutrow.bidbot.dao.BidAccountDAO#getAccounts()
	 */
	@Override
	public Collection<BidAccount> getAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

}