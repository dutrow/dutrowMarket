/**
 * 
 */
package dutrow.sales.dto;

import java.io.Serializable;

/**
 * @author dutroda1
 *
 */
public class AddressDTO implements Serializable {
	
	public String name;
	public String to;
	public String street;
	public String city;
	public String state;
	public String zip;
	/**
	 * @param name
	 * @param to
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public AddressDTO(String name, String to, String street, String city,
			String state, String zip) {
		super();
		this.name = name;
		this.to = to;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	
}
