package com.example.oblig2_quizapp.room

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class AnimalRepository(private val animalDAO: AnimalDAO){

    //Hovedoppgave: skal snakke med Room-databasen på vegne av ViewModel og vil trenge
    //DAO-metodene til å legge til, slette dyr.
    // clean API for Ui to communicate with
    //makes calls to DAO methods to perform database operations (pensumbok)

    //resultatene av en søkeoperasjon lagres hver gang en asynkron søkeoppgave fullføres
    //her vil da en observatør i viewModel overvåke dette liveobjektet
    //Repositoryklassen må inneha metoder som viewModel kan kalle for å starte databaseoperasjoner
    //Repoet vil bruke coroutine der det er nødvendig for å unngå å utføre databaseoperasjoner på hovedtråden


    //Gir LiveData som UI kan observere
    val allAnimals: LiveData<List<Animal>> = animalDAO.getAllAnimals()

   // val searchResults = MutableLiveData<List<Animal>>()

    //så lenge du gjør noe med databaser så må du gjøre det i en egen tråd! derfor dispatcher
    //coroutineScope for å kjøre DB-operasjoner i bakgrunnen

    //Kjører DB-operasjoner på bakgrunnstråd
    private val coroutineScope = CoroutineScope(Dispatchers.Main) //vi initialiserer dette kun en gang

    fun insertAnimal (newAnimal:Animal) {
        coroutineScope.launch(Dispatchers.IO) {
            animalDAO.insertAnimal(newAnimal) //du kan ikke snakke med databasen direkte så du bruker samme funksjoner som i DAO
            //for å kunne kommunisere (insertAnimal)
        }
    }
        fun deleteAnimal (name: String) {
            coroutineScope.launch(Dispatchers.IO) {
                animalDAO.deleteAnimal(name)
            }
        }

    fun findAnimal (name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            animalDAO.findAnimal(name)
        }
    }
}



