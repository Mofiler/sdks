package com.mofiler.api;

/*
 * @(#)FetcherListener	1.0 24-02-14

    Copyright (C) 2006-2014  Mario Zorz email me at mariozorz at gmail dot com
    The Prosciutto Project website: http://www.prosciuttoproject.org
    The Prosciutto Project blog:   http://prosciutto.boutiquestartups.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
public interface FetcherListener {

    //public void dataReceived(String a_methodCalled, String a_strPayload) throws Exception;
    public void dataReceived(String a_methodCalled, String a_strPayload, String a_strOriginalSentPayload, boolean a_bHTTPMethodIsPost) throws Exception;

}

