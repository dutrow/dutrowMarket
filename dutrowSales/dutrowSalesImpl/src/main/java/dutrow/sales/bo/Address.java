/**
 * 
 */
package dutrow.sales.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author dutroda1
 *
 */
@Entity
@Table(name = "DUTROW_SALES_ADDRESS")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@Column(nullable = false)
	String name;
	String to;
	@Column(nullable = false)
	String street;
	@Column(nullable = false)
	String city;
	@Column(nullable = false)
	String state;
	@Column(nullable = false)
	String zip;
	
	// JPA Requires no-arg constructor
	protected Address() {
	}
	
	/**
	 * @param name
	 * @param to
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Address(String name, String to, String street, String city,
			String state, String zip) {
		super();		
		this.name = name;
		this.to = to;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip
	 *            the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	
}
