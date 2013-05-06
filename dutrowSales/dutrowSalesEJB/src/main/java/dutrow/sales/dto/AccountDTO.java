/**
 * 
 */
package dutrow.sales.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author dutroda1
 * 
 */
public class AccountDTO implements Serializable{

	private static final long serialVersionUID = -5776569920367248797L;
	public String userId;
	public String firstName;
	public String middleName;
	public String lastName;
	public String email;

	public Collection<AddressDTO> addresses;

	/**
	 * @param userId
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param email
	 */
	public AccountDTO(String userId, String firstName, String middleName,
			String lastName, String email) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.addresses = new ArrayList<AddressDTO>();

	}

	@Override
	public boolean equals(Object o) {		
		if (o instanceof AccountDTO){
			AccountDTO a = (AccountDTO) o;
			return (this.userId == a.userId 
					&& this.email == a.email);
		}
		return false;
	}
	
	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("id=")
				.append(this.userId)
				.append(", first=")
				.append(this.firstName)
				.append(", middle=")
				.append(this.middleName)
				.append(", last=")
				.append(this.lastName)
				.append(", addresses=").append(this.addresses);
		return builder.toString();
	}
	
	

}
