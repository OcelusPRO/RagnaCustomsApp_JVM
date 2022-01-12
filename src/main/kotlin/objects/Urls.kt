package objects

object Urls {
    private const val RagnacustomsApiBaseUrl = "https://ragnacustoms.com"
    val searchURL = "$RagnacustomsApiBaseUrl/api/search"
    val songUrl = "$RagnacustomsApiBaseUrl/api/song"
    val downloadUrl = "$RagnacustomsApiBaseUrl/songs/download"
    val voteUrl = "$RagnacustomsApiBaseUrl/api/vote"
}
