/*
 * Diese Klasse beinhaltet alle Methoden zur Bearbeitung der Datenbank-Tabellen.
 */
package db;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import db.TransaktionenContract.transEntry;

public class BudgetLoader {

	// neue einmalige Einnahme verbuchen
	// neue Transaktion der Tabelle TransaktionenList hinzufuegen
	public static void addEinmaligeTransaktion(Context context, Integer id,
			String anmerkung, String datum, String zeit, String kategorie,
			String betrag) {
		// Zugang zur Datenbank
		try {
			TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			ContentValues cv = new ContentValues();
			cv.put(transEntry.COLUMN_NAME_TRANSAKTION_ID, id);
			cv.put(transEntry.COLUMN_NAME_ANMEKRUNG, anmerkung);
			cv.put(transEntry.COLUMN_NAME_DATUM, datum);
			cv.put(transEntry.COLUMN_NAME_UHRZEIT, zeit);
			cv.put(transEntry.COLUMN_NAME_KATEGORIE, kategorie);
			cv.put(transEntry.COLUMN_NAME_BETRAG, betrag);

			long newRowID;
			newRowID = db.insert(transEntry.TABLE_NAME, null, cv);

			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Einmalige Einnahme der ausgesuchten Kategorie gutschreiben
	public static void addEinmaligeTransaktionToKategorie(Context context,
			String datum, String kName, String betrag) {

		// Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// GET aktuellen restbetrag der Kategorie
		String[] projection = { transEntry.K_COLUMN_NAME_BEZEICHNER,
				transEntry.K_COLUMN_NAME_BUDGET,
				transEntry.K_COLUMN_NAME_RESTBETRAG };

		String sortOrder = transEntry.K_COLUMN_NAME_BEZEICHNER;

		try {

			Cursor c = db.query(transEntry.TABLE_NAME_Kategorie, projection,
					transEntry.K_COLUMN_NAME_BEZEICHNER + "= '" + kName + "'",
					null, null, null, sortOrder);

			c.moveToFirst();
			String aktuellerRest = c.getString(2);
			Integer neuerBetrag = Integer.parseInt(aktuellerRest)
					+ Integer.parseInt(betrag);

			ContentValues cv = new ContentValues();
			cv.put(transEntry.K_COLUMN_NAME_RESTBETRAG,
					String.valueOf(neuerBetrag));
			cv.put(transEntry.K_COLUMN_NAME_LAST_UPDATED, datum);

			db.update(transEntry.TABLE_NAME_Kategorie, cv,
					transEntry.K_COLUMN_NAME_BEZEICHNER + "= '" + kName + "'",
					null);

		} catch (Exception e) {
			e.printStackTrace();
			// Nachricht ueber NICHT erfolgreiches speichern
			AlertDialog.Builder alert = new AlertDialog.Builder(context);
			alert.setMessage("Fehler");
			alert.setTitle("Einnahme konnte nicht gespeichert werde.");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
		}
		db.close();

	}

	// Neue transaktionID ausgeben
	public static Integer getNewTransaktionID(Context context) {
		try {
			// Zugang zur Datenbank
			TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			Integer id = 0;

			final String SQL_getMaxID = "Select Max("
					+ transEntry.COLUMN_NAME_TRANSAKTION_ID + ") AS id FROM "
					+ transEntry.TABLE_NAME;

			Cursor c = db.rawQuery(SQL_getMaxID, null);

			c.moveToFirst();
			id = c.getInt(0);

			db.close();

			return id + 1;

		} catch (Exception e) {
			e.printStackTrace();

			return 1;
		}
	}

	// neue monatliche Transaktion buchen
	// neue Transaktion der Tabelle Automatic hinzufuegen
	public static void addMonatlicheTransaktion(Context context, String name,
			String betrag, String datum, String tag) {
		// Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(transEntry.M_COLUMN_NAME_BEZEICHNER, name);
		cv.put(transEntry.M_COLUMN_NAME_BETRAG, betrag);
		cv.put(transEntry.M_COLUMN_NAME_DATUM, datum);
		cv.put(transEntry.M_COLUMN_NAME_TAG, tag);

		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME_AUTOMATIC, null, cv);
	}

	// neues Sparziel anlegen
	public static void addSparziel(Context context, String name, String betrag,
			String datum, String sparbetrag) {
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(transEntry.T_COLUMN_NAME_BEZEICHNER, name);
		cv.put(transEntry.T_COLUMN_NAME_BETRAG, betrag);
		cv.put(transEntry.T_COLUMN_NAME_DATUM, datum);
		cv.put(transEntry.T_COLUMN_NAME_SPARBETRAG, sparbetrag);
		cv.put(transEntry.T_COLUMN_NAME_GUTHABEN, "0");

		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME_TARGET, null, cv);
	}

