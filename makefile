TARGET = 7
NAME = CrapperMapper
PROJPATH = "./"
ACT = CrapperMapperMenu
PKG = com.brilliantsquid.crappermapper
debug:
	ant debug
release:
	ant release
install:
	adb install ./bin/$(NAME)-debug.apk
uninstall:
	adb shell am start -a android.intent.action.DELETE -d package:$(PKG)
create:
	android create project --target $(TARGET) --name $(NAME) --path $(PROJPATH) --activity $(ACT) --package $(PKG)
launch:
	adb shell am start -a android.intent.action.MAIN -n $(PKG)/$(PKG).$(ACT)
run: install launch
	echo "Installed and launched"
