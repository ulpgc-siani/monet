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
package org.jsmtpd.core.common.acl;

import java.net.Inet4Address;
import java.util.BitSet;

/**
 * Holds an ip address + a network mask<br>
 * + Utilities to check if an adress is equals to this address or if contained in this mask
 * @author Jean-Francois POUX
 */
public class ExtendedInet4Address {

    private Inet4Address ipAd = null;
    private Inet4Address netmask = null;

    public ExtendedInet4Address(Inet4Address ipAd, Inet4Address netmask) {
        this.ipAd = ipAd;
        this.netmask = netmask;
    }

    public ExtendedInet4Address(Inet4Address ipAd) {
        this.ipAd = ipAd;
    }

    public boolean isEqualorInMask(Inet4Address ipIn) {

        if (ipIn == null)
            return false;

        if (ipIn.equals(ipAd))
            return true;

        if (netmask != null) {

            BitSet mask = makeBitSet(netmask.getAddress());
            BitSet adrs = makeBitSet(ipAd.getAddress());
            BitSet against = makeBitSet(ipIn.getAddress());

            for (int i = 0; i < 32; i++) {
                if (mask.get(i)) {
                    if ((adrs.get(i) != against.get(i)))
                        return false;
                }
            }
            return true;

        }

        return false;
    }

    public static BitSet makeBitSetST(String host) {
        String[] res;
        host = host.replace(".", "Z");
        res = host.split("Z");
        System.out.println("host=" + host + ", len=" + res.length);
        byte[] dt = new byte[res.length];

        for (int i = 0; i < res.length; i++) {
            short tmp = Short.parseShort(res[i]);
            dt[i] = (byte) tmp;
        }

        return makeBitSet(dt);
    }

    public static BitSet makeBitSet(byte[] in) {
        BitSet bb = new BitSet();
        for (int j = 0; j < in.length; j++) {
            byte bt = in[j];
            for (int i = 7; i >= 0; i--)
                if (((1 << i) & bt) != 0)
                    bb.set(-i + 7 + j * 8);
                else
                    bb.clear(-i + 7 + j * 8);
        }
        return bb;
    }

    static void debug(BitSet b) {

        String out = new String();
        int i = 0;
        for (int k = 0; k < b.size(); k++) {
            out += (b.get(k) ? "1" : "0");
            i++;
            if (i == 8) {
                out += " ";
                i = 0;
            }
        }
        System.out.println("bit set contains " + out + ", size=" + b.size());
    }

}