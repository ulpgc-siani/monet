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
package org.jsmtpd.core.common.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;


/**
 * Reads byte from an InputStream to byte array or String
 * @author Jean-Francois POUX
 */
public class BufferedWireReader extends Reader {

    private byte[] buffer;
    private InputStream is;
    private RefByteArrayOutputStream bos ;
    private byte[] dataSeparator;
    private int maxSize;
    
    public BufferedWireReader (int bufferSize, byte[] dataSeparator, InputStream is, int maxSize) {
        buffer=new byte[bufferSize];
        this.is=is;
        this.maxSize=maxSize;
        bos= new RefByteArrayOutputStream();
        this.dataSeparator=dataSeparator;
    }
    
    public byte[] readBlock () throws IOException {
        bos.reset();
        int readCount=0;
        while (true) {
            if (is.available()+bos.size()>maxSize)
                throw new InputSizeToBig();
            readCount=is.read(buffer);
            if (readCount<0)
                throw new EOFException();
            byte[] effectiveData = new byte[readCount];
            System.arraycopy(buffer,0,effectiveData,0,readCount);
            bos.write(effectiveData);
            if (checkEOS())
                break;
        }
        return bos.toByteArray();
    }
    
    public String readLine () throws IOException {
        return new String (readBlock());
    }
    
    private boolean checkEOS () {
        if (bos.size()<dataSeparator.length)
            return false;
        byte[] buffer = bos.toRefByteArray();
        for (int i=0;i<dataSeparator.length;i++) {
            if (dataSeparator[i]!=buffer[bos.validData()-i-1])
                return false;
        }
        return true;
    }
    
    public int read(char[] cbuf, int off, int len) throws IOException {
        return -1;
    }

    public void close() throws IOException {
        is.close();
    }

}
