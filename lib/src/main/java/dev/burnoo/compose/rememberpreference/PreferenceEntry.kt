package dev.burnoo.compose.rememberpreference

internal sealed class PreferenceEntry<out T> {
    data class NotEmpty<T>(val value: T) : PreferenceEntry<T>()
    object Empty : PreferenceEntry<Nothing>()
    object NotLoaded : PreferenceEntry<Nothing>()

    companion object {
        inline fun <reified T> fromNullable(value: T?) =
            if (value == null) Empty else NotEmpty(value)
    }
}

internal fun <T> PreferenceEntry<T>.getValue(initialValue: T, defaultValue: T): T = when (val currentEntry = this) {
    is PreferenceEntry.NotLoaded -> initialValue
    is PreferenceEntry.Empty -> defaultValue
    is PreferenceEntry.NotEmpty -> currentEntry.value
}
