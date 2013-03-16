/**
 * 
 */
package dutrow.sales.bo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@Entity @Table(name = "DUTROW_SALES_ACCT")
public class Account implements Serializable {
	/**
	 * 
	 */
	@Id //@Column(name="USER_ID")
	String userId;
	String firstName;
	String middleName;
	String lastName;
	Date startDate;
	Date endDate;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinTable(name = "DUTROW_SALES_ACCT_ADDRESS_LINK")//, joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ADDRESS_ID"))
	Map<String, Address> addresses;

	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@JoinColumn
	POC poc;

	// JPA Requires no-arg constructor
	public Account() {
		poc = new POC();
	}

	/**
	 * @param userId
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param dansStartDate
	 * @param endDate
	 * @param addresses2
	 * @param poc
	 */
	public Account(String userId, String firstName, String middleName,
			String lastName, Date startDate, Map<String, Address> addresses2,
			POC poc) {
		this.userId = userId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.startDate = startDate;
		this.endDate = null;
		this.addresses = addresses2;
		this.poc = poc;
	}

	public void copy(Account newInfo) {
		this.userId = newInfo.userId;
		this.firstName = newInfo.firstName;
		this.middleName = newInfo.middleName;
		this.lastName = newInfo.lastName;
		this.startDate = newInfo.startDate;
		this.endDate = newInfo.endDate;
		this.addresses = newInfo.addresses;
		this.poc = newInfo.poc;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the addresses
	 */
	public Map<String, Address> getAddresses() {
		return addresses;
	}

	/**
	 * @param addresses
	 *            the addresses to set
	 */
	public void setAddresses(Map<String, Address> addresses) {
		this.addresses = addresses;
	}

	/**
	 * @return the poc
	 */
	public POC getPoc() {
		return poc;
	}

	/**
	 * @param poc
	 *            the poc to set
	 */
	public void setPoc(POC poc) {
		this.poc = poc;
	}

	@Override
	public String toString() {

		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"E, y-M-d 'at' h:m:s a z");
		StringBuilder builder = new StringBuilder();
		builder.append("id=")
				.append(this.userId)
				.append(", first=")
				.append(this.firstName)
				.append(", middle=")
				.append(this.middleName)
				.append(", last=")
				.append(this.lastName)
				.append(", start=")
				.append(dateFormatter.format(this.startDate.getTime()))
				.append(", end=")
				.append(this.endDate != null ? dateFormatter
						.format(this.endDate.getTime()) : "")
				.append(", addresses=").append(this.addresses).append(", poc=")
				.append(this.poc);
		return builder.toString();
	}
}
