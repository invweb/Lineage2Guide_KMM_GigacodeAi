# Lineage 2 Guide

A comprehensive companion guide app for Lineage 2 (Interlude chronicle) with offline caching, real-time sync, and comprehensive game data. Available on **Android** and **Desktop** (macOS, Windows, Linux).

## 📱 Screenshots

### Desktop Application
<p align="center">
  <img src="screenshots/desktop_items.png" width="800" alt="Desktop Items Screen"/><br/>
  <em>Desktop — Items with type-based icons</em>
</p>

<p align="center">
  <img src="screenshots/desktop_quests.png" width="800" alt="Desktop Quests Screen"/><br/>
  <em>Desktop — Quests with colored type icons (Main, Side, Daily, Guild, Event)</em>
</p>

<p align="center">
  <img src="screenshots/desktop_skills.png" width="800" alt="Desktop Skills Screen"/><br/>
  <em>Desktop — Skills with type-based icons</em>
</p>



<p align="center">
  <img src="screenshots/desktop_classes.png" width="800" alt="Desktop Classes Screen"/><br/>
  <em>Desktop — Classes with class-type icons</em>
</p>

<p align="center">
  <img src="screenshots/desktop_npcs.png" width="800" alt="Desktop NPCs Screen"/><br/>
  <em>Desktop — NPCs with type-based icons (Quest Giver, Merchant, Boss, Monster)</em>
</p>

### Web Application
<p align="center">
  <img src="screenshots/web_items.png" width="800" alt="Web Items Screen"/><br/>
  <em>Web — Items with Material Icons and type-based color coding</em>
</p>

<p align="center">
  <img src="screenshots/web_skills.png" width="800" alt="Web Skills Screen"/><br/>
  <em>Web — Skills with Active/Passive type icons</em>
</p>

<p align="center">
  <img src="screenshots/web_npcs.png" width="800" alt="Web NPCs Screen"/><br/>
  <em>Web — NPCs with type-based icons (Quest Giver, Merchant, Boss, Monster)</em>
</p>

<p align="center">
  <img src="screenshots/web_quests.png" width="800" alt="Web Quests Screen"/><br/>
  <em>Web — Quests with type icons (Main, Side, Daily, Guild, Event)</em>
</p>

<p align="center">
  <img src="screenshots/web_classes.png" width="800" alt="Web Classes Screen"/><br/>
  <em>Web — Classes overview with race and sub-class info</em>
</p>

### iOS Application
<p align="center">
  <img src="screenshots/ios_items.png" width="280" alt="iOS Items Screen"/>
  <img src="screenshots/ios_quests.png" width="280" alt="iOS Quests Screen"/>
  <img src="screenshots/ios_skills.png" width="280" alt="iOS Skills Screen"/>
</p>

<p align="center">
  <img src="screenshots/ios_classes.png" width="280" alt="iOS Classes Screen"/>
  <img src="screenshots/ios_npcs.png" width="280" alt="iOS NPCs Screen"/>
</p>

<p align="center">
  <em>iOS — Compose Multiplatform UI with Material 3 design</em>
</p>

### Android Application
<p align="center">
  <img src="screenshots/splash0.jpg" width="200" alt="Splash Screen"/>
  <img src="screenshots/items.png" width="200" alt="Items Screen"/>
  <img src="screenshots/quests.png" width="200" alt="Quests Screen"/>
</p>

<p align="center">
  <img src="screenshots/skills.png" width="200" alt="Skills Screen"/>
  <img src="screenshots/classes.png" width="200" alt="Classes Screen"/>
  <img src="screenshots/npcs.png" width="200" alt="NPCs Screen"/>
</p>

## ✨ Features

### Game Data
- **📦 9,206+ items** — weapons, armor, consumables with stats and drop info
- **⚔️ 15,308+ skills** — damage, healing, buffs, debuffs with ranges and cooldowns
- **🧙 6,472+ NPCs** — monsters, vendors, quest givers, bosses
- **📜 329 quests** — full quest lines with rewards and requirements
- **🏰 89 classes** — class trees and progression paths

### UI/UX
- **🎨 Smart Icons** — Context-aware icons for all entities:
  - Items: Weapons (⚔️), Armor (🛡️), Accessories (💎), Potions (🧪), Scrolls (📜), etc.
  - Classes: Warriors (⭐), Archers (📡), Mages (🔮), Healers (❤️), Scouts (👁️), etc.
  - NPCs: Quest Giver (📮), Merchant (🛒), Boss (⚠️), Monster (🔒), etc.
  - Skills: Active (⚡), Passive (🎨)
  - Quests: Main (⭐), Side (👤), Daily (📅), Guild (👥), Event (🎉)
