package com.example.oblig2_quizapp.room

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class AnimalRepository(private val animalDAO: AnimalDAO){

    //Gir LiveData som viewModel kan observere
    val animals: LiveData<List<Animal>> = animalDAO.getAllAnimals()

    //Kjører DB-operasjoner på bakgrunnstråd
    private val coroutineScope = CoroutineScope(Dispatchers.Main) //vi initialiserer dette kun en gang

    fun insertAnimal (newAnimal:Animal) {
        coroutineScope.launch(Dispatchers.IO) {
            animalDAO.insertAnimal(newAnimal) //DAO brukes for å utføre databaseoperasjonene
        }
    }
        fun deleteAnimal (name: String) {
            coroutineScope.launch(Dispatchers.IO) {
                animalDAO.deleteAnimal(name)
            }
        }
    }

//Hovedoppgave: skal snakke med Room-databasen på vegne av ViewModel og vil trenge
//DAO-metodene til å legge til, slette dyr.
// clean API for Ui to communicate with
//makes calls to DAO methods to perform database operations (pensumbok)

//her vil da en observatør i viewModel overvåke dette liveData-objektet
//Repositoryklassen må inneha metoder som viewModel kan kalle for å starte databaseoperasjoner
//Repoet vil bruke coroutine der det er nødvendig for å unngå å utføre databaseoperasjoner på hovedtråden





