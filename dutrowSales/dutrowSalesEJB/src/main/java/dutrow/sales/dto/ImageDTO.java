/**
 * 
 */
package dutrow.sales.dto;

import java.io.Serializable;

/**
 * @author dutroda1
 *
 */
public class ImageDTO implements Serializable {
	byte [] bytes;

	/**
	 * @param bytes
	 */
	public ImageDTO(byte[] bytes) {
		super();
		this.bytes = bytes;
	}
	
}
