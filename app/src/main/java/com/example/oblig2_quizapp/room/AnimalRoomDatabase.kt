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
//Skal kun lages en så pensumboken sier at "it is best to implement defensive code within the class
//to prevent more than one instance from being created
//Serves an abstraction layer over SQLite, making it
@Database (entities = [Animal::class], version =1)
abstract class AnimalRoomDatabase : RoomDatabase() { //hvorfor abstract?
    abstract fun animalDAO(): AnimalDAO

    companion object {

        //sikrer at bare én database opprettes
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

        private class AnimalDatabaseCallback() : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                    // Hent DAO via INSTANCE etter at databasen er bygd
                    INSTANCE?.let { database ->
                        CoroutineScope(Dispatchers.IO).launch {

                            //Room gir en implementasjon av DAO som gjør av vi kan bruke funksjonene insert
                        val dao = database.animalDAO()
                            dao.insertAnimal(Animal(animalName = "cat", imageUri = "android.resource://com.example.oblig2_quizapp/drawable/cat"))
                            dao.insertAnimal(Animal(animalName = "dog", imageUri = "android.resource://com.example.oblig2_quizapp/drawable/dog"))
                            dao.insertAnimal(Animal(animalName = "fish", imageUri = "android.resource://com.example.oblig2_quizapp/drawable/fish"))
                    }
                }
            }
        }
    }
}

