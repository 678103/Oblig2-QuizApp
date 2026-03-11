package com.example.oblig2_quizapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//Oppsett for databasen, her bygger vi opp databasen med build
//Serves an abstraction layer over SQLite, making it
@Database (entities = [Animal::class], version =1)
abstract class AnimalRoomDatabase : RoomDatabase() { //hvorfor abstract?
    abstract fun animalDAO(): AnimalDAO

    companion object {
        private var INSTANCE: AnimalRoomDatabase? = null
        internal fun getDatabase(context: Context): AnimalRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(AnimalRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            Room.databaseBuilder(
                                context.applicationContext,
                                AnimalRoomDatabase::class.java,
                                "animal_database"
                            ).build()
                        // val db = Room.databaseBuilder(
                        //            applicationContext,
                        //            AppDatabase::class.java, "database-name"
                        //        ).build()
                        // dette er i denne linken: https://developer.android.com/training/data-storage/room/index.html
                    }
                }
            }
            return INSTANCE
        }
    }
}

