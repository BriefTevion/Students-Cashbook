package Charts;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import studentcashbook.Activities.MainActivity;
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

import com.example.studentcashbook.R;

public class ChartBar extends Fragment{

	
	GraphicalView barChartView = null;
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
			 View windows = inflater.inflate(R.layout.chartbar_frag, container, false);
			 try{
				 //LinearLayout layout = (LinearLayout) windows.findViewById(R.id.linearLayoutPieChart);
				 //layout.addView(createPie());
				 
				 RelativeLayout vG = (RelativeLayout) windows.findViewById(R.id.relativeLayoutBarChart);
					vG.addView(createBarChart());
				}
				catch(Exception e){
					Log.v("test", e.getMessage());
				}
			 
			 
	        return windows;
	        
	}

	private View createBarChart() {
		
		//Datenabfrage aus Datenbank
		 TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(MainActivity.getContext());	
		 SQLiteDatabase db = dbHelper.getReadableDatabase();
		 Integer monatlEinnahmenGesamt = 0;
		 Integer monatlAusgabenGesamt = 0;

		 Integer count = 0;
		 
		 String [] projection = {
					transEntry.M_COLUMN_NAME_BEZEICHNER,
					transEntry.M_COLUMN_NAME_BETRAG
			};
		 
		 Cursor c = db.query(transEntry.TABLE_NAME_AUTOMATIC, projection, null, null, null, null, null);
		 
		 count = c.getCount();
		 
		 
		 while(c.moveToNext()){
			 
			 if(c.getString(1).contains("-")){
				 monatlAusgabenGesamt = monatlAusgabenGesamt - Integer.parseInt(c.getString(1));
			 }else{
				 monatlEinnahmenGesamt = monatlEinnahmenGesamt + Integer.parseInt(c.getString(1)); 
			 }	 
			 
		 }
		 db.close();
		 
		 //Series
		 XYSeries mEinSeries = new XYSeries("Monatl. Einnahmen");
		 mEinSeries.add(1, monatlEinnahmenGesamt);
		 XYSeries mAusSeries = new XYSeries("Monatl. Ausgaben");
		 mAusSeries.add(2, monatlAusgabenGesamt);
		 
		 //Dataset
		 XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		 dataset.addSeries(mEinSeries);
		 dataset.addSeries(mAusSeries);
		 
		 //SeriesRenderer
		 XYSeriesRenderer mEinRenderer = new XYSeriesRenderer();
		 mEinRenderer.setColor(Color.RED);
		 mEinRenderer.setFillPoints(true);
		 mEinRenderer.setLineWidth(2);
		 mEinRenderer.setDisplayChartValues(true);
		 
		 XYSeriesRenderer mAusRenderer = new XYSeriesRenderer();
		 mAusRenderer.setColor(Color.BLUE);
		 mAusRenderer.setFillPoints(true);
		 mAusRenderer.setLineWidth(2);
		 mAusRenderer.setDisplayChartValues(true);
		 
		 //MultipleSeriesRenderer
		 XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

	     multiRenderer.setYTitle("in Euro");
	     multiRenderer.setZoomButtonsVisible(false);
	     multiRenderer.setBackgroundColor(Color.TRANSPARENT);
	     multiRenderer.setBarWidth(50);
	     multiRenderer.setBarSpacing(2);
	     multiRenderer.setLegendTextSize(25);
	     multiRenderer.setLabelsColor(Color.BLACK);
	     multiRenderer.setAxisTitleTextSize(25);
	     multiRenderer.setXLabelsColor(Color.BLACK);
         multiRenderer.setYLabelsColor(0,Color.BLACK);
         multiRenderer.setChartTitleTextSize(25);
         multiRenderer.setApplyBackgroundColor(true);
         multiRenderer.setMarginsColor(Color.parseColor("#3C8DC5"));
         multiRenderer.setShowAxes(false);
         multiRenderer.setShowLabels(false);
 
         
         
         if(monatlAusgabenGesamt>monatlEinnahmenGesamt){
	         multiRenderer.setYAxisMin(monatlEinnahmenGesamt);
	         multiRenderer.setYAxisMax(monatlAusgabenGesamt);
         }
         else{
        	 multiRenderer.setYAxisMin(monatlAusgabenGesamt);
	         multiRenderer.setYAxisMax(monatlEinnahmenGesamt);
         }

	     multiRenderer.addSeriesRenderer(mEinRenderer);
	     multiRenderer.addSeriesRenderer(mAusRenderer);
	     
	     
	     
	     
	     barChartView = ChartFactory.getBarChartView(MainActivity.getContext(), dataset, multiRenderer, Type.DEFAULT );

	     LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, LinearLayout.LayoutParams.WRAP_CONTENT  );
		 params.setMargins(30, 0, 0, 0);
	     barChartView.setLayoutParams(params);
		
		 
		 return barChartView;
		 
	 }
	
	 
}
