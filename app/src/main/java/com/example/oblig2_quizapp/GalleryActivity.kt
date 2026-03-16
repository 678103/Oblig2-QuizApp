package com.example.oblig2_quizapp

import android.content.Intent
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
import com.example.oblig2_quizapp.room.Animal
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter


class GalleryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GalleryScreen()
        }
    }


    @Composable
    fun GalleryScreen() {

        //henter galleryViewModel
        val galleryViewModel: GalleryViewModel = viewModel()

        //Observerer LiveData fra ViewModel
        val animals by galleryViewModel.animals.observeAsState(listOf())

        val context = LocalContext.current

        //Husker navnet på nytt dyr
        var newAnimalName by remember { mutableStateOf("") }
        var showDialog by remember { mutableStateOf(false) }
        var selectedURI by remember { mutableStateOf("") }


        //Launcher for å åpne telefonens bildebibliotek (underliggende intent)
        val pickImage =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent())
            { uri -> //sjekker om bruker velger et bilde
                uri?.let {
                    try {
                        context.contentResolver.takePersistableUriPermission(
                            it,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    } catch (e: Exception) {
                        // ignorerer feil for stubbet URI i tester
                    }
                    selectedURI = it.toString() //lagrer URI som en streng
                    showDialog = true //viser input for å skrive navn
                }
            }
        // Dialog for å skrive inn navn
        if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Name") },
                    text = {
                        TextField(
                            value = newAnimalName,
                            onValueChange = { newAnimalName = it },
                            placeholder = { Text("Give the image a name!") },

                            //test for å klare "Picturenumbertest
                            modifier = Modifier.testTag("animal_name_field")
                        )
                    },
                    //Bekreft-knapp
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newAnimalName.isNotBlank()) {

                                    // Legg til i databasen med ViewModel
                                    galleryViewModel.insertAnimal(
                                        Animal(animalName = newAnimalName, imageUri = selectedURI)
                                    )
                                    newAnimalName = ""
                                    showDialog = false
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Add a name!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier.testTag("confirm_button") //TEST-del legg til
                        ) {
                            Text("Add image")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick =
                                {
                                    showDialog = false
                                    newAnimalName = ""
                                    selectedURI = ""
                                }) {
                            Text("Go back")
                        }
                    }
                )
            }


        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            item {

                // Knapp for å legge til nytt bilde
                Button(
                    onClick = { pickImage.launch("image/*") },
                    modifier = Modifier.fillMaxWidth().testTag("add_image_button")
                ) {
                    Text("Add an image")
                }
            }

            items(animals) {
                animal ->
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(animal.animalName, modifier = Modifier.weight(1f))

                    //tegner opp bilde
                    if (animal.imageUri.isNotBlank()) {
                        Image(
                            //lastes opp async fra Coil-biblioteket
                            painter = rememberAsyncImagePainter(animal.imageUri),
                            contentDescription = animal.animalName,
                            modifier = Modifier.size(120.dp)
                        )
                    }
                    Button(onClick = {
                        galleryViewModel.deleteAnimal(animal.animalName)},
                        modifier = Modifier.testTag("delete_${animal.animalName}") //TEST slett
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}


