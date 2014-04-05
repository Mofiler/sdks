/*
 * Mofiler.cpp
 *
 *  Created on: 04/04/2014
 *      Author: Mario
 */

#include "Mofiler.h"
#include "MofilerConstants.h"


using namespace MofilerSDK;
using namespace bb::data;

Mofiler::Mofiler() : QObject(){
	// TODO Auto-generated constructor stub
	myFile = new QFile("data/mofilerstack.json");
	m_fetcher = new Fetcher();
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
	m_identity.insert(key, value);
}

QString Mofiler::getIdentity(QString key){
	if (m_identity.contains(key))
		return m_identity.value(key);
	else
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

void Mofiler::internal_injectValue(QString key, MofilerValue* mofvalue){
	int istacklength = K_MOFILER_STACK_LENGTH;
	int istackmaxlength = K_MOFILER_MAX_STACK_LENGTH;
	qDebug() << "size es: " << m_values.size();
	int isizeofvalues = m_values.size();
	if (((m_values.size() % istacklength) != 0) || (m_values.size() == 0)){
		//m_values.insert(key, mofvalue);
		//m_values.insert(key, internal_convertMofileValToVariant(mofvalue));
		m_values << internal_convertMofileValToVariant(mofvalue);
	}
	else if(m_values.size() > istackmaxlength){

		JsonDataAccess jda;
		QString jsondata;
		jda.saveToBuffer(m_values, &jsondata);

		//now send to server
		m_fetcher->initiateRequest(m_url, jsondata);

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
		JsonDataAccess jda;
		QString jsondata;
		jda.saveToBuffer(m_values, &jsondata);

		//now send to server
		m_fetcher->initiateRequest(m_url, jsondata);

		m_values.clear();

		m_values << internal_convertMofileValToVariant(mofvalue);


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

void Mofiler::doSaveDataToDisk(){

	if (!myFile->open(QIODevice::ReadWrite))
    {
        qDebug() << "\n Failed to open file";
        return;
    }

	//now build the JSON stuff and save data
	QString myJSONData;
	JsonDataAccess jda;
	jda.saveToBuffer(m_values, &myJSONData);

	jda.save(myJSONData, "data/mofilerstack.json");


    //myFile->write(buff);
    myFile->flush();
    myFile->close();


}
