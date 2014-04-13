/*
 * Diese Klasse laedt Daten bei update in das Widget.
 */
package widget;


import java.util.ArrayList;
import java.util.List;

import db.TransaktionenDBHelper;
import db.TransaktionenContract.transEntry;

import studentcashbook.activities.MainActivity.itemListData;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

public class WidgetLoader extends AppWidgetProvider {
	RemoteViews remoteViews;
	AppWidgetManager appWidgetManager;
	ComponentName thisWidget;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		this.appWidgetManager = appWidgetManager;
		remoteViews = new RemoteViews(context.getPackageName(),com.example.studentcashbook.R.layout.widgetlayout );
		thisWidget = new ComponentName(context, WidgetLoader.class);
		
		String daten = getDataForWidget(context);
		
		//Daten in Widget schreiben
		remoteViews.setTextViewText(com.example.studentcashbook.R.id.textView1_widget, daten);
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		
	}
	
	//Daten abfragen
	public String getDataForWidget(Context context){
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);	
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String [] projection = {
				transEntry.COLUMN_NAME_TRANSAKTION_ID,
				transEntry.COLUMN_NAME_DATUM,
				transEntry.COLUMN_NAME_BETRAG,
				transEntry.COLUMN_NAME_KATEGORIE
		};
		
		String sortOrder = transEntry.COLUMN_NAME_TRANSAKTION_ID + " DESC";
		
		Cursor c = db.query(transEntry.TABLE_NAME, projection, null, null, null, null, sortOrder, "3");
		
		String data="";
		
		if(c.getCount()>1){
		
			//Daten erhalten
			while(c.moveToNext()){
				data = data + c.getString(1) + " " + c.getString(3) + "    " + c.getString(2) + "€\n";
			}
			
		}
		db.close();
		return data;
	
	}
	
}