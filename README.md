# compose-remember-preference
Jetpack Compose library for remembering state persistently, based on DataStore preferences.

## Getting started
Library is distributed through Maven Central. To use it you need to add following dependancy to your module `build.gradle`:
```groovy
dependencies {
    implementation 'dev.burnoo:compose-remember-preference:0.2.0'
}
```
To store state in `@Composable` when app is running you would use `remember { mutableStateOf(x) }`. The library comes with the same functionality, but supports data persistence, saving and restoring data using DataStore preferences. It has it owns functions that returns `MutableState`.
```kotlin
@Composable
fun Component() {
    var string by rememberStringPreference(keyName = "stringKey")
    var int by rememberIntPreference(keyName = "intKey", defaultValue = 0)
    var boolean by rememberBooleanPreference(
        keyName = "booleanKey", initialValue = false, defaultValue = false
    )
}
```
Each function has additional value parameters, which can be used for handling all possible states:
```kotlin
@Composable
fun OnboardingExample() {
    var isOnboardingCompleted by rememberBooleanPreference(
        keyName = "onboardingKey", // preference is stored using this key
        initialValue = null, // returned before preference is loaded
        defaultValue = false, // returned when preference is not set yet
    )
    when (isOnboardingCompleted) {
        null -> Loader()
        false -> Onboarding(onCompleted = { isOnboardingCompleted = true })
        true -> MainScreen()
    }
}
```

## Demo
![demo](https://user-images.githubusercontent.com/17478192/110863632-430a9c80-82c1-11eb-9381-cc415f3fa505.gif)

## Supported types
(the same as DataStore preferences)
- `String` - `rememberStringByPreference`
- `Int` - `rememberIntByPreference`
- `Boolean` - `rememberBooleanByPreference`
- `Float` - `rememberFloatByPreference`
- `Double` - `rememberDoubleByPreference`
- `Float` - `rememberFloatByPreference`
- `Long` - `rememberLongByPreference`
- `String<Set>` - `rememberStringSetByPreference`

## Nullability
Each function has two versions nullable and non nullable. When `initailValue` and `defaultValue` are non nullable it returns `MutableState<T>` otherwise `MutableState<T?>`
```kotlin
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
```

## License
TODO
