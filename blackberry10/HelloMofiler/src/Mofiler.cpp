/*
 * Mofiler.cpp
 *
 *  Created on: 04/04/2014
 *      Author: Mario
 */

#include "Mofiler.h"
#include "MofilerConstants.h"
#include "MODevice.h"


using namespace MofilerSDK;
using namespace bb::data;

Mofiler::Mofiler() : QObject(){
	// TODO Auto-generated constructor stub
	m_fetcher = new Fetcher();
	m_fetcher->setParent(this);

    bool result = connect(m_fetcher, SIGNAL(methodResponded(QNetworkReply*)),
            this, SLOT(methodResponded(QNetworkReply*)));

    loadDataFromStorage();
}

/* APP KEY */
QString Mofiler::getAppKey(){
        return m_appKey;
    }

void Mofiler::setAppKey(QString a_strAppKey){
	m_appKey = a_strAppKey;
    emit appKeyChanged(m_appKey);
}

/* APP NAME */
QString Mofiler::getAppName(){
        return m_appName;
    }

void Mofiler::setAppName(QString a_strAppName){
	m_appName = a_strAppName;
    emit appNameChanged(m_appName);
}


/* APP VERSION */
QString Mofiler::getAppVersion(){
        return m_appVersion;
    }

void Mofiler::setAppVersion(QString a_strAppVersion){
	m_appVersion = a_strAppVersion;
    emit appVersionChanged(m_appVersion);
}

/* cookie */
QString Mofiler::getCookie(){
        return m_cookie;
    }

void Mofiler::setCookie(QString a_strCookie){
	m_cookie = a_strCookie;
    emit cookieChanged(m_cookie);
}

/* URL */
QUrl Mofiler::getUrl(){
        return m_url;
    }

void Mofiler::setUrl(QUrl a_url){
	m_url = a_url;
    emit urlChanged(m_url);
}

/* IDENTiTY */
void Mofiler::addIdentity(QString key, QString value){
	//m_identity.insert(key, value);
	QVariantMap valret;// = new QVariant();
	valret[key] = value;

	m_identity << valret;

}

QString Mofiler::getIdentity(QString key){

	/*for (int i=0; i < m_identity.size(); i++){
		QVariantMap elem = (QVariantMap) (m_identity[i]);
		if (elem.contains(key))
			return elem.value(key).toString();
	}*/

	/*QVariantList membersList;
	foreach(QVariantMap s, m_identity){
		membersList << s;
	}*/

	return NULL;
}

/* VALUE INJECTOR */
void Mofiler::injectValue(QString key, QString value){
	MofilerValue *mofvalue = new MofilerValue();
	mofvalue->attribute = key;
	mofvalue->value = value;
	internal_injectValue(key, mofvalue);
}

/* VALUE INJECTOR */
void Mofiler::injectValue(QString key, QString value, long expireAfterMs){
	MofilerValue *mofvalue = new MofilerValue();
	mofvalue->attribute = key;
	mofvalue->value = value;
	mofvalue->expireAfterMs = expireAfterMs;
	internal_injectValue(key, mofvalue);
}

QVariantMap Mofiler::internal_convertMofileValToVariant(MofilerValue * mofvalue){

	QVariantMap valret;// = new QVariant();
	uint tstamp = mofvalue->get_tstamp();

	valret["tstamp"] = tstamp;
	valret[mofvalue->attribute] = mofvalue->value;
	if (mofvalue->expireAfterMs > 0)
		valret["expireAfterMs"] = (uint) (mofvalue->expireAfterMs);

	return valret;
}

void Mofiler::flushData(){
	internal_sendData();
}

