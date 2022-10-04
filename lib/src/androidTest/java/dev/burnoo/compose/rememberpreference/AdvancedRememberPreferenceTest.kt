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
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test

class AdvancedRememberPreferenceTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @After
    fun tearDown() {
        runBlocking { context.dataStore.edit { it.clear() } }
    }

    @Test
    fun shouldUpdateStateWhenDataStoreIsChanged() {
        val keyName = "string-key"
        val newText = "newText"
        composeTestRule.setContent {
            val string = rememberStringPreference(
                keyName = keyName,
                initialValue = "initialText",
                defaultValue = "defaultText"
            )
            LaunchedEffect(Unit) {
                context.dataStore.edit {
                    it[stringPreferencesKey(keyName)] = newText
                }
            }
            BasicText(string.value)
        }
        assertText(newText)
    }

    private fun assertText(text: String, index: Int = 0) {
        composeTestRule.onNode(isRoot()).onChildAt(index).assertTextEquals(text)
    }
}