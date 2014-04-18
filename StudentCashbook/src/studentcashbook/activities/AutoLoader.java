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
import db.BudgetLoader;
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

						if(Integer.parseInt(cT.getString(1))==aktuellesDatum.getDayOfMonth()){

							Integer id = BudgetLoader.getNewTransaktionID(context, dbRead);

							BudgetLoader.addEinmaligeTransaktion(dbWrite, context, id, "", AktuellesDatum, AktuelleUhrzeit, cT.getString(0), cT.getString(2));

						}
					}
					
					
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

			dbRead.close();
			dbWrite.close();
			
			 
		}
	}
			 
	
}
