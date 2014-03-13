package DB;

import android.provider.BaseColumns;

public final class TransaktionenContract {

	//leerer Konstruktor
	public TransaktionenContract(){}
	
	//TabellenInhalt definieren
	public static abstract class transEntry implements BaseColumns{
		public static final String TABLE_NAME = "transaktionenTable";
		public static final String COLUMN_NAME_TRANSAKTION_ID = "transaktionenTableID";
		public static final String COLUMN_NAME_KATEGORIE = "kategorie";
		public static final String COLUMN_NAME_DATUM = "datum";
		public static final String COLUMN_NAME_UHRZEIT = "uhrzeit";
		public static final String COLUMN_NAME_ANMEKRUNG = "anmerkung";
		public static final String COLUMN_NAME_BETRAG = "betrag";
		
		public static final String TABLE_NAME_Kategorie = "kategorienTable";
		public static final String K_COLUMN_NAME_BEZEICHNER = "name";
		public static final String K_COLUMN_NAME_BUDGET = "budget";
		public static final String K_COLUMN_NAME_RESTBETRAG = "restbetrag";
		public static final String K_COLUMN_NAME_LAST_UPDATED = "lastUpdated";
		
	}
}
