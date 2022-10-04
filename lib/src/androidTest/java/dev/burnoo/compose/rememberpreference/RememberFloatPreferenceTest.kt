package dev.burnoo.compose.rememberpreference

import android.content.Context
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.test.core.app.ApplicationProvider
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test

class RememberFloatPreferenceTest {

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
        val keyName = "float-key"
        composeTestRule.setContent {
            val float = rememberFloatPreference(
                keyName = keyName,
                defaultValue = 0f
            )
            BasicText(float.value.toString())
        }
        assertText("0.0")
    }

    @Test
    fun shouldRememberFloat() = runBlocking {
        val keyName = "float-key"
        composeTestRule.setContent {
            val float = rememberFloatPreference(
                keyName = keyName,
                initialValue = 0f,
                defaultValue = 0f
            )
            LaunchedEffect(Unit) {
                float.value = float.value + 1f
            }
            BasicText(float.value.toString())
        }
        assertText("1.0")
        val preferences = context.dataStore.data.first()
        preferences[floatPreferencesKey(keyName)] shouldBe 1f
    }

    private fun assertText(text: String, index: Int = 0) {
        composeTestRule.onNode(isRoot()).onChildAt(index).assertTextEquals(text)
    }
}