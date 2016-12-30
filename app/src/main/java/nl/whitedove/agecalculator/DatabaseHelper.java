package nl.whitedove.agecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.joda.time.DateTime;

import java.util.ArrayList;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "agecalculator";

    private static final String TAB_PERSOON = "Persoon";
    private static final String PSN_ID = "Id";
    private static final String PSN_NAAM = "Naam";
    private static final String PSN_GEBDATUM = "GebDatum";
    private static final String PSN_GESELECTEERD = "Geselecteerd";
    private static final String PSN_GETOOND = "Getoond";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TAB_PERSOON + "("
                + PSN_ID + " INTEGER PRIMARY KEY NOT NULL,"
                + PSN_NAAM + " TEXT NOT NULL,"
                + PSN_GEBDATUM + " INTEGER NOT NULL,"
                + PSN_GESELECTEERD + " INTEGER NOT NULL,"
                + PSN_GETOOND + " INTEGER NOT NULL"
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void UpdatePersoon(Persoon persoon) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("BEGIN TRANSACTION");

        String selectQuery = "SELECT "
                + PSN_ID
                + " FROM " + TAB_PERSOON
                + " WHERE " + PSN_ID + " = ?"
                + " LIMIT 1";

        Cursor cursor;
        cursor = db.rawQuery(selectQuery, new String[]{Integer.toString(persoon.getId())});

        if (cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(PSN_NAAM, persoon.getNaam());
            values.put(PSN_GEBDATUM, persoon.getGebdatum().getMillis());
            values.put(PSN_GESELECTEERD, persoon.getGeselecteerd());
            values.put(PSN_GETOOND, persoon.getGetoond());
            db.update(TAB_PERSOON, values, PSN_ID + " = ?", new String[]{Integer.toString(persoon.getId())});
        } else {
            ContentValues values = new ContentValues();
            values.put(PSN_NAAM, persoon.getNaam());
            values.put(PSN_GEBDATUM, persoon.getGebdatum().getMillis());
            values.put(PSN_GESELECTEERD, persoon.getGeselecteerd() ? 1 : 0);
            values.put(PSN_GETOOND, persoon.getGetoond());
            db.insert(TAB_PERSOON, null, values);
        }
        db.execSQL("END TRANSACTION");
        cursor.close();
    }

    ArrayList<Persoon> GetPersonen() {

        ArrayList<Persoon> personen = new ArrayList<>();

        String selectQuery = "SELECT "
                + PSN_ID + ","
                + PSN_NAAM + ","
                + PSN_GEBDATUM + ","
                + PSN_GESELECTEERD + ","
                + PSN_GETOOND
                + " FROM " + TAB_PERSOON
                + " ORDER BY " + PSN_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Persoon persoon = new Persoon();
                persoon.setId(cursor.getInt(0));
                persoon.setNaam(cursor.getString(1));
                persoon.setGebdatum(new DateTime(cursor.getLong(2)));
                persoon.setGeselecteerd(cursor.getInt(3) == 1);
                persoon.setGetoond(cursor.getInt(4) == 1);
                personen.add(persoon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return personen;
    }
}