void Mofiler::internal_injectValue(QString key, MofilerValue* mofvalue){
	int istacklength = K_MOFILER_STACK_LENGTH;
	int istackmaxlength = K_MOFILER_MAX_STACK_LENGTH;
	qDebug() << "size es: " << m_values.size();
	int isizeofvalues = m_values.size();
	if (((m_values.size() % istacklength) != 0) || (m_values.size() == 0)){
		//m_values.insert(key, mofvalue);
		//m_values.insert(key, internal_convertMofileValToVariant(mofvalue));
		m_values << internal_convertMofileValToVariant(mofvalue);
		doSaveDataToDisk();
	}
	else if(m_values.size() > istackmaxlength){

		//here build the JSON body to be sent

		/*
		 * EXAMPLE:
{ mofiler_device_context:
   { display: '320x240',
     'X-Mofiler-SessionID': '7672286413323581556',
     manufacturer: 'Blackberry',
     network: ';deviceside=true',
     model: '8300',
     'X-Mofiler-InstallID': '-6994742345649929124',
     locale: 'en' },
  user_values:
   [ { mykey0: 'myvalue', tstamp: 1396613815758 },
     { mykey1: 'myvalue', tstamp: 1396613816091 },
     { mykey2: 'myvalue', tstamp: 1396613816451 },
     { mykey3: 'myvalue', tstamp: 1396613816655 },
     { mykey4: 'myvalue', tstamp: 1396613816813 },
     { mykey5: 'myvalue', tstamp: 1396613816957 },
     { mykey6: 'myvalue', tstamp: 1396613817282 },
     { mykey7: 'myvalue', tstamp: 1396613817688 },
     { mykey8: 'myvalue', tstamp: 1396613818757 },
     { mykey9: 'myvalue', tstamp: 1396613819140 } ],
  identity: [ { username: 'johndoe' } ] }
		 *
		 */
		internal_sendData();

		m_values.clear();
		m_values << internal_convertMofileValToVariant(mofvalue);
		doSaveDataToDisk();

		/*
		//send this and clean all
		restApi.pushKeyValueStack(jsonUserValues);
		jsonUserValues = new JSONArray();
		mofilerValues.setJsonStack(jsonUserValues);
		if (bUseExpireAfter)
			internal_populateVector(key, value, expireAfterMs);
		else
			internal_populateVector(key, value);
		doSaveDataToDisk();
		*/
	}
	else
	{

		internal_sendData();

		m_values.clear();
		m_values << internal_convertMofileValToVariant(mofvalue);
		doSaveDataToDisk();


		/*
		//send stack data and then push the new data into the stack
		//deferredObj = new MofilerDeferredObject(key, value);
		restApi.pushKeyValueStack(jsonUserValues);
		doSaveDataToDisk();
		jsonUserValues = new JSONArray();
		if (bUseExpireAfter)
			internal_populateVector(key, value, expireAfterMs);
		else
			internal_populateVector(key, value);
		*/
	}
}

void Mofiler::internal_sendData()
{
	MODevice* moDev = new MODevice();
	QVariant vPackage = internal_buildPackageToSend(m_values, moDev->getDeviceInfo(), m_identity);
	delete moDev;

	m_valuesSent = internal_copyValueStack(m_values);

	JsonDataAccess jda;
	QString jsondata;
	//jda.saveToBuffer(m_values, &jsondata);
	jda.saveToBuffer(vPackage, &jsondata);

	if (jda.hasError()) {
		const DataAccessError err = jda.error();
		const QString errorMsg = tr("Error converting Qt data to JSON: %1").arg(err.errorMessage());
		//setResultAndState(errorMsg, QtDisplayed);
		qDebug() << errorMsg;
	} else {
		/*setRhsTitleAndText(tr("JSON Data from Qt"), jsonBuffer);
		setResultAndState(result + tr("Success"), ReadyToWrite);*/
		qDebug() << jsondata;
	}

	m_fetcher->addHeader("X-Mofiler-AppKey", m_appKey);
	//m_fetcher->addHeader("X-Mofiler-NoiseLevel", "0");
	m_fetcher->addHeader("X-Mofiler-ApiVersion", "0.1");

	//now send to server
	m_fetcher->initiateRequest(m_url, jsondata);

}

