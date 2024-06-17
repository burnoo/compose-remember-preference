package dev.burnoo.compose.rememberpreference

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal val Context.dataStore by preferencesDataStore(name = "RememberPreference")

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
    crossinline getPreferencesKey: (keyName: String) -> Preferences.Key<NNT>,
): MutableState<T> {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    return remember {
        preferenceMutableState(
            coroutineScope = coroutineScope,
            context = context,
            keyName = keyName,
            initialValue = initialValue,
            defaultValue = defaultValue,
            getPreferencesKey = getPreferencesKey
        )
    }
}

private inline fun <reified T, reified NNT : T> preferenceMutableState(
    coroutineScope: CoroutineScope,
    context: Context,
    keyName: String,
    initialValue: T,
    defaultValue: T,
    getPreferencesKey: (keyName: String) -> Preferences.Key<NNT>,
): MutableState<T> {
    val preferenceEntryMutableState: MutableState<PreferenceEntry<T>> = mutableStateOf(PreferenceEntry.NotLoaded)
    val key: Preferences.Key<NNT> = getPreferencesKey(keyName)
    coroutineScope.launch {
        context.dataStore.data
            .map { it[key] }
            .distinctUntilChanged()
            .map { PreferenceEntry.fromNullable(it) }
            .collectLatest { preferenceEntryMutableState.value = it }
    }

    return object : MutableState<T> {
        override var value: T
            get() = preferenceEntryMutableState.value.getValue(initialValue, defaultValue)
            set(value) {
                val rollbackValue = preferenceEntryMutableState.value
                preferenceEntryMutableState.value = PreferenceEntry.fromNullable(value)
                coroutineScope.launch {
                    try {
                        context.dataStore.edit {
                            if (value != null) {
                                it[key] = value as NNT
                            } else {
                                it.remove(key)
                            }
                        }
                    } catch (e: Exception) {
                        preferenceEntryMutableState.value = rollbackValue
                    }
                }
            }

        override fun component1() = value
        override fun component2(): (T) -> Unit = { value = it }
    }
}
