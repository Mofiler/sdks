#include "Fetcher.hpp"

#include <QIODevice>

/*#include <bps/bps.h>
#include <bps/netstatus.h>
#include <bps/locale.h>
*/
using namespace MofilerSDK;

Fetcher::Fetcher() : QObject()
{
    // Create a network access manager and connect a custom slot to its
    // finished signal.
    myNetworkAccessManager = new QNetworkAccessManager(this);

    bool result = connect(myNetworkAccessManager, SIGNAL(finished(QNetworkReply*)),
            this, SLOT(requestFinished(QNetworkReply*)));

    // Create a file in the file system that we can use to save the data model.
    //myFile = new QFile("data/model.xml");
    //myFile = new QFile("data/video.mp4");

}

void Fetcher::addHeader(QString key, QString value){
	m_headers.insert(key, value);
}

void Fetcher::initiateRequest(QUrl a_Url, QString data)
{
	qDebug() << "initiating request";
	qDebug() << "URL to hit is: " << a_Url;

    // Create and send the network request.
    QNetworkRequest request = QNetworkRequest();
	//request.setUrl(a_Url.toString() +  "/api/values/");
	request.setUrl(a_Url);


    QByteArray postDataSize = QByteArray::number(data.size());

	request.setHeader(QNetworkRequest::ContentTypeHeader,
    				"application/json");
	request.setHeader( QNetworkRequest::ContentLengthHeader,QString(postDataSize).toUtf8());
	request.setRawHeader("Accept-Encoding", "application/gzipped");
	request.setRawHeader("Pragma", "no-cache");
	/*request.setRawHeader("X-Mofiler-NoiseLevel", "0");
	request.setRawHeader("X-Mofiler-SessionID", "0");
	request.setRawHeader("X-Mofiler-InstallID", "0");
	request.setRawHeader("X-Mofiler-ApiVersion", "0.1");
	request.setRawHeader("X-Mofiler-AppKey", "MYAPP_KEY_HERE");*/
	foreach( QString key, m_headers.keys() )
		request.setRawHeader(key.toAscii(), QString(m_headers.value(key)).toAscii());

	if (data != NULL && data.size() > 0){
	    myNetworkAccessManager->post(request, data.toUtf8());
	}
	else
	{
	    myNetworkAccessManager->get(request);
	}
}

void Fetcher::requestFinished(QNetworkReply* reply)
{

	qDebug() << "Fetcher: requestFinished";

    // Check the network reply for errors.
    if (reply->error() == QNetworkReply::NoError)
    {
        //qDebug() << "listo el archivo: " << QUrl("file://" + QDir::homePath() + "/model.xml");
    }
    else
    {
        qDebug() << "\n Problem with the network";
        qDebug() << "\n" << reply->errorString();
    }

    emit methodResponded(reply);

    reply->deleteLater();
}

QUrl Fetcher::myHomeDir(QString a_strfilename)
{
	return QUrl("file://" + QDir::homePath() + a_strfilename);
}
void Fetcher::networkStatusUpdateHandler(bool status, QString type)
{
    /*if (status)
    {
        myNetwrkConnDot->setImageSource(QUrl("asset:///images/greenDot.png"));
        initiateRequest();
    } else
    {
        myNetwrkConnDot->setImageSource(QUrl("asset:///images/redDot.png"));

        myLabel->setText("Network connection has been lost");
        myLabel->textStyle()->setColor(Color::Red);
    }

    myRefreshAction->setEnabled(status);
    myNetwrkTypeSymb->setImageSource(QUrl(type));*/
}
