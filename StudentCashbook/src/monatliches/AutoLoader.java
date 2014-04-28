/*
 * Diese Klasse wird nach vollstaendigen Boots des Smartphones ausgefuehrt.
 * Sie prueft, ob fuer den aktuellen Tag eine monatliche Transaktion, ein Sparziel abgebucht werden muessen oder
 * ob die restbudgets der Kategorien zurueckgesetz weden muessen.
 */
package monatliches;

import java.text.DateFormat;
import java.util.Date;

import org.joda.time.DateMidnight;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import db.TransaktionenContract.transEntry;
import db.BudgetLoader;
import db.TransaktionenDBHelper;

public class AutoLoader extends BroadcastReceiver {
	
	@Override
	  public void onReceive(Context context, Intent intent) {
		
		//Wenn Boot fertig
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
			//Datenbankverbindung oeffnen
			TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);	
			SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
			SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
			//Aktuelles Datum und Uhrzeit abfragen
			DateMidnight aktuellesDatum = new DateMidnight(new Date());
			String AktuellesDatum = DateFormat.getDateInstance().format(new Date());
			String AktuelleUhrzeit = DateFormat.getTimeInstance().format(new Date());
			
			
			//pruefen ob es sich um den ersten Boot des Tages handelt
			if(getLastBoot(context)!=AktuellesDatum){
				
				setLastBootToday(context, AktuellesDatum);
				
				try{
					//wenn es sich um den ersten Tag im Monat handelt
					if(aktuellesDatum.getDayOfMonth()==1){
						//Sparziele buchen wenn noetig
						Cursor c = BudgetLoader.getDetailsOfSparziele(dbRead, context);
	
							while(c.moveToNext()){
	
								Integer id = BudgetLoader.getNewTransaktionID(context, dbRead);
								
								BudgetLoader.addEinmaligeTransaktion(dbWrite, context, id, "", AktuellesDatum, AktuelleUhrzeit, c.getString(0), c.getString(2));
							
								BudgetLoader.addCreditToSparziel(dbWrite, dbRead, context, Integer.parseInt(c.getString(2)), c.getString(0));
							}
							
						}
						
						//monatliche Transaktionen buchen wenn noetig
						Cursor cT = BudgetLoader.getDetailsOfMonthlyTransaktionen(dbRead, context);
						
						while(cT.moveToNext()){
							//wenn am heutigen tag im monat eine Transaktion zu buchen ist
							if(Integer.parseInt(cT.getString(1))==aktuellesDatum.getDayOfMonth()){
	
								Integer id = BudgetLoader.getNewTransaktionID(context, dbRead);
	
								BudgetLoader.addEinmaligeTransaktion(dbWrite, context, id, "", AktuellesDatum, AktuelleUhrzeit, cT.getString(0), cT.getString(2));
	
							}
						}
						
						//wenn heute der erste tag im Monat ist
						if(aktuellesDatum.getDayOfMonth()==1){
							//Restbudgets der Kategorien zuruecksetzen
							Cursor cK = BudgetLoader.getDetailsOfKategorien(dbRead, context);
							
							
							while(cK.moveToNext()){
								BudgetLoader.resetRestbudgetsKategorien(dbWrite, context, cK.getString(0), cK.getString(2));
							
							}
						}
					}
					catch(Exception e){
						Log.v("test", ""+ e.getMessage());
					}
			}
			//Datenbankverbindun schliessen
			dbRead.close();
			dbWrite.close();
			
			 
		}
		
	}
		
		private void setLastBootToday(Context context, String date){
			SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = spref.edit();
			try{
				//verschluesselter Wert speichern;
				editor.putString("LASTBOOT", date);
				editor.commit();

			}
			catch(Exception e){

			}
			
		}
		
		private String getLastBoot(Context context){
			SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context);

			String lastBoot = spref.getString("LASTBOOT", "");
			
			return lastBoot;
		}
			 
	
}
