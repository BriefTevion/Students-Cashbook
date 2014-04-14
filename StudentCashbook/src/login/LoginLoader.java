/*
 * Diese Klasse binhaltet alle Methoden zur Bearbeitugn des key-value-stores
 */
package login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginLoader {

	public static boolean setPassword(Context context, String pswEncrypt){
		//Holen der Preferences
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = spref.edit();
		try{
			//verschluesselter Wert speichern;
			editor.putString("PASSWORD", pswEncrypt);
			editor.commit();
			
			return true;
		}
		catch(Exception e){
			return false;
		}

	}
	
	
	public static boolean setPasswordAndSecurefrage(Context context, String pswEncrypt, String secureEingabeEncrypt){
		//Holen der Preferences
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = spref.edit();
		try{
			//Wert speichern;
			editor.putString("PASSWORD", pswEncrypt);
			editor.putString("SECUREFRAGE", secureEingabeEncrypt);
			editor.commit();
			
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	//Antwort der Sicherheitsfrage ausgeben
	public static String getSecureInput(Context context){
		//Holen der Preferences
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context);
		
		//Passwort auslesen & keine Entschluesselung noetig
		String secureInput = spref.getString("SECUREFRAGE", "");
		
		return secureInput;
	}
	
	//Passwort aus key-value-store ausgeben
	public static String getPassword(Context context){
		//Holen der Preferences
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context);
		
		//Passwort auslesen & keine Entschluesselung noetig
		String password = spref.getString("PASSWORD", "");
		
		return password;
	}
}
