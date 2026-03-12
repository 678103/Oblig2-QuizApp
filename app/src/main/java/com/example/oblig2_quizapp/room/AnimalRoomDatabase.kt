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

        private var INSTANCE: AnimalRoomDatabase? = null

        fun getDatabase(context: Context): AnimalRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimalRoomDatabase::class.java,
                    "animal_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // Kjører i bakgrunnstråd
                            CoroutineScope(Dispatchers.IO).launch {
                                // Hent DAO via INSTANCE etter at databasen er bygd
                                INSTANCE?.animalDAO()?.apply {
                                    insertAnimal(
                                        Animal(animalName = "cat", imageUri = "android.resource://com.example.oblig2_quizapp/drawable/cat")
                                    )
                                    insertAnimal(Animal(animalName = "dog", imageUri = "android.resource://com.example.oblig2_quizapp/drawable/dog")
                                    )
                                    insertAnimal(Animal(animalName = "fish", imageUri = "android.resource://com.example.oblig2_quizapp/drawable/fish")
                                    )
                                }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}

