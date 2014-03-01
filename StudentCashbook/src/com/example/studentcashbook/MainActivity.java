package com.example.studentcashbook;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private String[] activitiesList;
	private DrawerLayout dl;
	private ListView dLv;
	private ActionBarDrawerToggle abdt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Drawer List initialisieren
		activitiesList = getResources().getStringArray(R.array.string_array_navigation_drawer);
		dl = (DrawerLayout) findViewById(R.id.drawer_layout);
		dLv = (ListView) findViewById(R.id.left_drawer);
		
		//Adapter erzeugen für die ListView
		dLv.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_listview_item, activitiesList));

		//Icon
		dl = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		//Konfigurieren des Drawer Icons
		abdt = new ActionBarDrawerToggle(this, dl, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close)
		{	
		};
		
		//ActionBarDrawerToggle als Drawer Listener setzen
		dl.setDrawerListener(abdt);
		
		//Pfeil-Icon setzen
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		
	}

	//Konfiguration wird übergeben
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		abdt.onConfigurationChanged(newConfig);
	}
	
	//Aufruf bei Klick auf Item
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		// Aufruf von abdt.onOptionsItemSelected()
        // Wenn true, dann Click Event behandelt
        if (abdt.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        abdt.syncState();
    }
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
}
