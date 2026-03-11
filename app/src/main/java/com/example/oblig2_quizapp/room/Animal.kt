package com.example.oblig2_quizapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//Room- database, som har kontakt med DAO
//Entity represents a table in the database
// imageURI er den pathen vi lagrer bilde til

    @Entity (tableName = "animals")
    class Animal (

        @PrimaryKey (autoGenerate = true)
        @ColumnInfo (name = "animalId")
        val id: Int = 0,

        @ColumnInfo (name= "animalName")
        val animalName: String,
        val imageUri: String

        )

