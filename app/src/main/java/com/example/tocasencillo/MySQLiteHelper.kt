package com.example.tocasencillo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

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
        const val ID_DB = "_id"
        const val NAME = "nombre"
        const val TEMPO = "velocidad"
        const val SONG_KEY = "tonalidad"
        const val CC_BAR_1 = "ccBar1"
        const val CC_BAR_2 = "ccBar2"
        const val CC_BAR_5 = "ccBar5"
        const val CC_MUSIC_1 = "ccMusica1"
        const val CC_MUSIC_2 = "ccMusica2"
        const val CC_MUSIC_3 = "ccMusica3"
        const val CC_MUSIC_4 = "ccMusica4"
        const val TEXT = "texto"
        const val TYPE = "tipo"
        const val TIMES = "veces"
        const val POSITION = "posicion"
        const val ID_SONG = "_id_cancion"
        const val ID_FRAGMENT = "_id_fragmento"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        //defining our DB
        val createSongCommand =
            """CREATE TABLE $SONG_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $NAME TEXT UNIQUE)"""

        val createTitleCommand =
            """CREATE TABLE $TITLE_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $NAME TEXT, $TEMPO INTEGER, $SONG_KEY TEXT)"""

        val createContentCommand =
            """CREATE TABLE $CONTENT_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $CC_BAR_1 TEXT, $CC_BAR_5 TEXT, 
                $CC_MUSIC_1 TEXT,$CC_MUSIC_2 TEXT, $CC_MUSIC_3 TEXT, $CC_MUSIC_4 TEXT )"""

        val createNoteCommand =
            """CREATE TABLE $NOTE_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $TEXT TEXT)"""

        val createTagCommand =
            """CREATE TABLE $LABEL_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $TYPE TEXT)"""

        val createSongFragmentCommand =
            """CREATE TABLE $SONG_FRAGMENT_TABLE
                ($ID_SONG INTEGER,
                $ID_FRAGMENT INTEGER,
                $TYPE TEXT,
                $POSITION INTEGER,
                FOREIGN KEY($ID_SONG) REFERENCES $SONG_TABLE($ID_DB))"""

        val createRepeatCommand =
            """CREATE TABLE $REPEAT_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $TIMES TEXT)"""

        val createBoxRepeatCommand =
            """CREATE TABLE $BOX_REPEAT_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $TEXT TEXT)"""

        val createAlternateEndingCommand =
            """CREATE TABLE $ALTERNATE_ENDING_TABLE 
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $CC_BAR_2 TEXT, 
                $CC_MUSIC_1 TEXT,$CC_MUSIC_2 TEXT)"""

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

        insertPrefabData(db)
    }

    private fun insertPrefabData(db: SQLiteDatabase?) {
        val tags = arrayOf(
            "Intro", "Estrofa", "PreStrb", "Strb",
            "Puente", "Solo", "Final"
        )
        for (tag in tags) {
            val insertTags = """INSERT INTO $LABEL_TABLE ($TYPE) 
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
        dropAndRebuildTables(db)
    }

    private fun dropAndRebuildTables(db: SQLiteDatabase?) {
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
            put(NAME, song)
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
            put(CC_BAR_1, ccBar1)
            put(CC_BAR_5, ccBar5)
            put(CC_MUSIC_1, txtCC1)
            put(CC_MUSIC_2, txtCC2)
            put(CC_MUSIC_3, txtCC3)
            put(CC_MUSIC_4, txtCC4)
        }

        val db = this.writableDatabase
        db.insert(CONTENT_TABLE, null, data)
    }

    fun saveAlternateEnd(ccBar2: String, txtCC1: String, txtCC2: String) {
        val data = ContentValues().apply {
            put(CC_BAR_2, ccBar2)
            put(CC_MUSIC_1, txtCC1)
            put(CC_MUSIC_2, txtCC2)
        }

        val db = this.writableDatabase
        db.insert(ALTERNATE_ENDING_TABLE, null, data)
    }

    fun saveTitle(title: String, tempo: String, key: String) {
        val data = ContentValues().apply {
            put(NAME, title)
            put(TEMPO, tempo)
            put(SONG_KEY, key)

        }

        val db = this.writableDatabase
        db.insert(TITLE_TABLE, null, data)
    }

    fun saveNote(note: String) {
        val data = ContentValues().apply {
            put(TEXT, note)
        }

        val db = this.writableDatabase
        db.insert(NOTE_TABLE, null, data)
    }

    fun saveBoxRepeat(boxText: String) {
        val data = ContentValues().apply {
            put(TEXT, boxText)
        }

        val db = this.writableDatabase
        db.insert(BOX_REPEAT_TABLE, null, data)
    }

    fun saveSongFragment(song: Int, fragment: Int, type: String, posic: Int) {
        val data = ContentValues().apply {
            put(ID_SONG, song)
            put(ID_FRAGMENT, fragment)
            put(TYPE, type)
            put(POSITION, posic)

        }

        val db = this.writableDatabase
        db.insert(SONG_FRAGMENT_TABLE, null, data)
    }

    @SuppressLint("Recycle")
    fun searchSongIdByName(songName: String): Int {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT $ID_DB " +
                    "FROM $SONG_TABLE " +
                    "WHERE $NAME = '$songName'", null
        ).apply {
            moveToFirst()
        }
        return cursor.getInt(0)
    }

    @SuppressLint("Recycle")
    fun lastSong(): Int {
        val database = this.readableDatabase
        val cursor =
            database.rawQuery(
                "SELECT MAX($ID_DB) " +
                        "FROM $SONG_TABLE", null
            ).apply {
                moveToFirst()
            }
        return cursor.getInt(0)
    }

    @SuppressLint("Recycle")
    fun lastFragment(type: String): Int {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT MAX($ID_DB) " +
                    "FROM $type", null
        ).apply {
            moveToFirst()
        }
        return cursor.getInt(0)
    }

    @SuppressLint("Recycle")
    fun searchAlternateEndingById(myPosic: Int): ArrayList<String> {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * " +
                    "FROM $ALTERNATE_ENDING_TABLE " +
                    "WHERE $ID_DB = '$myPosic'", null
        ).apply {
            moveToFirst()
        }
        val mArrayList = ArrayList<String>()
        mArrayList.add(cursor.getString(1))
        mArrayList.add(cursor.getString(2))
        mArrayList.add(cursor.getString(3))
        return mArrayList
    }

    @SuppressLint("Recycle")
    fun searchTitleById(myPosic: Int): ArrayList<String> {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * " +
                    "FROM $TITLE_TABLE " +
                    "WHERE $ID_DB = '$myPosic'", null
        ).apply {
            moveToFirst()
        }
        val mArrayList = ArrayList<String>()
        mArrayList.add(cursor.getString(1))
        mArrayList.add(cursor.getString(2))
        mArrayList.add(cursor.getString(3))
        return mArrayList
    }

    @SuppressLint("Recycle")
    fun searchContentById(myPosic: Int): ArrayList<String> {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * " +
                    "FROM $CONTENT_TABLE " +
                    "WHERE $ID_DB = '$myPosic'", null
        ).apply {
            moveToFirst()
        }
        val mArrayList = ArrayList<String>()
        mArrayList.add(cursor.getString(1))
        mArrayList.add(cursor.getString(2))
        mArrayList.add(cursor.getString(3))
        mArrayList.add(cursor.getString(4))
        mArrayList.add(cursor.getString(5))
        mArrayList.add(cursor.getString(6))
        return mArrayList
    }

    @SuppressLint("Recycle")
    fun searchBoxRepeatById(myPosic: Int): String {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * " +
                    "FROM $BOX_REPEAT_TABLE " +
                    "WHERE $ID_DB = '$myPosic'", null
        ).apply {
            moveToFirst()
        }
        return cursor.getString(1)
    }

    @SuppressLint("Recycle")
    fun searchNoteById(myPosic: Int): String {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * " +
                    "FROM $NOTE_TABLE " +
                    "WHERE $ID_DB = '$myPosic'", null
        ).apply {
            moveToFirst()
        }
        return cursor.getString(1)
    }

    @SuppressLint("Recycle")
    fun deleteSong(songName: String) {
        val idSong = searchSongIdByName(songName)
        val database = this.readableDatabase
        val db = this.writableDatabase
        val cursor: Cursor = database.rawQuery(
            "SELECT * " +
                    "FROM $SONG_FRAGMENT_TABLE " +
                    "WHERE $ID_SONG = '$idSong'",
            null
        )
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            db.delete(cursor.getString(2), "$ID_DB = ?", arrayOf(cursor.getString(1)))
        }
        db.delete(SONG_FRAGMENT_TABLE, "$ID_SONG = ?", arrayOf(cursor.getString(2)))
        db.delete(SONG_TABLE, "$ID_SONG = ?", arrayOf(idSong.toString()))
    }
}