package objects

import CONFIG
import java.io.File

object Directories {
    val customSongDirectory: File = File(CONFIG.customSongDirectoryPath)
    val customSongsBackupDirectory: File = File(CONFIG.customSongBackupDirectoryPath)
}
