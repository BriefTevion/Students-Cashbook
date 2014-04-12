/*
 * Diese Klasse beinhaltet ausschliesslich alle Notwendigen Methoden fuer den Navigation Drawer dieser App
 * Auf sie kann ueber extend zugegriffen werden.
 */

package studentcashbook.activities;

import java.util.ArrayList;
import java.util.List;

import Network.StartNetworkConnectForNotifications;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentcashbook.R;

public class BaseActivity extends FragmentActivity {
	private String[] activitiesList;
	private DrawerLayout dl;
	private ListView dLv;
	private ActionBarDrawerToggle abdt;
	itemListAdapter adapter;
	static Context mcontext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startAct();
		
		try{
		dl = (DrawerLayout) findViewById(R.id.drawer_layout);
		dLv = (ListView) findViewById(R.id.left_drawer);

		//Adapter erzeugen fuer die ListView
		adapter = new itemListAdapter();

        dLv.setAdapter(adapter);

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
		}
		catch(Exception e){
			Log.v("test", e.getMessage());
		}

		//Pfeil-Icon setzen
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		mcontext = getApplicationContext();


	}

	//Konfiguration wird uebergeben
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
	
	
	
	//zum oeffnen der entsprechenden Activitiy
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
			if(EinstellungenActivity.getKeyTippNotifications()==true){
				try{
				startNotificationContentDownload();
				}
				catch(Exception e){
					Log.v("test", e.getMessage());
				}
			}
			break;
		}
		
		dl.closeDrawer(dLv);
	}

	private void startNotificationContentDownload(){
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        
        
        if (networkInfo != null && networkInfo.isConnected()) {
          StartNetworkConnectForNotifications downloadTask = new StartNetworkConnectForNotifications();
          downloadTask.execute();
        }
		
	}
	
	
	//Benachrichtigung senden
	public static void sendNotification(String nachricht) {
		String urlString= "";
		String message = nachricht;	
		
		if(message.contains("http")){
			String [] mArray = nachricht.split("-");
			if(mArray.length>0){
							
				urlString = mArray[1];
				message = mArray[0];
			}
		}
		else{
			message = message.substring(0,  message.length()-1);
			urlString="";
		}
		
		final String URL = urlString;
		String [] mText = message.split("\n\n");
		
		
		//Builder erstellen
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(mcontext)
			    .setSmallIcon(R.drawable.ic_tipps)
			    .setContentTitle(mText[0])
			    .setContentText(mText[1]);
		
		NotificationCompat.InboxStyle inboxStyle =
		        new NotificationCompat.InboxStyle();
		String[] events = new String[2];
		events[0] = mText[1];
		events[1] = new String("F??r mehr Infos klicken!");
		// Titel des BigView
		inboxStyle.setBigContentTitle(mText[0]);
		
		// Moves events into the big view
		for (int i=0; i < events.length; i++) {

		    inboxStyle.addLine(events[i]);
		}
		// Moves the big view style object into the notification object.
		mBuilder.setStyle(inboxStyle);
		
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = 
		        (NotificationManager) mcontext.getSystemService(NOTIFICATION_SERVICE);
		
		if(URL!=""){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(URL));
		
			PendingIntent pending = PendingIntent.getActivity(mcontext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		    mBuilder.setContentIntent(pending);
		}
		
		// Sets an ID for the notification
		int mNotificationId = 001;
	
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
		
	}


	private class DrawerItemClickListener implements ListView.OnItemClickListener  {

		public void onItemClick(AdapterView parent, View view, int position, long id) {
			//Toast.makeText(MainActivity.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();

			TextView text = (TextView) view.findViewById(R.id.textView1);
			String item = text.getText().toString();
			int number=0;

			if(item.contains("??bersicht")){
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
	
	public class itemListData{
		String name;
	}
	
	public List<itemListData> getDataForDrawer()
	{
		
		//Daten abfragen
		String [] projection = getResources().getStringArray(R.array.string_array_navigation_drawer);
				
		List<itemListData> list = new ArrayList <itemListData>();
		

			for(int i=0; i<projection.length; i++){
				String kName = projection[i];

				//Daten in Liste uebergeben
				itemListData ild = new itemListData();
				ild.name = kName;
				list.add(ild);
					
					
			}	

					return list;
					
	}

	//benutzerdefinierter Adapter
		public class itemListAdapter extends BaseAdapter{
			List<itemListData> list =  getDataForDrawer();
			
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

				if(arg1==null)
				{
					LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					arg1 = inflater.inflate(R.layout.drawer_listview_item, arg2,false);
				}

				ImageView img = (ImageView) arg1.findViewById(R.id.imageView1);
				TextView name = (TextView)arg1.findViewById(R.id.textView1);


				itemListData data = list.get(arg0);
				
				if(data.name.contains("??bersicht")){
					img.setImageResource(R.drawable.ic_ueber);
					name.setText(data.name);
				}
				else if(data.name.contains("Monatliches")){
					img.setImageResource(R.drawable.ic_monatliches);
					name.setText(data.name);
				}
				else if(data.name.contains("Kategorien")){
					img.setImageResource(R.drawable.ic_kategorien);
					name.setText(data.name);
				}
				else if(data.name.contains("Sparziele")){
					img.setImageResource(R.drawable.ic_ziele);
					name.setText(data.name);
				}
				else if(data.name.contains("Einstellungen")){
					img.setImageResource(R.drawable.ic_einstellungen);
					name.setText(data.name);
				}
				else if(data.name.contains("Abmelden")){
					img.setImageResource(R.drawable.ic_logout);
					name.setText(data.name);
				}


				

				return arg1;
			}
			
			public itemListData getItems(int position)
			{
				return list.get(position);
			}
			
			
		}


}

