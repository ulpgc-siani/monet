package org.monet.space.analytics.model;

import org.sumus.query.Chart;

import java.util.WeakHashMap;

public class ChartStore {
    private final WeakHashMap<String, Chart> cache;

    private static ChartStore instance;

    public static ChartStore Instance() {
        if (instance == null)
            instance = new ChartStore();
        return instance;
    }

    public Chart get(String id) {
        return cache.get(id);
    }

    public void put(String id, Chart chart) {
        cache.put(id, chart);
    }

    public void remove(String id) {
        cache.remove(id);
    }

    private ChartStore() {
        cache = new WeakHashMap<String, Chart>(100);
    }
}