- **🔍 Search & Filter** — Find items by name, rarity, type; filter skills by class
- **📡 Pull-to-refresh** — Sync with live data
- **📴 Offline support** — Room cache keeps data available without connection
- **🔄 Sync indicator** — Visual feedback during data sync
- **📊 Pagination** — Optimized loading with lazy lists

### Technical
- **🔄 Real-time sync** — Automatic data synchronization
- **💾 Offline caching** — Disk-based caching for 24 hours
- **🌐 API-first architecture** — Clean separation between client and server
- **📱 Cross-platform** — Android, iOS + Desktop (macOS, Windows, Linux)
- **🛠 MVVM Architecture** — Clean, maintainable code structure

## 🛠 Tech Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Kotlin, TypeScript |
| **UI** | Jetpack Compose + Material 3 (Android/Desktop), Vanilla HTML/CSS/JS + Material Icons (Web) |
| **Architecture** | MVVM |
| **DI** | Koin 4.0.0 |
| **Local DB** | Room 2.7.2 (Android), File Cache (Desktop) |
| **API** | Retrofit 2.12.0 (Android), Ktor Client 3.2.3 (Desktop), Fetch API (Web) |
| **Server** | Ktor 3.2.3 (Netty) |
| **Images** | Coil 3.1.0 (image loading) |
| **Build** | AGP 9.4.1, Gradle 9.6.0, Kotlin Multiplatform, Vite |
| **SDK** | Android: compileSdk 37, minSdk 24 |

## 📊 Data

