package interfaces

import CONFIG
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import extentions.download
import objects.Directories
import objects.Urls
import java.io.File
import java.net.URL
import java.util.zip.ZipFile


class SongData() {
    var id: Int = 0
    var name: String = ""
    var levels: List<LevelData> = listOf()
    var author: String = ""
    var mapper: String = ""
    var hash: String = ""
    var folder: File? = null
    var infoData: InfoData? = null

    fun voteForSong(note: Int): Pair<Boolean, String> {
        URL("${Urls.voteUrl}?id=$id&note=$note&key=${CONFIG.apiKey}").openConnection().getInputStream().use {
            return if (String(it.readBytes()).contains("success")) Pair(true, "") else Pair(false,
                String(it.readBytes()))
        }
    }

    fun addSongOnCustomFolder() {
        val backupSong = Directories.customSongsBackupDirectory.listFiles()?.find { song ->
            song.isDirectory && SongData.fromDirectory(song).id == id
        }
        if (backupSong != null) copySong(backupSong)
        else downloadSong()
    }

    private fun downloadSong() {
        val url = URL("${Urls.downloadUrl}/$id")
        val downloaded = url.download(File(
            Directories.customSongDirectory.absolutePath
                    + File.separator
                    + name
                    + author
                    + mapper
        ), CONFIG.downloadSpeed)
        ZipFile(downloaded).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    File(entry.name).outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
        downloaded.delete()
    }

    private fun copySong(folder: File) {
        folder.copyRecursively(File(Directories.customSongDirectory.path + File.separator + folder.name))
    }

    companion object {
        fun fromDirectory(file: File): SongData {
            if (!file.isDirectory) throw IllegalArgumentException("File is not a directory")
            val songData = SongData()
            songData.folder = file
            songData.id = file.listFiles()?.first { it.name == ".id" }?.readText()?.toInt()
                ?: throw IllegalArgumentException("No .id file found")

            val infoData = InfoData.fromFile(file.listFiles()?.first { it.name == "info.dat" }
                ?: throw IllegalArgumentException("No info.dat file found"))
            songData.name = infoData._songName
            songData.author = infoData._songAuthorName
            songData.mapper = infoData._levelAuthorName
            songData.hash = file.listFiles()?.first { it.name == ".hash" }?.readText()
                ?: throw IllegalArgumentException("No hash found")
            songData.infoData = infoData
            songData.levels = mutableListOf()
            file.listFiles()?.filter { it.extension.endsWith("dat") && it.nameWithoutExtension != "info" }?.forEach {
                (songData.levels as MutableList<LevelData>).add(LevelData.fromFile(it))
            } ?: throw IllegalArgumentException("No level files found")

            return songData
        }

        fun fromId(id: Int): SongData {
            val song = Directories.customSongDirectory.listFiles()
                ?.find { song -> song.isDirectory && SongData.fromDirectory(song).id == id }
                ?: Directories.customSongsBackupDirectory.listFiles()
                    ?.find { song -> song.isDirectory && SongData.fromDirectory(song).id == id }
            if (song != null) return fromDirectory(song)
            val data = URL("${Urls.songUrl}/$id").readText()
            return Gson().fromJson(data, SongData::class.java)
        }
    }
}

/*
*  Information on info.dat
* */
data class InfoData(
    @SerializedName("_version") val _version: String,
    @SerializedName("_songName") val _songName: String,
    @SerializedName("_songSubName") val _songSubName: String,
    @SerializedName("_songAuthorName") val _songAuthorName: String,
    @SerializedName("_levelAuthorName") val _levelAuthorName: String,
    @SerializedName("_beatsPerMinute") val _beatsPerMinute: Int,
    @SerializedName("_shuffle") val _shuffle: Int,
    @SerializedName("_shufflePeriod") val _shufflePeriod: Double,
    @SerializedName("_previewStartTime") val _previewStartTime: Int,
    @SerializedName("_previewDuration") val _previewDuration: Int,
    @SerializedName("_songApproximativeDuration") val _songApproximativeDuration: Int,
    @SerializedName("_songFilename") val _songFilename: String,
    @SerializedName("_coverImageFilename") val _coverImageFilename: String,
    @SerializedName("_environmentName") val _environmentName: String,
    @SerializedName("_songTimeOffset") val _songTimeOffset: Int,
    @SerializedName("_customData") val _customData: _customData,
    @SerializedName("_difficultyBeatmapSets") val _difficultyBeatmapSets: List<_difficultyBeatmapSets>,
) {
    companion object {
        fun fromFile(file: File): InfoData {
            val json = file.readText()
            return fromJson(json)
        }

        fun fromJson(json: String): InfoData {
            return Gson().fromJson(json, InfoData::class.java)
        }
    }
}

data class MMA2(
    @SerializedName("version") val version: String,
)

data class _editors(
    @SerializedName("MMA2") val mMA2: MMA2,
    @SerializedName("_lastEditedBy") val _lastEditedBy: String,
)

data class _difficultyBeatmapSets(
    @SerializedName("_beatmapCharacteristicName") val _beatmapCharacteristicName: String,
    @SerializedName("_difficultyBeatmaps") val _difficultyBeatmaps: List<_difficultyBeatmaps>,
)

data class _difficultyBeatmaps(
    @SerializedName("_difficulty") val _difficulty: String,
    @SerializedName("_difficultyRank") val _difficultyRank: Int,
    @SerializedName("_beatmapFilename") val _beatmapFilename: String,
    @SerializedName("_noteJumpMovementSpeed") val _noteJumpMovementSpeed: Int,
    @SerializedName("_noteJumpStartBeatOffset") val _noteJumpStartBeatOffset: Int,
    @SerializedName("_customData") val _customData: _customData,
)

data class _customData(
    @SerializedName("_editorOffset") val _editorOffset: Int,
    @SerializedName("_editorOldOffset") val _editorOldOffset: Int,
    @SerializedName("_warnings") val _warnings: List<String>,
    @SerializedName("_information") val _information: List<String>,
    @SerializedName("_suggestions") val _suggestions: List<String>,
    @SerializedName("_requirements") val _requirements: List<String>,
)
//endregion

/*
* Level information
* */
data class LevelData(
    @SerializedName("_version") val version: String,
    @SerializedName("_customData") val customData: CustomData,
    @SerializedName("_events") val events: List<String>,
    @SerializedName("_notes") val notes: List<Notes>,
    @SerializedName("_obstacles") val obstacles: List<String>,
    @SerializedName("_waypoints") val waypoints: List<String>,
) {
    companion object {
        fun fromFile(file: File): LevelData {
            val json = file.readText()
            return fromJson(json)
        }

        fun fromJson(json: String): LevelData {
            return Gson().fromJson(json, LevelData::class.java)
        }
    }
}

data class CustomData(
    @SerializedName("_BPMChanges") val bpmChanges: List<BPMChanges>,
    @SerializedName("_bookmarks") val bookmarks: List<String>,
)

data class BPMChanges(
    @SerializedName("_BPM") val bpm: Double,
    @SerializedName("_time") val time: Int,
    @SerializedName("_beatsPerBar") val beatsPerBar: Int,
    @SerializedName("_metronomeOffset") val metronomeOffset: Int,
)

data class Notes(
    @SerializedName("_time") val time: Int,
    @SerializedName("_lineIndex") val lineIndex: Int,
    @SerializedName("_lineLayer") val lineLayer: Int,
    @SerializedName("_type") val type: Int,
    @SerializedName("_cutDirection") val cutDirection: Int,
)
//endregion