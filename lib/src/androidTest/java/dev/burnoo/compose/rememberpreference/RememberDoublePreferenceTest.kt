package dev.burnoo.compose.rememberpreference

import android.content.Context
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.test.core.app.ApplicationProvider
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test

class RememberDoublePreferenceTest {

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
        val keyName = "double-key"
        composeTestRule.setContent {
            val double = rememberDoublePreference(
                keyName = keyName,
                defaultValue = .0
            )
            BasicText(double.value.toString())
        }
        assertText("0.0")
    }

    @Test
    fun shouldRememberDouble() = runBlocking {
        val keyName = "double-key"
        composeTestRule.setContent {
            val double = rememberDoublePreference(
                keyName = keyName,
                initialValue = .0,
                defaultValue = .0
            )
            LaunchedEffect(Unit) {
                double.value = double.value + 1.0
            }
            BasicText(double.value.toString())
        }
        assertText("1.0")
        val preferences = context.dataStore.data.first()
        preferences[doublePreferencesKey(keyName)] shouldBe 1f
    }

    private fun assertText(text: String, index: Int = 0) {
        composeTestRule.onNode(isRoot()).onChildAt(index).assertTextEquals(text)
    }
}