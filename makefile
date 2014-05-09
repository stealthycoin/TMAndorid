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
create:
	android create project --target $(TARGET) --name $(NAME) --path $(PROJPATH) --activity $(ACT) --package $(PKG)
