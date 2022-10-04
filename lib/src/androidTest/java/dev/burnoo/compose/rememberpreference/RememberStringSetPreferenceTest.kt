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

class RememberStringSetPreferenceTest {

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
        val keyName = "string-set-key"
        val initialStringSet = setOf("initialText")
        composeTestRule.setContent {
            val stringSet = rememberStringSetPreference(
                keyName = keyName,
                defaultValue = initialStringSet
            )
            BasicText(stringSet.value.toString())
        }
        assertText(initialStringSet.toString())
    }

    @Test
    fun shouldRememberString() = runBlocking {
        val keyName = "string-set-key"
        val newStringSet = setOf("newText")
        composeTestRule.setContent {
            val stringSet = rememberStringSetPreference(
                keyName = keyName,
                initialValue = setOf("initialText"),
                defaultValue = setOf("defaultText")
            )
            LaunchedEffect(Unit) {
                stringSet.value = newStringSet
            }
            BasicText(stringSet.value.toString())
        }
        assertText(newStringSet.toString())
        val preferences = context.dataStore.data.first()
        preferences[stringPreferencesKey(keyName)] shouldBe newStringSet
    }

    private fun assertText(text: String, index: Int = 0) {
        composeTestRule.onNode(isRoot()).onChildAt(index).assertTextEquals(text)
    }
}