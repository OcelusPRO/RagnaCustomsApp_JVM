import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.application
import views.APPLICATION_FORM
import views.TWITCH_FORM

lateinit var APP: ApplicationScope;
val openedForm = mutableListOf<String>()

fun main() = application {
    APP = this
    APPLICATION_FORM()
    if (CONFIG.twitch.autoStart) TWITCH_FORM()
}





