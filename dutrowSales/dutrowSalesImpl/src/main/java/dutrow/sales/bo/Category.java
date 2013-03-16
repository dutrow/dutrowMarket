/**
 * 
 */
package dutrow.sales.bo;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Transient;

/**
 * @author dutroda1
 * 
 */
public enum Category {
	ALCOHOL_TOBACCO("Alcohol & Tobacco"), AUDIO_VIDEO("Audio & Video"), FRESH_FOODS(
			"Fresh Foods"), HEALTH_BEAUTY("Health & Beauty"), HOME_KITCHEN(
			"Home & Kitchen"), OFFICE_PRODUCTS("Office Products"), PC_COMPUTERS(
			"PC Computers"), SCIENCE_TOYS("Science & Toys");

	/*
	 * Alcohol &amp; Tobacco Audio &amp; Video Fresh Foods Health &amp; Beauty
	 * Home &amp; Kitchen Office Products PC Computers Science &amp; Toys
	 */

	public final String prettyName;

	private Category(String prettyName) {
		this.prettyName = prettyName;
	}

	public static Category getCategory(String prettyName) {
		for (Category category : values()) {
			if (category.prettyName.equals(prettyName)) {
				return category;
			}
		}
		return null;
	}

	@Transient
	private Category category;

	@Access(AccessType.PROPERTY)
	// @Column(name="CATEGORY", length=32)
	protected String getDBCategory() {
		return category == null ? null : category.prettyName;
	}

	protected void setDBCategory(String dbValue) {
		category = Category.getCategory(dbValue);

	}
}
