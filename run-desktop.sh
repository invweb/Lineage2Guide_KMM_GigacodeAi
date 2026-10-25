#!/bin/bash
# Запуск Desktop приложения Lineage 2 Guide
# Требует: JDK 17+, Gradle build выполнен

cd "$(dirname "$0")"

echo "Building Desktop module..."
./gradlew :desktop:desktopJar :shared:compileKotlinDesktop --quiet

if [ $? -ne 0 ]; then
    echo "Build failed. Please check for errors above."
    exit 1
fi

# Находим все JAR в classpath
CLASSPATH=""
for jar in $(./gradlew -q :desktop:printClasspath --console=plain 2>/dev/null | tr ':' '\n'); do
    if [ -f "$jar" ]; then
        CLASSPATH="$CLASSPATH:$jar"
    fi
done

# Запускаем с native library path
java \
    -Djava.library.path="$(find ~/.gradle/caches -name "skiko-runtime*" -type d 2>/dev/null | head -1)" \
    -cp "$CLASSPATH" \
    com.zx_tole.lineage2_guide.DesktopAppKt
