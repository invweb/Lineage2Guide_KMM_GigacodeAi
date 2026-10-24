# ✅ Чек-лист настройки Xcode для Lineage2Guide iOS

## 🎯 Что нужно сделать:

### 1️⃣ Открыть проект в Xcode
```bash
open iosApp/iosApp.xcodeproj
```

### 2️⃣ Добавить Run Script Phase
1. Выберите проект **iosApp** в Navigator
2. Выберите target **iosApp**
3. Перейдите на **Build Phases**
4. Нажмите **+** → **New Run Script Phase**
5. В поле **Script** вставьте код из `XCODE_SETUP.md` (Шаг 2)

### 3️⃣ Настроить Header Search Paths
1. В **Build Settings** найдите **Header Search Paths**
2. Добавьте:
   ```
   $(PROJECT_DIR)/../shared/build/bin/iosSimulatorArm64/releaseFramework
   ```
3. Установите флаг **recursive**

### 4️⃣ Настроить Framework Search Paths
1. В **Build Settings** найдите **Framework Search Paths**
2. Добавьте тот же путь, что и в шаге 3

### 5️⃣ Собрать и запустить
1. Выберите simulator (например, **iPhone 15**)
2. Нажмите **Cmd + B** для сборки
3. Нажмите **Cmd + R** для запуска

## 📋 Файлы которые уже есть:

- ✅ `iosApp/iosApp/iosApp.swift` - AppDelegate
- ✅ `iosApp/iosApp/IOSViewController.swift` - iOS view controller
- ✅ `iosApp/iosApp/ComposeView.swift` - Compose wrapper
- ✅ `iosApp/iosApp/Kotlin/IOSApp.kt` - Compose UI
- ✅ `shared/build/bin/iosSimulatorArm64/releaseFramework/shared.framework` - фреймворк собран

## ⚠️ Важно:

- Shared фреймворк уже собран для iOS Simulator
- Все iOS файлы готовы
- Осталось только настроить Xcode согласно чек-листу

## 📖 Подробная инструкция:

Смотрите файл `iosApp/XCODE_SETUP.md`

## 🚀 Готово!

После выполнения всех шагов ваше iOS приложение будет работать! 🎉
