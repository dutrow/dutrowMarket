/**
 * 
 */
package dutrow.sales.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author dutroda1
 *
 */
@Entity
@Table(name = "DUTROW_SALES_IMAGE")
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@Lob
	@Column(nullable = false)
	byte [] image;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}
}