	// Sparbetrag eines Sparzieles erfragen
	public static Integer getSparbetragOfTarget(Context context,
			String sparzielTitel) {
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		// GET aktuellen restbetrag der Kategorie
		String[] projection = { transEntry.T_COLUMN_NAME_BEZEICHNER,
				transEntry.T_COLUMN_NAME_SPARBETRAG };

		String sortOrder = transEntry.T_COLUMN_NAME_BEZEICHNER;

		Cursor c = db.query(transEntry.TABLE_NAME_TARGET, projection,
				transEntry.T_COLUMN_NAME_BEZEICHNER + "= '" + sparzielTitel
						+ "'", null, null, null, sortOrder);

		c.moveToFirst();
		Integer sparbetrag = Integer.parseInt(c.getString(1));

		return sparbetrag;
	}

	// Datum des Sparzieles abfragen
	public static Cursor getDetailsOfSparziele(Context context) {
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		// GET aktuellen restbetrag der Kategorie
		String[] projection = { transEntry.T_COLUMN_NAME_BEZEICHNER,
				transEntry.T_COLUMN_NAME_DATUM,
				transEntry.T_COLUMN_NAME_SPARBETRAG };

		String sortOrder = transEntry.T_COLUMN_NAME_BEZEICHNER;

		Cursor c = db.query(transEntry.TABLE_NAME_TARGET, projection, null,
				null, null, null, sortOrder);
		db.close();
		return c;

	}

	// Details zu den Monatstransaktionen
	public static Cursor getDetailsOfMonthlyTransaktionen(Context context) {
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		// GET aktuellen restbetrag der Kategorie
		String[] projection = { transEntry.COLUMN_NAME_KATEGORIE,
				transEntry.COLUMN_NAME_DATUM, transEntry.COLUMN_NAME_BETRAG };

		String sortOrder = transEntry.COLUMN_NAME_KATEGORIE;

		Cursor c = db.query(transEntry.TABLE_NAME, projection, null, null,
				null, null, sortOrder);
		db.close();
		return c;

	}

	// neue Kategorie anlegen
	public static void addKategorie(Context context, String name,
			String budget, String restbetrag, String datum) {
		// Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(transEntry.K_COLUMN_NAME_BEZEICHNER, name);
		cv.put(transEntry.K_COLUMN_NAME_BUDGET, budget);
		cv.put(transEntry.K_COLUMN_NAME_RESTBETRAG, restbetrag);
		cv.put(transEntry.K_COLUMN_NAME_LAST_UPDATED, datum);

		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME_Kategorie, null, cv);

