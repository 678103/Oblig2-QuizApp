package com.example.oblig2_quizapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class QuizContentProvider : ContentProvider() { //fungerer som en server som gir tilgang til dataene i databasen

    companion object {
        //navnet på provideren (som også brukes i manifestet)
        const val AUTHORITY = "com.example.oblig2_quizapp.provider"

        //path i URI
        const val PATH = "animals"

        //tabellen i databasen
        const val TABLE_NAME = "animals"

        //URI typer
        const val ANIMALS = 1   //henter alle dyr
        const val ANIMAL_ID = 2  //henter ett spesifikt dyr

        //matcher URI til riktig type
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH, ANIMALS)
            addURI(AUTHORITY, "$PATH/#", ANIMAL_ID)
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    //boilcode
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        val context = context ?: return null

        //finner SQLite databasen som Room har laget
        val dbPath = context.getDatabasePath("animal_database").absolutePath

        //Åpner databasen. OBs readonly
        val db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)

        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = TABLE_NAME

        //sjekker hvilken URI som brukes
        when (uriMatcher.match(uri)) {

            // hent alle dyr
            ANIMALS -> {}

            // hent ett dyr
            ANIMAL_ID -> {
                queryBuilder.appendWhere("id = ${uri.lastPathSegment}")
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
            // Viktig: returner kolonnene oppgaven krever
            return queryBuilder.query(
                db,
                arrayOf("animalId AS _id", "animalName AS name", "imageUri AS URI"),
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
        }

    override fun getType(uri: Uri): String = when (uriMatcher.match(uri)) {
        ANIMALS -> "vnd.android.cursor.dir/$AUTHORITY.$PATH"
        ANIMAL_ID -> "vnd.android.cursor.item/$AUTHORITY.$PATH"
        else -> throw IllegalArgumentException("Unknown URI")
    }

        //Oppgaven krever kun lesing, ikke insert/update/delete

        override fun insert(uri: Uri, values: ContentValues?): Uri? {
            throw UnsupportedOperationException("Insert not supported")
        }

        override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
            throw UnsupportedOperationException("Update not supported")
        }

        override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
            throw UnsupportedOperationException("Delete not supported")
        }

    }
