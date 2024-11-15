/*
 * 
 * Jsmtpd, Java SMTP daemon
 * Copyright (C) 2005  Jean-Francois POUX, jf.poux@laposte.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package org.jsmtpd.tools;

/**
 * @author Jean-Francois POUX
 * Jsmtp
 */
public class MeasureTool {

    private long start = 0;
    private static MeasureTool instance = null;

    private MeasureTool() {

    }

    public static MeasureTool getInstance() {
        if (instance == null)
            instance = new MeasureTool();
        return instance;
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public long getElapsed() {
        return System.currentTimeMillis() - start;
    }
}