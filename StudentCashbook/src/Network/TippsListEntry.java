package Network;

public class TippsListEntry {

		Integer ID;
		String title;
		String description;
		
		public TippsListEntry(Integer ID, String title, String description) {
			this.ID = ID;
			this.title = title;
			this.description = description;
		}
		
		
		public int getID(){
			return ID;
		}
		
		public String getTitle(){
			return title;
		}
		
		public String getDescription(){
			return description;
		}

	}
