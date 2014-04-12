package Charts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import studentcashbook.activities.MainActivity;
import DB.TransaktionenContract.transEntry;
import DB.TransaktionenDBHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.studentcashbook.R;

public class ChartEinAusgaben extends Fragment {

	
	GraphicalView pieChartView = null;
	private DefaultRenderer pieRenderer= new DefaultRenderer();
	//Werte abhaenig  von Anzahl der Werte
	private static int[] colors = {Color.WHITE, Color.BLUE};
	private CategorySeries pieSeries = new CategorySeries("");
	
	
	
	
	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		 View windows = inflater.inflate(R.layout.charteinausgaben_frag, container, false);
		 RelativeLayout vG = (RelativeLayout) windows.findViewById(R.id.linearLayoutPieChart);
		 TextView tv = (TextView) windows.findViewById(R.id.default_Pie);
		 
		 
		 if(checkIfNull()==true){
			 tv.setText("Diesen Monat wurden noch keine Transkationen durchgeführt.");
			 tv.setVisibility(View.VISIBLE);
			 
			 
		 }
		 else{
			 tv.setVisibility(View.GONE);
		 
			 try{		 	
					vG.addView(createPie());
				}
				catch(Exception e){
					Log.v("test", e.getMessage());
				}
			 
		 }
        return windows;
        
}
	 
	 
	//Tortendiagramm erstellen
	 public GraphicalView createPie(){
		 
		 //Datenabfrage aus Datenbank
		 TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(MainActivity.getContext());	
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
		 
		 int buchungsMonat=0;
		 
		 while(c.moveToNext()){
			 
			 //Konvertieren des Datums von String zu Date
			 //ausserdem auslesen des erstellten Monats

				try {
					date = (Date) sdfDate.parse(c.getString(1));
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					buchungsMonat = cal.get(Calendar.MONTH);

					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.v("test", e.getMessage());
				}

			 
			
			//den aktuellen Monat bekommen
			Calendar ca = Calendar.getInstance();
			int aktuellerMonat = ca.get(Calendar.MONTH);
			
			 //Wenn Datum innerhalb diesen Monats
			 if(buchungsMonat == aktuellerMonat){
 
				 
				 if(c.getString(2).contains("-")){
					 ausgabeGesamt = ausgabeGesamt - Integer.parseInt(c.getString(2));
				 }else{
					 einnahmeGesamt = einnahmeGesamt + Integer.parseInt(c.getString(2));
					 
				 }
			 }
			 
		 }
		 
		 
		 db.close();
		 
		//Daten aus Abfrage in Tortendiagramm einbauen
		 double[] values = {einnahmeGesamt, ausgabeGesamt};
		 String[] categoryNames ={"Einnahmen", "Ausgaben"};
		 
		 
		 
		 for(int i=0;i<=1; i++){
			 pieSeries.add(categoryNames[i], values[i]);
			 SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			 renderer.setColor(colors[(pieSeries.getItemCount() - 1) % colors.length]);
			 renderer.setDisplayChartValues(true);
			 pieRenderer.addSeriesRenderer(renderer);
		 }

		 pieRenderer.setLabelsTextSize(30);
		 pieRenderer.setShowLegend(false);
		 pieRenderer.setDisplayValues(true);
		 pieRenderer.setPanEnabled(false);
		 
		 pieChartView = ChartFactory.getPieChartView(MainActivity.getContext(), pieSeries, pieRenderer);

		 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT );
		 pieChartView.setLayoutParams(params);
		

	     
	     return pieChartView;
		 
	 }
	 
	 
	 public boolean checkIfNull(){
		 
		//Datenabfrage aus Datenbank
		 TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(MainActivity.getContext());	
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
		 
		 Log.v("test", "count " + count );
		 
		 if(count>=1){
		 
		 
				 int buchungsMonat=0;
				 
				 while(c.moveToNext()){
					 
					 //Konvertieren des Datums von String zu Date
					 //ausserdem auslesen des erstellten Monats
		
						try {
							date = (Date) sdfDate.parse(c.getString(1));
							
							Calendar cal = Calendar.getInstance();
							cal.setTime(date);
							buchungsMonat = cal.get(Calendar.MONTH);
		
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Log.v("test", e.getMessage());
						}
		
					 
					
					//den aktuellen Monat bekommen
					Calendar ca = Calendar.getInstance();
					int aktuellerMonat = ca.get(Calendar.MONTH);
					
					 //Wenn Datum innerhalb diesen Monats
					 if(buchungsMonat == aktuellerMonat){
		 
						 
						 if(c.getString(2).contains("-")){
							 ausgabeGesamt = ausgabeGesamt - Integer.parseInt(c.getString(2));
						 }else{
							 einnahmeGesamt = einnahmeGesamt + Integer.parseInt(c.getString(2));
							 
						 }
					 }
					 
				 }
		 }
		 
		 db.close();
		 
		 
		 //Wenn keine aktuellen Daten verfuegbar, dann return true
		 if(einnahmeGesamt==0 && ausgabeGesamt==0){
			 return true;
		 }
		 else{
			 return false;
		 }

	 }

	 
	 
}
