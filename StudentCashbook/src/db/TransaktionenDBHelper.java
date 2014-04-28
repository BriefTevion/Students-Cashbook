/*
 * Diese Klasse erstellt bei Installation die Tabellen.
 * Bei Bedarf stellt sie auch Methoden zum upgraden oder loeschen der Datenbank.
 */
package db;

import db.TransaktionenContract.transEntry;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransaktionenDBHelper extends SQLiteOpenHelper {

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Transaktionen.db";

	// SQL Statement um Tabelle transaktionenTable zu erstellen
	private static final String SQL_CreateTransaktionenTable = "CREATE TABLE "
			+ transEntry.TABLE_NAME + " ("
			+ transEntry.COLUMN_NAME_TRANSAKTION_ID + " INTEGER PRIMARY KEY,"
			+ transEntry.COLUMN_NAME_ANMEKRUNG + TEXT_TYPE + COMMA_SEP
			+ transEntry.COLUMN_NAME_DATUM + TEXT_TYPE + COMMA_SEP
			+ transEntry.COLUMN_NAME_UHRZEIT + TEXT_TYPE + COMMA_SEP
			+ transEntry.COLUMN_NAME_KATEGORIE + TEXT_TYPE + COMMA_SEP
			+ transEntry.COLUMN_NAME_BETRAG + TEXT_TYPE + " )";

	// SQL Statement um Tabelle kategorienTable zu erstellen
	private static final String SQL_CreateKategorienTable = "CREATE TABLE "
			+ transEntry.TABLE_NAME_Kategorie + " ("
			+ transEntry.K_COLUMN_NAME_BEZEICHNER + " TEXT PRIMARY KEY,"
			+ transEntry.K_COLUMN_NAME_BUDGET + TEXT_TYPE + COMMA_SEP
			+ transEntry.K_COLUMN_NAME_RESTBETRAG + TEXT_TYPE + COMMA_SEP
			+ transEntry.K_COLUMN_NAME_LAST_UPDATED + TEXT_TYPE + " )";

	// SQL Statement um Tabelle monatlicheEinnahmenTable zu erstellen
	private static final String SQL_CreateMonatlicheTable = "CREATE TABLE "
			+ transEntry.TABLE_NAME_AUTOMATIC + " ("
			+ transEntry.M_COLUMN_NAME_BEZEICHNER + " TEXT PRIMARY KEY,"
			+ transEntry.M_COLUMN_NAME_DATUM + TEXT_TYPE + COMMA_SEP
			+ transEntry.M_COLUMN_NAME_BETRAG + TEXT_TYPE + COMMA_SEP
			+ transEntry.M_COLUMN_NAME_TAG + TEXT_TYPE + " )";

	// SQL Statement um Tabelle sparzieleTable zu erstellen
	private static final String SQL_CreateSparzieleTable = "CREATE TABLE "
			+ transEntry.TABLE_NAME_TARGET + " ("
			+ transEntry.T_COLUMN_NAME_BEZEICHNER + " TEXT PRIMARY KEY,"
			+ transEntry.T_COLUMN_NAME_DATUM + TEXT_TYPE + COMMA_SEP
			+ transEntry.T_COLUMN_NAME_BETRAG + TEXT_TYPE + COMMA_SEP
			+ transEntry.T_COLUMN_NAME_SPARBETRAG + TEXT_TYPE + COMMA_SEP
			+ transEntry.T_COLUMN_NAME_GUTHABEN + TEXT_TYPE + " )";

	// Delete SQL-Statements
	private static final String SQL_DELETE_ENTRIES_transaktionenTable = "DROP TABLE IF EXISTS "
			+ transEntry.TABLE_NAME;
	private static final String SQL_DELETE_ENTRIES_kategorienTable = "DROP TABLE IF EXISTS "
			+ transEntry.TABLE_NAME_Kategorie;
	private static final String SQL_DELETE_ENTRIES_monatlicheTable = "DROP TABLE IF EXISTS "
			+ transEntry.TABLE_NAME_AUTOMATIC;
	private static final String SQL_DELETE_ENTRIES_sparzielTable = "DROP TABLE IF EXISTS "
			+ transEntry.TABLE_NAME_TARGET;

	public TransaktionenDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(SQL_CreateTransaktionenTable);
			db.execSQL(SQL_CreateKategorienTable);
			db.execSQL(SQL_CreateMonatlicheTable);
			db.execSQL(SQL_CreateSparzieleTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES_transaktionenTable);
		db.execSQL(SQL_DELETE_ENTRIES_kategorienTable);
		db.execSQL(SQL_DELETE_ENTRIES_monatlicheTable);
		db.execSQL(SQL_DELETE_ENTRIES_sparzielTable);

		onCreate(db);

	}

}
