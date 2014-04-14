/*
 * Diese Klasse beinhaltet alle Methoden zur Bearbeitung der Datenbank-Tabellen.
 */
package db;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import db.TransaktionenContract.transEntry;

public class BudgetLoader {

	//neue einmalige Einnahme verbuchen
	//neue Transaktion der Tabelle TransaktionenList hinzufuegen
	public static void addEinmaligeTransaktion(Context context, Integer id, String anmerkung, String datum, String zeit, String kategorie, String betrag){
		//Zugang zur Datenbank
		try{
			TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);	
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			ContentValues cv = new ContentValues();
			cv.put(transEntry.COLUMN_NAME_TRANSAKTION_ID, id);
			cv.put(transEntry.COLUMN_NAME_ANMEKRUNG, anmerkung);
			cv.put(transEntry.COLUMN_NAME_DATUM, datum);
			cv.put(transEntry.COLUMN_NAME_UHRZEIT, zeit);
			cv.put(transEntry.COLUMN_NAME_KATEGORIE, kategorie);
			cv.put(transEntry.COLUMN_NAME_BETRAG, betrag);
			
			long newRowID;
			newRowID = db.insert(transEntry.TABLE_NAME, null, cv);
			
			db.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//Einmalige Einnahme der ausgesuchten Kategorie gutschreiben
	public static void addEinmaligeTransaktionToKategorie(Context context, String datum, String kName,
			String betrag) {
		
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
	
		//GET aktuellen restbetrag der Kategorie
		String [] projection = {
				transEntry.K_COLUMN_NAME_BEZEICHNER,
				transEntry.K_COLUMN_NAME_BUDGET,
				transEntry.K_COLUMN_NAME_RESTBETRAG
		};

		String sortOrder = transEntry.K_COLUMN_NAME_BEZEICHNER;

		try{ 
			
			Cursor c = db.query(transEntry.TABLE_NAME_Kategorie, projection, transEntry.K_COLUMN_NAME_BEZEICHNER 
					+ "= '" + kName +"'", null, null, null, sortOrder);
			
			c.moveToFirst();
			String aktuellerRest = c.getString(2);
			Integer neuerBetrag = Integer.parseInt(aktuellerRest) + Integer.parseInt(betrag);
			
			ContentValues cv = new ContentValues();
		      cv.put(transEntry.K_COLUMN_NAME_RESTBETRAG, String.valueOf(neuerBetrag));
		      cv.put(transEntry.K_COLUMN_NAME_LAST_UPDATED, datum);
		      
			db.update(transEntry.TABLE_NAME_Kategorie, cv, transEntry.K_COLUMN_NAME_BEZEICHNER 
					+ "= '" + kName +"'", null);	
		
		
		}
		catch(Exception e){
			e.printStackTrace();
			//Nachricht ueber NICHT erfolgreiches speichern
			AlertDialog.Builder alert = new AlertDialog.Builder(context);
			alert.setMessage("Fehler");
			alert.setTitle("Einnahme konnte nicht gespeichert werde.");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
		}
		db.close();
		
	}
	
	//Neue transaktionID ausgeben
	public static Integer getNewTransaktionID(Context context){
		try{
			//Zugang zur Datenbank
			TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);	
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			Integer id = 0;
			
			final String SQL_getMaxID = "Select Max(" + transEntry.COLUMN_NAME_TRANSAKTION_ID + ") AS id FROM " + 
										transEntry.TABLE_NAME;	
			
			Cursor c = db.rawQuery(SQL_getMaxID, null);
					
				c.moveToFirst();
				id = c.getInt(0);
			
				db.close();
				
				return id+1;		
			
		}
		catch(Exception e){
			e.printStackTrace();
			
			return 1;
		}
	}
	
	//neue monatliche Transaktion buchen
	//neue Transaktion der Tabelle Automatic hinzufuegen
	public static void addMonatlicheTransaktion(Context context, String name, String betrag, String datum, String tag){
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(transEntry.M_COLUMN_NAME_BEZEICHNER, name);
		cv.put(transEntry.M_COLUMN_NAME_BETRAG, betrag);
		cv.put(transEntry.M_COLUMN_NAME_DATUM, datum);
		cv.put(transEntry.M_COLUMN_NAME_TAG, tag);
		
		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME_AUTOMATIC, null, cv);
	}


	//neues Sparziel anlegen
	public static void addSparziel(Context context, String name, String betrag, String datum, String sparbetrag){
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(transEntry.T_COLUMN_NAME_BEZEICHNER, name);
		cv.put(transEntry.T_COLUMN_NAME_BETRAG, betrag);
		cv.put(transEntry.T_COLUMN_NAME_DATUM, datum);
		cv.put(transEntry.T_COLUMN_NAME_SPARBETRAG, sparbetrag);
		cv.put(transEntry.T_COLUMN_NAME_GUTHABEN, "0");

		
		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME_TARGET, null, cv);
	}
	
	//neue Kategorie anlegen
	public static void addKategorie(Context context, String name, String budget, String restbetrag, String datum){
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(transEntry.K_COLUMN_NAME_BEZEICHNER, name);
		cv.put(transEntry.K_COLUMN_NAME_BUDGET, budget);
		cv.put(transEntry.K_COLUMN_NAME_RESTBETRAG, restbetrag);
		cv.put(transEntry.K_COLUMN_NAME_LAST_UPDATED, datum);
		
		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME_Kategorie, null, cv);
		
		db.close();
	}

}
