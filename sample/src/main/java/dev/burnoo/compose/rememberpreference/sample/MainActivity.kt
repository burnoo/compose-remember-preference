package dev.burnoo.compose.rememberpreference.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.burnoo.compose.rememberpreference.rememberIntPreference
import dev.burnoo.compose.rememberpreference.rememberStringPreference
import dev.burnoo.compose.rememberpreference.sample.ui.theme.AppTheme

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    App()
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun App() {
    var count by rememberIntPreference(
        keyName = "countKey",
        initialValue = 0,
        defaultValue = 0
    )
    val (text, setText) = rememberStringPreference(keyName = "textKey", defaultValue = "Hello!")

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Clicker(count, onClick = { count++ })
        AnimatedVisibility(visible = text != null) {
            TextField(
                value = text.orEmpty(),
                onValueChange = setText,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun Clicker(count: Int = 0, onClick: () -> Unit = {}) {
    Button(onClick = onClick) {
        Text(text = count.toString())
    }
}