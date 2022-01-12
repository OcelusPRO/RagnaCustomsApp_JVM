import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

val CONFIG = Configuration.loadConfiguration(File("./config.json"))

class ConfigurationException(message: String) : Exception(message)

data class TwitchConfig(
    val twitchToken: String = "",
    val twitchChannel: String = "",
    val autoStart: Boolean = false,
)

data class Configuration(
    val apiKey: String = "",
    val customSongDirectoryPath: String = System.getProperty("user.home") + "${File.separator}Documents${File.separator}Ragnarock${File.separator}CustomSongs",
    val customSongBackupDirectoryPath: String = System.getProperty("user.home") + "${File.separator}Documents${File.separator}Ragnarock${File.separator}bkp-CustomSongs",
    val twitch: TwitchConfig = TwitchConfig(),
    val downloadSpeed: Double = 0.0,
) {
    companion object {
        fun loadConfiguration(file: File): Configuration {
            if (file.createNewFile()) {
                val config = Configuration()
                file.writeText(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(config))
                throw ConfigurationException("Veuillez remplir le fichier de configuration")
            }
            return try {
                val cfg = Gson().fromJson(file.readText(), Configuration::class.java)
                file.writeText(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(cfg))
                cfg
            } catch (e: Exception) {
                throw ConfigurationException("La configuration n'est pas valide")
            }
        }
    }
}
