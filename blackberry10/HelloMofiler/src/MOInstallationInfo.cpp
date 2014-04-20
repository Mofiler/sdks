/*
 * MOInstallationInfo.cpp
 *
 *  Created on: 20/04/2014
 *      Author: Mario
 */

#include "MOInstallationInfo.h"
#include <QDebug>

namespace MofilerSDK {

MOInstallationInfo::MOInstallationInfo() : QObject()  {
	// TODO Auto-generated constructor stub
    m_file = new QFile("data/mofileruiid.txt");

    //now read and populate
	if (!m_file->open(QIODevice::ReadWrite | QIODevice::Text))
    {
        qDebug() << "\n Failed to open file";
        return;
    }
	QTextStream in(m_file);
	QString line = in.readLine();
	m_uiid = line;
	generateId(false);
	m_file->close();


	/* session id */
	int number;
	int randomValue = qrand() % 4294967296;
	int value;
	QString aString=QString::number(value);
	m_sessionid = aString;

}

MOInstallationInfo::~MOInstallationInfo() {
	// TODO Auto-generated destructor stub
}

void MOInstallationInfo::generateId(bool a_bForceNew){

	//TODO generate id random here
	if ((m_uiid == NULL) || a_bForceNew){
		QUuid tmpuid = QUuid::createUuid();
		m_uiid = tmpuid.toString();
	} else {
		if (m_uiid.length() == 0){
			QUuid tmpuid = QUuid::createUuid();
			m_uiid = tmpuid.toString();
		}
	}

	if (!m_file->open(QIODevice::ReadWrite | QIODevice::Text))
    {
        qDebug() << "\n Failed to open file";
        return;
    }
	QTextStream out(m_file);
	out << m_uiid;

	m_file->flush();
	m_file->close();
}

QString MOInstallationInfo::getInstallationId(){
	return m_uiid;
}

QString MOInstallationInfo::getSessionId(){
	return m_sessionid;
}

} /* namespace MofilerSDK */
