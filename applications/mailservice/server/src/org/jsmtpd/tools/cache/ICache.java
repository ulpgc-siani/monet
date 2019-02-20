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
package org.jsmtpd.tools.cache;

/**
 * Cache things
 * @author jean-francois POUX
 *
 * @param <Key>
 * @param <Value>
 */
public interface ICache <Key,Value>{
    /**
     * get something from cache
     * @param k key
     * @return
     * @throws CacheFaultException when not found
     */
    public Value get (Key k);
    /**
     * Cache something
     * @param key
     * @param value
     */
    public void cache (Key key, Value value);
    /**
     * Clears the entire cache
     *
     */
    public void clear();
    /**
     * Expires a cached item
     * @param k
     */
    public void destroy (Key k);
}
