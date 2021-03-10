package dev.burnoo.compose.rememberpreference.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import dev.burnoo.compose.rememberpreference.sample.ui.theme.ComposerememberpreferenceTheme
import dev.burnoo.compose.rememberpreferences.rememberIntPreference

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposerememberpreferenceTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Clicker()
                }
            }
        }
    }
}

@Composable
fun Clicker() {
    var count by rememberIntPreference(keyName = "countKey", initialValue = 0, defaultValue = 0)

    Button(onClick = { count++ }) {
        Text(text = count.toString())
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposerememberpreferenceTheme {
        Greeting("Android")
    }
}