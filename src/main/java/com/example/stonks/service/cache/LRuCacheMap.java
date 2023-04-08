package com.example.stonks.service.cache;

import java.util.LinkedHashMap;
import java.util.Map;

class LRuCacheMap<K, V> extends LinkedHashMap<K, V> {

    private final int maxSize;

    public LRuCacheMap(int maxCacheSize) {
        super(maxCacheSize, 0.75f, true);
        this.maxSize = maxCacheSize;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() >= maxSize;
    }
}
