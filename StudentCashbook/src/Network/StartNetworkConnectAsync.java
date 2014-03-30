package Network;

import android.os.AsyncTask;

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

	
	}


