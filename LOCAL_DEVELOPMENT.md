# Lineage 2 Guide - Local Development Setup

## 🚀 Быстрый старт

### 1. Запуск сервера
```bash
./gradlew :server:run
```
Сервер запустится на `http://localhost:8080`

### 2. Запуск Desktop приложения
```bash
./gradlew :desktop:runDesktop
```

### 3. Запуск Android (в Android Studio)
Открой проект в Android Studio и запусти `Lineage2App`

---

## 📁 Структура проекта

```
MyApplication4/
├── server/              # Ktor сервер (API + иконки)
│   ├── src/main/
│   │   └── resources/
│   │       ├── icons/items/    # 1955 иконок предметов
│   │       └── icon_mapping.json
│   └── build.gradle
├── shared/              # Общий код (KMP)
│   └── src/commonMain/kotlin/
│       ├── data/
│       │   ├── icons/         # IconLoader, Coil
│       │   ├── network/       # API сервисы
│       │   └── repository/    # Репозитории
│       └── ui/                # UI компоненты
├── desktop/             # Desktop приложение (Compose Desktop)
└── app/                 # Android приложение
```

---

## 🔧 API Endpoints

Сервер работает на `http://localhost:8080`

### Основные endpoints:
- `GET /api/v1/items?offset=0&limit=50` - список предметов
- `GET /api/v1/items/{id}` - предмет по ID
- `GET /api/v1/skills?offset=0&limit=50` - список навыков
- `GET /api/v1/npcs?offset=0&limit=50` - список NPC
- `GET /api/v1/quests?offset=0&limit=50` - список квестов
- `GET /api/v1/classes?offset=0&limit=50` - список классов

### Иконки:
- `GET /api/v1/icons/items/{id}` - иконка предмета (PNG)

---

## 📊 Данные

- **Предметы:** 9206 (1955 с иконками)
- **Навыки:** 15308
- **NPC:** 6472
- **Квесты:** 329
- **Классы:** 89

Данные загружаются из `lineage2-api` (Interlude chronicle).

---

## 🛠 Разработка

### Добавление новых иконок
1. Скачай иконку из `lineage2-api` репозитория
2. Положи в `server/src/main/resources/icons/items/`
3. Добавь маппинг в `icon_mapping.json`

### Тестирование API
```bash
# Проверка сервера
curl http://localhost:8080/

# Проверка предметов
curl "http://localhost:8080/api/v1/items?offset=0&limit=1"

# Проверка иконки
curl -o /tmp/test.png "http://localhost:8080/api/v1/icons/items/1"
file /tmp/test.png
```

---

## 📦 Деплой (когда будет готово)

### Варианты хостинга из России:
- **Timeweb Cloud** - от ~150₽/мес
- **Beget** - от ~150₽/мес
- **Yandex Cloud** - 300₽ на 30 дней

### Сборка JAR для деплоя:
```bash
./gradlew :server:shadowJar
# JAR будет в: server/build/libs/server-1.0-all.jar
```

### Запуск на сервере:
```bash
PORT=8080 java -jar server-1.0-all.jar
```

---

## 🐛 Решение проблем

### Иконки не загружаются
1. Убедись, что сервер запущен: `curl http://localhost:8080/`
2. Проверь маппинг: `cat server/src/main/resources/icon_mapping.json | python3 -m json.tool | head`
3. Проверь файл иконки: `curl -o /tmp/test.png "http://localhost:8080/api/v1/icons/items/1"`

### Сервер не запускается
```bash
# Останови все процессы на порту 8080
lsof -i :8080 | grep -v PID | awk '{print $2}' | xargs kill -9

# Перезапусти
./gradlew :server:run
```

### Desktop не подключается к серверу
1. Убедись, что сервер запущен
2. Проверь URL в `DefaultSettingsManager` (должен быть `http://localhost:8080`)
3. Перезапусти Desktop приложение

---

## 📝 Чек-лист перед коммитом

- [ ] Сервер запускается: `./gradlew :server:run`
- [ ] Desktop запускается: `./gradlew :desktop:runDesktop`
- [ ] Иконки отображаются (илиFallback работают)
- [ ] API endpoints возвращают данные
- [ ] Коммит с понятным описанием
- [ ] Push в репозиторий

---

## 🎯 Следующие шаги

1. [ ] Добавить иконки для NPC
2. [ ] Добавить иконки для навыков
3. [ ] Оптимизировать загрузку данных
4. [ ] Добавить кэширование
5. [ ] Подготовить к деплою на хостинг
