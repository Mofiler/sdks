/*
 * MODevice.cpp
 *
 *  Created on: 17/04/2014
 *      Author: Mario
 */

#include "MODevice.h"
#include <bb/device/DisplayInfo>
#include <bb/device/HardwareInfo>
#include <bb/system/LocaleHandler>
#include <QLocale>

namespace MofilerSDK {

MODevice::MODevice() : QObject() {
	// TODO Auto-generated constructor stub

}

MODevice::~MODevice() {
	// TODO Auto-generated destructor stub
}

QString MODevice::getDisplaySize(){
	bb::device::DisplayInfo display;
	return QString(display.pixelSize().width()) + "x" + QString(display.pixelSize().height());
}

QString MODevice::getDeviceManufacturer(){
	return "Blackberry";
}

QString MODevice::getDeviceModelName(){
	bb::device::HardwareInfo hardinfo;
	return hardinfo.modelName();
}

bool MODevice::isPhysicalKeyboardDevice(){
	bb::device::HardwareInfo hardinfo;
	return hardinfo.isPhysicalKeyboardDevice();
}

QString MODevice::getLocale(){
	bb::system::LocaleHandler * region;
	QLocale localecurr = region->locale();
	return localecurr.languageToString(localecurr.language());
}

QVariantMap MODevice::getDeviceInfo(){
	QVariantMap allinfo;

	allinfo.insert("manufacturer", getDeviceManufacturer());
	allinfo.insert("model", getDeviceModelName());
	allinfo.insert("display", getDisplaySize());
	allinfo.insert("is_physical_keyboard", isPhysicalKeyboardDevice() + "");
	allinfo.insert("locale", getLocale());

	return allinfo;
}

} /* namespace MofilerSDK */
