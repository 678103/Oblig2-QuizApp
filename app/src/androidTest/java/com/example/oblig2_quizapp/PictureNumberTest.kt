package com.example.oblig2_quizapp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsRule
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PictureNumberTest {

    @get:Rule
    val intentsRule = IntentsRule()

    @get:Rule
    val composeRule = createAndroidComposeRule<GalleryActivity>()

    @Before
    fun setup() {
        // Lager falsk intent intent-resultat (bilde som skal velges automatisk)
        val resultData = Intent().apply {
            data = Uri.parse(
                "android.resource://com.example.oblig2_quizapp/drawable/cat")
        }
        //lager det som et activityResult, at brukeren faktisk valgte bilde og resultdata
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

        // Stub Intent - returnerer cat-bildet uten å da åpne bildevelgeren
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result)
    }

    @Test
    fun add_and_delete_UpdatesCount() {

        val viewModel = composeRule.activity.viewModels<GalleryViewModel>().value
        val startCount = viewModel.getAnimalCount()

        // Klikk add-knappen - stub returnerer bilde automatisk
        composeRule.onNodeWithTag("add_image_button").performClick()

        // Måtte legge inn sleep slik at dialog skal rekke å vises
        Thread.sleep(2000)

        // Skriv navn og bekreft
        composeRule.onNodeWithTag("animal_name_field").performTextInput("testanimal")
        composeRule.onNodeWithTag("confirm_button").performClick()

        // Ny thread fordi den skal rekke å oppdatere databasen
        Thread.sleep(1000)
        assertEquals(startCount + 1, viewModel.getAnimalCount())

        // Slett og sjekk
        composeRule.onNodeWithTag("delete_testanimal").performClick()

        //ny sleep for at den skal regge å slettes
        Thread.sleep(1000)
        assertEquals(startCount, viewModel.getAnimalCount())
    }
}