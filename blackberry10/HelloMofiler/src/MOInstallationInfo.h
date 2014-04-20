/*
 * MOInstallationInfo.h
 *
 *  Created on: 20/04/2014
 *      Author: Mario
 */

#ifndef MOINSTALLATIONINFO_H_
#define MOINSTALLATIONINFO_H_

#include <QObject>
#include <QUuid>
#include <QFile>
#include <QIODevice>


namespace MofilerSDK {

class MOInstallationInfo : public QObject {
	Q_OBJECT
public:
	MOInstallationInfo();
	virtual ~MOInstallationInfo();

	void generateId(bool a_bForceNew);
	QString getInstallationId();
	QString getSessionId();

private:
	//QUuid   m_uiid;
	QString m_uiid;
	QFile   *m_file;
	QFile   *m_fileSessId;
	QString m_sessionid;

};

} /* namespace MofilerSDK */
#endif /* MOINSTALLATIONINFO_H_ */
