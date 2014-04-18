/*
 * Diese Klasse behandelt die Monatlichen Buchungen
 */
package studentcashbook.activities;

import java.text.DateFormat;
import java.util.Date;

import org.joda.time.DateMidnight;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import db.TransaktionenContract.transEntry;
import db.TransaktionenDBHelper;

public class AutoLoader extends BroadcastReceiver {

	@Override
	  public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
			TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);	
			SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
			SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
			
			try{
					DateMidnight aktuellesDatum = new DateMidnight(new Date());
					String AktuellesDatum = DateFormat.getDateInstance().format(new Date());
					String AktuelleUhrzeit = DateFormat.getTimeInstance().format(new Date());

					
					if(aktuellesDatum.getDayOfMonth()==1){
					//Sparziele buchen wenn noetig
					Cursor c = getDetailsOfSparziele(dbRead, context);

						while(c.moveToNext()){

							Integer id = getNewTransaktionID(context, dbRead);
							
							addEinmaligeTransaktion(dbWrite, context, id, "", AktuellesDatum, AktuelleUhrzeit, c.getString(0), c.getString(2));
						
							addCreditToSparziel(dbWrite, dbRead, context, Integer.parseInt(c.getString(2)), c.getString(0));
						}
						
					}
					
					//monatliche Transaktionen buchen wenn noetig
					Cursor cT = getDetailsOfMonthlyTransaktionen(dbRead, context);
					
					while(cT.moveToNext()){

						if(Integer.parseInt(cT.getString(1))==aktuellesDatum.getDayOfMonth()){

							Integer id = getNewTransaktionID(context, dbRead);

							addEinmaligeTransaktion(dbWrite, context, id, "", AktuellesDatum, AktuelleUhrzeit, cT.getString(0), cT.getString(2));

						}
					}
					
					
					if(aktuellesDatum.getDayOfMonth()==1){
						//Restbudgets der Kategorien zuruecksetzen
						Cursor cK = getDetailsOfKategorien(dbRead, context);
						
						
						while(cK.moveToNext()){
							resetRestbudgetsKategorien(dbWrite, context, cK.getString(0), cK.getString(2));
						
						}
					}
				}
				catch(Exception e){
					Log.v("test", ""+ e.getMessage());
				}

			dbRead.close();
			dbWrite.close();
			
			 
		}
	}
			 
	//Neue transaktionID ausgeben
		public static Integer getNewTransaktionID(Context context, SQLiteDatabase db){
			try{

				Integer id = 0;
				
				final String SQL_getMaxID = "Select Max(" + transEntry.COLUMN_NAME_TRANSAKTION_ID + ") AS id FROM " + 
											transEntry.TABLE_NAME;	
				
				Cursor c = db.rawQuery(SQL_getMaxID, null);
						
					c.moveToFirst();
					id = c.getInt(0);
				
					
					
					return id+1;		
				
			}
			catch(Exception e){
				e.printStackTrace();
				
				return 1;
			}
		}
		
		//neue Transaktion der Tabelle TransaktionenList hinzufuegen
		public void addEinmaligeTransaktion(SQLiteDatabase db, Context context, Integer id, String anmerkung, String datum, String zeit, String kategorie, String betrag){
			//Zugang zur Datenbank
			try{

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
		
		//Sparziel Betrag gutschreiben
		public void addCreditToSparziel(SQLiteDatabase dbWrite, SQLiteDatabase dbRead, Context context, Integer betrag, String sparzielTitel){
			Integer credit = betrag;
			Integer neuesGuthaben = credit + getCurrentCreditOfTarget(dbRead, context, sparzielTitel);
			
			ContentValues cv = new ContentValues();
		      cv.put(transEntry.T_COLUMN_NAME_GUTHABEN, String.valueOf(neuesGuthaben));

		      
			dbWrite.update(transEntry.TABLE_NAME_TARGET, cv, transEntry.T_COLUMN_NAME_BEZEICHNER 
					+ "= '" + sparzielTitel +"'", null);	
			
		}
		
		//Aktuelles Guthaben eines Sparzieles abfragen, bei der Annahme, dass es keine gleichnamigen Ziele gibt
		private static Integer getCurrentCreditOfTarget(SQLiteDatabase db, Context context, String sparzielTitel) {

			//GET aktuellen restbetrag der Kategorie
			String [] projection = {
					transEntry.T_COLUMN_NAME_BEZEICHNER,
					transEntry.T_COLUMN_NAME_GUTHABEN
			};

			String sortOrder = transEntry.T_COLUMN_NAME_BEZEICHNER;

				
			Cursor c = db.query(transEntry.TABLE_NAME_TARGET, projection, transEntry.T_COLUMN_NAME_BEZEICHNER 
					+ "= '" + sparzielTitel +"'", null, null, null, sortOrder);
			
			c.moveToFirst();
			Integer aktuellesGuthaben = Integer.parseInt(c.getString(1));
			
			
				
			return aktuellesGuthaben;
		}
		
		//Get Datum des Sparzieles
		public Cursor getDetailsOfSparziele(SQLiteDatabase db, Context context){

			//GET aktuellen restbetrag der Kategorie
			String [] projection = {
					transEntry.T_COLUMN_NAME_BEZEICHNER,
					transEntry.T_COLUMN_NAME_DATUM,
					transEntry.T_COLUMN_NAME_SPARBETRAG
			};

			String sortOrder = transEntry.T_COLUMN_NAME_BEZEICHNER;

				
			Cursor c = db.query(transEntry.TABLE_NAME_TARGET, projection, null, null, null, null, sortOrder);
	
			return c;
			
		}
		
		//Details der Katgorie ausgeben
		public static Cursor getDetailsOfKategorien(SQLiteDatabase db, Context context){

			String [] projection = {
					transEntry.K_COLUMN_NAME_BEZEICHNER,
					transEntry.K_COLUMN_NAME_RESTBETRAG,
					transEntry.K_COLUMN_NAME_BUDGET
			};
			String sortOrder = transEntry.K_COLUMN_NAME_BEZEICHNER;
			
			Cursor c = db.query(transEntry.TABLE_NAME_Kategorie, projection, null, null, null, null, sortOrder);
			return c;
		}
		
		//Reset der restbudgets der Kategorien
		public static void resetRestbudgetsKategorien(SQLiteDatabase db, Context context, String name, String betrag){
			
			String sql="update "+ transEntry.TABLE_NAME_Kategorie+" set restbetrag='" + 
					betrag + "' where name='" + name + "'";					
			try{
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		//Details zu den Monatstransaktionen
		public Cursor getDetailsOfMonthlyTransaktionen(SQLiteDatabase db, Context context){


			//GET aktuellen restbetrag der Kategorie
			String [] projection = {
					transEntry.M_COLUMN_NAME_BEZEICHNER,
					transEntry.M_COLUMN_NAME_TAG,
					transEntry.M_COLUMN_NAME_BETRAG
			};

			String sortOrder = transEntry.M_COLUMN_NAME_BEZEICHNER;

				
			Cursor c = db.query(transEntry.TABLE_NAME_AUTOMATIC, projection, null, null, null, null, sortOrder);
			
			return c;
					
				}
}