QVariantList Mofiler::internal_copyValueStack(QVariantList a_list)
{
	QVariantList newlist;

	foreach(QVariant s, a_list){
		newlist << s;
	}

	return newlist;
}

QVariantList Mofiler::concatArray(QVariantList arr1, QVariantList arr2)
{
	QVariantList newlist;

	foreach(QVariant s, arr1){
		newlist << s;
	}

	foreach(QVariant s, arr2){
		newlist << s;
	}
	return newlist;
}


QVariantMap Mofiler::internal_buildPackageToSend(QVariantList a_user_values, QVariantMap a_dev_context, QVariantList a_identities){

	QVariantMap mapBody;

	mapBody.insert("mofiler_device_context", a_dev_context);
	mapBody.insert("user_values", a_user_values);
	mapBody.insert("identity", a_identities);


	return mapBody;
}


void Mofiler::loadDataFromStorage()
{
	//now build the JSON stuff and save data
	JsonDataAccess jda;
	QVariant res = jda.load("data/mofilerstack.json");

	if (jda.hasError()) {
		const DataAccessError err = jda.error();
		const QString errorMsg = tr("loadDataFromStorage - Error converting Qt data to JSON: %1").arg(err.errorMessage());
		//setResultAndState(errorMsg, QtDisplayed);
		qDebug() << errorMsg;
	} else {
		/*setRhsTitleAndText(tr("JSON Data from Qt"), jsonBuffer);
		setResultAndState(result + tr("Success"), ReadyToWrite);*/
		m_values = res.value<QVariantList>();
		//m_values << res;
		qDebug() << m_values;
	}

}

void Mofiler::doSaveDataToDisk(){

	/*if (!myFile->open(QIODevice::ReadWrite))
    {
        qDebug() << "\n Failed to open file";
        return;
    }*/

	//now build the JSON stuff and save data
	//QString myJSONData;
	JsonDataAccess jda;
	//jda.saveToBuffer(m_values, &myJSONData);

	//jda.save(myJSONData, "data/mofilerstack.json");
	jda.save(m_values, "data/mofilerstack.json");

	if (jda.hasError()) {
		const DataAccessError err = jda.error();
		const QString errorMsg = tr("doSaveDataToDisk -- Error converting Qt data to JSON: %1").arg(err.errorMessage());
		//setResultAndState(errorMsg, QtDisplayed);
		qDebug() << errorMsg;
	} else {
		/*setRhsTitleAndText(tr("JSON Data from Qt"), jsonBuffer);
		setResultAndState(result + tr("Success"), ReadyToWrite);*/
		qDebug() << m_values;
	}


	/*
    //myFile->write(buff);
    myFile->flush();
    myFile->close();*/


}


void Mofiler::methodResponded(QNetworkReply* reply)
{

	qDebug() << "Estamos en el requestFinished en MOFILER";

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

    	int istacklength = K_MOFILER_STACK_LENGTH;
    	int istackmaxlength = K_MOFILER_MAX_STACK_LENGTH;
    	qDebug() << "size es: " << m_valuesSent.size();
    	int isizeofvalues = m_valuesSent.size();
    	if(m_valuesSent.size() > istackmaxlength){
			//clean all
    		m_values.clear();
			doSaveDataToDisk();
    	} else {
    		m_values = concatArray(m_values, m_valuesSent);
    	}


		/*JSONArray jsonTmpArray = (JSONArray) new JSONArray((String)(a_vectBusinessObject.elementAt(2)));
		if (jsonTmpArray.length() > K_MOFILER_MAX_STACK_LENGTH){
				//clean all
				jsonUserValues = new JSONArray();
				mofilerValues.setJsonStack(jsonUserValues);
				doSaveDataToDisk();
		} else {
			jsonUserValues = concatArray(jsonUserValues, jsonTmpArray);
		}*/


    }

    reply->deleteLater();
}

