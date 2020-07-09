package org.monet.space.mobile.stringclusterizer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RegexClusterizer<T> extends Clusterizer<T> {
    private String pattern;

    public RegexClusterizer() {
    }

    public RegexClusterizer(StringExtractor<T> extractor) {
        super(extractor);
    }

    public RegexClusterizer<T> split(String pattern) {
        this.pattern = pattern;
        return this;
    }

    protected Map<String, List<T>> buildClusterizationMap(Cluster<T> cluster) {
        Map<String, List<T>> map = this.buildMap();
        Iterator var3 = cluster.elements().iterator();

        while(var3.hasNext()) {
            Item<T> element = (Item)var3.next();
            String suffix = this.extract(element.get()).substring(cluster.id().length());
            String[] split = suffix.split(this.pattern);
            if (split.length != 1) {
                ((List)map.get(this.key(cluster, split))).add(element.get());
            }
        }

        return map;
    }

    private String key(Cluster<T> cluster, String[] split) {
        String key =  (cluster.id().isEmpty() ? "" : cluster.id() + this.pattern) + (split[0].isEmpty() ? split[1] : split[0]);
        return key;
    }
}
