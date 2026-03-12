package com.example.oblig2_quizapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

//DAO defines methods for interacting with the database
//for basic usecases you use interface
//DAO dont have properties, but they do define one or more methods
//for interacting with the data in you app's database
//Query-metode som interagerer med databasen
@Dao
interface AnimalDAO {

    //Legge til
    @Insert
     fun insertAnimal(animal:Animal) //suspend = funksjonen kan midlertidig stoppe og systemet
    //kan gjøre andre ting i mellomtiden

    //Slette
    @Query("DELETE FROM animals WHERE animalName = :name")
    fun deleteAnimal(name:String)

    @Query("SELECT * FROM animals where animalName = :name")
    fun findAnimal(name: String) : List<Animal>

    //hente ut alle i en liste
    @Query("SELECT * FROM animals")
    fun getAllAnimals(): LiveData<List<Animal>>
// DAO is using Livedata so that the repo can observe changes to the database

    @Query("SELECT COUNT(*) FROM ANIMALS")
    fun getCount(): Int
}