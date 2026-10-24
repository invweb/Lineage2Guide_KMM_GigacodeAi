# Настройка Xcode для Lineage2Guide iOS

## ✅ Что уже сделано:
- Shared модуль собран для iOS Simulator
- Фреймворк находится в: `shared/build/bin/iosSimulatorArm64/releaseFramework/shared.framework`
- iOS проект создан с базовой структурой

## 📋 Шаг 1: Откройте проект в Xcode

```bash
open iosApp/iosApp.xcodeproj
```

## 📋 Шаг 2: Настройка Build Phases

1. В Xcode выберите проект **iosApp** в Navigator (слева)
2. Выберите target **iosApp** в основной панели
3. Перейдите на вкладку **Build Phases**
4. Раскройте секцию **Link Binary With Libraries**
5. Нажмите **+** и добавьте:
   - `shared.framework` (если не появилась автоматически)

### Добавьте новый Run Script phase:

1. В **Build Phases** нажмите **+** → **New Run Script Phase**
2. Раскройте новую секцию **Run Script**
3. В поле **Shell** введите: `/bin/sh`
4. В поле **Script** вставьте:

```bash
# Copy shared framework from Gradle build
SHARED_FRAMEWORK_PATH="${PROJECT_DIR}/../shared/build/bin/iosSimulatorArm64/releaseFramework/shared.framework"

if [ -d "$SHARED_FRAMEWORK_PATH" ]; then
    echo "Found shared.framework at: $SHARED_FRAMEWORK_PATH"
    cp -R "$SHARED_FRAMEWORK_PATH" "${BUILT_PRODUCTS_DIR}/"
else
    echo "shared.framework not found at: $SHARED_FRAMEWORK_PATH"
    echo "Building shared framework..."
    cd "${PROJECT_DIR}/.."
    ./gradlew :shared:linkReleaseFrameworkIosSimulatorArm64
    cd "${PROJECT_DIR}"
    cp -R "$SHARED_FRAMEWORK_PATH" "${BUILT_PRODUCTS_DIR}/" 2>/dev/null || echo "Build may have failed"
fi
```

## 📋 Шаг 3: Настройка Header Search Paths

1. В **Build Settings** найдите **Header Search Paths**
2. Нажмите **→** и выберите **Add**
3. Добавьте путь:
   ```
   $(PROJECT_DIR)/../shared/build/bin/iosSimulatorArm64/releaseFramework
   ```
4. Установите флаг **recursive**

## 📋 Шаг 4: Настройка Framework Search Paths

1. В **Build Settings** найдите **Framework Search Paths**
2. Добавьте:
   ```
   $(PROJECT_DIR)/../shared/build/bin/iosSimulatorArm64/releaseFramework
   ```

## 📋 Шаг 5: Обновите iOS файлы

Убедитесь, что следующие файлы существуют в `iosApp/iosApp/`:

### iosApp.swift
```swift
import UIKit
import SwiftUI
import shared

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Initialize Koin for iOS
        IOSAppKoinInitializer().initialize()
        
        let window = UIWindow(frame: UIScreen.main.bounds)
        let viewController = IOSViewController()
        window.rootViewController = viewController
        self.window = window
        window.makeKeyAndVisible()
        return true
    }
}
```

### IOSViewController.swift
```swift
import UIKit
import shared

class IOSViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.systemBackground
        
        // Create and add Compose UI
        let composeView = ComposeView()
        composeView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(composeView)
        
        NSLayoutConstraint.activate([
            composeView.topAnchor.constraint(equalTo: view.topAnchor),
            composeView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            composeView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            composeView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
        
        // Set Compose content
        composeView.setContent {
            IOSApp()
        }
    }
}
```

### ComposeView.swift
```swift
import UIKit
import shared

@MainActor
class ComposeView: UIView {
    private var composeContent: (@Composable () -> Void)?
    
    func setContent(@Composable content: @escaping () -> Void) {
        self.composeContent = content
        
        // For now, show a placeholder
        // In a full implementation, this would initialize Compose Multiplatform
        let label = UILabel()
        label.text = "iOS App - Compose Multiplatform"
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        addSubview(label)
        
        NSLayoutConstraint.activate([
            label.centerXAnchor.constraint(equalTo: centerXAnchor),
            label.centerYAnchor.constraint(equalTo: centerYAnchor)
        ])
    }
}
```

### Kotlin/IOSApp.kt
```kotlin
package com.zx_tole.lineage2_guide.ios

import androidx.compose.runtime.Composable
import com.zx_tole.lineage2_guide.ui.items.ItemsScreen
import com.zx_tole.lineage2_guide.ui.theme.Lineage2Theme
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module

@Composable
fun IOSApp() {
    Lineage2Theme {
        ItemsScreen(
            onNavigateToDetail = { itemId ->
                // Navigate to detail screen
            }
        )
    }
}

class IOSAppKoinInitializer : KoinComponent {
    fun initialize() {
        startKoin {
            modules(
                module {
                    // iOS-specific dependencies
                }
            )
        }
    }
}
```

## 📋 Шаг 6: Сборка и запуск

### Вариант A: Автоматическая сборка
1. В Xcode выберите simulator (например, **iPhone 15**)
2. Нажмите **Cmd + B** для сборки
3. Нажмите **Cmd + R** для запуска

### Вариант B: Ручная сборка сначала
```bash
# Собрать shared framework
./gradlew :shared:linkReleaseFrameworkIosSimulatorArm64

# Открыть Xcode
open iosApp/iosApp.xcodeproj

# Собрать и запустить в Xcode (Cmd + R)
```

## ⚠️ Возможные проблемы и решения

### Проблема 1: "shared.framework not found"
**Решение:**
```bash
# Собрать фреймворк вручную
./gradlew :shared:linkReleaseFrameworkIosSimulatorArm64
```

### Проблема 2: "Module 'shared' not found"
**Решение:**
1. Проверьте **Framework Search Paths** в Build Settings
2. Убедитесь, что путь правильный:
   ```
   $(PROJECT_DIR)/../shared/build/bin/iosSimulatorArm64/releaseFramework
   ```

### Проблема 3: Ошибки компиляции Kotlin
**Решение:**
1. Очистите build: `Product → Clean Build Folder` (Shift + Cmd + K)
2. Пересоберите shared модуль:
   ```bash
   ./gradlew :shared:clean :shared:linkReleaseFrameworkIosSimulatorArm64
   ```

### Проблема 4: Кэши Gradle
**Решение:**
```bash
./gradlew clean
rm -rf ~/.gradle/caches/build-cache-1
```

## 📱 Тестирование на реальном устройстве

Для тестирования на физическом iOS устройстве:

```bash
# Собрать для device (arm64)
./gradlew :shared:linkReleaseFrameworkIosArm64
```

В Xcode:
1. Подключите устройство через USB
2. Выберите устройство в списке устройств
3. Настройте **Code Signing** в Build Settings
4. Соберите и запустите (Cmd + R)

## 🎯 Следующие шаги

1. ✅ Настроить Xcode (этот документ)
2. ⏳ Протестировать на iOS Simulator
3. ⏳ Добавить полноценный Compose UI
4. ⏳ Настроить навигацию
5. ⏳ Протестировать на реальном устройстве
6. ⏳ Подготовить к публикации в App Store

## 📞 Поддержка

Если возникли проблемы:
1. Проверьте логи в Xcode (Report Navigator)
2. Проверьте вывод Gradle в терминале
3. Убедитесь, что все пути правильные
4. Попробуйте очистить кэши и пересобрать

Удачи! 🚀
