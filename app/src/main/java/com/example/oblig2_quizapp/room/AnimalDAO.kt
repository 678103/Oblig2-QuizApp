package com.example.oblig2_quizapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//DAO defines methods for interacting with the database

@Dao
interface AnimalDAO {

    //Legge til med innebygd Room-notasjon
    @Insert
     fun insertAnimal(animal:Animal)

    //Slette, med egendefinert Query
    @Query("DELETE FROM animals WHERE animalName = :name")
    fun deleteAnimal(name:String)


    //henter ut alle dyr i en LiveData-liste
    @Query("SELECT * FROM animals")
    fun getAllAnimals(): LiveData<List<Animal>>


}