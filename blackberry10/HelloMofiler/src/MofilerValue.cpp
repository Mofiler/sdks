/*
 * MofilerValue.cpp
 *
 *  Created on: 04/04/2014
 *      Author: Mario
 */

#include "MofilerValue.h"
using namespace MofilerSDK;

MofilerValue::MofilerValue() {
	// TODO Auto-generated constructor stub
	expireAfterMs = -1;
	tstamp = QDateTime::currentMSecsSinceEpoch();
}

MofilerValue::~MofilerValue() {
	// TODO Auto-generated destructor stub
}

long MofilerValue:: get_tstamp(){
	return tstamp;
}

