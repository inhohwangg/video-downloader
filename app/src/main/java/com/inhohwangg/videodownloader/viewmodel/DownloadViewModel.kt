package com.inhohwangg.videodownloader.viewmodel

import android.app.Application
import android.content.ContentValues
import android.media.MediaScannerConnection
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yausername.youtubedl_android.YoutubeDL
import com.inhohwangg.videodownloader.data.DownloadException
import com.inhohwangg.videodownloader.data.DownloadRepository
import com.inhohwangg.videodownloader.ui.DownloadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class DownloadViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DownloadRepository(application.applicationContext)
    private val _uiState = MutableStateFlow<DownloadUiState>(DownloadUiState.Idle)
    val uiState: StateFlow<DownloadUiState> = _uiState.asStateFlow()
    private val _urlText = MutableStateFlow("")
    val urlText: StateFlow<String> = _urlText.asStateFlow()
    private val _logMessage = MutableStateFlow("")
    val logMessage: StateFlow<String> = _logMessage.asStateFlow()

    init { viewModelScope.launch { try { YoutubeDL.getInstance().init(getApplication()) } catch (e: Exception) {} } }

    fun onUrlChange(newUrl: String) {
        _urlText.value = newUrl
        if (_uiState.value is DownloadUiState.Error || _uiState.value is DownloadUiState.Success) _uiState.value = DownloadUiState.Idle
    }

    fun startDownload() {
        val url = _urlText.value.trim()
        if (url.isEmpty()) { _uiState.value = DownloadUiState.Error("URL을 입력해주세"); return }
        if (!url.startsWith("http")) { _uiState.value = DownloadUiState.Error("올바른 URL형식이 아닙"); return }
        viewModelScope.launch { executeDownload(url, false) }
    }

    private suspend fun executeDownload(url: String, isRetry: Boolean) {
        _uiState.value = DownloadUiState.Preparing
        try {
            val file = repository.download(url,
                onProgress = { _uiState.value = DownloadUiState.Downloading(it) },
                onLog = { _logMessage.value = it; if(it.contains("Merging")||it.contains("ffmpeg")) _uiState.value = DownloadUiState.Merging }
            )
            if (file != null) { scanMediaFile(file); _uiState.value = DownloadUiState.Success }
            else _uiState.value = DownloadUiState.Error("파일을 찾을 수 없")
        } catch (e: DownloadException.NeedsUpdate) {
            if (isRetry) { _uiState.value = DownloadUiState.Error("다운로드 실패"); return }
            _uiState.value = DownloadUiState.Updating
            val updated = repository.updateEngine { _logMessage.value = it }
            if (updated) executeDownload(url, true)
            else _uiState.value = DownloadUiState.Error("ꗈ페의에 실패 수 없")
        } catch (e: Exception) {
            _uiState.value = DownloadUiState.Error(e.message?.take(80) ?: "ꗈ페의 실패)
        }
    }

    private fun scanMediaFile(file: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getApplication<Application>().contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, ContentValues().apply { put(MediaStore.Video.Media.DISPLAY_NAME, file.name); put(MediaStore.Video.Media.MIME_TYPE, "video/mp4"); put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/VideoDownloader"); put(MediaStore.Video.Media.IS_PENDING, 0) })
        } else MediaScannerConnection.scanFile(getApplication(), arrayOf(file.absolutePath), arrayOf("video/mp4"), null)
    }

    fun resetState() { _uiState.value = DownloadUiState.Idle }
}
