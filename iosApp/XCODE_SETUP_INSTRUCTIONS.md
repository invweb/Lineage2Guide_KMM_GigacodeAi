# iOS Setup Instructions

## Xcode Project Setup

The Xcode project (`iosApp/iosApp.xcodeproj`) has been created. Follow these steps to complete the setup:

### 1. Open the Project
```bash
open iosApp/iosApp.xcodeproj
```

### 2. Add Swift Source Files
In Xcode, right-click on the `iosApp` group → **Add Files to "iosApp"**:
- `iosApp/iosApp.swift`
- `iosApp/ComposeView.swift`
- `iosApp/ComposeViewController.swift`
- `Info.plist`

### 3. Configure Build Phases

#### Add Build Phase: Run Script (Framework Build)
1. Select the project → **iosApp** target → **Build Phases** tab
2. Click **+** → **New Run Script Phase**
3. Expand the phase and set:
   - **Shell:** `/bin/bash`
   - **Script:**
   ```bash
   cd "${PROJECT_DIR}/.."
   ./gradlew :shared:iosSimulatorArm64MainBinaries --no-configuration-cache
   ```

#### Add Build Phase: Link Binary With Libraries
1. In **Build Phases** → **Link Binary With Libraries**
2. Click **+**
3. Search for and add `shared.framework` (it should appear from the build products)
4. Set **Embed** to **Do Not Embed**

### 4. Configure Build Settings

#### Header Search Paths
1. Select the project → **iosApp** target → **Build Settings** tab
2. Search for **Header Search Paths**
3. Add: `$(PROJECT_DIR)/../shared/build/bin/iosSimulatorArm64/releaseFramework`
4. Set to **recursive**

#### Framework Search Paths
1. Search for **Framework Search Paths**
2. Add: `$(PROJECT_DIR)/../shared/build/bin/iosSimulatorArm64/releaseFramework`
3. Set to **recursive**

### 5. Set Deployment Target
1. In **Build Settings**, search for **iOS Deployment Target**
2. Set to **15.0**

### 6. Build and Run
1. Select an iOS Simulator (iPhone 15 recommended)
2. Press **Cmd + B** to build
3. Press **Cmd + R** to run

## Troubleshooting

### "shared.framework not found"
Run the Gradle task manually first:
```bash
cd /Users/vasiliikarpenko/AndroidStudioProjects/MyApplication4
./gradlew :shared:iosSimulatorArm64MainBinaries --no-configuration-cache
```

### "Module 'shared' not found"
Make sure Header Search Paths and Framework Search Paths are set correctly in Build Settings.

### "koinViewModel not found"
This should not happen if the shared module is built correctly. Verify the build output:
```bash
ls -la shared/build/bin/iosSimulatorArm64/releaseFramework/shared.framework/
```

## API Configuration

The default API URL is `http://localhost:8080`. To change it:

1. Edit `iosApp/iosApp/Kotlin/IOSApp.kt`
2. Change `DEFAULT_BASE_URL` constant
3. For emulator, use `http://10.0.2.2:8080` instead of `localhost`

## Running the Mock Server

To test with local data, run the Ktor server:
```bash
cd server
./gradlew run
```
Server starts on `http://localhost:8080`.
