package com.example.oblig2_quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.testTag

class QuizActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizScreen()
        }
    }
}
        @Composable
        fun QuizScreen(quizViewModel : QuizViewModel = viewModel()) {

            //observeAsState er en bro mellom LiveData og Compose

            //emptyList() gir en startverdi
            val animals by quizViewModel.allAnimals.observeAsState(emptyList())

            //lager første spørsmål når vi minst har 3 dyr
            if(animals.size >= 3 && quizViewModel.currentAnimal ==null) {
                quizViewModel.generateNewQuestion(animals)
            }

            //riktig dyr som skal vises i quizen
            val currentAnimal = quizViewModel.currentAnimal

            //svaralternativer som vises som knapper
            val answers = quizViewModel.answers


            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                item{
                    Text("QuizApp!", modifier = Modifier.testTag("quizApp_text"))
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Sjekk om det er færre enn 3 dyr
                if (animals.size < 3) {
                    item {
                        Text("Det må være minst 3 bilder i galleriet for å starte quiz!")
                    }
                }
                else {
                    // Score (med test) og antall forsøk
                    item {
                        Text(
                            text = "Score: ${quizViewModel.quizScore}",
                            modifier = Modifier.testTag("score_test"))

                        Text("Forsøk: ${quizViewModel.attempts}")
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Bilde i quizen
                    currentAnimal?.let { animal ->
                        item {
                            if (animal.imageUri.isNotBlank()) {
                                //tegner bildet opp på skjermen med image
                                Image(
                                    painter = rememberAsyncImagePainter(animal.imageUri),
                                    contentDescription = animal.animalName,
                                    modifier = Modifier.size(200.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // Fast spørsmålstekst
                    item {
                        Text("Hvilket dyr er på bildet?", fontSize = 18.sp)

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Lager en knapp med hvert svaralternativ
                    // med testTag for å legge inn testSvar
                    items(answers) { answer ->
                        Button(
                            onClick = {
                               quizViewModel.checkAnswer(answer)

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag("answer_${answer.animalName}")
                        ) {
                            Text(answer.animalName)
                        }
                    }

                    // Resultat etter at bruker har svart
                    if (quizViewModel.showResult) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                if (quizViewModel.selectedOption == currentAnimal?.animalName)
                                    "Riktig!"
                                    else
                                        "Feil! Riktig svar: ${currentAnimal?.animalName}",
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            //lager nytt spørsmål når bruker trkker neste
                            Button(onClick = {
                                quizViewModel.generateNewQuestion(animals)

                            }) {

                                Text("Neste spørsmål")
                            }
                        }
                    }
                }
            }
        }




