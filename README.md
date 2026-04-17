<div align="center">

<br/>

<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
<img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"/>

<br/><br/>

# 📥 Video Downloader

### 유튜브, SNS 영상을 한 번의 탭으로 — 서버 없이, 직접, 빠르게.

<br/>

[![Version](https://img.shields.io/badge/version-1.0.0-3182F6?style=flat-square)](https://github.com/inhohwangg/video-downloader/releases)
[![License](https://img.shields.io/badge/license-MIT-00C471?style=flat-square)](LICENSE)
[![minSdk](https://img.shields.io/badge/minSdk-24_(Android_7)-FF6B35?style=flat-square)](https://developer.android.com/)
[![Build](https://img.shields.io/badge/build-passing-00C471?style=flat-square)]()

</div>

<br/>

---

<br/>

## ✨ 이런 앱이에요

> 유튜브 영상을 저장하고 싶었는데, 광고 가득한 웹사이트는 싫고  
> 서버에 내 URL을 넘기는 것도 찝찝했던 분들을 위해 만들었어요.

**Video Downloader**는 당신의 스마트폰 안에서만 작동해요.  
외부 서버 없이, 내 기기가 직접 영상을 받아 갤러리에 저장합니다.

<br/>

---

<br/>

## 🎯 핵심 기능

<br/>

| 기능 | 설명 |
|:---:|:---|
| 🎬 **최고 화질 다운로드** | 1080p 이상 비디오 + 최고 품질 오디오를 자동 선택 |
| 🔀 **자동 병합 (Muxing)** | 영상과 오디오를 FFmpeg로 자동 합성 → MP4 저장 |
| 🔄 **자가 업데이트 엔진** | 다운로드 오류 발생 시 yt-dlp를 자동 패치 후 재시도 |
| 📤 **공유 연동** | 유튜브 앱에서 "공유 → Video Downloader"로 즉시 실행 |
| 🔒 **프라이버시 보호** | 모든 처리가 내 기기 안에서만 — 서버 전송 없음 |
| 🎨 **토스 스타일 UI** | 깔끔하고 직관적인 Jetpack Compose UI |

<br/>

---

<br/>

## 📱 사용 방법

<br/>

### 방법 1 — 앱에서 직접 입력

```
1. 앱을 열어요
2. URL 입력창에 영상 주소를 붙여넣어요
3. 파란 [다운로드] 버튼을 탭해요
4. 갤러리 → Movies → VideoDownloader 폴더에 저장돼요 🎉
```

<br/>

### 방법 2 — 유튜브에서 바로 공유

```
1. 유튜브 앱에서 원하는 영상을 열어요
2. [공유] 버튼을 탭해요
3. 앱 목록에서 [Video Downloader]를 선택해요
4. 자동으로 앱이 열리고 다운로드가 시작돼요 ⚡
```

<br/>

---

<br/>

## 🔄 자동 업데이트 엔진이란?

유튜브는 수시로 내부 알고리즘을 바꿔서, 다운로드 앱들이 갑자기 작동을 멈추는 일이 자주 있어요.  
이 앱은 그런 상황을 스스로 해결해요.

```
다운로드 시도
    ↓
❌ 오류 감지 (유튜브 알고리즘 변경 등)
    ↓
🔄 yt-dlp 최신 버전으로 자동 업데이트 (백그라운드)
    ↓
✅ 자동 재시도 → 다운로드 성공
```

> 사용자가 직접 앱을 업데이트하지 않아도, 엔진이 스스로 최신 상태를 유지해요.

<br/>

---

<br/>

## 🛠️ 기술 스택

<br/>

```
📦 Video Downloader
├── 🎨 UI              → Jetpack Compose + Material3
├── 🧠 상태 관리        → ViewModel + StateFlow + Coroutines
├── ⬇️  다운로드 엔진    → yausername/youtubedl-android (yt-dlp)
├── 🎬 영상 처리        → FFmpeg (오디오·비디오 병합)
├── 🚀 다운로드 가속     → aria2c (멀티 스레드)
└── 🏗️  아키텍처        → Single Activity + Repository Pattern
```

<br/>

| 라이브러리 | 용도 |
|:---|:---|
| `youtubedl-android` | yt-dlp Android 포팅 — 영상 URL 파싱 및 다운로드 |
| `ffmpeg-android` | 오디오 + 비디오 스트림 병합 (Muxing) |
| `aria2c` | 멀티 커넥션 다운로드 가속 |
| `Jetpack Compose` | 선언형 UI 프레임워크 |
| `Kotlin Coroutines` | 비동기 다운로드 처리 |

<br/>

---

<br/>

## 🚀 빌드 & 설치

<br/>

### 요구 사항

- Android Studio Hedgehog (2023.1.1) 이상
- JDK 11 이상
- Android 7.0 (API 24) 이상 기기 또는 에뮬레이터

<br/>

### 설치 방법

```bash
# 1. 레포지토리 클론
git clone https://github.com/inhohwangg/video-downloader.git

# 2. Android Studio에서 열기
# File → Open → video-downloader 폴더 선택

# 3. Gradle Sync (자동으로 진행됨)
# JitPack에서 youtubedl-android 라이브러리를 다운로드해요

# 4. 기기 또는 에뮬레이터에서 실행
# Run → Run 'app'
```

<br/>

> ⚠️ **에뮬레이터 주의**: `arm64-v8a` 아키텍처 에뮬레이터 사용을 권장해요.  
> x86 에뮬레이터에서는 FFmpeg 바이너리가 정상 동작하지 않을 수 있어요.

<br/>

---

<br/>

## 📁 프로젝트 구조

```
app/src/main/
├── java/com/inhohwangg/videodownloader/
│   ├── MainActivity.kt                  ← 권한 처리 + ViewModel 연결
│   ├── data/
│   │   └── DownloadRepository.kt        ← 다운로드 로직 + yt-dlp 업데이트
│   ├── viewmodel/
│   │   └── DownloadViewModel.kt         ← UI 상태 관리 + 재시도 로직
│   └── ui/
│       ├── DownloadScreen.kt            ← 메인 UI (Jetpack Compose)
│       └── theme/
│           ├── Color.kt                 ← 토스 컬러 팔레트
│           ├── Type.kt                  ← 타이포그래피
│           └── Theme.kt                 ← MaterialTheme 설정
└── res/
    └── xml/
        └── file_paths.xml               ← FileProvider 경로 설정
```

<br/>

---

<br/>

## ⚠️ 주의사항

<br/>

- 이 앱은 **개인 학습 및 연구 목적**으로 제작되었어요
- 다운로드한 영상의 **저작권은 원 저작자에게 있어요**
- 저작권자의 허락 없이 영상을 재배포하는 것은 법적 문제가 될 수 있어요
- **개인 소장 용도**로만 사용해 주세요

<br/>

---

<br/>

## 📄 라이선스

```
MIT License

Copyright (c) 2025 inhohwangg

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction.
```

<br/>

---

<br/>

<div align="center">

Made with ❤️ by **inhohwangg**

<br/>

[![GitHub](https://img.shields.io/badge/GitHub-inhohwangg-181717?style=flat-square&logo=github)](https://github.com/inhohwangg)

</div>
