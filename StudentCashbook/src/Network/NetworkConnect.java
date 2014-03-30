package Network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkConnect {

	// JSON Node names
	private static final String TAG_ID = "ID";
	private static final String TAG_TITLE = "Title";
	private static final String TAG_MESSAGE = "Description";
	
		
	//Methode um eine Verbindung zum Server aufzubauen und um gewuenschte Daten zu erhalten
	public String downloadTipps() throws Exception{

			InputStream is = null;
			
			try{
				URL url = new URL("http://space-labs.appspot.com/repo/2185003/budget/api.html");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
				conn.setRequestMethod("GET");
				conn.connect();
				
				//int response = conn.getResponseCode();
				is = conn.getInputStream();
				
				String contentAsString = convertStreamToString(is);
				
				String tipp = parseDownload(contentAsString);
				
				return tipp;
			}
			
			finally{
				if(is!=null){
					is.close();
				}
				
			}
		
		} 

	//Konvertiert Server Antwort in String
	private static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader bf = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		
		//solange bis es keine Zeile mehr in Server Antwort gibt
		while((line = bf.readLine())!=null){
			sb.append(line);
		}
		is.close();
		
		return sb.toString();
		
		
	}
	
	//einen zufaelligen Tipp aus dem Download heraussuchen
	private static String parseDownload(String rawString) {
			
			StringBuffer parsedString = new StringBuffer();
			String tipp = null;
			
			try {
				JSONArray tipps = new JSONArray(rawString);
				
				//zufaellige ID finden
				int randomID = (int) Math.random() * tipps.length();

				//Nachricht der zufaelligen ID erhalten
				JSONObject h = tipps.getJSONObject(randomID);
 
			    String nachricht = h.getString(TAG_MESSAGE);
   
			  return nachricht;
				
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
	}
	
	
	
	//Methode um eine Verbindung zum Server aufzubauen und neue Tipps zu pushen
//		public String uploadTipps() throws Exception{
//
//				InputStream is = null;
//				
//				try{
//					//URL url = new URL("http://space-labs.appspot.com/repo/465001/highscore_add.sjs?Name=" + player + "&Level=" + level + "&Points=" + points + "&Client=" + client);
//					//HttpURLConnection conn = (HttpURLConnection) url.openConnection();	
//					
//					conn.setRequestMethod("GET");	
//					conn.connect();	
//			         		
//					//int response = conn.getResponseCode();	
//					is = conn.getInputStream();	
//					
//					// Convert the InputStream into a string	
//				         	String contentAsString = convertStreamToString (is);	
//				         		
//					return contentAsString;	
//							        	
//				    } finally {	
//				        if (is != null) {	
//				            is.close();	
//				        } 	
//				    }
//	} 
	

}
