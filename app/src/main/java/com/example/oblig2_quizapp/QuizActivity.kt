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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.items

class QuizActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val quizViewModel : QuizViewModel = viewModel()
            QuizScreen(quizViewModel)
        }
    }
}
        @Composable
        fun QuizScreen(viewModel: QuizViewModel) {

            val animals = viewModel.allAnimals
            val currentAnimal = viewModel.currentAnimal
            val answers = viewModel.answers

            var showResult by remember { mutableStateOf(false) }
            var isCorrect by remember { mutableStateOf(false) }

            //sjekker om det er færre enn tre dyr
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                // Sjekk om det er færre enn 3 dyr
                if (animals.size < 3) {
                    item {
                        Text("Det må være minst 3 bilder i galleriet for å starte quiz!")
                    }
                } else {
                    // Score og forsøk
                    item {
                        Text("Poeng: ${viewModel.quizScore}")
                        Text("Forsøk: ${viewModel.attempts}")
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Bildet
                    currentAnimal?.let { animal ->
                        item {
                            if (!animal.imageUri.isNullOrBlank()) {
                                Image(
                                    painter = rememberAsyncImagePainter(animal.imageUri),
                                    contentDescription = animal.animalName,
                                    modifier = Modifier.size(200.dp)
                                )
                            } else if (animal.idDrawable != null) {
                                Image(
                                    painter = painterResource(id = animal.idDrawable),
                                    contentDescription = animal.animalName,
                                    modifier = Modifier.size(200.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // Spørsmål
                    item {
                        Text("Hvilket dyr er på bildet?", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Svar-knapper
                    // Svar-knapper
                    items(answers) { answer ->
                        Button(
                            onClick = {
                                viewModel.checkAnswer(answer)
                                isCorrect = (answer == currentAnimal)
                                showResult = true
                            },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Text(answer.animalName)
                        }
                    }


                    // Resultat og neste spørsmål
                    if (showResult) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                if (isCorrect) "Riktig!" else "Feil! Riktig svar: ${currentAnimal?.animalName}",
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                viewModel.generateNewQuestion()
                                showResult = false
                            }) {
                                Text("Neste spørsmål")
                            }
                        }
                    }
                }
            }
        }




