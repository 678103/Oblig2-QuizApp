package com.example.oblig2_quizapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

// Fungerer som en server som gir andre apper lesetilgang til databasen
class QuizContentProvider : ContentProvider() {

    companion object {

        const val AUTHORITY = "com.example.oblig2_quizapp.provider"

        // Stien i URI-en
        const val PATH = "animals"

        const val TABLE_NAME = "animals"

        // Kode som representerer "hent alle dyr"
        const val ANIMALS = 1

        // UriMatcher gjenkjenner hvilken URI som kommer inn
        // NO_MATCH = returner -1 hvis ingen URI matcher
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            // content://com.example.oblig2_quizapp.provider/animals → ANIMALS (1)
            addURI(AUTHORITY, PATH, ANIMALS)
        }
    }

    // Kalles når provideren starter — returnerer true hvis alt er OK
    override fun onCreate(): Boolean {
        return true
    }

    // Kalles når noen spør etter data via URI
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ):
            Cursor? {

        // Henter appens kontekst — returnerer null hvis ikke tilgjengelig
        val context = context ?: return null

        // Finner filstien til Room-databasen på enheten
        val dbPath = context.getDatabasePath("animal_database").absolutePath

        // Åpner databasen kun for lesing
        val db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)

        // QueryBuilder hjelper med spørringen
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = TABLE_NAME

        // Sjekker hvilken URI som kom inn
        when (uriMatcher.match(uri)) {
            // Hent alle dyr
            ANIMALS -> {}

            // Ukjent URI
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        // Kjører spørringen og returnerer kolonner med navn oppgaven krever
        // "animalName AS name" og "imageUri AS URI" gir riktige kolonnenavn utad
        return queryBuilder.query(
            db,
            arrayOf("animalName AS name", "imageUri AS URI"),
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }
    // Forteller hva slags data URI-en inneholder
    override fun getType(uri: Uri): String = when (uriMatcher.match(uri)) {
        ANIMALS -> "vnd.android.cursor.dir/$AUTHORITY.$PATH"
        else -> throw IllegalArgumentException("Unknown URI")
    }

    // Oppgaven krever kun lesing — disse kaster feil hvis noen prøver å bruke dem
    //må likevell ha de fordi klassen er abstract
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