package views

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import openedForm

@Preview
@Composable
fun TWITCH_FORM() {
    var isOpen by remember { mutableStateOf(true) }
    if (isOpen) {
        Window(
            onCloseRequest = { isOpen = false },
            title = "Twitch bot",
            state = WindowState(
                position = WindowPosition.Aligned(Alignment.Center),
                height = 600.dp,
                width = 800.dp
            ),
        ) {
            openedForm.add("TwitchForm")
            // Start twitch bot

            // Start twitch bot form
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF2F4F4F)
                ) {

                }
            }
        }
    }
}
