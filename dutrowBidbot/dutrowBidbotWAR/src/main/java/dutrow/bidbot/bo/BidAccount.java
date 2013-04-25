/**
 * 
 */
package dutrow.bidbot.bo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;



/**
 * @author dutroda1
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "DUTROW_BIDBOT_ACCT")
public class BidAccount implements Serializable {
	/**
	 * 
	 */
	@Id
	// @Column(name="USER_ID")
	String userId;
	String salesAccount;
	String salesPassword;

	@OneToMany(mappedBy = "bidder", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	List<BidOrder> orders;

	
	protected BidAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param userId
	 * @param salesAccount
	 * @param salesPassword
	 */
	public BidAccount(String userId, String salesAccount,
			String salesPassword) {
		super();
		this.userId = userId;
		this.salesAccount = salesAccount;
		this.salesPassword = salesPassword;
		this.orders = new ArrayList<BidOrder>();
	}

	@Override
	public String toString() {

		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"E, y-M-d 'at' h:m:s a z");
		StringBuilder builder = new StringBuilder();
		builder.append("id=").append(this.userId).append(", salesAccount=")
				.append(this.salesAccount).append(", salesPassword=")
				.append(this.salesPassword);
		return builder.toString();
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the salesAccount
	 */
	public String getSalesAccount() {
		return salesAccount;
	}

	/**
	 * @param salesAccount
	 *            the salesAccount to set
	 */
	public void setSalesAccount(String salesAccount) {
		this.salesAccount = salesAccount;
	}

	/**
	 * @return the salesPassword
	 */
	public String getSalesPassword() {
		return salesPassword;
	}

	/**
	 * @param salesPassword
	 *            the salesPassword to set
	 */
	public void setSalesPassword(String salesPassword) {
		this.salesPassword = salesPassword;
	}

	/**
	 * @param bidOrder
	 */
	public boolean addOrder(BidOrder bidOrder) {
		return orders.add(bidOrder);
	}

	/**
	 * @return the orders
	 */
	public List<BidOrder> getOrders() {
		return orders;
	}

	/**
	 * @param orders
	 *            the orders to set
	 */
	public void setOrders(List<BidOrder> orders) {
		this.orders = orders;
	}
}
