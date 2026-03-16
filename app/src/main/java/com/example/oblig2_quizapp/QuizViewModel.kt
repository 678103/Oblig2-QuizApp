package com.example.oblig2_quizapp

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.oblig2_quizapp.room.AnimalRepository
import com.example.oblig2_quizapp.room.Animal
import androidx.lifecycle.LiveData
import com.example.oblig2_quizapp.room.AnimalRoomDatabase

class QuizViewModel(application: Application): AndroidViewModel(application) {

    //denne klassen holds all the data needed for the UI
    // brukes til å connecte til UI. Her skal alle bilder håndteres

    //Repository- mellomledd til databasen
    private val repository: AnimalRepository //repository er mellomleddet til databasen

    //deklarerer livedata-listen  med animals
    val allAnimals: LiveData<List<Animal>>


    //Dyret som vises i Quiz akkurat nå
    var currentAnimal by mutableStateOf<Animal?>(null)
        private set

    // tre svaralternativer
    var answers by mutableStateOf<List<Animal>>(emptyList())
        private set

    //antall riktige svar på rad
    var quizScore by mutableIntStateOf(0)
        private set

    //antall forsøk
    var attempts by mutableIntStateOf(1)
        private set

    //om resultattekst skal vises
    var showResult by mutableStateOf(false)
        private set

    //hva bruker trykket på av svar
    var selectedOption by mutableStateOf<String?>(null)
        private set

    init {
        //kobler viewModel til Room via repository
        val database = AnimalRoomDatabase.getDatabase(application)
        repository = AnimalRepository(database.animalDAO())
        allAnimals = repository.animals
    }

    // Lager nytt spørsmål: 1 riktig og 2 feil svar
    fun generateNewQuestion(animalList: List<Animal>) {
        if (animalList.size < 3) return

        val animal = animalList.random()
        currentAnimal = animal

        val wrongAnswers = animalList
            .filter { it != animal }
            .shuffled()
            .take(2)

        //blander rett og gale svar
        answers = (listOf(animal) + wrongAnswers).shuffled()

        // Nullstill resultatvisning for nytt spm
        showResult = false
        selectedOption = null
    }

    // Sjekker brukerens svar og oppdaterer score
    fun checkAnswer(answer: Animal): Boolean {

        //lagrer hva bruker valgte
        selectedOption = answer.animalName

        //viser resultat til UI
        showResult = true

        val correct = answer.animalName == currentAnimal?.animalName

        if (correct) {
            quizScore++
        } else {
            quizScore = 0
            attempts++
        }
        return correct
    }

    }
