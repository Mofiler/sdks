/*
 * MODevice.h
 *
 *  Created on: 17/04/2014
 *      Author: Mario
 */

#ifndef MODEVICE_H_
#define MODEVICE_H_

#include <QObject>
#include <QMetaType>
#include <QVariant>
#include <QString>
#include <QMap>

namespace MofilerSDK {

class MODevice : public QObject
{
	Q_OBJECT
	public:
		MODevice();
		virtual ~MODevice();

		QString getDisplaySize();
		QString getDeviceManufacturer();
		QString getDeviceModelName();
		bool isPhysicalKeyboardDevice();
		QString getLocale();
		QVariantMap getDeviceInfo();
};

} /* namespace MofilerSDK */
#endif /* MODEVICE_H_ */
