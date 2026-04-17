package com.inhohwangg.videodownloader.data

import android.content.Context
import android.os.Environment
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.mapper.VideoInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DownloadRepository(private val context: Context) {
    private val downloadDir: File get() {
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "VideoDownloader")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    suspend fun download(url: String, onProgress: (Float) -> Unit, onLog: (String) -> Unit): File? = withContext(Dispatchers.IO) {
        val request = YoutubeDLRequest(url).apply {
            addOption("-f", "bestvideo[height>=1080]+bestaudio/bestvideo+bestaudio/best")
            addOption("--merge-output-format", "mp4")
            addOption("-o", "${downloadDir.absolutePath}/%(title)s.%(ext)s")
            addOption("--downloader", "aria2c")
            addOption("--downloader-args", "aria2c:-x 16 -s 16 -k 1M")
            addOption("--no-overwrites")
            addOption("--embed-thumbnail")
        }
        try {
            YoutubeDL.getInstance().execute(request) { progress, _, line -> onProgress(progress/100f); onLog(line) }
            downloadDir.listFiles()?.filter { it.extension == "mp4" || it.extension == "mkv" }?.maxByOrNull { it.lastModified() }
        } catch (e: Exception) {
            onLog("에러 밡큑: ${e.message}. yt-dlp 엔진업데이트...")
            throw DownloadException.NeedsUpdate(e.message ?: "앩든 예옣")
        }
    }

    suspend fun updateEngine(onLog: (String) -> Unit): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            onLog("yt-dlp 최신 업데이트 중...")
            val status = YoutubeDL.getInstance().updateYoutubeDL(context)
            onLog("업데이트 완료: $status")
            true
        } catch (e: Exception) { onLog("업데이트 실패: ${e.message}"); false }
    }

    suspend fun fetchVideoInfo(url: String): VideoInfo? = withContext(Dispatchers.IO) {
        return@withContext try { YoutubeDL.getInstance().getInfo(YoutubeDLRequest(url).apply { addOption("--dump-json"); addOption("--no-playlist") }) } catch (e: Exception) { null }
    }
}

sealed class DownloadException(message: String) : Exception(message) {
    class NeedsUpdate(message: String) : DownloadException(message)
    class NetworkError(message: String) : DownloadException(message)
    class UnsupportedUrl(message: String) : DownloadException(message)
}
