/*
 * Mofiler.h
 *
 *  Created on: 04/04/2014
 *      Author: Mario
 */

#ifndef MOFILER_H_
#define MOFILER_H_

#include <QObject>
#include <QMetaType>
#include <QString>
#include <QMap>
#include <QUrl>
#include <QFile>
#include <QDebug>
#include <bb/data/JsonDataAccess>
#include "MofilerValue.h"
#include "MofilerConstants.h"
#include "Fetcher.hpp"
#include "MOInstallationInfo.h"

namespace MofilerSDK
{

	class Mofiler : public QObject
	{
		Q_OBJECT

		Q_PROPERTY( QString appKey READ getAppKey WRITE setAppKey NOTIFY appKeyChanged);
		Q_PROPERTY( QString appName READ getAppName WRITE setAppName NOTIFY appNameChanged);
		Q_PROPERTY( QString appVersion READ getAppVersion WRITE setAppVersion NOTIFY appVersionChanged);
		Q_PROPERTY( QString cookie READ getCookie WRITE setCookie NOTIFY cookieChanged);
		Q_PROPERTY( QUrl url READ getUrl WRITE setUrl NOTIFY urlChanged);

	public:
		Mofiler();
		virtual ~Mofiler(){}

		/* INVOKABLE METHODS */
		/* identity */
		Q_INVOKABLE void addIdentity(QString key, QString value);
		Q_INVOKABLE QString getIdentity(QString key);
		Q_INVOKABLE void injectValue(QString key, QString value);
		Q_INVOKABLE void injectValue(QString key, QString value, long expireAfterMs);
		Q_INVOKABLE void flushData();


		/* PROPERTY GETTERS/SETTERS */
		/* APP KEY */
		QString getAppKey();
		void setAppKey(QString a_strAppKey);

		/* APP NAME */
		QString getAppName();
		void setAppName(QString a_strAppName);

		/* APP VERSION */
		QString getAppVersion();
		void setAppVersion(QString a_strAppVersion);

		/* cookie */
		QString getCookie();
		void setCookie(QString a_strCookie);

		/* URL */
		QUrl getUrl();
		void setUrl(QUrl a_url);

	signals:
		void appKeyChanged(QString);
		void appNameChanged(QString);
		void appVersionChanged(QString);
		void cookieChanged(QString);
		void urlChanged(QUrl);

	private slots:
		void methodResponded(QNetworkReply* reply);

	private:
		QString m_appKey;
		QString m_appName;
		QString m_appVersion;
		QString m_cookie;
		//QMap<QString, QString> 	m_identity;
		QVariantList m_identity;
		QUrl	m_url;

		//QMap<QString, MofilerValue*> m_values;
		//QVariantMap m_values;
		QVariantList m_values;
		QVariantList m_valuesSent; //copy to be kept in case of communication error
		QVariantMap m_devicecontext;

		Fetcher *m_fetcher;
		MOInstallationInfo *m_moinstall;

		void internal_injectValue(QString key, MofilerValue* value);

		void internal_sendData();
		QVariantMap internal_convertMofileValToVariant(MofilerValue * mofvalue);
		QVariantMap internal_buildPackageToSend(QVariantList a_user_values, QVariantMap a_dev_context, QVariantList a_identities);
		QVariantList internal_copyValueStack(QVariantList a_list);
		QVariantList concatArray(QVariantList arr1, QVariantList arr2);

		void doSaveDataToDisk();
		void loadDataFromStorage();
	};
}
#endif /* MOFILER_H_ */
