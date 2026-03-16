package com.example.oblig2_quizapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//Oppsett for databasen, her bygger vi opp databasen med build
//Serves an abstraction layer over SQLite
@Database (entities = [Animal::class], version =1)
abstract class AnimalRoomDatabase : RoomDatabase() {
    abstract fun animalDAO(): AnimalDAO

    companion object {

        //sikrer at bare én database finnes i applikasjonen
        private var INSTANCE: AnimalRoomDatabase? = null

        fun getDatabase(context: Context): AnimalRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimalRoomDatabase::class.java,
                    "animal_database"
                )
                    .addCallback(AnimalDatabaseCallback())
                    .build()

                INSTANCE = instance
                return instance
            }
        }

        //Callback fordi den setter inn bildene ETTER databasen er laget
        //Callback er en indre klasse av RoomDatabase.
        private class AnimalDatabaseCallback : Callback() {


            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                    // Hent DAO via INSTANCE etter at databasen er bygd
                    //.let er en Kotlin-funksjon som kjøres en  kodeblokk kun hvis objektet ikke er null
                    INSTANCE?.let {
                        database ->
                        CoroutineScope(Dispatchers.IO).launch {
                            //Room gir en implementasjon av DAO som gjør at vi kan bruke funksjonene insert
                             val dao = database.animalDAO()

                            dao.insertAnimal(
                                Animal(animalName = "cat", imageUri = "android.resource://com.example.oblig2_quizapp/drawable/cat"))
                            dao.insertAnimal(
                                Animal(animalName = "dog", imageUri = "android.resource://com.example.oblig2_quizapp/drawable/dog"))
                            dao.insertAnimal(
                                Animal(animalName = "fish", imageUri = "android.resource://com.example.oblig2_quizapp/drawable/fish"))
                        }           //packagename//drawable/imagename
                }
            }
        }
    }
}

