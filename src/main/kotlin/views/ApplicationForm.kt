package views

import APP
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
fun APPLICATION_FORM() {
    Window(
        onCloseRequest = APP::exitApplication,
        title = "Ragnacustoms",
        state = WindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            height = 800.dp,
            width = 1200.dp
        ),
    ) {
        openedForm.add("ApplicationForm")

        // Start ragnacustoms services
        // Start Main form
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.DarkGray
            ) {
                Button(
                    onClick = ::onButtonClick,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text("Start twitch bot")
                }
            }
        }
    }
}

@Composable
fun onButtonClick(){
    if (!openedForm.contains("TwitchForm")) TWITCH_FORM()
}
