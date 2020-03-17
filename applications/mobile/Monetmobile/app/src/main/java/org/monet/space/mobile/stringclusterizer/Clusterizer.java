package org.monet.space.mobile.stringclusterizer;

import java.util.*;

public abstract class Clusterizer<T> {

    protected final StringExtractor<T> extractor;
    private final List<Cluster<T>> filteredLeafs = new ArrayList<>();

    public Clusterizer(){
        this.extractor = new StringExtractor<T>() {
            @Override
            public String extract(T object) {
                return object.toString();
            }
        };
    }

    public Clusterizer(StringExtractor<T> extractor){
        this.extractor = extractor;
    }

    @SafeVarargs
    public final ItemList<T> clusterize(T... elements) {
        return clusterize(Arrays.asList(elements));
    }

    private ItemList<T> removeExtraClusters(ItemList<T> clusterize) {
        for (Item<T> item : new ArrayList<>(clusterize.edit())) removeExtraClusters(item);
        return clusterize;
    }

    private void removeExtraClusters(Item<T> item) {
        if(item.items().size() == 1 && item.id().equals(item.items().get(0).id())) {
            int index = item.parent().items().edit().indexOf(item);
            item.parent().items().edit().remove(item);
            item.parent().items().edit().add(index, item.items().get(0));
        } else removeExtraClusters(item.items());
    }

    @SuppressWarnings("unchecked")
    public final ItemList<T> clusterize(List<T> elements) {
        if(elements.isEmpty()) return new ItemList<>();
        Cluster<T> cluster = new Cluster<>("", elements, extractor);
        process(cluster);
        return removeExtraClusters(cluster.items());
    }

    private void process(Cluster<T> cluster){
        List<Cluster<T>> leafClusters = filter(getLeafClusters(cluster));
        while(!leafClusters.isEmpty()) {
            process(leafClusters);
            leafClusters = filter(getLeafClusters(cluster));
        }
    }

    private List<Cluster<T>> filter(List<Cluster<T>> leafClusters) {
        List<Cluster<T>> result = new ArrayList<>();
        for (Cluster<T> leafCluster : leafClusters) {
            if(filteredLeafs.contains(leafCluster)) continue;
            result.add(leafCluster);
        }
        return result;
    }

    private void process(List<Cluster<T>> leafClusters) {
        for (Cluster<T> leafCluster : leafClusters){
            String id = leafCluster.id();
            clusterize(leafCluster);
            if (!id.equals(leafCluster.id()) || leafCluster.clusters().size() != 0) continue;
            filteredLeafs.add(leafCluster);
        }
    }

    private void clusterize(Cluster<T> cluster) {
        Map<String, List<T>> map = buildClusterizationMap(cluster);
//        if (thereIsOnlyOneCluster(map, cluster)) updateClusterId(cluster, map);
        createClusters(cluster, map);
    }

    protected abstract Map<String, List<T>> buildClusterizationMap(Cluster<T> cluster);

    private boolean thereIsOnlyOneCluster(Map<String, List<T>> map, Cluster cluster) {
        return map.size() == 1 && map.values().iterator().next().size() == cluster.elements().size();
    }

    private void updateClusterId(Cluster cluster, Map<String, List<T>> map) {
        cluster.id(map.keySet().iterator().next());
    }

    private void createClusters(Cluster<T> cluster, Map<String, List<T>> map) {
        for (String key : map.keySet()) {
            if (map.get(key).size() == 1 && key.equals(map.get(key).get(0))) continue;
            cluster.add(new Cluster<>(key, map.get(key), extractor));
        }
    }

    protected LinkedHashMap<String, List<T>> buildMap() {
        return new LinkedHashMap<String, List<T>>() {
            @Override
            public List<T> get(Object key) {
                if (!containsKey(key)) put((String) key, new ArrayList<T>());
                return super.get(key);
            }
        };
    }

    protected List<Cluster<T>> getLeafClusters(Cluster<T> root) {
        List<Cluster<T>> clusters = new ArrayList<>();
        for (Cluster<T> cluster : root.allClusters()) {
            if (cluster.clusters().size() > 0) continue;
            clusters.add(cluster);
        }
        return clusters;
    }

    protected String extract(T element) {
        return extractor.extract(element);
    }
}
