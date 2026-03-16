package com.example.oblig2_quizapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.oblig2_quizapp.room.AnimalRepository
import com.example.oblig2_quizapp.room.Animal
import androidx.lifecycle.LiveData
import com.example.oblig2_quizapp.room.AnimalRoomDatabase

//AVM fordi vi må hente context i databasen (derfor application som parameter)

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AnimalRepository //repository som snakker med Room

    val animals: LiveData<List<Animal>> //livedata med listen av dyr


    //init kjøres automatisk når viewModel opprettes
    init {
        //Henter databasen -> henter dao -> lager repo og kobler livedata til animals
        val database = AnimalRoomDatabase.getDatabase(application)

        //sender DAO inn i databasen, via konstruktør slik at repo kan bruke DAO for interaksjon med databasen
        repository = AnimalRepository(database.animalDAO())

        //kobler livedata til allAnimals
        animals  = repository.animals

    }
    fun insertAnimal(animal: Animal) {
        repository.insertAnimal(animal)

    }

    // Slett dyr
    fun deleteAnimal(name: String) {
        repository.deleteAnimal(name)
        }

    //for test
    fun getAnimalCount(): Int {
        //siden det er livedata så må vi hente ut nårævrende verdi
        return animals.value?.size ?:0
    }
    }
