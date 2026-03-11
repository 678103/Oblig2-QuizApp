package com.example.oblig2_quizapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

//DAO defines methods for interacting with the database
//for basic usecases you use interface
//DAO dont have properties, but they do define one or more methods
//for interacting with the data in you app's database
@Dao
interface AnimalDAO {

    @Insert
    fun insert(animal:Animal)

    @Delete
    fun delete(animal: Animal)

    //Query-metode som interagerer med databasen
    @Query("SELECT * FROM animals")
    fun getAllAnimals(): List<Animal>
    //eller fun getAllAnimals(): LiveData<List<Animal>> (?)
}