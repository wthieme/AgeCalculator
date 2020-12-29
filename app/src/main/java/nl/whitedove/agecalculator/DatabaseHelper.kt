package nl.whitedove.agecalculator

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.joda.time.DateTime
import java.util.*

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val sql = ("CREATE TABLE " + TAB_PERSOON + "("
                + PSN_ID + " INTEGER PRIMARY KEY NOT NULL,"
                + PSN_NAAM + " TEXT NOT NULL,"
                + PSN_GEBDATUM + " INTEGER NOT NULL,"
                + PSN_GESELECTEERD + " INTEGER NOT NULL,"
                + PSN_GETOOND + " INTEGER NOT NULL,"
                + PSN_GEVINKT + " INTEGER NOT NULL"
                + ")")
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun updatePersoon(persoon: Persoon) {
        val db = this.writableDatabase
        db.execSQL("BEGIN TRANSACTION")
        val selectQuery = ("SELECT "
                + PSN_ID
                + " FROM " + TAB_PERSOON
                + " WHERE " + PSN_ID + " = ?"
                + " LIMIT 1")
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, arrayOf<String?>(persoon.getId().toString()))
        if (cursor.moveToFirst()) {
            val values = ContentValues()
            values.put(PSN_NAAM, persoon.getNaam())
            values.put(PSN_GEBDATUM, persoon.getGebdatum()!!.millis)
            values.put(PSN_GESELECTEERD, if (persoon.getGeselecteerd() == true) 1 else 0)
            values.put(PSN_GETOOND, if (persoon.getGetoond() == true) 1 else 0)
            values.put(PSN_GEVINKT, if (persoon.getGevinkt() == true) 1 else 0)
            db.update(TAB_PERSOON, values, "$PSN_ID = ?", arrayOf<String?>(persoon.getId().toString()))
        } else {
            val values = ContentValues()
            values.put(PSN_NAAM, persoon.getNaam())
            values.put(PSN_GEBDATUM, persoon.getGebdatum()!!.millis)
            values.put(PSN_GESELECTEERD, if (persoon.getGeselecteerd() == true) 1 else 0)
            values.put(PSN_GETOOND, if (persoon.getGetoond() == true) 1 else 0)
            values.put(PSN_GEVINKT, if (persoon.getGevinkt() == true) 1 else 0)
            db.insert(TAB_PERSOON, null, values)
        }
        db.execSQL("END TRANSACTION")
        cursor.close()
    }

    fun getAllePersonen(): ArrayList<Persoon> {
        val personen = ArrayList<Persoon>()
        val selectQuery = ("SELECT "
                + PSN_ID + ","
                + PSN_NAAM + ","
                + PSN_GEBDATUM + ","
                + PSN_GESELECTEERD + ","
                + PSN_GETOOND + ","
                + PSN_GEVINKT
                + " FROM " + TAB_PERSOON
                + " ORDER BY " + PSN_ID)
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val persoon = Persoon()
                persoon.setId(cursor.getInt(0))
                persoon.setNaam(cursor.getString(1))
                persoon.setGebdatum(DateTime(cursor.getLong(2)))
                persoon.setGeselecteerd(cursor.getInt(3) == 1)
                persoon.setGetoond(cursor.getInt(4) == 1)
                persoon.setGevinkt(cursor.getInt(5) == 1)
                personen.add(persoon)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return personen
    }

    fun getPersoon(id: Int): Persoon {
        val selectQuery = ("SELECT "
                + PSN_ID + ","
                + PSN_NAAM + ","
                + PSN_GEBDATUM + ","
                + PSN_GESELECTEERD + ","
                + PSN_GETOOND + ","
                + PSN_GEVINKT
                + " FROM " + TAB_PERSOON
                + " WHERE " + PSN_ID + " = ?"
                + " ORDER BY " + PSN_ID)
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, arrayOf<String?>(id.toString()))
        val persoon = Persoon()
        if (cursor.moveToFirst()) {
            do {
                persoon.setId(cursor.getInt(0))
                persoon.setNaam(cursor.getString(1))
                persoon.setGebdatum(DateTime(cursor.getLong(2)))
                persoon.setGeselecteerd(cursor.getInt(3) == 1)
                persoon.setGetoond(cursor.getInt(4) == 1)
                persoon.setGevinkt(cursor.getInt(5) == 1)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return persoon
    }

    fun getAangevinktePersonen(): ArrayList<Persoon> {
        val personen = ArrayList<Persoon>()
        val selectQuery = ("SELECT "
                + PSN_ID + ","
                + PSN_NAAM + ","
                + PSN_GEBDATUM + ","
                + PSN_GESELECTEERD + ","
                + PSN_GETOOND + ","
                + PSN_GEVINKT
                + " FROM " + TAB_PERSOON
                + " WHERE " + PSN_GETOOND + " = ?"
                + " AND " + PSN_GEVINKT + " = ?"
                + " ORDER BY " + PSN_ID)
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, arrayOf<String?>("1", "1"))
        if (cursor.moveToFirst()) {
            do {
                val persoon = Persoon()
                persoon.setId(cursor.getInt(0))
                persoon.setNaam(cursor.getString(1))
                persoon.setGebdatum(DateTime(cursor.getLong(2)))
                persoon.setGeselecteerd(cursor.getInt(3) == 1)
                persoon.setGetoond(cursor.getInt(4) == 1)
                persoon.setGevinkt(cursor.getInt(5) == 1)
                personen.add(persoon)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return personen
    }

    fun getAantalPersonen(): Long {
        val db = this.readableDatabase
        return DatabaseUtils.queryNumEntries(db, TAB_PERSOON)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME: String = "agecalculator"
        private const val TAB_PERSOON: String = "Persoon"
        private const val PSN_ID: String = "Id"
        private const val PSN_NAAM: String = "Naam"
        private const val PSN_GEBDATUM: String = "GebDatum"
        private const val PSN_GESELECTEERD: String = "Geselecteerd"
        private const val PSN_GETOOND: String = "Getoond"
        private const val PSN_GEVINKT: String = "Gevinkt"
        private var sInstance: DatabaseHelper? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseHelper {
            if (sInstance == null) {
                sInstance = DatabaseHelper(context.applicationContext)
            }
            return sInstance as DatabaseHelper
        }
    }
}