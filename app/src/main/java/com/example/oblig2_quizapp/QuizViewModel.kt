package com.example.oblig2_quizapp

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.oblig2_quizapp.room.AnimalRepository
import com.example.oblig2_quizapp.room.Animal
import com.example.oblig2_quizapp.room.AnimalRoomDatabase

class QuizViewModel(application: Application): AndroidViewModel(application) {

    //make a galleryViewModel , list of animals pictures and names
    //newImage- gi den en state
    //no remember here since were not in a Composable

    //denne klassen holds all the data needed for the UI
    // brukes til å connecte til UI. Her skal alle bilder håndteres
 // Her må vi ha score for riktig poengscore
    // i tillegg må det inneholde spørsmålet

    private val repository: AnimalRepository

    // Liste med alle dyr fra databasen
    var allAnimals by mutableStateOf<List<Animal>>(emptyList())
        private set

    //Dyret som vises akkurat nå
    var currentAnimal by mutableStateOf<Animal?>(null)
        private set

    //listen med tre svaralternativer
    var answers by mutableStateOf<List<Animal>>(emptyList())
        private set

    //antall riktige svar
    var quizScore by mutableIntStateOf(0)
        private set

    //antall forsøk
    var attempts by mutableIntStateOf(1)
        private set

    //om resultatet vises
    var showResult by mutableStateOf(false)
        private set

    //svaret som bruker valgte
    var selectedOption by mutableStateOf<String?>(null)
        private set

    init {
        val database = AnimalRoomDatabase.getDatabase(application)
        repository = AnimalRepository(database.animalDAO())

        // Observerer LiveData fra repository og oppdaterer Compose state
        repository.allAnimals.observeForever { list ->
            allAnimals = list

            // Generer første spørsmål når det er minst 3 dyr
            if (list.size >= 3 && currentAnimal == null) {
                generateNewQuestion()
            }
        }
    }

    //Lager nytt spørsmål, ett riktig og to feile svar
    // Lager nytt spørsmål: 1 riktig og 2 feil svar
    fun generateNewQuestion() {
        if (allAnimals.size < 3) return

        val animal = allAnimals.random()
        currentAnimal = animal

        val wrongAnswers = allAnimals.filter { it != animal }.shuffled().take(2)
        answers = (listOf(animal) + wrongAnswers).shuffled()

        // Nullstill resultat for UI
        showResult = false
        selectedOption = null
    }

    // Sjekker brukerens svar og oppdaterer score
    fun checkAnswer(selected: Animal) {
        selectedOption = selected.animalName
        showResult = true

        if (selected == currentAnimal) {
            quizScore++
        } else {
            quizScore = 0
            attempts++
        }
    }
}