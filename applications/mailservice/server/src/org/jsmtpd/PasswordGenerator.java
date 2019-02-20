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
package org.jsmtpd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;

import org.jsmtpd.tools.Base64Helper;

public class PasswordGenerator {

    public static void main(String[] args) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String str = "";
            while (str != null) {
                System.out.println("> Enter the password to digest (exit to quit) ");
                str = in.readLine();
                if ("exit".equals(str))
                    return;
                else {
                    System.out.println(Base64Helper.encode(md5.digest(str.getBytes())));
                }

            }
        } catch (Exception e) {
        }

    }
}