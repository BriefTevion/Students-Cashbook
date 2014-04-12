package Network;

import studentcashbook.Activities.BaseActivity;
import android.os.AsyncTask;

public class StartNetworkConnectForNotifications extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls){
			NetworkConnect nc = new NetworkConnect();
			try{
				return nc.downloadTipps();
				
			}catch(Exception e){
				return e.getMessage();
			}
		
	}
		
		// onPostExecute displays the results of the AsyncTask.	
	 	@Override	
	 	protected void onPostExecute(String tipp) {
	 		BaseActivity.sendNotification(tipp);
	 	}
		
	}
