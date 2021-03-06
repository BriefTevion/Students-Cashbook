/*
 * Diese Klasse beinhaltet alle Methoden rund um die Uebersicht.
 */
package uebersicht;

import java.util.ArrayList;
import java.util.List;


import org.achartengine.GraphicalView;

import server.StartNetworkConnectAsync;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import charts.TabPagerAdapter;

import com.example.studentcashbook.R;

import db.BudgetLoader;
import db.TransaktionenContract.transEntry;
import db.TransaktionenDBHelper;
import drawer.BaseActivity;

public class MainActivity extends BaseActivity {

	itemListAdapter adapter;
	TabPagerAdapter TabAdapter;
	GraphicalView pieChartView = null;
	private static Context mContext;
	private static ProgressDialog progressDia = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContent(getApplicationContext());

		// context setzen
		mContext = this;

		// Adapter fuer ListView erzeugen
		adapter = new itemListAdapter();
		ListView view = (ListView) findViewById(R.id.list);
		view.setAdapter(adapter);

		// Swipe View Adapter laden
		TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(TabAdapter);
		PagerTabStrip pts = (PagerTabStrip) findViewById(R.id.pager_title_strip);
		pts.setDrawFullUnderline(false);

		// Restbetraganzeige setzen
		getActionBar().setTitle("Restbudget: " + getRestbudget() + "€");

	}

	// Back Taste ausgeschalten
	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	public void startAct() {
		setContentView(R.layout.activity_main);
	}

	// wenn action button geklickt
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_tipp:
			openNewTipp();
			return true;
		case R.id.action_nEinnahme:
			openEinnahme();
			return true;
		case R.id.action_nAusgabe:
			openAusgabe();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	// Wenn action button geklickt
	public void openEinnahme() {
		Intent intent = new Intent(getApplicationContext(),
				EinnahmeActivity.class);
		startActivity(intent);

	}

	// Wenn action button geklickt
	public void openAusgabe() {
		Intent intent = new Intent(getApplicationContext(),
				AusgabeActivity.class);
		startActivity(intent);
	}

	// Tipp anzeigen
	public static void showTipp(String nachricht) {
		String urlString = "";
		String message = nachricht;

		try {
			// Tipp bearbeiten
			// Trennen von URL und Tipptext
			if (message.contains("http")) {
				String[] mArray = nachricht.split("-");
				if (mArray.length > 0) {
					urlString = mArray[1];
					message = mArray[0];
				}
			} else {
				message = message.substring(0, message.length() - 1);
			}

			final String URL = urlString;

			progressDia.dismiss();

			// Tipp Anzeige erstellen
			AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
			alert.setMessage(message);
			alert.setPositiveButton("OK", null);

			if (URL != "") {

				alert.setNegativeButton("Weitere Infos",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Weiterleitung auf Webseite
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setData(Uri.parse(URL));
								mContext.startActivity(intent);

							}
						});
			}
			alert.setCancelable(true);
			alert.create().show();
		} catch (Exception e) {

		}

	}

	// Action button neuer Tipp geklickt
	public static void openNewTipp() {
		// Tipps anzeigen
		ConnectivityManager connMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			progressDia = ProgressDialog.show(mContext, "", "Tipps laden...",
					true);
			StartNetworkConnectAsync downloadTask = new StartNetworkConnectAsync();
			downloadTask.execute();
		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
			alert.setMessage("Keine oder schlechte Interetverbindung!");
			alert.setTitle("Achtung");
			alert.setPositiveButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
		}
	}

	// Das Restbudget abfragen
	private Integer getRestbudget() {
		try {

			return BudgetLoader.getSumOfTransaktionen(mContext);
		} catch (Exception e) {
			return 0;
		}
	}

	// Context abfragen
	private static Context x;

	public static Context getContext() {
		return x;
	}

	// Context setzen
	public static void setContent(Context y) {
		x = y;
	}

	/*
	 * Nachfolgend die Methoden der Klasse zur Erzeugung des ListItems
	 */

	public class itemListData {
		String name;
		String datum;
		String betrag;
	}

	public List<itemListData> getDataForListiew() {

		// Daten abfragen
		// Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(
				getApplicationContext());
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String[] projection = { transEntry.COLUMN_NAME_TRANSAKTION_ID,
				transEntry.COLUMN_NAME_DATUM, transEntry.COLUMN_NAME_BETRAG,
				transEntry.COLUMN_NAME_KATEGORIE };

		String sortOrder = transEntry.COLUMN_NAME_TRANSAKTION_ID + " DESC";

		Cursor c = db.query(transEntry.TABLE_NAME, projection, null, null,
				null, null, sortOrder, "3");

		List<itemListData> list = new ArrayList<itemListData>();

		// Daten erhalten
		while (c.moveToNext()) {
			String name = c.getString(3);
			String betrag;
			String datum = c.getString(1);

			itemListData ild = new itemListData();

			if (c.getString(2).matches("")) {
				betrag = "0";
			} else {
				betrag = c.getString(2);
			}

			// Daten in Liste uebergeben
			ild.name = name;
			ild.datum = datum;
			ild.betrag = betrag;

			list.add(ild);

		}
		db.close();
		return list;

	}

	/*
	 * Adapter fuer die ListView
	 */
	public class itemListAdapter extends BaseAdapter {
		List<itemListData> list = getDataForListiew();

		@Override
		public int getCount() {
			return list.size();

		}

		@Override
		public itemListData getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			if (arg1 == null) {
				LayoutInflater inflater = (LayoutInflater) MainActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				arg1 = inflater.inflate(R.layout.listitemmain, arg2, false);
			}

			TextView name = (TextView) arg1.findViewById(R.id.textView1);
			TextView datum = (TextView) arg1.findViewById(R.id.textView2);
			TextView betrag = (TextView) arg1.findViewById(R.id.textView3);

			itemListData data = list.get(arg0);

			name.setText(data.name);
			datum.setText(data.datum);
			betrag.setText(data.betrag + "€");

			return arg1;
		}

		public itemListData getItems(int position) {
			return list.get(position);
		}

	}

}