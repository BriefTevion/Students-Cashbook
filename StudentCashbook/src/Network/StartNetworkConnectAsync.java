package Network;

import android.os.AsyncTask;
import android.util.Log;

public class StartNetworkConnectAsync extends AsyncTask<String, Void, String> {

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
 		Log.v("test", "Tipp: " + tipp);
 	}
	
	}


