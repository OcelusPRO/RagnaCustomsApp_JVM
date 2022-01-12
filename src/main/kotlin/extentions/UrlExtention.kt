package extentions

import java.io.File
import java.net.URL


/**
 * Download file from url
 * @param speed max download speed on byte per second, 0 = unlimited
 *
 * **WARNING NOT IMPLEMENTED**
 * @param outputFile file to save the downloaded file
 *
 * @return the file downloaded
 */
fun URL.download(outputFile: File, speed: Double = 0.0): File {
    outputFile.writeBytes(ByteArray(0)) //clear file

    //TODO: Implement download speed limiter
    /*val rateLimiter: RateLimiter = RateLimiter.create(if (speed == 0.0) Double.MAX_VALUE else speed)
    val inputStream: InputStream = openStream()
    println(inputStream.readAllBytes())
    var data: Int = inputStream.read()
    while (data != -1) {
        rateLimiter.acquire(1)
        outputFile.appendBytes(byteArrayOf(data.toByte()))
        data = inputStream.read()
    }
    inputStream.close()
    */
    outputFile.writeBytes(readBytes())
    return outputFile
}

