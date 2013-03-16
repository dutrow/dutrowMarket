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
}
