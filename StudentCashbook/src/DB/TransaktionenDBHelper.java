package DB;

import DB.TransaktionenContract.TabInhalt;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransaktionenDBHelper extends SQLiteOpenHelper {

	//SQL Statement um Tabelle TransaktionenList zu erstellen
	private static final String createTransaktionenList = "CREATE TABLE" + TabInhalt.TABLE_NAME + " (" +
			TabInhalt.COLUMN_NAME_TRANSAKTION_ID + " INTEGER PRIMARY KEY," + 
			TabInhalt.COLUMN_NAME_ANMEKRUNG + " TEXT," + 
			TabInhalt.COLUMN_NAME_DATUM + " TEXT," +
			TabInhalt.COLUMN_NAME_UHRZEIT + " TEXT," +
			TabInhalt.COLUMN_NAME_KATEGORIE + " TEXT," +
			TabInhalt.COLUMN_NAME_BETRAG + " DOUBLE)";
	
	
	public TransaktionenDBHelper(Context context) {
		super(context, "transaktionenList", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(createTransaktionenList);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	//neue Transaktion der Tabelle TransaktionenList hinzufuegen
	public void addTransaktion(Integer id, String anmerkung, String datum, String zeit, String kategorie, Double betrag){
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(TabInhalt.COLUMN_NAME_TRANSAKTION_ID, id);
		cv.put(TabInhalt.COLUMN_NAME_ANMEKRUNG, anmerkung);
		cv.put(TabInhalt.COLUMN_NAME_DATUM, datum);
		cv.put(TabInhalt.COLUMN_NAME_UHRZEIT, zeit);
		cv.put(TabInhalt.COLUMN_NAME_KATEGORIE, kategorie);
		cv.put(TabInhalt.COLUMN_NAME_BETRAG, betrag);
		
		long newRowID = db.insert("transaktionenList", null, cv);
	}
	
	//Abfrage der letzten drei Transaktionen
	public String getLastThreeTransaktions(){
		SQLiteDatabase db = getReadableDatabase();
		String result = "";
		
		String [] projection = {
				TabInhalt.COLUMN_NAME_DATUM,
				TabInhalt.COLUMN_NAME_BETRAG
		};
		
		String sortOrder = TabInhalt.COLUMN_NAME_DATUM + " DESC";
		
		try{
		Cursor c = db.query(TabInhalt.TABLE_NAME, projection, null, null, null, null, sortOrder, "3");
		
		if (c.getCount() == 0){
			
			return "No data";
		}
		else{
		//solange cursor liest strings zu einem brauchbaren string zusammenfassen
		while(c.moveToNext()){
			String datum = c.getString(0);
			Double betrag = c.getDouble(1);
			
			result = result + datum + " " + betrag + "\n";
			
		}
		
		return result;
		}
		}
		catch(Exception e){
			e.printStackTrace();
			
			return "Error";
		}
	}

}
