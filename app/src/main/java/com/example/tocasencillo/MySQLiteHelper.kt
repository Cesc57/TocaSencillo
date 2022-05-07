package com.example.tocasencillo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*

class MySQLiteHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE, null, 30) {

    companion object {
        const val DATABASE = "cancionero.db"
        const val USER_TABLE = "usuario"
        const val SONG_TABLE = "cancion"
        const val TITLE_TABLE = "titulo"
        const val CONTENT_TABLE = "contenido"
        const val NOTE_TABLE = "nota"
        const val LABEL_TABLE = "etiqueta"
        const val SONG_FRAGMENT_TABLE = "cancion_fragmento"
        const val REPEAT_TABLE = "repeticion"
        const val BOX_REPEAT_TABLE = "caja_repeticion"
        const val ALTERNATE_ENDING_TABLE = "final_alternativo"
        const val LABEL_VALUE_TABLE = "valor_etiqueta"
        const val REPEAT_VALUE_TABLE = "valor_repeticion"
        const val ID_DB = "_id"
        const val ID_USER = "id_usuario"
        const val NAME = "nombre"
        const val MAIL = "mail"
        const val TEMPO = "velocidad"
        const val SONG_KEY = "tonalidad"
        const val CC_BAR_1 = "cc_bar1"
        const val CC_BAR_2 = "cc_bar2"
        const val CC_BAR_5 = "cc_ar_5"
        const val CC_MUSIC_1 = "cc_musica1"
        const val CC_MUSIC_2 = "cc_musica2"
        const val CC_MUSIC_3 = "cc_musica3"
        const val CC_MUSIC_4 = "cc_musica4"
        const val TEXT = "texto"
        const val TYPE = "tipo"
        const val TIMES = "veces"
        const val POSITION = "posicion"
        const val ID_SONG = "_id_cancion"
        const val ID_SONG_FRAGMENT = "_id_cancion_fragmento"
        const val ID_REPEAT_VALUE = "_id_valor_repeticion"
        const val ID_TAG_VALUE = "_id_valor_etiqueta"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        //defining our DB

        val createUserCommand =
            """CREATE TABLE $USER_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $MAIL TEXT UNIQUE)"""

        val createSongCommand =
            """CREATE TABLE $SONG_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $NAME TEXT UNIQUE,
                $ID_USER INTEGER,
                FOREIGN KEY($ID_USER) REFERENCES $USER_TABLE($ID_DB))"""

        val createSongFragmentCommand =
            """CREATE TABLE $SONG_FRAGMENT_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $ID_SONG INTEGER,
                $TYPE TEXT,
                $POSITION INTEGER,
                FOREIGN KEY($ID_SONG) REFERENCES $SONG_TABLE($ID_DB))"""

        val createTitleCommand =
            """CREATE TABLE $TITLE_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $NAME TEXT,
                $TEMPO INTEGER,
                $SONG_KEY TEXT,
                $ID_SONG_FRAGMENT INTEGER,
                FOREIGN KEY($ID_SONG_FRAGMENT) REFERENCES $SONG_FRAGMENT_TABLE($ID_DB))"""

        val createContentCommand =
            """CREATE TABLE $CONTENT_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $CC_BAR_1 TEXT,
                $CC_BAR_5 TEXT,
                $CC_MUSIC_1 TEXT,
                $CC_MUSIC_2 TEXT,
                $CC_MUSIC_3 TEXT,
                $CC_MUSIC_4 TEXT,
                $ID_SONG_FRAGMENT INTEGER,
                FOREIGN KEY($ID_SONG_FRAGMENT) REFERENCES $SONG_FRAGMENT_TABLE($ID_DB))"""

        val createNoteCommand =
            """CREATE TABLE $NOTE_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $TEXT TEXT,
                $ID_SONG_FRAGMENT INTEGER,
                FOREIGN KEY($ID_SONG_FRAGMENT) REFERENCES $SONG_FRAGMENT_TABLE($ID_DB))"""

        val createBoxRepeatCommand =
            """CREATE TABLE $BOX_REPEAT_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $TEXT TEXT,
                $ID_SONG_FRAGMENT INTEGER,
                FOREIGN KEY($ID_SONG_FRAGMENT) REFERENCES $SONG_FRAGMENT_TABLE($ID_DB))"""

        val createAlternateEndingCommand =
            """CREATE TABLE $ALTERNATE_ENDING_TABLE 
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $CC_BAR_2 TEXT, 
                $CC_MUSIC_1 TEXT,
                $CC_MUSIC_2 TEXT,
                $ID_SONG_FRAGMENT INTEGER,
                FOREIGN KEY($ID_SONG_FRAGMENT) REFERENCES $SONG_FRAGMENT_TABLE($ID_DB))"""

        val createValueTagCommand =
            """CREATE TABLE $LABEL_VALUE_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $TYPE TEXT)"""

        val createValueRepeatCommand =
            """CREATE TABLE $REPEAT_VALUE_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $TIMES TEXT)"""

        val createTagCommand =
            """CREATE TABLE $LABEL_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $ID_SONG_FRAGMENT INTEGER,
                $ID_TAG_VALUE,
                FOREIGN KEY($ID_SONG_FRAGMENT) REFERENCES $SONG_FRAGMENT_TABLE($ID_DB),
                FOREIGN KEY($ID_TAG_VALUE) REFERENCES $LABEL_VALUE_TABLE($ID_DB))"""

        val createRepeatCommand =
            """CREATE TABLE $REPEAT_TABLE
                ($ID_DB INTEGER PRIMARY KEY AUTOINCREMENT,
                $ID_SONG_FRAGMENT INTEGER,
                $ID_REPEAT_VALUE,
                FOREIGN KEY($ID_SONG_FRAGMENT) REFERENCES $SONG_FRAGMENT_TABLE($ID_DB),
                FOREIGN KEY($ID_REPEAT_VALUE) REFERENCES $REPEAT_VALUE_TABLE($ID_DB))"""

        //exec create tables
        with(db) {
            this!!.execSQL(createUserCommand)
            this.execSQL(createSongCommand)
            this.execSQL(createTitleCommand)
            this.execSQL(createContentCommand)
            this.execSQL(createNoteCommand)
            this.execSQL(createSongFragmentCommand)
            this.execSQL(createBoxRepeatCommand)
            this.execSQL(createAlternateEndingCommand)
            this.execSQL(createValueRepeatCommand)
            this.execSQL(createRepeatCommand)
            this.execSQL(createValueTagCommand)
            this.execSQL(createTagCommand)
        }

        insertPrefabData(db)
    }

