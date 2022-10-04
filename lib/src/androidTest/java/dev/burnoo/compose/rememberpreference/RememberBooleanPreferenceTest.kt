package dev.burnoo.compose.rememberpreference

import android.content.Context
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test

class RememberBooleanPreferenceTest {

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
        val keyName = "boolean-key"
        composeTestRule.setContent {
            val boolean = rememberBooleanPreference(
                keyName = keyName,
                defaultValue = false
            )
            BasicText(boolean.value.toString())
        }
        assertText("false")
    }

    @Test
    fun shouldRememberBoolean() = runBlocking {
        val keyName = "boolean-key"
        composeTestRule.setContent {
            val boolean = rememberBooleanPreference(
                keyName = keyName,
                initialValue = false,
                defaultValue = false
            )
            LaunchedEffect(Unit) {
                boolean.value = true
            }
            BasicText(boolean.value.toString())
        }
        assertText("true")
        val preferences = context.dataStore.data.first()
        preferences[booleanPreferencesKey(keyName)] shouldBe true
    }

    private fun assertText(text: String, index: Int = 0) {
        composeTestRule.onNode(isRoot()).onChildAt(index).assertTextEquals(text)
    }
}