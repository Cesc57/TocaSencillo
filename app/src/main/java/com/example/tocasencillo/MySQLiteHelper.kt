package com.example.tocasencillo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySQLiteHelper(context: Context) : SQLiteOpenHelper(
    context, "song_book.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {

        //defining our DB
        val createSongCommand =
            """CREATE TABLE cancion
                (id_cancion INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT)"""

        val createTitleCommand =
            """CREATE TABLE titulo 
                (id_titulo INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT, velocidad INTEGER, tonalidad TEXT)"""

        val createContentCommand =
            """CREATE TABLE contenido 
                (id_contenido INTEGER PRIMARY KEY AUTOINCREMENT,
                ccBar1 TEXT, ccBar5 TEXT, 
                ccMusica1 TEXT,ccMusica2 TEXT, ccMusica3 TEXT, ccMusica4 TEXT )"""

        val createNoteCommand =
            """CREATE TABLE nota
                (id_nota INTEGER PRIMARY KEY AUTOINCREMENT,
                texto TEXT)"""

        val createTagCommand =
            """CREATE TABLE etiqueta
                (id_etiqueta INTEGER PRIMARY KEY AUTOINCREMENT,
                tipo TEXT)"""

        val createSongFragmentCommand =
            """CREATE TABLE cancion_fragmento 
                (id_cancion INTEGER,
                id_fragmento INTEGER,
                tipo TEXT,
                posicion INTEGER,
                FOREIGN KEY(id_cancion) REFERENCES cancion(id))"""


        //exec create tables
        with(db) {
            this!!.execSQL(createSongCommand)
            this.execSQL(createTitleCommand)
            this.execSQL(createContentCommand)
            this.execSQL(createNoteCommand)
            this.execSQL(createTagCommand)
            this.execSQL(createSongFragmentCommand)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS song_book"
        db!!.execSQL(dropTable)
        onCreate(db)
    }

    /*fun showSongs(): Cursor? {
        val db: SQLiteDatabase = this.readableDatabase
        return db.rawQuery("""SELECT * FROM cancion""", null)
    }*/

}