    private fun insertPrefabData(db: SQLiteDatabase?) {
        val tags = arrayOf(
            "Intro", "Estrofa", "PreStrb", "Strb",
            "Puente", "Solo", "Final"
        )
        for (tag in tags) {
            val insertTags = """INSERT INTO $LABEL_VALUE_TABLE ($TYPE) 
                    VALUES ('$tag')"""
            with(db) {
                this!!.execSQL(insertTags)
            }
        }

        val reps = arrayOf("x3", "x4")
        for (rep in reps) {
            val insertReps = """INSERT INTO $REPEAT_VALUE_TABLE ($TIMES) 
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
        val dropTableUser = "DROP TABLE IF EXISTS $USER_TABLE"
        db!!.execSQL(dropTableUser)
        val dropTableSong = "DROP TABLE IF EXISTS $SONG_TABLE"
        db.execSQL(dropTableSong)
        val dropTableSongFragment = "DROP TABLE IF EXISTS $SONG_FRAGMENT_TABLE"
        db.execSQL(dropTableSongFragment)
        val dropTableTitle = "DROP TABLE IF EXISTS $TITLE_TABLE"
        db.execSQL(dropTableTitle)
        val dropTableContent = "DROP TABLE IF EXISTS $CONTENT_TABLE"
        db.execSQL(dropTableContent)
        val dropTableNote = "DROP TABLE IF EXISTS $NOTE_TABLE"
        db.execSQL(dropTableNote)
        val dropTableLabel = "DROP TABLE IF EXISTS $LABEL_TABLE"
        db.execSQL(dropTableLabel)
        val dropTableRepeat = "DROP TABLE IF EXISTS $REPEAT_TABLE"
        db.execSQL(dropTableRepeat)
        val dropTableBoxRepeat = "DROP TABLE IF EXISTS $BOX_REPEAT_TABLE"
        db.execSQL(dropTableBoxRepeat)
        val dropAlternateEndingRepeat = "DROP TABLE IF EXISTS $ALTERNATE_ENDING_TABLE"
        db.execSQL(dropAlternateEndingRepeat)

        val dropValueRepeat = "DROP TABLE IF EXISTS $REPEAT_VALUE_TABLE"
        db.execSQL(dropValueRepeat)
        val dropValueTag = "DROP TABLE IF EXISTS $LABEL_VALUE_TABLE"
        db.execSQL(dropValueTag)

        onCreate(db)
    }

    fun saveUser(mail: String) {
        val userMail = ContentValues().apply {
            put(MAIL, mail)
        }
        val db = this.writableDatabase
        db.insert(USER_TABLE, null, userMail)
    }

    fun saveSong(song: String, userId: Int) {
        val songName = ContentValues().apply {
            put(NAME, song)
            put(ID_USER, userId)
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
        lastSongFragment: Int

    ) {
        val data = ContentValues().apply {
            put(CC_BAR_1, ccBar1)
            put(CC_BAR_5, ccBar5)
            put(CC_MUSIC_1, txtCC1)
            put(CC_MUSIC_2, txtCC2)
            put(CC_MUSIC_3, txtCC3)
            put(CC_MUSIC_4, txtCC4)
            put(ID_SONG_FRAGMENT, lastSongFragment)
        }

        val db = this.writableDatabase
        db.insert(CONTENT_TABLE, null, data)
    }

    fun saveAlternateEnd(ccBar2: String, txtCC1: String, txtCC2: String, lastSongFragment: Int) {
        val data = ContentValues().apply {
            put(CC_BAR_2, ccBar2)
            put(CC_MUSIC_1, txtCC1)
            put(CC_MUSIC_2, txtCC2)
            put(ID_SONG_FRAGMENT, lastSongFragment)
        }

        val db = this.writableDatabase
        db.insert(ALTERNATE_ENDING_TABLE, null, data)
    }

    fun saveTitle(title: String, tempo: String, key: String, lastSongFragment: Int) {
        val data = ContentValues().apply {
            put(NAME, title)
            put(TEMPO, tempo)
            put(SONG_KEY, key)
            put(ID_SONG_FRAGMENT, lastSongFragment)

        }

        val db = this.writableDatabase
        db.insert(TITLE_TABLE, null, data)
    }

    fun saveNote(note: String, lastSongFragment: Int) {
        val data = ContentValues().apply {
            put(TEXT, note)
            put(ID_SONG_FRAGMENT, lastSongFragment)
        }

        val db = this.writableDatabase
        db.insert(NOTE_TABLE, null, data)
    }

    fun saveBoxRepeat(boxText: String, lastSongFragment: Int) {
        val data = ContentValues().apply {
            put(TEXT, boxText)
            put(ID_SONG_FRAGMENT, lastSongFragment)
        }

        val db = this.writableDatabase
        db.insert(BOX_REPEAT_TABLE, null, data)
    }

    fun saveSongFragment(song: Int, type: String, posic: Int) {
        val data = ContentValues().apply {
            put(ID_SONG, song)
            put(TYPE, type)
            put(POSITION, posic)

        }

        val db = this.writableDatabase
        db.insert(SONG_FRAGMENT_TABLE, null, data)
    }

    fun saveLabel(lastSongFragment: Int, myId: Int) {
        val data = ContentValues().apply {
            put(ID_SONG_FRAGMENT, lastSongFragment)
            put(ID_TAG_VALUE, myId)
        }

        val db = this.writableDatabase
        db.insert(LABEL_TABLE, null, data)
    }

    fun saveRepeat(lastSongFragment: Int, myId: Int) {
        val data = ContentValues().apply {
            put(ID_SONG_FRAGMENT, lastSongFragment)
            put(ID_REPEAT_VALUE, myId)
        }

        val db = this.writableDatabase
        db.insert(REPEAT_TABLE, null, data)
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
    fun searchUserIdByMail(mail: String): Int {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT $ID_DB " +
                    "FROM $USER_TABLE " +
                    "WHERE $MAIL = '$mail'", null
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
    fun searchLabelValue(id: Int): Int {
        val database = this.readableDatabase
        val cursor =
            database.rawQuery(
                "SELECT * " +
                        "FROM $LABEL_TABLE " +
                        "WHERE $ID_SONG_FRAGMENT = $id", null
            ).apply {
                moveToFirst()
            }
        return cursor.getInt(2)
    }


    @SuppressLint("Recycle")
    fun searchRepeatValue(id: Int): Int {
        val database = this.readableDatabase
        val cursor =
            database.rawQuery(
                "SELECT * " +
                        "FROM $REPEAT_TABLE " +
                        "WHERE $ID_SONG_FRAGMENT = $id", null
            ).apply {
                moveToFirst()
            }
        return cursor.getInt(2)
    }

    @SuppressLint("Recycle")
    fun lastSongFragment(): Int {
        val database = this.readableDatabase
        val cursor =
            database.rawQuery(
                "SELECT MAX($ID_DB) " +
                        "FROM $SONG_FRAGMENT_TABLE", null
            ).apply {
                moveToFirst()
            }
        return cursor.getInt(0)
    }

    @SuppressLint("Recycle")
    fun searchAlternateEndingById(myId: Int): ArrayList<String> {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * " +
                    "FROM $ALTERNATE_ENDING_TABLE " +
                    "WHERE $ID_SONG_FRAGMENT = '$myId'", null
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
    fun searchTitleById(myId: Int): ArrayList<String> {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * " +
                    "FROM $TITLE_TABLE " +
                    "WHERE $ID_SONG_FRAGMENT = '$myId'", null
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
    fun searchContentById(myId: Int): ArrayList<String> {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * " +
                    "FROM $CONTENT_TABLE " +
                    "WHERE $ID_SONG_FRAGMENT = '$myId'", null
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
    fun searchBoxRepeatById(myId: Int): String {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * " +
                    "FROM $BOX_REPEAT_TABLE " +
                    "WHERE $ID_SONG_FRAGMENT = '$myId'", null
        ).apply {
            moveToFirst()
        }
        return cursor.getString(1)
    }

    @SuppressLint("Recycle")
    fun searchNoteById(myId: Int): String {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * " +
                    "FROM $NOTE_TABLE " +
                    "WHERE $ID_SONG_FRAGMENT = '$myId'", null
        ).apply {
            moveToFirst()
        }
        return cursor.getString(1)
    }

    fun deleteSong(name: String) {
        val args = arrayOf(name)
        val db = this.writableDatabase
        db.delete(SONG_TABLE, "$NAME = ?", args)
        //db.execSQL("DELETE FROM $SONG_TABLE WHERE $NAME = '$name'")
        db.close()
    }

    fun deleteSongFragment(fragId: Int) {
        val args = arrayOf(fragId.toString())
        val db = this.writableDatabase
        db.delete(SONG_FRAGMENT_TABLE, "$ID_SONG = ?", args)
        db.close()
    }

    fun deleteFragment(idFrag: Int, table: String) {
        val args = arrayOf(idFrag.toString())
        val db = this.writableDatabase
        db.delete(table, "$ID_SONG_FRAGMENT = ?", args)
        db.close()
    }

    fun deletePrefabs(table: String, songTableId: Int) {
        val args = arrayOf(songTableId.toString())
        val db = this.writableDatabase
        Log.d("labelitos", songTableId.toString())
        db.delete(table, "$ID_SONG_FRAGMENT = ?", args)
        db.close()
    }
}