package org.monet.space.mobile.stringclusterizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Clusterizer<T> {
    protected final StringExtractor<T> extractor;
    private final List<Cluster<T>> filteredLeafs = new ArrayList();

    public Clusterizer() {
        this.extractor = new StringExtractor<T>() {
            public String extract(T object) {
                return object.toString();
            }
        };
    }

    public Clusterizer(StringExtractor<T> extractor) {
        this.extractor = extractor;
    }

    @SafeVarargs
    public final ItemList<T> clusterize(T... elements) {
        return this.clusterize(Arrays.asList(elements));
    }

    public final ItemList<T> clusterize(List<T> elements) {
        if (elements.isEmpty()) {
            return new ItemList();
        } else {
            Cluster<T> cluster = new Cluster("", elements, this.extractor);
            this.process(cluster);
            return cluster.items();
        }
    }

    private void process(Cluster<T> cluster) {
        for(List leafClusters = this.filter(this.getLeafClusters(cluster)); !leafClusters.isEmpty(); leafClusters = this.filter(this.getLeafClusters(cluster))) {
            this.process(leafClusters);
        }

    }

    private List<Cluster<T>> filter(List<Cluster<T>> leafClusters) {
        List<Cluster<T>> result = new ArrayList();
        Iterator var3 = leafClusters.iterator();

        while(var3.hasNext()) {
            Cluster<T> leafCluster = (Cluster)var3.next();
            if (!this.filteredLeafs.contains(leafCluster)) {
                result.add(leafCluster);
            }
        }

        return result;
    }

    private void process(List<Cluster<T>> leafClusters) {
        Iterator var2 = leafClusters.iterator();

        while(var2.hasNext()) {
            Cluster<T> leafCluster = (Cluster)var2.next();
            String id = leafCluster.id();
            this.clusterize(leafCluster);
            if (id.equals(leafCluster.id()) && leafCluster.clusters().size() == 0) {
                this.filteredLeafs.add(leafCluster);
            }
        }

    }

    private void clusterize(Cluster<T> cluster) {
        Map<String, List<T>> map = this.buildClusterizationMap(cluster);
        if (this.thereIsOnlyOneCluster(map, cluster)) {
            this.updateClusterId(cluster, map);
        } else {
            this.createClusters(cluster, map);
        }

    }

    protected abstract Map<String, List<T>> buildClusterizationMap(Cluster<T> var1);

    private boolean thereIsOnlyOneCluster(Map<String, List<T>> map, Cluster cluster) {
        return map.size() == 1 && ((List)map.values().iterator().next()).size() == cluster.elements().size();
    }

    private void updateClusterId(Cluster cluster, Map<String, List<T>> map) {
        cluster.id((String)map.keySet().iterator().next());
    }

    private void createClusters(Cluster<T> cluster, Map<String, List<T>> map) {
        Iterator var3 = map.keySet().iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            if (((List)map.get(key)).size() != 1) {
                cluster.add(new Cluster(key, (List)map.get(key), this.extractor));
            }
        }

    }

    protected LinkedHashMap<String, List<T>> buildMap() {
        return new LinkedHashMap<String, List<T>>() {
            public List<T> get(Object key) {
                if (!this.containsKey(key)) {
                    this.put((String)key, new ArrayList());
                }

                return (List)super.get(key);
            }
        };
    }

    protected List<Cluster<T>> getLeafClusters(Cluster<T> root) {
        List<Cluster<T>> clusters = new ArrayList();
        Iterator var3 = root.allClusters().iterator();

        while(var3.hasNext()) {
            Cluster<T> cluster = (Cluster)var3.next();
            if (cluster.clusters().size() <= 0) {
                clusters.add(cluster);
            }
        }

        return clusters;
    }

    protected String extract(T element) {
        return this.extractor.extract(element);
    }
}
