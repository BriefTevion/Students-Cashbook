/*
 * Diese Klasse beinhaltet ausschliesslich alle Notwendigen Methoden fuer den Navigation Drawer dieser App
 * Auf sie kann ueber extend zugegriffen werden.
 */

package studentcashbook.Activities;

import com.example.studentcashbook.R;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BaseActivity extends FragmentActivity {
	private String[] activitiesList;
	private DrawerLayout dl;
	private ListView dLv;
	private ActionBarDrawerToggle abdt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startAct();
		//DrawerList initialisieren
		activitiesList = getResources().getStringArray(R.array.string_array_navigation_drawer);
		dl = (DrawerLayout) findViewById(R.id.drawer_layout);
		dLv = (ListView) findViewById(R.id.left_drawer);

		//Adapter erzeugen f??r die ListView
		dLv.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_listview_item, activitiesList));

		// Set the list's click listener
        dLv.setOnItemClickListener(new DrawerItemClickListener());


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

	//Konfiguration wird ??bergeben
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
	
	
	
	//zum ??ffnen der entsprechenden Activitiy
	public void startAct(){
		
		setContentView(R.layout.activity_main);
	}


	public void changeActivity(Integer number){
		switch(number){

		case 1:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
		case 2:
			Intent intent2 = new Intent(this, MonatlichesActivity.class);
			startActivity(intent2);
			break;
		case 3:
			Intent intent3 = new Intent(this, KategorienActivity.class);
			startActivity(intent3);
			break;
		case 4:
			Intent intent4 = new Intent(this, SparzielActivity.class);
			startActivity(intent4);
			break;
		case 5:
			Intent intent5 = new Intent(this, EinstellungenActivity.class);
			startActivity(intent5);
			break;
		case 6:
			Intent intent6 = new Intent(this, LoginActivity.class);
			startActivity(intent6);
			break;
		}
		
		dl.closeDrawer(dLv);
	}


	private class DrawerItemClickListener implements ListView.OnItemClickListener  {

		public void onItemClick(AdapterView parent, View view, int position, long id) {
			//Toast.makeText(MainActivity.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();

			String item = (String) ((TextView)view).getText();
			int number=0;

			if(item.contains("Übersicht")){
				number = 1;
			}
			else if(item.contains("Monatliches")){
				number = 2;
			}
			else if(item.contains("Kategorien")){
				number = 3;
			}
			else if(item.contains("Sparziele")){
				number = 4;
			}
			else if(item.contains("Einstellungen")){
				number = 5;
			}
			else if(item.contains("Abmelden")){
				number = 6;
			}
			

			changeActivity(number);


	    }

	}
}



