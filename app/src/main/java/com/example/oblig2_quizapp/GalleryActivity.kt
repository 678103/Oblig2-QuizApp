package com.example.oblig2_quizapp

import androidx.compose.foundation.lazy.items
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.oblig2_quizapp.room.Animal
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


class GalleryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Henter GalleryViewModel
        val viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]

        setContent {
            //Observerer LiveData fra ViewModel
            val animals by viewModel.animals.observeAsState(listOf())
            GalleryScreen(animals, viewModel)
        }
    }

    //Lager UI med composable
    @Composable
    fun GalleryScreen(animals: List<Animal>, viewModel: GalleryViewModel) {

        val context = LocalContext.current

        //Husker navnet på nytt dyr
        var newAnimalName by remember { mutableStateOf("") }
        var showDialog by remember { mutableStateOf(false) }
        var selectedURI by remember { mutableStateOf("") }


        //Launcher for å hente URI fra bruker
        val pickImage =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    selectedURI = it.toString() //lagrer URI som en streng
                    showDialog = true //viser input for å skrive navn
                }
            }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            // Knapp for å legge til nytt bilde
            Button(
                onClick = { pickImage.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add an image")
            }

            // Dialog for å skrive inn navn
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Navn på dyret") },
                    text = {
                        TextField(
                            value = newAnimalName,
                            onValueChange = { newAnimalName = it },
                            placeholder = { Text("Give the image a name!") }
                        )
                    },
                    confirmButton = {
                        Button(onClick = {
                            if (newAnimalName.isNotBlank()) {
                                // Legg til i databasen via ViewModel
                                viewModel.insertAnimal(
                                    Animal(
                                        animalName = newAnimalName,
                                        imageUri = selectedURI
                                    )
                                )
                                newAnimalName = ""
                                showDialog = false
                            } else {
                                Toast.makeText(context, "Skriv inn navn!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }) {
                            Text("Legg til")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            showDialog = false
                            newAnimalName = ""
                            selectedURI = ""
                        }) {
                            Text("Avbryt")
                        }
                    }
                )
            }

            // Viser listen av dyr
            LazyColumn {
                items(animals) { animal ->
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(animal.animalName, modifier = Modifier.weight(1f))

                        //sjekker hvilket type bilde det er
                        if (!animal.imageUri.isNullOrBlank()) {
                            Image(
                                painter = rememberAsyncImagePainter(animal.imageUri),
                                contentDescription = animal.animalName,
                                modifier = Modifier.size(80.dp)
                            )
                        } else if (animal.idDrawable != null) {
                            Image(
                                painter = painterResource(id = animal.idDrawable),
                                contentDescription = animal.animalName,
                                modifier = Modifier.size(80.dp)
                            )
                        }
                        Button(onClick = { viewModel.deleteAnimal(animal.animalName) }) {
                            Text("Slett")
                        }
                    }
                }
            }
        }
    }
}