Game data sourced from [lineage2-api](https://github.com/cuteshaun/lineage2-api) (Interlude chronicle):

| Entity | Count | Compressed |
|--------|-------|------------|
| Items | 9,206 | 249 KB |
| Skills | 15,308 | 270 KB |
| NPCs | 6,472 | 372 KB |
| Quests | 329 | 22 KB |
| Classes | 89 | 63 KB |
| **Total** | **31,404** | **975 KB** (from 24 MB source) |

#### iOS
1. Clone the repository:
```bash
git clone https://github.com/invweb/Lineage2Guide.git
cd Lineage2Guide
```

2. Build the shared framework:
```bash
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```

3. Open the Xcode project:
```bash
open iosApp/iosApp.xcodeproj
```

4. Select **iPhone 17 Pro** (or your preferred simulator) and press **Run** (⌘R)

The iOS app uses Compose Multiplatform with the same MVVM architecture and Koin DI as the Android version.

#### Desktop

### Prerequisites
- Android Studio (latest)
- JDK 17+
- Android SDK (API 37)
- Node.js 18+ (for web version)

### Run the App

#### Android
1. Clone the repository:
```bash
git clone https://github.com/invweb/Lineage2Guide.git
cd Lineage2Guide
```

2. Open in Android Studio
3. Sync Gradle files
4. Run on emulator or device

#### Desktop
1. Clone the repository:
```bash
git clone https://github.com/invweb/Lineage2Guide.git
cd Lineage2Guide
```

2. Run the server:
```bash
./gradlew :server:run
```
Server starts on `http://localhost:8080`

3. Run Desktop app:
```bash
./gradlew :desktop:runDesktop
```

#### Web
1. Clone the repository:
```bash
git clone https://github.com/invweb/Lineage2Guide.git
cd Lineage2Guide
```

2. Run the backend server:
```bash
./gradlew :server:run
```
Server starts on `http://localhost:8080`

3. Start the web server:
```bash
./gradlew :web:run
```

4. Open browser:
Navigate to `http://localhost:8082`

The web app features a modern Material Design interface with:
- **Smart Icons** — Context-aware Material Icons for all entities (weapons, armor, skills, NPCs, etc.)
- **Type-based Color Coding** — Visual distinction by type (weapons = red, armor = blue, accessories = gold, etc.)
- **Search & Filter** — Real-time search by name, type, or description
- **Responsive Design** — Works on desktop and mobile browsers
- **Pagination** — Optimized loading with 100 items per page
- **Stats Dashboard** — Quick overview of available data count

### Run the Mock Server (Development)
The app uses a Ktor server for game data. For development:

```bash
cd server
./gradlew run
```

Server starts on `http://localhost:8080` (Android emulator: `http://10.0.2.2:8080`).

## 📂 Project Structure

```
├── app/                    # Android application module
│   ├── di/                 # Koin dependency injection
│   ├── data/               # Room DAOs, Retrofit API, repositories
│   └── ui/                 # Compose screens & navigation
│       ├── items/          # Items list & detail
│       ├── skills/         # Skills list & detail
│       ├── npcs/           # NPCs list & detail
│       ├── quests/         # Quests list & detail
│       ├── classes/        # Classes list & detail
│       └── common/         # Shared components & states
├── desktop/                # Desktop application module (Compose Desktop)
├── web/                    # Web application (plain HTML/CSS/JS)
│   ├── src/                # React components
│   ├── package.json        # Node dependencies
│   └── vite.config.ts      # Vite configuration
├── shared/                 # Shared Kotlin Multiplatform module
│   ├── commonMain/         # Shared code (ViewModels, UI, Data)
│   ├── androidMain/        # Android-specific code
│   ├── iosMain/            # iOS-specific code (ComposeUIViewController bridge)
│   └── desktopMain/        # Desktop-specific code
├── iosApp/                 # iOS Xcode project + Swift UI
└── server/                 # Ktor backend module
    ├── data/               # RealDataLoader, ServerMockData
    ├── dto/                # API data transfer objects
    ├── routing/            # API endpoints
    └── resources/          # Compressed .gz game data files
```

## 📡 API Endpoints

| Endpoint | Description |
|----------|-------------|
| `GET /api/v1/items` | List all items (supports `?offset=&limit=&search=&rarity=&type=`) |
| `GET /api/v1/items/{id}` | Get item by ID |
| `GET /api/v1/skills` | List all skills (supports `?offset=&limit=&classRestriction=&type=`) |
| `GET /api/v1/skills/{id}` | Get skill by ID |
| `GET /api/v1/npcs` | List all NPCs (supports `?offset=&limit=&search=&type=`) |
| `GET /api/v1/npcs/{id}` | Get NPC by ID |
| `GET /api/v1/quests` | List all quests (supports `?offset=&limit=&search=&type=`) |
| `GET /api/v1/quests/{id}` | Get quest by ID |
| `GET /api/v1/classes` | List all classes (supports `?offset=&limit=`) |
| `GET /api/v1/classes/{id}` | Get class by ID |
| `GET /api/v1/sync` | Check for updates |
| `GET /api/v1/icons/items/{id}` | Get item icon (PNG) |

## 🔄 Recent Updates

### v1.4.0 (Latest)
- **🌐 Web Application** — New lightweight web app with Material Icons
- **🎨 Smart Icons** — Context-aware Material Icons for all entities with type-based color coding
- **🔍 Enhanced Search** — Real-time search across all categories
- **📱 Responsive** — Works on desktop and mobile browsers
- **⚡ Lightweight** — Pure HTML/CSS/JS, no framework overhead
- **📊 Stats Dashboard** — Quick overview of available data

### v1.4.1
- **🍎 iOS Application** — New iOS app with Compose Multiplatform
- **🔄 Koin DI** — Full Koin dependency injection on iOS
- **📱 Same MVVM** — Shared ViewModels and UI logic with Android

### v1.3.0
- **📱 Cross-platform** — Now available on Android, Desktop, and Web
- **🛠 Desktop Application** — New Compose Desktop app with all 5 categories

### v1.2.0
- **✨ Desktop Application** — New Compose Desktop app with all 5 categories
- **🎨 Smart Icons** — Context-aware icons for all entities based on type
- **📊 Pagination** — Optimized data loading with pagination (max 1000 items per category)
- **💾 File-based Caching** — 24-hour disk cache for offline access
- **🔧 Deduplication** — Automatic deduplication of data from API
- **🎯 Color Coding** — Visual distinction by type (weapons red, armor blue, etc.)

### v1.1.0
- **📱 Mobile Application** — Full Android app with Jetpack Compose
- **📡 Real-time Sync** — Pull-to-refresh and automatic sync
- **🔍 Search & Filter** — Advanced filtering by type, rarity, level
- **💾 Offline Support** — Room database for offline access

## 🤖 AI-Assisted Development

This project was developed with AI assistance using **GigaCode** by Sber — an interactive AI coding assistant designed for software engineering tasks. AI was used for architecture decisions, code generation, debugging, and implementing complex features including data compression, serialization, pagination, and the full MVVM architecture.

## 📝 License

MIT License
