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
package org.jsmtpd.core.common.io.dataStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jsmtpd.core.common.io.BareLFException;
import org.jsmtpd.core.common.io.InvalidStreamParserInitialisation;
import org.jsmtpd.core.common.io.InputSizeToBig;

/**
 * @author Jean-Francois POUX
 */
public class DataStreamParser {

    private ByteArrayOutputStream bos = null;
    private byte[] buffer = null;
    private long maxMessageSize;
    private int readSize = 0;
    private byte[] cleanData = null;
    private long currentSize = 0;

    /**
     * 
     * @param bufferSize read buffer to use
     * @param maxMessageSize maximum message size
     * @throws InvalidStreamParserInitialisation
     */
    public DataStreamParser(int bufferSize, long maxMessageSize) throws InvalidStreamParserInitialisation {
        if (bufferSize < 10)
            throw new InvalidStreamParserInitialisation();
        
        if (maxMessageSize < 10)
            throw new InvalidStreamParserInitialisation();
        
        bos = new ByteArrayOutputStream(bufferSize);
        this.maxMessageSize = maxMessageSize;
        buffer = new byte[bufferSize];
    }

    /**
     * adds a string to the buffer
     * @param toAppend
     */
    public void appendString(String toAppend) {
        String tmp = toAppend + "\r\n";
        try {
            bos.write(tmp.getBytes());
        } catch (IOException e) {}
    }

    /**
     * Reads the input stream by block, returns when crlf is encountered
     * @param in
     * @throws IOException
     * @throws InputSizeToBig
     * @throws BareLFException
     */
    public void parseInputStream(InputStream in) throws IOException, InputSizeToBig, BareLFException {
        while (true) {
            readSize = in.read(buffer);
            if (readSize<0)
            	throw new IOException("Negative byte count read");
            currentSize += readSize;
            bos.write(buffer, 0, readSize);
            if (currentSize > maxMessageSize)
                throw new InputSizeToBig();

            if (checkEOS())
                break;

        }
    }

    /**
     * Checks in the buffer if there is a crlf sequence
     * @return
     * @throws BareLFException
     */
    private boolean checkEOS() throws BareLFException {

        //  Detect CR LF . CR LF => return true
        if (buffer[readSize - 1] == 10) // don't cast bos to array immediatly (inc perf)
        {
            int sz = bos.size();
            byte[] pt = bos.toByteArray();
            if ((pt[sz - 2] == 13) && (pt[sz - 3] == 46) && (pt[sz - 4] == 10) && (pt[sz - 5] == 13)) { // last thing in the buffer is <CR><LF>.<CR><LF> 
                cleanData = new byte[bos.size() - 5];
                System.arraycopy(bos.toByteArray(), 0, cleanData, 0, bos.size() - 5);
                return true;
            }
        }

        return false;
    }

    public byte[] getData() {
        if (cleanData != null)
            return cleanData;
        return bos.toByteArray();
    }

    public void checkData() {
    }
}