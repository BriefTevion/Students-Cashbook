package Charts;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import studentcashbook.activities.MainActivity;
import DB.TransaktionenContract.transEntry;
import DB.TransaktionenDBHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.studentcashbook.R;

public class ChartBar extends Fragment{

	
	GraphicalView pieChartView = null;
	private DefaultRenderer pieRenderer= new DefaultRenderer();
	private static int[] colors = {Color.BLUE, Color.WHITE};
	private CategorySeries pieSeries = new CategorySeries("");
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		 View windows = inflater.inflate(R.layout.chartbar_frag, container, false);
		 RelativeLayout vG = (RelativeLayout) windows.findViewById(R.id.linearLayoutMonatlichesChart);
		 TextView tv = (TextView) windows.findViewById(R.id.default_text);
		 
		 
		 if(checkIfNull()==true){
			 tv.setText("Es liegen keine monatlichen Transaktionen vor.");
			 tv.setVisibility(View.VISIBLE);
	 
		 }
		 else{
			 tv.setVisibility(View.GONE);
		 
			 try{		 	
					vG.addView(createChart());
				}
				catch(Exception e){
					Log.v("test", e.getMessage());
				}
			 
		 }
        return windows;
	        
	}


	private View createChart() {
		
		//Datenabfrage aus Datenbank
		 TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(MainActivity.getContext());	
		 SQLiteDatabase db = dbHelper.getReadableDatabase();

		 Integer count = 0;
		 Integer eingabeFarbe = 0;
		 
		 String [] projection = {
					transEntry.M_COLUMN_NAME_BEZEICHNER,
					transEntry.M_COLUMN_NAME_BETRAG
			};
		 String sortOrder = transEntry.M_COLUMN_NAME_BETRAG + " DESC";
		 
		 Cursor c = db.query(transEntry.TABLE_NAME_AUTOMATIC, projection, null, null, null, null, sortOrder);
		 
		 count = c.getCount();
		 
		 //wenn daten vorhanden sind
		 if(count>0){	 
			 
				while(c.moveToNext()){
					
					
					SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
					//wenn monatliche einnahme
					if(Integer.parseInt(c.getString(1))>=0){
						pieSeries.add(c.getString(0), Integer.parseInt(c.getString(1)));
						eingabeFarbe = 0; 
					}
					//wenn monatliche ausgabe
					else{
						pieSeries.add(c.getString(0), -Integer.parseInt(c.getString(1)));
						eingabeFarbe = 1; 
						
					}
					
					renderer.setColor(colors[eingabeFarbe]);
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
		 }

		 
		 db.close();

		 return pieChartView;
		 
	 }
	
	
	//pruefen ob daten vorliegen
	private boolean checkIfNull() {
		//Datenabfrage aus Datenbank
		 TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(MainActivity.getContext());	
		 SQLiteDatabase db = dbHelper.getReadableDatabase();

		 Integer count = 0;
		 
		 String [] projection = {
					transEntry.M_COLUMN_NAME_BEZEICHNER
			};
		 
		 Cursor c = db.query(transEntry.TABLE_NAME_AUTOMATIC, projection, null, null, null, null, null);
		 
		 count = c.getCount();
		 
		 db.close();
		 
		 if(count==0){
			 return true;
		 }
		 else{
			 return false;
		 }


	}
	 
}
