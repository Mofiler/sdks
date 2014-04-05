/*
 * MofilerValue.h
 *
 *  Created on: 04/04/2014
 *      Author: Mario
 */

#ifndef MOFILERVALUE_H_
#define MOFILERVALUE_H_
#include <QObject>
#include <QMetaType>
#include <QString>
#include <QDateTime>

namespace MofilerSDK
{
	class MofilerValue
	{
		//Q_OBJECT
	public:
		MofilerValue();
		virtual ~MofilerValue();
		long get_tstamp();

		QString attribute;
		QString value;
		long expireAfterMs;

	private:
		long tstamp;
	};
}
#endif /* MOFILERVALUE_H_ */
