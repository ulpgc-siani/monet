package org.monet.space.mobile.net;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class ProxyFactory {

    private static ProxyFactory instance;

    private Map<String, ProxyLayer> items;


    public synchronized static ProxyFactory getInstance() {
        if (instance == null) {
            instance = new ProxyFactory();
        }
        return instance;
    }

    private ProxyFactory() {
        this.items = new HashMap<String, ProxyLayer>();
    }

    public synchronized ProxyLayer getProxy(Context context, String accountName) throws Exception {

        ProxyLayer proxyLayer = this.items.get(accountName);
        if (proxyLayer != null) {
            return proxyLayer;
        }

        proxyLayer = new ProxyLayer(context, accountName);
        this.items.put(accountName, proxyLayer);
        return proxyLayer;

    }

}
