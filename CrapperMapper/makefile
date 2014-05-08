build:
	cordova build
install:
	adb install ./platforms/android/ant-build/CrapperMapper-debug.apk
uninstall:
	adb shell am start -a android.intent.action.DELETE -d package:com.brilliantsquid.crappermapper
reinstall: uninstall
	sleep 10
	adb install ./platforms/android/ant-build/CrapperMapper-debug.apk