		db.close();
	}

	// Zusammenrechnen aller Transkationen
	public static Integer getSumOfTransaktionen(Context context) {
		Integer sum = 0;

		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		// GET aktuellen restbetrag der Kategorie
		String[] projection = { transEntry.COLUMN_NAME_TRANSAKTION_ID,
				transEntry.COLUMN_NAME_BETRAG };

		String sortOrder = transEntry.COLUMN_NAME_TRANSAKTION_ID;

		try {

			Cursor c = db.query(transEntry.TABLE_NAME, projection, null, null,
					null, null, sortOrder);

			while (c.moveToNext()) {
				sum = sum + Integer.parseInt(c.getString(1));

			}

		} catch (Exception e) {

		}

		db.close();
		return sum;

	}

	// Datum der letzten Transaktion erhalten
	public static String getDateOfLastTransaktion(Context context) {
		// Daten abfragen
		// Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String[] projection = { transEntry.COLUMN_NAME_TRANSAKTION_ID,
				transEntry.COLUMN_NAME_DATUM };

		String sortOrder = transEntry.COLUMN_NAME_TRANSAKTION_ID + " DESC";

		Cursor c = db.query(transEntry.TABLE_NAME, projection, null, null,
				null, null, sortOrder, "1");

		c.moveToFirst();
		String datum = c.getString(1);

		db.close();

		return datum;
	}

	// pruefen ob Ausgabe im Budget liegt
	public static boolean checkIfKategorieEnoughMoney(Context context,
			String kategorieTitel, Integer betrag) {

		// Aktuelles Restbudget abfragen
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String[] projection = { transEntry.K_COLUMN_NAME_BEZEICHNER,
				transEntry.K_COLUMN_NAME_RESTBETRAG };

		Cursor c = db.query(transEntry.TABLE_NAME_Kategorie, projection,
				transEntry.K_COLUMN_NAME_BEZEICHNER + "= '" + kategorieTitel
						+ "'", null, null, null, null);

		c.moveToFirst();
		Integer restBetrag = Integer.parseInt(c.getString(1));

		// Wenn ein des Restbudget mindestens so hoch ist wie der betrag
		if (restBetrag > betrag) {
			return true;
		} else {
			return false;
		}

	}

	// Details der Katgorie ausgeben
	public static Cursor getDetailsOfKategorien(Context context) {
		// Aktuelles Restbudget abfragen
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String[] projection = { transEntry.K_COLUMN_NAME_BEZEICHNER,
				transEntry.K_COLUMN_NAME_RESTBETRAG,
				transEntry.K_COLUMN_NAME_BUDGET };
		String sortOrder = transEntry.K_COLUMN_NAME_BEZEICHNER;

		Cursor c = db.query(transEntry.TABLE_NAME_Kategorie, projection, null,
				null, null, null, sortOrder);
		db.close();
		return c;
	}

	/*
	 * Folgende Methoden werden ausschliesslich fuer den AutoLoader verwendet
	 */

	// Neue transaktionID ausgeben
	public static Integer getNewTransaktionID(Context context, SQLiteDatabase db) {
		try {

			Integer id = 0;

			final String SQL_getMaxID = "Select Max("
					+ transEntry.COLUMN_NAME_TRANSAKTION_ID + ") AS id FROM "
					+ transEntry.TABLE_NAME;

			Cursor c = db.rawQuery(SQL_getMaxID, null);

			c.moveToFirst();
			id = c.getInt(0);

			return id + 1;

		} catch (Exception e) {
			e.printStackTrace();

			return 1;
		}
	}

	// neue Transaktion der Tabelle TransaktionenList hinzufuegen
	public static void addEinmaligeTransaktion(SQLiteDatabase db,
			Context context, Integer id, String anmerkung, String datum,
			String zeit, String kategorie, String betrag) {
		// Zugang zur Datenbank
		try {

			ContentValues cv = new ContentValues();
			cv.put(transEntry.COLUMN_NAME_TRANSAKTION_ID, id);
			cv.put(transEntry.COLUMN_NAME_ANMEKRUNG, anmerkung);
			cv.put(transEntry.COLUMN_NAME_DATUM, datum);
			cv.put(transEntry.COLUMN_NAME_UHRZEIT, zeit);
			cv.put(transEntry.COLUMN_NAME_KATEGORIE, kategorie);
			cv.put(transEntry.COLUMN_NAME_BETRAG, betrag);

			long newRowID;
			newRowID = db.insert(transEntry.TABLE_NAME, null, cv);

			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Sparziel Betrag gutschreiben
	public static void addCreditToSparziel(SQLiteDatabase dbWrite,
			SQLiteDatabase dbRead, Context context, Integer betrag,
			String sparzielTitel) {
		Integer credit = betrag;
		Integer neuesGuthaben = credit
				+ getCurrentCreditOfTarget(dbRead, context, sparzielTitel);

		ContentValues cv = new ContentValues();
		cv.put(transEntry.T_COLUMN_NAME_GUTHABEN, String.valueOf(neuesGuthaben));

		dbWrite.update(transEntry.TABLE_NAME_TARGET, cv,
				transEntry.T_COLUMN_NAME_BEZEICHNER + "= '" + sparzielTitel
						+ "'", null);

	}

	// Aktuelles Guthaben eines Sparzieles abfragen, bei der Annahme, dass es
	// keine gleichnamigen Ziele gibt
	private static Integer getCurrentCreditOfTarget(SQLiteDatabase db,
			Context context, String sparzielTitel) {

		// GET aktuellen restbetrag der Kategorie
		String[] projection = { transEntry.T_COLUMN_NAME_BEZEICHNER,
				transEntry.T_COLUMN_NAME_GUTHABEN };

		String sortOrder = transEntry.T_COLUMN_NAME_BEZEICHNER;

		Cursor c = db.query(transEntry.TABLE_NAME_TARGET, projection,
				transEntry.T_COLUMN_NAME_BEZEICHNER + "= '" + sparzielTitel
						+ "'", null, null, null, sortOrder);

		c.moveToFirst();
		Integer aktuellesGuthaben = Integer.parseInt(c.getString(1));

		return aktuellesGuthaben;
	}

	// Get Datum des Sparzieles
	public static Cursor getDetailsOfSparziele(SQLiteDatabase db,
			Context context) {

		// GET aktuellen restbetrag der Kategorie
		String[] projection = { transEntry.T_COLUMN_NAME_BEZEICHNER,
				transEntry.T_COLUMN_NAME_DATUM,
				transEntry.T_COLUMN_NAME_SPARBETRAG };

		String sortOrder = transEntry.T_COLUMN_NAME_BEZEICHNER;

		Cursor c = db.query(transEntry.TABLE_NAME_TARGET, projection, null,
				null, null, null, sortOrder);

		return c;

	}

	// Details der Katgorie ausgeben
	public static Cursor getDetailsOfKategorien(SQLiteDatabase db,
			Context context) {

		String[] projection = { transEntry.K_COLUMN_NAME_BEZEICHNER,
				transEntry.K_COLUMN_NAME_RESTBETRAG,
				transEntry.K_COLUMN_NAME_BUDGET };
		String sortOrder = transEntry.K_COLUMN_NAME_BEZEICHNER;

		Cursor c = db.query(transEntry.TABLE_NAME_Kategorie, projection, null,
				null, null, null, sortOrder);
		return c;
	}

	// Reset der restbudgets der Kategorien
	public static void resetRestbudgetsKategorien(SQLiteDatabase db,
			Context context, String name, String betrag) {

		String sql = "update " + transEntry.TABLE_NAME_Kategorie
				+ " set restbetrag='" + betrag + "' where name='" + name + "'";
		try {
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Details zu den Monatstransaktionen
	public static Cursor getDetailsOfMonthlyTransaktionen(SQLiteDatabase db,
			Context context) {

		// GET aktuellen restbetrag der Kategorie
		String[] projection = { transEntry.M_COLUMN_NAME_BEZEICHNER,
				transEntry.M_COLUMN_NAME_TAG, transEntry.M_COLUMN_NAME_BETRAG };

		String sortOrder = transEntry.M_COLUMN_NAME_BEZEICHNER;

		Cursor c = db.query(transEntry.TABLE_NAME_AUTOMATIC, projection, null,
				null, null, null, sortOrder);

		return c;

	}
}
