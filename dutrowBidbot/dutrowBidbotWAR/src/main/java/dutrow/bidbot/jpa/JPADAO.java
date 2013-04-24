/**
 * 
 */
package dutrow.bidbot.jpa;

import javax.persistence.EntityManager;

/**
 * @author dutroda1
 *
 */
public class JPADAO {
	protected EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unused")
	private JPADAO() {
	} // force EntityManager constructor

	public JPADAO(EntityManager emIn) {
		setEntityManager(emIn);
	}
}
