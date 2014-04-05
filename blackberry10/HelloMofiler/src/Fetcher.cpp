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

void Fetcher::initiateRequest(QUrl a_Url, QString data)
{
	qDebug() << "initiating request";

    // Create and send the network request.
    QNetworkRequest request = QNetworkRequest();
    //request.setUrl(QUrl("http://developer.blackberry.com/cascades/files/documentation/images/model.xml"));
    //request.setUrl(QUrl("http://techslides.com/demos/sample-videos/small.mp4"));
    request.setUrl(a_Url);
    //http://techslides.com/demos/sample-videos/small.mp4

	request.setHeader(QNetworkRequest::ContentTypeHeader,
    				"application/json");
	request.setRawHeader("Accept-Encoding", "application/gzipped");
	request.setRawHeader("Pragma", "no-cache");
	request.setRawHeader("X-Mofiler-NoiseLevel", "0");
	request.setRawHeader("X-Mofiler-SessionID", "0");
	request.setRawHeader("X-Mofiler-InstallID", "0");
	request.setRawHeader("X-Mofiler-ApiVersion", "0.1");
	request.setRawHeader("X-Mofiler-AppKey", "MYAPP_KEY_HERE");

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

	qDebug() << "Estamos en el requestFinished";

    // Check the network reply for errors.
    if (reply->error() == QNetworkReply::NoError)
    {
        // Open the file and print an error if the file cannot be opened.
        /*if (!myFile->open(QIODevice::ReadWrite))
        {
            qDebug() << "\n Failed to open file";
            return;
        }

        // Write to the file using the reply data and close the file.
        QByteArray buff = reply->readAll();

        qDebug() << "vamos a imprimir lo que vino de la red";
        //qDebug() << buff;


        //myFile->write(reply->readAll());
        myFile->write(buff);
        myFile->flush();
        myFile->close();*/

        qDebug() << "listo el archivo: " << QUrl("file://" + QDir::homePath() + "/model.xml");

    }
    else
    {
        qDebug() << "\n Problem with the network";
        qDebug() << "\n" << reply->errorString();
    }

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
