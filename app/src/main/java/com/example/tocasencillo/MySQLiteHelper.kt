package com.example.tocasencillo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySQLiteHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE, null, 6
) {

    companion object {
        const val DATABASE = "cancionero.db"
        const val SONG_TABLE = "cancion"
        const val TITLE_TABLE = "titulo"
        const val CONTENT_TABLE = "contenido"
        const val NOTE_TABLE = "nota"
        const val LABEL_TABLE = "etiqueta"
        const val SONG_FRAGMENT_TABLE = "cancion_fragmento"
        const val REPEAT_TABLE = "repeticion"
        const val BOX_REPEAT_TABLE = "caja_repeticion"
        const val ALTERNATE_ENDING_TABLE = "final_alternativo"
        const val ID = "_id"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        //defining our DB
        val createSongCommand =
            """CREATE TABLE $SONG_TABLE
                ($ID INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT)"""

        val createTitleCommand =
            """CREATE TABLE $TITLE_TABLE
                ($ID INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT, velocidad INTEGER, tonalidad TEXT)"""

        val createContentCommand =
            """CREATE TABLE $CONTENT_TABLE
                ($ID INTEGER PRIMARY KEY AUTOINCREMENT,
                ccBar1 TEXT, ccBar5 TEXT, 
                ccMusica1 TEXT,ccMusica2 TEXT, ccMusica3 TEXT, ccMusica4 TEXT )"""

        val createNoteCommand =
            """CREATE TABLE $NOTE_TABLE
                ($ID INTEGER PRIMARY KEY AUTOINCREMENT,
                texto TEXT)"""

        val createTagCommand =
            """CREATE TABLE $LABEL_TABLE
                ($ID INTEGER PRIMARY KEY AUTOINCREMENT,
                tipo TEXT)"""

        val createSongFragmentCommand =
            """CREATE TABLE $SONG_FRAGMENT_TABLE
                (_id_cancion INTEGER,
                _id_fragmento INTEGER,
                tipo TEXT,
                posicion INTEGER,
                FOREIGN KEY(_id_cancion) REFERENCES cancion(_id))"""

        val createRepeatCommand =
            """CREATE TABLE $REPEAT_TABLE
                ($ID INTEGER PRIMARY KEY AUTOINCREMENT,
                veces TEXT)"""

        val createBoxRepeatCommand =
            """CREATE TABLE $BOX_REPEAT_TABLE
                ($ID INTEGER PRIMARY KEY AUTOINCREMENT,
                texto TEXT)"""

        val createAlternateEndingCommand =
            """CREATE TABLE $ALTERNATE_ENDING_TABLE 
                ($ID INTEGER PRIMARY KEY AUTOINCREMENT,
                ccBar2 TEXT, 
                ccMusica1 TEXT,ccMusica2 TEXT)"""

        //exec create tables
        with(db) {
            this!!.execSQL(createSongCommand)
            this.execSQL(createTitleCommand)
            this.execSQL(createContentCommand)
            this.execSQL(createNoteCommand)
            this.execSQL(createTagCommand)
            this.execSQL(createSongFragmentCommand)
            this.execSQL(createRepeatCommand)
            this.execSQL(createBoxRepeatCommand)
            this.execSQL(createAlternateEndingCommand)
        }

        val tags = arrayOf(
            "Intro", "Estrofa", "PreStrb", "Strb",
            "Puente", "Solo", "Final"
        )
        for (tag in tags) {
            val insertTags = """INSERT INTO $LABEL_TABLE (tipo) 
                VALUES ('$tag')"""
            with(db) {
                this!!.execSQL(insertTags)
            }
        }

        val reps = arrayOf("x3", "x4")
        for (rep in reps) {
            val insertReps = """INSERT INTO $REPEAT_TABLE (veces) 
                VALUES ('$rep')"""
            with(db) {
                this!!.execSQL(insertReps)
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableSong = "DROP TABLE IF EXISTS $SONG_TABLE"
        db!!.execSQL(dropTableSong)
        val dropTableTitle = "DROP TABLE IF EXISTS $TITLE_TABLE"
        db.execSQL(dropTableTitle)
        val dropTableContent = "DROP TABLE IF EXISTS $CONTENT_TABLE"
        db.execSQL(dropTableContent)
        val dropTableNote = "DROP TABLE IF EXISTS $NOTE_TABLE"
        db.execSQL(dropTableNote)
        val dropTableLabel = "DROP TABLE IF EXISTS $LABEL_TABLE"
        db.execSQL(dropTableLabel)
        val dropTableSongFragment = "DROP TABLE IF EXISTS $SONG_FRAGMENT_TABLE"
        db.execSQL(dropTableSongFragment)
        val dropTableRepeat = "DROP TABLE IF EXISTS $REPEAT_TABLE"
        db.execSQL(dropTableRepeat)
        val dropTableBoxRepeat = "DROP TABLE IF EXISTS $BOX_REPEAT_TABLE"
        db.execSQL(dropTableBoxRepeat)
        val dropAlternateEndingRepeat = "DROP TABLE IF EXISTS $ALTERNATE_ENDING_TABLE"
        db.execSQL(dropAlternateEndingRepeat)
        onCreate(db)
    }

    fun saveSong(song: String) {
        val songName = ContentValues().apply {
            put("nombre", song)
        }
        val db = this.writableDatabase
        db.insert(SONG_TABLE, null, songName)
    }

    fun saveContent(
        ccBar1: String,
        ccBar5: String,
        txtCC1: String,
        txtCC2: String,
        txtCC3: String,
        txtCC4: String,
    ) {
        val data = ContentValues().apply {
            put("ccBar1", ccBar1)
            put("ccBar5", ccBar5)
            put("ccMusica1", txtCC1)
            put("ccMusica2", txtCC2)
            put("ccMusica3", txtCC3)
            put("ccMusica4", txtCC4)
        }

        val db = this.writableDatabase
        db.insert(CONTENT_TABLE, null, data)
    }

    fun saveAlternateEnd(ccBar2: String, txtCC1: String, txtCC2: String) {
        val data = ContentValues().apply {
            put("ccBar2", ccBar2)
            put("ccMusica1", txtCC1)
            put("ccMusica2", txtCC2)
        }

        val db = this.writableDatabase
        db.insert(ALTERNATE_ENDING_TABLE, null, data)
    }

    fun saveTitle(title: String, tempo: String, key: String) {
        val data = ContentValues().apply {
            put("nombre", title)
            put("velocidad", tempo)
            put("tonalidad", key)

        }

        val db = this.writableDatabase
        db.insert(TITLE_TABLE, null, data)
    }

    fun saveNote(note: String) {
        val data = ContentValues().apply {
            put("texto", note)
        }

        val db = this.writableDatabase
        db.insert(NOTE_TABLE, null, data)
    }

    fun saveBoxRepeat(boxText: String) {
        val data = ContentValues().apply {
            put("texto", boxText)
        }

        val db = this.writableDatabase
        db.insert(BOX_REPEAT_TABLE, null, data)
    }

    fun saveSongFragment(song: Int, fragment: Int, type: String, posic: Int) {
        val data = ContentValues().apply {
            put("_id_cancion", song)
            put("_id_fragmento", fragment)
            put("tipo", type)
            put("posicion", posic)

        }

        val db = this.writableDatabase
        db.insert(SONG_FRAGMENT_TABLE, null, data)
    }


    @SuppressLint("Recycle")
    fun lastSong(): Int {
        val database = this.readableDatabase
        val cursor =
            database.rawQuery("SELECT MAX(_id) FROM $SONG_FRAGMENT_TABLE", null).apply {
                moveToFirst()
            }
        return cursor.getInt(0)
    }

    @SuppressLint("Recycle")
    fun lastFragment(type: String): Int {
        val database = this.readableDatabase
        val cursor = database.rawQuery("SELECT MAX(_id) FROM $type", null).apply {
            moveToFirst()
        }
        return cursor.getInt(0)
    }
}