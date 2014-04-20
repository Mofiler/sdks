#ifndef Fetcher_H_
#define Fetcher_H_

#include <QObject>
#include <QFile>
#include <QtNetwork>
#include <QString>

//#include "StatusEvent.h"

//namespace bb {namespace cascades {class Application;}}
namespace MofilerSDK
{
	class Fetcher : public QObject
	{
		Q_OBJECT

	public:
		Fetcher();
		virtual ~Fetcher(){}

		Q_INVOKABLE void addHeader(QString key, QString value);
		Q_INVOKABLE void initiateRequest(QUrl a_Url, QString data);
		Q_INVOKABLE QUrl myHomeDir(QString);

	signals:
		void networkStatusUpdated(bool status, QString type);
		void methodResponded(QNetworkReply* reply);

	private slots:
		void requestFinished(QNetworkReply* reply);
		void networkStatusUpdateHandler(bool status, QString type);

	private:
		QNetworkAccessManager *myNetworkAccessManager;
		QMap<QString, QString> 	m_headers;
		//StatusEvent *statusEvent;
	};
}

#endif /* Fetcher_H_ */
