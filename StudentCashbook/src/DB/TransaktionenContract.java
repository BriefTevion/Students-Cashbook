package DB;

import android.provider.BaseColumns;

public final class TransaktionenContract {

	//leerer Konstruktor
	public TransaktionenContract(){}
	
	//TabellenInhalt definieren
	public static abstract class TabInhalt implements BaseColumns{
		public static final String TABLE_NAME = "transaktionenList";
		public static final String COLUMN_NAME_TRANSAKTION_ID = "transaktionenID";
		public static final String COLUMN_NAME_KATEGORIE = "kategorie";
		public static final String COLUMN_NAME_DATUM = "datum";
		public static final String COLUMN_NAME_UHRZEIT = "uhrzeit";
		public static final String COLUMN_NAME_ANMEKRUNG = "anmerkung";
		public static final String COLUMN_NAME_BETRAG = "betrag";
		
	}
}
