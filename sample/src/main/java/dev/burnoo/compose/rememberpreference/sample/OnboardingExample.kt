package dev.burnoo.compose.rememberpreference.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dev.burnoo.compose.rememberpreference.rememberBooleanPreference

@Composable
fun OnboardingExample() {
    var isOnboardingCompleted by rememberBooleanPreference(
        keyName = "onboardingKey",
        initialValue = null, // returned before preference is loaded
        defaultValue = false, // returned when preference is not set yet
    )
    when (isOnboardingCompleted) {
        null -> Loader()
        false -> Onboarding(onCompleted = { isOnboardingCompleted = true })
        true -> MainScreen()
    }
}

@Composable
fun Loader() = Unit

@Composable
fun Onboarding(onCompleted: () -> Unit) = Unit

@Composable
fun MainScreen() = Unit