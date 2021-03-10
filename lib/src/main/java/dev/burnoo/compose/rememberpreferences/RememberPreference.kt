package dev.burnoo.compose.rememberpreferences

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore(name = "ComposableDataStore")

@Composable
fun rememberIntPreference(
    keyName: String,
    initialValue: Int? = null,
    defaultValue: Int? = null,
): MutableState<Int?> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::intPreferencesKey
)

@Composable
fun rememberIntPreference(
    keyName: String,
    initialValue: Int,
    defaultValue: Int,
): MutableState<Int> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::intPreferencesKey
)

@Composable
fun rememberDoublePreference(
    keyName: String,
    initialValue: Double? = null,
    defaultValue: Double? = null,
): MutableState<Double?> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::doublePreferencesKey
)

@Composable
fun rememberDoublePreference(
    keyName: String,
    initialValue: Double,
    defaultValue: Double,
): MutableState<Double> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::doublePreferencesKey
)

@Composable
fun rememberStringPreference(
    keyName: String,
    initialValue: String? = null,
    defaultValue: String? = null,
): MutableState<String?> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::stringPreferencesKey
)

@JvmName("rememberStringPreferenceNotNull")
@Composable
fun rememberStringPreference(
    keyName: String,
    initialValue: String,
    defaultValue: String,
): MutableState<String> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::stringPreferencesKey
)


@Composable
fun rememberBooleanPreference(
    keyName: String,
    initialValue: Boolean? = null,
    defaultValue: Boolean? = null,
): MutableState<Boolean?> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::booleanPreferencesKey
)

@Composable
fun rememberBooleanPreference(
    keyName: String,
    initialValue: Boolean,
    defaultValue: Boolean,
): MutableState<Boolean> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::booleanPreferencesKey
)

@Composable
fun rememberFloatPreference(
    keyName: String,
    initialValue: Float? = null,
    defaultValue: Float? = null,
): MutableState<Float?> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::floatPreferencesKey
)

@Composable
fun rememberFloatPreference(
    keyName: String,
    initialValue: Float,
    defaultValue: Float,
): MutableState<Float> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::floatPreferencesKey
)

@Composable
fun rememberLongPreference(
    keyName: String,
    initialValue: Long? = null,
    defaultValue: Long? = null,
): MutableState<Long?> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::longPreferencesKey
)

@Composable
fun rememberLongPreference(
    keyName: String,
    initialValue: Long,
    defaultValue: Long,
): MutableState<Long> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::longPreferencesKey
)

@Composable
fun rememberStringSetPreference(
    keyName: String,
    initialValue: Set<String>? = null,
    defaultValue: Set<String>? = null,
): MutableState<Set<String>?> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::stringSetPreferencesKey
)

@JvmName("rememberStringSetPreferenceNotNull")
@Composable
fun rememberStringSetPreference(
    keyName: String,
    initialValue: Set<String>,
    defaultValue: Set<String>,
): MutableState<Set<String>> = rememberPreference(
    keyName,
    initialValue,
    defaultValue,
    ::stringSetPreferencesKey
)

@Composable
private inline fun <reified T, reified NNT : T> rememberPreference(
    keyName: String,
    initialValue: T,
    defaultValue: T,
    getPreferencesKey: (keyName: String) -> Preferences.Key<NNT>,
): MutableState<T> {
    val currentState: MutableState<PreferenceEntry<T>> =
        remember { mutableStateOf(PreferenceEntry.NotLoaded()) }
    val coroutineScope = rememberCoroutineScope()
    val key: Preferences.Key<NNT> = getPreferencesKey(keyName)
    val context = LocalContext.current
    context.dataStore.data
        .map { PreferenceEntry.fromNullable(it[key]) }
        .onEach {
            if (currentState.value is PreferenceEntry.NotLoaded) {
                currentState.value = it
            }
        }
        .collectAsState(initial = PreferenceEntry.NotLoaded())

    return object : MutableState<T> {
        override var value: T
            get() = when (val currentStateValue = currentState.value) {
                is PreferenceEntry.NotLoaded -> initialValue
                is PreferenceEntry.Empty -> defaultValue
                is PreferenceEntry.NotEmpty -> currentStateValue.value
            }
            set(value) {
                currentState.value = PreferenceEntry.fromNullable(value)
                coroutineScope.launch {
                    context.dataStore.edit {
                        if (value != null) {
                            it[key] = value as NNT
                        } else {
                            it.remove(key)
                        }
                    }
                }
            }

        override fun component1() = value
        override fun component2(): (T) -> Unit = { value = it }
    }
}

private sealed class PreferenceEntry<out T> {
    data class NotEmpty<T>(val value: T) : PreferenceEntry<T>()
    class Empty<T> : PreferenceEntry<T>()
    class NotLoaded<T> : PreferenceEntry<T>()

    companion object {
        inline fun <reified T> fromNullable(value: T?) =
            if (value == null) Empty() else NotEmpty(value)
    }
}