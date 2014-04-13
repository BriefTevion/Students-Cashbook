package crypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class CryptHelper {
	
	 private final static String HEX = "0123456789ABCDEF";
	 
	 /*
	  * Es folgen zunaechst die Methoden um zu verschüsseln
	  */
//				//Basis-Methode zum Verschluesseln eines Strings
//				 public static String encrypt(String s, String cleartext) throws Exception {
//			         byte[] rawKey = getRawKey(s.getBytes());
//			         byte[] result = encrypt(rawKey, cleartext.getBytes());
//			         return toHex(result);
//				 }
//				 
//				 //Methode um zufaeligen 128 Bit-Schluessel zu erhalten
//				 private static byte[] getRawKey(byte[] seed) throws Exception {
//			         KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			         SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//			         sr.setSeed(seed);
//				     kgen.init(128, sr);
//				     SecretKey skey = kgen.generateKey();
//				     byte[] raw = skey.getEncoded();
//				     return raw;
//				 }
//				 
//				 //Methode um zu AES-verschluesseln
//				 private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
//			         SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//			         Cipher cipher = Cipher.getInstance("AES");
//			         cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
//			         byte[] encrypted = cipher.doFinal(clear);
//			         return encrypted;
//			     }
//				 
//				 //Methode um String zu Hex zu konvertieren
				 public static String toHex(byte[] buf) {
			         if (buf == null)
			                 return "";
			         StringBuffer result = new StringBuffer(2*buf.length);
			         for (int i = 0; i < buf.length; i++) {
			                 appendHex(result, buf[i]);
			         } 
			         return result.toString();
			 }
//				 
//				 
				 private static void appendHex(StringBuffer sb, byte b) {
				         sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
				 }
//	 
//				 
//	/*
//	 * Es folgen die Methoden um zu entschluesseln
//	 */
//				 //Basis-Methode zum Entschuesseln
//				 public static String decrypt(String seed, String encrypted) throws Exception {
//		                byte[] rawKey = getRawKey(seed.getBytes());
//		                byte[] enc = toByte(encrypted);
//		                byte[] result = decrypt(rawKey, enc);
//		                return new String(result);
//		        }
//				 
//				 //Methode um AES-entschluesseln
//				 private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
//			         		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//			                Cipher cipher = Cipher.getInstance("AES");
//			            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//			            byte[] decrypted = cipher.doFinal(encrypted);
//			                return decrypted;
//
//			         
//				 }
//
//				 
				 //Methode um hexString in Byte Array zu konvertieren
				 public static byte[] toByte(String hexString) {
					 int len = hexString.length()/2;
		                byte[] result = new byte[len];
		                for (int i = 0; i < len; i++)
		                        result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
		                return result;
		        
				 }
				 
				 
		//Konvertieren eines Strings in einen Hex wert und umgekehrt		 
		 public static String toHex(String txt) {
                return toHex(txt.getBytes());
        }
        public static String fromHex(String hex) {
                return new String(toByte(hex));
        }

				 
}
