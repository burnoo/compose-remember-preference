package dev.burnoo.compose.rememberpreference.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import dev.burnoo.compose.rememberpreference.rememberStringPreference

@Composable
fun NullabilityExample() {
    val nullableStringState: MutableState<String?> = rememberStringPreference(
        keyName = "keyS1",
        initialValue = null, // null is default
        defaultValue = null // null is default
    )
    val stringState: MutableState<String> = rememberStringPreference(
        keyName = "keyS2",
        initialValue = "Loading",
        defaultValue = "Default"
    )
}