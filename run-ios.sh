#!/bin/bash
# Run iOS app on simulator

echo "🚀 Building iOS app..."

# Build iOS framework
echo "📦 Building shared iOS framework..."
./gradlew :shared:compileKotlinIosSimulatorArm64 --quiet

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo "✅ Build successful!"
echo ""
echo "📱 To run on iOS Simulator:"
echo "   Option 1 (Xcode):"
echo "      1. Open: iosApp/iosApp.xcodeproj"
echo "      2. Select 'iosApp' scheme (iOS Simulator)"
echo "      3. Press Cmd+R or click Run"
echo ""
echo "   Option 2 (Gradle):"
echo "      ./gradlew :shared:iosSimulatorArm64EmulatorRun"
