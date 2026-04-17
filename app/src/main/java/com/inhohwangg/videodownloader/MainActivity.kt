package com.inhohwangg.videodownloader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inhohwangg.videodownloader.ui.DownloadScreen
import com.inhohwangg.videodownloader.ui.theme.VideoDownloaderTheme
import com.inhohwangg.videodownloader.viewmodel.DownloadViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: DownloadViewModel by viewModels()
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
        if (perms.values.all { it }) viewModel.startDownload()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleSharedIntent(intent)
        setContent {
            VideoDownloaderTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val urlText by viewModel.urlText.collectAsStateWithLifecycle()
                DownloadScreen(modifier = Modifier.fillMaxSize().systemBarsPadding(), uiState = uiState, urlText = urlText, onUrlChange = viewModel::onUrlChange, onDownloadClick = { checkPermissionsAndDownload() })
            }
        }
    }

    private fun checkPermissionsAndDownload() {
        val required = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) listOf(Manifest.permission.READ_MEDIA_VIDEO) else listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        val notGranted = required.filter { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
        if (notGranted.isEmpty()) viewModel.startDownload() else permissionLauncher.launch(notGranted.toTypedArray())
    }

    private fun handleSharedIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return
            viewModel.onUrlChange(Regex{}("https?://[^\\s]+").find(text)?.value ?: text)
        }
    }

    override fun onNewIntent(intent: Intent) { super.onNewIntent(intent); handleSharedIntent(intent) }
}
