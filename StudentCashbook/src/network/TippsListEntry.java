/*
 * Diese Klasse stellt den Aufbau eines Tipss in der Tippslist dar.
 */
package network;

public class TippsListEntry {

	Integer ID;
	String title;
	String description;
	String url;

	public TippsListEntry(Integer ID, String title, String description,
			String url) {
		this.ID = ID;
		this.title = title;
		this.description = description;
		this.url = url;
	}

	public int getID() {
		return ID;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getURL() {
		return url;
	}

}
