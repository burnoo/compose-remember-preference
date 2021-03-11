package dev.burnoo.compose.rememberpreference.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dev.burnoo.compose.rememberpreference.rememberBooleanPreference
import dev.burnoo.compose.rememberpreference.rememberIntPreference
import dev.burnoo.compose.rememberpreference.rememberStringPreference

@Composable
fun Component() {
    var string by rememberStringPreference(keyName = "stringKey")
    var int by rememberIntPreference(keyName = "intKey", defaultValue = 0)
    var boolean by rememberBooleanPreference(
        keyName = "booleanKey", initialValue = false, defaultValue = false
    )
}