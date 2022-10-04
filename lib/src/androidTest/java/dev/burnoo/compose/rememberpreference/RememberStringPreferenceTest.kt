package dev.burnoo.compose.rememberpreference

import android.content.Context
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.test.core.app.ApplicationProvider
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test

class RememberStringPreferenceTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @After
    fun tearDown() {
        runBlocking { context.dataStore.edit { it.clear() } }
    }

    @Test
    fun shouldDisplayDefaultValue() {
        val keyName = "string-key"
        val initialText = "initialText"
        composeTestRule.setContent {
            val string = rememberStringPreference(
                keyName = keyName,
                defaultValue = initialText
            )
            BasicText(string.value.toString())
        }
        assertText(initialText)
    }

    @Test
    fun shouldRememberString() = runBlocking {
        val keyName = "string-key"
        val newText = "newText"
        composeTestRule.setContent {
            val string = rememberStringPreference(
                keyName = keyName,
                initialValue = "initialText",
                defaultValue = "defaultText"
            )
            LaunchedEffect(Unit) {
                string.value = newText
            }
            BasicText(string.value)
        }
        assertText(newText)
        val preferences = context.dataStore.data.first()
        preferences[stringPreferencesKey(keyName)] shouldBe newText
    }

    private fun assertText(text: String, index: Int = 0) {
        composeTestRule.onNode(isRoot()).onChildAt(index).assertTextEquals(text)
    }
}