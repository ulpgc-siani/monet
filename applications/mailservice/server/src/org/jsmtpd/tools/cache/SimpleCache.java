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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple RAM Cache.
 * @author jean-francois POUX
 *
 * @param <Key>
 * @param <Value>
 */
public class SimpleCache<Key,Value> implements ICache<Key,Value> {
    private Map<Key,Value> cachedValues = Collections.synchronizedMap(new HashMap<Key,Value>());
    private List<Key> keys = Collections.synchronizedList(new ArrayList<Key>());
    private int cacheSize = 200;
    
    public SimpleCache(int cacheSize) {
        this.cacheSize=cacheSize;
    }
    
    public synchronized  Value get(Key k) {
        if (!keys.contains(k))
            return null;
        keys.remove(k);
        keys.add(0,k); // put key in front (latest use)
        return cachedValues.get(k);
    }
    
    public synchronized  void cache(Key key, Value value) {
        if (keys.size()>cacheSize) {
            int removeSize = Math.round(cacheSize / 10);
            for (int c=0;c<removeSize;c++) {
                Key cKey = keys.get(keys.size()-1);
                keys.remove(cKey);
                cachedValues.remove(cKey);
            }
        }
        keys.add(0,key);
        cachedValues.put(key,value);
    }
    
    public synchronized  void clear() {
        keys.clear();
        cachedValues.clear();
        
    }
    
    public synchronized  void destroy(Key k) {
        keys.remove(k);
        cachedValues.remove(k);
    }

    
    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }
   
}
