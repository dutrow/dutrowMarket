/**
 * 
 */
package dutrow.bidbot.bo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author dutroda1
 * 
 */
@SuppressWarnings("serial")
@Entity @Table(name = "DUTROW_BIDBOT_ACCT")
public class BidAccount implements Serializable {
	/**
	 * 
	 */
	@Id //@Column(name="USER_ID")
	String userId;
	String salesAccount;
	String salesPassword;
	
	@OneToMany
	List<BidOrder> orders;
	
	@Override
	public String toString() {

		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"E, y-M-d 'at' h:m:s a z");
		StringBuilder builder = new StringBuilder();
		builder.append("id=")
				.append(this.userId)
				.append(", salesAccount=")
				.append(this.salesAccount)
				.append(", salesPassword=")
				.append(this.salesPassword)
				;
		return builder.toString();
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
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
	 * @param salesAccount the salesAccount to set
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
	 * @param salesPassword the salesPassword to set
	 */
	public void setSalesPassword(String salesPassword) {
		this.salesPassword = salesPassword;
	}

	/**
	 * @return the orders
	 */
	public List<BidOrder> getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(List<BidOrder> orders) {
		this.orders = orders;
	}
}
