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
    m_fileSessId = new QFile("data/mofilersessid.txt");

    //now read and populate installation id
	if (!m_file->open(QIODevice::ReadWrite | QIODevice::Text))
    {
        qDebug() << "\n Failed to open file uiid";
        return;
    }
	QTextStream in(m_file);
	QString line = in.readLine();
	m_uiid = line;
	m_file->close();
	generateId(false);


	/* session id */
    //now read and populate session id
	if (!m_fileSessId->open(QIODevice::ReadWrite | QIODevice::Text))
    {
        qDebug() << "\n Failed to open file uiid";
        return;
    }
	QTextStream inS(m_fileSessId);
	QString lineS = inS.readLine();
	m_sessionid = lineS;
	m_fileSessId->close();

	//open again and truncate
	if (!m_fileSessId->open(QIODevice::ReadWrite | QIODevice::Text | QIODevice::Truncate))
    {
        qDebug() << "\n Failed to open file uiid";
        return;
    }

	if (m_sessionid == NULL){
		//generate new session id
		m_sessionid = QString("1");
	} else {
		//increment this by one
		int sessionid = m_sessionid.toInt();
		sessionid++;
		m_sessionid = QString::number(sessionid);
	}
	inS << m_sessionid;
	m_fileSessId->close();

}

MOInstallationInfo::~MOInstallationInfo() {
	// TODO Auto-generated destructor stub
}

void MOInstallationInfo::generateId(bool a_bForceNew){

	bool bNewIDGenerated = false;
	//TODO generate id random here
	if ((m_uiid == NULL) || a_bForceNew){
		QUuid tmpuid = QUuid::createUuid();
		m_uiid = tmpuid.toString();
		bNewIDGenerated = true;
	} else {
		if (m_uiid.length() == 0){
			QUuid tmpuid = QUuid::createUuid();
			m_uiid = tmpuid.toString();
			bNewIDGenerated = true;
		}
	}

	if (bNewIDGenerated){
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
}

QString MOInstallationInfo::getInstallationId(){
	return m_uiid;
}

QString MOInstallationInfo::getSessionId(){
	return m_sessionid;
}

} /* namespace MofilerSDK */
