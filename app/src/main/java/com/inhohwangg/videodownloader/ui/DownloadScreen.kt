package com.inhohwangg.videodownloader.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inhohwangg.videodownloader.ui.theme.*

sealed class DownloadUiState {
    object Idle        : DownloadUiState()
    object Preparing   : DownloadUiState()
    data class Downloading(val progress: Float) : DownloadUiState()
    object Updating    : DownloadUiState()
    object Merging     : DownloadUiState()
    object Success     : DownloadUiState()
    data class Error(val message: String) : DownloadUiState()
}

@Composable
fun DownloadScreen(
    modifier: Modifier = Modifier,
    uiState: DownloadUiState = DownloadUiState.Idle,
    urlText: String = "",
    onUrlChange: (String) -> Unit = {},
    onDownloadClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    Box(modifier = modifier.fillMaxSize().background(TossBackground)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(64.dp))
            AppTitleSection()
            Spacer(Modifier.height(48.dp))
            UrlInputCard(urlText, onUrlChange) { focusManager.clearFocus(); onDownloadClick() }
            Spacer(Modifier.height(20.dp))
            DownloadButton(uiState) { focusManager.clearFocus(); onDownloadClick() }
            Spacer(Modifier.height(28.dp))
            StatusMessage(uiState)
        }
    }
}

@Composable private fun AppTitleSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(64.dp).shadow(12.dp, RoundedCornerShape(20.dp), ambientColor = TossPrimary.copy(0.2f), spotColor = TossPrimary.copy(0.3f)).clip(RoundedCornerShape(20.dp)).background(Brush.linearGradient(listOf(TossPrimary, TossPrimaryDark))), contentAlignment = Alignment.Center) { Text("▼", fontSize = 28.sp, color = Color.White) }
        Spacer(Modifier.height(20.dp))
        Text("영상 다운로더", style = MaterialTheme.typography.headlineLarge, color = TossTextPrimary, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(8.dp))
        Text("유튜브, SNS 영상을 �륨게 저장핸턴", style = MaterialTheme.typography.bodyMedium, color = TossTextSecondary)
    }
}

@Composable private fun UrlInputCard(urlText: String, onUrlChange: (String) -> Unit, onDone: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(20.dp), ambientColor = Color.Black.copy(0.05f), spotColor = Color.Black.copy(0.08f)).clip(RoundedCornerShape(20.dp)).background(TossCardBg).padding(20.dp)) {
        Text("영상 URL", style = MaterialTheme.typography.bodyMedium, color = TossTextSecondary, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = urlText, onValueChange = onUrlChange, modifier = Modifier.fillMaxWidth(), placeholder = { Text("https://youtube.com/watch?v=...", color = TossTextHint) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onDone = { onDone() }), shape = RoundedCornerShape(14.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TossPrimary, unfocusedBorderColor = TossDivider, focusedContainerColor = TossSurface, unfocusedContainerColor = TossSurface, cursorColor = TossPrimary))
        AnimatedVisibility(visible = urlText.isNotEmpty()) {
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { onUrlChange("") }, contentPadding = PaddingValues(8.dp, 4.dp)) { Text("직고", color = TossTextSecondary) }
            }
        }
    }
}

@Composable private fun DownloadButton(uiState: DownloadUiState, onClick: () -> Unit) {
    val isLoading = uiState !is DownloadUiState.Idle && uiState !is DownloadUiState.Success && uiState !is DownloadUiState.Error
    Box(modifier = Modifier.fillMaxWidth().height(60.dp).shadow(if(!isLoading) 12.dp else 0.dp, RoundedCornerShape(16.dp), ambientColor = TossPrimary.copy(0.3f), spotColor = TossPrimary.copy(0.4f)).clip(RoundedCornerShape(16.dp)).background(Brush.linearGradient(colors = if(isLoading) listOf(TossSurface,TossSurface) else listOf(TossPrimary,TossPrimaryDark))), contentAlignment = Alignment.Center) {
        when(uiState) {
            is DownloadUiState.Downloading -> Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 20.dp)) { LinearProgressIndicator(progress = { uiState.progress }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)), color = TossPrimary, trackColor = TossDivider); Spacer(Modifier.height(6.dp)); Text("${null}%".replace("null","${(uiState.progress*100).toInt()}"), color = TossTextPrimary, fontWeight = FontWeight.Bold) }
            is DownloadUiState.Updating, is DownloadUiState.Preparing, is DownloadUiState.Merging -> CircularProgressIndicator(modifier = Modifier.size(28.dp), color = TossPrimary, strokeWidth = 3.dp)
            else -> Button(onClick = onClick, modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.White), elevation = ButtonDefaults.buttonElevation(0.dp)) { Text("다운로드", fontWeight = FontWeight.Bold, fontSize = 17.sp) }
        }
    }
}

@Composable private fun StatusMessage(uiState: DownloadUiState) {
    AnimatedContent(targetState = uiState, transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) }, label = "status") { state ->
        val (msg, color) = when(state) {
            is DownloadUiState.Idle -> "" to TossTextSecondary
            is DownloadUiState.Preparing -> "⋾ 다운로드 준비 중..." to TossTextSecondary
            is DownloadUiState.Updating -> "🔄 최신 엔진으로 업데이트 중..." to TossWarning
            is DownloadUiState.Merging -> "🎬 오디오와 영상을 합치는 중..." to TossPrimary
            is DownloadUiState.Downloading -> "📥 다운로드 중..." to TossPrimary
            is DownloadUiState.Success -> "✅ 갤러리에 저장 완료!" to TossSuccess
            is DownloadUiState.Error -> "❌ ${state.message}" to TossError
        }
        if(msg.isNotEmpty()) Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(color.copy(0.08f)).padding(16.dp,12.dp), Alignment.Center) { Text(msg, color=color, fontWeight=FontWeight.Medium, textAlign = TextAlign.Center) }
        else Spacer(Modifier.height(0.dp))
    }
}

@Preview(showBackground=true,backgroundColor=0xFFF9FAFB)
@Composable fun PreviewIdle() = VideoDownloaderTheme { DownloadScreen() }

@Preview(showBackground=true,backgroundColor=0xFFF9FAFB)
@Composable fun PreviewSuccess() = VideoDownloaderTheme { DownloadScreen(uiState=DownloadUiState.Success) }
