/*
 * Diese Klasse erstellt das Kuchendiagramm, das die Restbudgets der Kategorien zeigt.
 */
package charts;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import uebersicht.MainActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

import db.TransaktionenContract.transEntry;
import db.TransaktionenDBHelper;

public class ChartRestbudgets extends Fragment {

	GraphicalView pieChartView = null;
	private DefaultRenderer pieRenderer = new DefaultRenderer();
	private static int[] colors = { Color.BLUE, Color.WHITE };
	private CategorySeries pieSeries = new CategorySeries("");

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Layout abfragen
		View windows = inflater.inflate(R.layout.chartrestbudgets_frag,
				container, false);
		RelativeLayout vG = (RelativeLayout) windows
				.findViewById(R.id.linearLayoutPieChart);
		TextView tv = (TextView) windows.findViewById(R.id.default_Pie);

		if (checkIfNull() == true) {
			tv.setText("Bisher wurde noch keine Kategorien mit Budget angelegt.");
			tv.setVisibility(View.VISIBLE);

		} else {
			tv.setVisibility(View.GONE);

			try {
				vG.addView(createPie());
			} catch (Exception e) {
				Log.v("test", e.getMessage());
			}

		}
		return windows;

	}

	// Tortendiagramm erstellen
	public GraphicalView createPie() {

		// Datenabfrage aus Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(
				MainActivity.getContext());
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Integer count = 0;
		Integer entryCounter = 0;
		Integer eingabeFarbe = 0;

		String[] projection = { transEntry.K_COLUMN_NAME_BEZEICHNER,
				transEntry.K_COLUMN_NAME_RESTBETRAG };
		String sortOrder = transEntry.K_COLUMN_NAME_RESTBETRAG + " DESC";

		Cursor c = db.query(transEntry.TABLE_NAME_Kategorie, projection, null,
				null, null, null, sortOrder);

		count = c.getCount();

		if (count > 0) {

			while (c.moveToNext()) {

				if (Integer.parseInt(c.getString(1)) > 0) {

					Log.v("test", c.getString(0));

					pieSeries.add(c.getString(0),
							Integer.parseInt(c.getString(1)));
					SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();

					if ((entryCounter % 2) == 0) {
						eingabeFarbe = 1;
					} else {
						eingabeFarbe = 0;
					}

					renderer.setColor(colors[eingabeFarbe]);
					renderer.setDisplayChartValues(true);
					pieRenderer.addSeriesRenderer(renderer);
					entryCounter = entryCounter + 1;
				}
			}

			pieRenderer.setLabelsTextSize(30);
			pieRenderer.setShowLegend(false);
			pieRenderer.setDisplayValues(true);
			pieRenderer.setPanEnabled(false);

			pieChartView = ChartFactory.getPieChartView(
					MainActivity.getContext(), pieSeries, pieRenderer);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			pieChartView.setLayoutParams(params);

		}

		db.close();
		return pieChartView;

	}

	public boolean checkIfNull() {

		// Datenabfrage aus Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(
				MainActivity.getContext());
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Integer count = 0;

		String[] projection = { transEntry.K_COLUMN_NAME_BEZEICHNER };

		Cursor c = db.query(transEntry.TABLE_NAME_Kategorie, projection, null,
				null, null, null, null);

		count = c.getCount();

		db.close();

		if (count == 0) {
			return true;
		} else {
			return false;
		}

	}

}
