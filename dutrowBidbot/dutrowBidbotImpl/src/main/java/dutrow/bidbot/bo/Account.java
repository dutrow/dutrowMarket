/**
 * 
 */
package dutrow.bidbot.bo;

import java.util.Calendar;
import java.util.Map;

/**
 * @author dutroda1
 *
 */
public class Account {
	String userId;
	String firstName;
	String middleName;
	String lastName;
	Calendar startDate;
	Calendar endDate;
	Map<String,Address> addresses; 
	POC poc;
	/**
	 * @param userId
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param dansStartDate
	 * @param endDate
	 * @param addresses
	 * @param poc
	 */
	public Account(String userId, String firstName, String middleName,
			String lastName, Calendar dansStartDate, 
			Map<String, Address> addresses, POC poc) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.startDate = dansStartDate;
		this.endDate = null;
		this.addresses = addresses;
		this.poc = poc;
	}
	
	public void copy(Account newInfo){
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
	 * @param userId the userId to set
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
	 * @param firstName the firstName to set
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
	 * @param middleName the middleName to set
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
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the addresses
	 */
	public Map<String, Address> getAddresses() {
		return addresses;
	}
	/**
	 * @param addresses the addresses to set
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
	 * @param poc the poc to set
	 */
	public void setPoc(POC poc) {
		this.poc = poc;
	}
}
