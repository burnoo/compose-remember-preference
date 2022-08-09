# compose-remember-preference
[![Maven Central](https://img.shields.io/maven-central/v/dev.burnoo/compose-remember-preference)](https://search.maven.org/search?q=dev.burnoo.compose-remember-preference)

Jetpack Compose library for remembering state persistently, based on DataStore preferences. Basically it's persistent version of `remember { mutableStateOf(x) }`.

## Getting started
Library is distributed through Maven Central. To use it you need to add following dependancy to your module `build.gradle`:
```groovy
dependencies {
    implementation 'dev.burnoo:compose-remember-preference:0.3.5'
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

## Demo ([source](https://github.com/burnoo/compose-remember-preference/blob/master/sample/src/main/java/dev/burnoo/compose/rememberpreference/sample/MainActivity.kt))
![demo](https://user-images.githubusercontent.com/17478192/110863632-430a9c80-82c1-11eb-9381-cc415f3fa505.gif)

## Supported types
(the same as DataStore preferences)
- `String` - `rememberStringPreference`
- `Int` - `rememberIntPreference`
- `Boolean` - `rememberBooleanPreference`
- `Float` - `rememberFloatPreference`
- `Double` - `rememberDoublePreference`
- `Long` - `rememberLongPreference`
- `String<Set>` - `rememberStringSetPreference`

## Nullability
Each function has two versions - nullable and non nullable. When `initailValue` and `defaultValue` are non nullable it returns `MutableState<T>` otherwise `MutableState<T?>`:
- `(keyName: String, initialValue: T, defaultValue: T) -> MutableState<T>`
- `(keyName: String, initialValue: T? = null, defaultValue: T? = null) -> MutableState<T?>`
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
```
Copyright 2020 Bruno Wieczorek

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
