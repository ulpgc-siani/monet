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
package org.jsmtpd.core.common.io.commandStream;

import java.io.IOException;
import java.io.InputStream;

import org.jsmtpd.core.common.io.BareLFException;
import org.jsmtpd.core.common.io.BufferedWireReader;
import org.jsmtpd.core.common.io.InputSizeToBig;
import org.jsmtpd.tools.ByteArrayTool;

/**
 * Used to read input and get strings from it, to replace a bufferedreader/readline<br>
 * Strings are size limited, to prevent someone from injecting long random data to server, causing it to
 * burn memory until out of memory exception...
 * @author Jean-Francois POUX
 */
public class CommandStreamParser {

    private boolean rejectBareLF;
    private BufferedWireReader rd;
    
    public CommandStreamParser(InputStream is, int maxMessageSize, boolean rejectBareLF) {
        this.rejectBareLF = rejectBareLF;
        rd= new BufferedWireReader (512,ByteArrayTool.LF,is,maxMessageSize);
    }

    public String readLine() throws InputSizeToBig, IOException, BareLFException {
        /*
        // BOs stuff, read in a loop for telnet-like client :D
        byte[] buffer = new byte[maxMessageSize];
        int readCount = 0;

        readCount = is.read(buffer);
        String dbg = new String (buffer);
        if (readCount > 3) {
            byte[] realBuffer = new byte[readCount];
            System.arraycopy(buffer, 0, realBuffer, 0, readCount);


            if (realBuffer[readCount - 1] != ByteArrayTool.LF[0])
                throw new InputSizeToBig();

            if ((realBuffer[readCount - 2] != ByteArrayTool.CR[0]) && (rejectBareLF))
                throw new BareLFException();

            return new String(realBuffer).trim();
        }

        return null;
        */
        byte[] data = rd.readBlock();
        
        if (data==null)
            throw new IOException("Null stream");
        if (data.length<3)
            return "";
        
        if ((data[data.length-2]!=ByteArrayTool.CR[0]) && rejectBareLF)
            throw new BareLFException();
        
        String res = new String (data);
        res=res.replaceAll("\r","");
        res=res.replaceAll("\n","");
        
        
        return res;
    }

}