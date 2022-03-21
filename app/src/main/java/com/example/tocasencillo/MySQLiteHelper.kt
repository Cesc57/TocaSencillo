package com.example.tocasencillo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySQLiteHelper(context: Context) : SQLiteOpenHelper(
    context, "song_book.db", null, 23) {

    override fun onCreate(db: SQLiteDatabase?) {

        //defining our DB
        val createSongCommand =
            """CREATE TABLE cancion
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT)"""

        val createTitleCommand =
            """CREATE TABLE titulo 
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT, velocidad INTEGER, tonalidad TEXT)"""

        val createContentCommand =
            """CREATE TABLE contenido 
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                ccBar1 TEXT, ccBar5 TEXT, 
                ccMusica1 TEXT,ccMusica2 TEXT, ccMusica3 TEXT, ccMusica4 TEXT )"""

        val createNoteCommand =
            """CREATE TABLE nota
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                texto TEXT)"""

        val createTagCommand =
            """CREATE TABLE etiqueta
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
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

        val tags = arrayOf("Intro", "Estrofa", "PreStrb", "Strb",
            "Puente", "Solo", "Final")

        for (tag in tags) {
            val myTag = tag
            val insertTags = """INSERT INTO etiqueta (tipo) 
                VALUES ('$myTag')"""
            with(db) {
                this!!.execSQL(insertTags)
            }
        }

    }

    /*private fun saveTags(tag: String) {
        val data = ContentValues().apply {
            put("tag", tag)
        }

        //Abrir BDD en modo escritura:
        val myDB = this.writableDatabase
        myDB.insert("etiqueta",null, data)
    }*/

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableSong = "DROP TABLE IF EXISTS cancion"
        db!!.execSQL(dropTableSong)
        val dropTableTitle = "DROP TABLE IF EXISTS titulo"
        db.execSQL(dropTableTitle)
        val dropTableContent = "DROP TABLE IF EXISTS contenido"
        db.execSQL(dropTableContent)
        val dropTableNote = "DROP TABLE IF EXISTS nota"
        db.execSQL(dropTableNote)
        val dropTableLabel = "DROP TABLE IF EXISTS etiqueta"
        db.execSQL(dropTableLabel)
        val dropTableSongFragment = "DROP TABLE IF EXISTS cancion_fragmento"
        db.execSQL(dropTableSongFragment)
        onCreate(db)
    }

    fun saveSong(song: String) {
        val songName = ContentValues().apply {
            put("nombre", song)
        }
        val db = this.writableDatabase
        db.insert("cancion", null, songName)
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
        db.insert("contenido", null, data)
    }

    fun saveSongFragment(song: Int, fragment: Int, type: String, posic: Int) {
        val data = ContentValues().apply {
            put("id_cancion", song)
            put("id_fragmento", fragment)
            put("tipo",type)
            put("posicion", posic)

        }

        val db = this.writableDatabase
        db.insert("cancion_fragmento",null,data)
    }

    fun lastSong(): Int {
        val database = this.readableDatabase
        val cursor =
            database.rawQuery("SELECT MAX(id) FROM cancion", null)
        cursor.moveToFirst()
        return cursor.getInt(0)
    }

    fun lastFragment(type: String): Int {
        val database = this.readableDatabase
        val cursor =
            database.rawQuery("SELECT MAX(id) FROM $type", null)
        cursor.moveToFirst()
        return  cursor.getInt(0)
    }

    /*fun showSongs(): Cursor? {
        val db: SQLiteDatabase = this.readableDatabase
        return db.rawQuery("""SELECT * FROM cancion""", null)
    }*/

}