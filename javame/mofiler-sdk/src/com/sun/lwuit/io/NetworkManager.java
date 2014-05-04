/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores
 * CA 94065 USA or visit www.oracle.com if you need additional information or
 * have any questions.
 */

package com.sun.lwuit.io;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Main entry point for managing the connection requests, this is essentially a
 * threaded queue that makes sure to route all connections via the network thread
 * while sending the callbacks through the LWUIT EDT.
 *
 * @author Shai Almog
 */
public class NetworkManager {
    /**
     * Indicates an unknown access point type
     */
    public static final int ACCESS_POINT_TYPE_UNKNOWN = 1;

    /**
     * Indicates a wlan (802.11b/c/g/n) access point type
     */
    public static final int ACCESS_POINT_TYPE_WLAN = 2;

    /**
     * Indicates an access point based on a cable
     */
    public static final int ACCESS_POINT_TYPE_CABLE = 3;

    /**
     * Indicates a 3g network access point type
     */
    public static final int ACCESS_POINT_TYPE_NETWORK3G = 4;

    /**
     * Indicates a 2g network access point type
     */
    public static final int ACCESS_POINT_TYPE_NETWORK2G = 5;


    /**
     * Indicates a corporate routing server access point type (e.g. BIS etc.)
     */
    public static final int ACCESS_POINT_TYPE_CORPORATE = 6;

    private static final Object LOCK = new Object();
    private static final NetworkManager INSTANCE = new NetworkManager();
    private Vector pending = new Vector();
    private boolean running;
    private int threadCount = 1;
    private int timeout = 300000;
    private Hashtable threadAssignements = new Hashtable();
    private Hashtable userHeaders;

    private NetworkManager() {
    }

}
