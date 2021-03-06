# Auto-generated by IDE. Any changes made by user will be lost!
BASEDIR =  $$quote($$_PRO_FILE_PWD_)

device {
    CONFIG(debug, debug|release) {
        INCLUDEPATH +=  $$quote(${QNX_TARGET}/usr/include/qt4/QtCore)

        DEPENDPATH +=  $$quote(${QNX_TARGET}/usr/include/qt4/QtCore)

        LIBS += -lQtCore

        SOURCES +=  $$quote($$BASEDIR/src/Fetcher.cpp) \
                 $$quote($$BASEDIR/src/MODevice.cpp) \
                 $$quote($$BASEDIR/src/MOInstallationInfo.cpp) \
                 $$quote($$BASEDIR/src/Mofiler.cpp) \
                 $$quote($$BASEDIR/src/MofilerValue.cpp) \
                 $$quote($$BASEDIR/src/StatusEvent.cpp) \
                 $$quote($$BASEDIR/src/applicationui.cpp) \
                 $$quote($$BASEDIR/src/main.cpp)

        HEADERS +=  $$quote($$BASEDIR/src/Fetcher.hpp) \
                 $$quote($$BASEDIR/src/MODevice.h) \
                 $$quote($$BASEDIR/src/MOInstallationInfo.h) \
                 $$quote($$BASEDIR/src/Mofiler.h) \
                 $$quote($$BASEDIR/src/MofilerConstants.h) \
                 $$quote($$BASEDIR/src/MofilerValue.h) \
                 $$quote($$BASEDIR/src/StatusEvent.h) \
                 $$quote($$BASEDIR/src/applicationui.hpp)
    }

    CONFIG(release, debug|release) {
        INCLUDEPATH +=  $$quote(${QNX_TARGET}/usr/include/qt4/QtCore)

        DEPENDPATH +=  $$quote(${QNX_TARGET}/usr/include/qt4/QtCore)

        LIBS += -lQtCore

        SOURCES +=  $$quote($$BASEDIR/src/Fetcher.cpp) \
                 $$quote($$BASEDIR/src/MODevice.cpp) \
                 $$quote($$BASEDIR/src/MOInstallationInfo.cpp) \
                 $$quote($$BASEDIR/src/Mofiler.cpp) \
                 $$quote($$BASEDIR/src/MofilerValue.cpp) \
                 $$quote($$BASEDIR/src/StatusEvent.cpp) \
                 $$quote($$BASEDIR/src/applicationui.cpp) \
                 $$quote($$BASEDIR/src/main.cpp)

        HEADERS +=  $$quote($$BASEDIR/src/Fetcher.hpp) \
                 $$quote($$BASEDIR/src/MODevice.h) \
                 $$quote($$BASEDIR/src/MOInstallationInfo.h) \
                 $$quote($$BASEDIR/src/Mofiler.h) \
                 $$quote($$BASEDIR/src/MofilerConstants.h) \
                 $$quote($$BASEDIR/src/MofilerValue.h) \
                 $$quote($$BASEDIR/src/StatusEvent.h) \
                 $$quote($$BASEDIR/src/applicationui.hpp)
    }
}

simulator {
    CONFIG(debug, debug|release) {
        INCLUDEPATH +=  $$quote(${QNX_TARGET}/usr/include/qt4/QtCore)

        DEPENDPATH +=  $$quote(${QNX_TARGET}/usr/include/qt4/QtCore)

        LIBS += -lQtCore

        SOURCES +=  $$quote($$BASEDIR/src/Fetcher.cpp) \
                 $$quote($$BASEDIR/src/MODevice.cpp) \
                 $$quote($$BASEDIR/src/MOInstallationInfo.cpp) \
                 $$quote($$BASEDIR/src/Mofiler.cpp) \
                 $$quote($$BASEDIR/src/MofilerValue.cpp) \
                 $$quote($$BASEDIR/src/StatusEvent.cpp) \
                 $$quote($$BASEDIR/src/applicationui.cpp) \
                 $$quote($$BASEDIR/src/main.cpp)

        HEADERS +=  $$quote($$BASEDIR/src/Fetcher.hpp) \
                 $$quote($$BASEDIR/src/MODevice.h) \
                 $$quote($$BASEDIR/src/MOInstallationInfo.h) \
                 $$quote($$BASEDIR/src/Mofiler.h) \
                 $$quote($$BASEDIR/src/MofilerConstants.h) \
                 $$quote($$BASEDIR/src/MofilerValue.h) \
                 $$quote($$BASEDIR/src/StatusEvent.h) \
                 $$quote($$BASEDIR/src/applicationui.hpp)
    }
}

INCLUDEPATH +=  $$quote($$BASEDIR/src)

CONFIG += precompile_header

PRECOMPILED_HEADER =  $$quote($$BASEDIR/precompiled.h)

lupdate_inclusion {
    SOURCES +=  $$quote($$BASEDIR/../src/*.c) \
             $$quote($$BASEDIR/../src/*.c++) \
             $$quote($$BASEDIR/../src/*.cc) \
             $$quote($$BASEDIR/../src/*.cpp) \
             $$quote($$BASEDIR/../src/*.cxx) \
             $$quote($$BASEDIR/../assets/*.qml) \
             $$quote($$BASEDIR/../assets/*.js) \
             $$quote($$BASEDIR/../assets/*.qs)

    HEADERS +=  $$quote($$BASEDIR/../src/*.h) \
             $$quote($$BASEDIR/../src/*.h++) \
             $$quote($$BASEDIR/../src/*.hh) \
             $$quote($$BASEDIR/../src/*.hpp) \
             $$quote($$BASEDIR/../src/*.hxx)
}

TRANSLATIONS =  $$quote($${TARGET}.ts)
