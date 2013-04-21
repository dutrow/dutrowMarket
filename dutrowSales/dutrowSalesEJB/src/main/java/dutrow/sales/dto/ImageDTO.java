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
	/**
	 * 
	 */
	private static final long serialVersionUID = 6585226487346965706L;
	byte [] bytes;

	/**
	 * @param bytes
	 */
	public ImageDTO(byte[] bytes) {
		super();
		this.bytes = bytes;
	}
	
}
