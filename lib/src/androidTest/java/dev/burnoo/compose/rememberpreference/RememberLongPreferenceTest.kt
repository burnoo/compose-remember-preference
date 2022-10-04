package dev.burnoo.compose.rememberpreference

import android.content.Context
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.test.core.app.ApplicationProvider
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test

class RememberLongPreferenceTest {

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
        val keyName = "long-key"
        composeTestRule.setContent {
            val long = rememberLongPreference(
                keyName = keyName,
                defaultValue = 0L
            )
            BasicText(long.value.toString())
        }
        assertText("0")
    }

    @Test
    fun shouldRememberLong() = runBlocking {
        val keyName = "long-key"
        composeTestRule.setContent {
            val long = rememberLongPreference(
                keyName = keyName,
                initialValue = 0L,
                defaultValue = 0L
            )
            LaunchedEffect(Unit) {
                long.value = long.value + 1L
            }
            BasicText(long.value.toString())
        }
        assertText("1")
        val preferences = context.dataStore.data.first()
        preferences[longPreferencesKey(keyName)] shouldBe 1L
    }

    private fun assertText(text: String, index: Int = 0) {
        composeTestRule.onNode(isRoot()).onChildAt(index).assertTextEquals(text)
    }
}