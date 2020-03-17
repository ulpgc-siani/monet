package org.monet.space.mobile.stringclusterizer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CharacterClusterizer<T> extends Clusterizer<T> {
    public CharacterClusterizer() {
    }

    public CharacterClusterizer(StringExtractor<T> extractor) {
        super(extractor);
    }

    protected Map<String, List<T>> buildClusterizationMap(Cluster<T> cluster) {
        Map<String, List<T>> map = this.buildMap();
        Iterator var3 = cluster.elements().iterator();

        while(var3.hasNext()) {
            Item<T> element = (Item)var3.next();
            if (this.extract(element.get()).length() > cluster.id().length() + 1) {
                ((List)map.get(this.extract(element.get()).substring(0, cluster.id().length() + 1))).add(element.get());
            }
        }

        return map;
    }
}
