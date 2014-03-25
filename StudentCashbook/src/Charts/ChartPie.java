package Charts;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import studentcashbook.Activities.MainActivity;

import DB.TransaktionenContract.transEntry;
import DB.TransaktionenDBHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.studentcashbook.R;

public class ChartPie extends Fragment {

	GraphicalView pieChartView = null;
	private DefaultRenderer pieRenderer= new DefaultRenderer();
	//Werte abhaenig  von Anzahl der Werte
	private static int[] colors = {Color.BLUE, Color.RED};
	private CategorySeries pieSeries = new CategorySeries("");
	
	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		 View windows = inflater.inflate(R.layout.chartpie_frag, container, false);
        Context context = null;
        
        //createPie(context, inflater, container);
        
        return windows;
        
}
	 
	 //Tortendiagramm erstellen
	 public void createPie(Context context, LayoutInflater inflater, ViewGroup container){
		 
		 //Datenabfrage aus Datenbank
		 TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);	
		 SQLiteDatabase db = dbHelper.getReadableDatabase();
		 Integer einnahmeGesamt = 0;
		 Integer ausgabeGesamt = 0;
		 Date date = null;
		 SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
		 Integer count = 0;
		 
		 String [] projection = {
					transEntry.COLUMN_NAME_TRANSAKTION_ID,
					transEntry.COLUMN_NAME_DATUM,
					transEntry.COLUMN_NAME_BETRAG,
					transEntry.COLUMN_NAME_KATEGORIE
			};
		 
		 Cursor c = db.query(transEntry.TABLE_NAME, projection, null, null, null, null, null);
		 
		 count = c.getCount();
		 
		 while(c.moveToNext()){
			 
			 //Konvertieren des Datums von String zu Date
			 try {
				date = (Date) sdfDate.parse(c.getString(1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			 
			
			//den aktuellen Monat bekommen
			
			 //Wenn Datum innerhalb diesen Monats
			 //if(){
 
				 
				 if(c.getString(2).contains("-")){
					 ausgabeGesamt = ausgabeGesamt - Integer.parseInt(c.getString(2));
				 }else{
					 einnahmeGesamt = einnahmeGesamt + Integer.parseInt(c.getString(2));
					 
				 }
			 //}
			 
		 }
		 
		 
		 db.close();
		 
		//Daten aus Abfrage in Tortendiagramm einbauen
		 double[] values = {einnahmeGesamt, ausgabeGesamt};
		 String[] categoryNames ={"Einnahme", "Ausgabe"};
		 
		 for(int i=0;i<2; i++){
			 pieSeries.add(categoryNames[i], values[i]);
			 SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			 renderer.setColor(colors[(pieSeries.getItemCount() - 1) % colors.length]);
			 renderer.setDisplayChartValues(true);
			 pieRenderer.addSeriesRenderer(renderer);
		 }
		 
		 
		 pieChartView = ChartFactory.getPieChartView(context, pieSeries, pieRenderer);
		 
		 View windows = inflater.inflate(R.layout.chartpie_frag, container, false);
	        
	     LinearLayout ll = (LinearLayout) windows.findViewById(R.id.linearLayout);
	 

		 
	 }
	 
}
