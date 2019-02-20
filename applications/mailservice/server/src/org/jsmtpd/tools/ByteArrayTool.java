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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tool to manipulate byte arrays
 * Implementations is quite slow at the momment...
 * @author Jean-Francois POUX
 */
public class ByteArrayTool {

    // this is slow ...
    public static byte[] replaceBytes(byte[] src, byte[] pattern, byte[] replace) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int start = 0;
        int i;
        for (i = 0; i < src.length; i++) {
            if (src[i] == pattern[0]) {
                boolean found = true;
                for (int j = 0; j < pattern.length; j++) { //starts at j=1!    
                    if (i + pattern.length > src.length)  { //don't loop anymore
                        found = false;
                        break;
                    }
                    if (src[i + j] != pattern[j]) {
                        found = false;
                        break;
                    }
                }
                bos.write(src, start, i - start); //copy to begin of pattern
                if (found) { // add pattern
                    bos.write(replace, 0, replace.length);
                    i += pattern.length - 1; // move src forward
                    start = i + 1;
                } else
                    start = i;
            }
        }
        bos.write(src, start, i - start);
        return bos.toByteArray();
    }

    // check for any LF, if it is no preceeded with CR, add CR
    // slow impl ...
    public static byte[] crlfFix(byte[] src) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int i = 0; i < src.length; i++) {
            if ((src[i] == LF[0]) && (i > 1)) {
                if (src[i - 1] != CR[0])
                    bos.write(CR, 0, 1);
            }
            bos.write(src[i]);
        }
        return bos.toByteArray();
    }

    public static boolean compare (byte[] a, byte[] b) {
        return Arrays.equals(a,b);
    }
    
    public static List<byte[]> split (byte[] input, byte separator) {
        if (input==null)
            return null;
        
        List<byte[]> lst = new ArrayList<byte[]>();
        int startPos=0;
        for (int i = 0; i < input.length; i++) {
            if (input[i]==separator) {
                byte[] tmp = new byte[i-startPos];
                System.arraycopy(input,startPos,tmp,0,i-startPos);
                lst.add(tmp);
                startPos=i+1;
            }
        }
        if ((startPos!=0)&&(startPos<input.length)) {
            byte[] tmp = new byte[input.length-startPos];
            System.arraycopy(input,startPos,tmp,0,input.length-startPos);
            lst.add(tmp);
        }
        return lst;
    }
    public static int patternAt (byte[] toCheck, byte[] pattern) {
        /*
        for (int i = toCheck.length-1; i ==0; i--) {
            if (toCheck[i]==pattern[0]) { // look for first byte of pattern
                int offset = toCheck.length-i;
                if (offset>=pattern.length) {
                    for (int j = 0; j < pattern.length; j++) {
                        byte b = pattern[j];
                        
                    }
                }
            }
            
        }
        */
        return -1;
    }
    public static final byte[] CRLF = { 13, 10 };
    public static final byte[] LF = { 10 };
    public static final byte[] CR = { 13 };
    public static final byte[] EOM = { 13, 10, 46, 13, 10 };
    public static final byte NULL = 0;
}