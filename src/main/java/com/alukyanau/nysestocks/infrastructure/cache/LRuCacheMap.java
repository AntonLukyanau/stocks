package com.alukyanau.nysestocks.infrastructure.cache;

import java.util.LinkedHashMap;
import java.util.Map;

class LRuCacheMap<K, V> extends LinkedHashMap<K, V> {

    private final int maxSize;

    LRuCacheMap(int maxCacheSize) {
        super(maxCacheSize, 0.75f, true);
        this.maxSize = maxCacheSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
