package com.example.oblig2_quizapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.oblig2_quizapp.room.AnimalRepository
import com.example.oblig2_quizapp.room.Animal
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.oblig2_quizapp.room.AnimalRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//AVM fordi vi må hente Room DAO vi application

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AnimalRepository //repository som snakker med Room

    val animals: LiveData<List<Animal>> //livedata med listen av dyr

    init {
        //Henter DAO fra room
        val database = AnimalRoomDatabase.getDatabase(application)
        repository = AnimalRepository(database.animalDAO())
        animals  = repository.allAnimals

    }

    fun insertAnimal(animal: Animal) {
        //viewmodelscope standard i android. coroutine stopper automatisk når viewModel dør
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAnimal(animal)
        } //legg til nytt dyr
    }

    // Slett dyr på bakgrunnstråd
    fun deleteAnimal(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAnimal(name)
        }

    }
}
