package com.alukyanau.nysestocks.infrastructure.cache;

import java.lang.ref.SoftReference;
import java.util.Set;

/**
 * Implement LRuCache algorithm
 * Store pairs key - value similar to {@link java.util.WeakHashMap} but with SoftReferences
 * Values can disappear after full GC
 * Size is limited by maxCacheSize in inner {@link ConcurrentLRuCache}.
 *
 * @param <K> Type of key
 * @param <V> Type of value
 */
public class RequestCache<K, V> {

    private final ConcurrentLRuCache<K, SoftReference<V>> lRuCache;

    public RequestCache(int maxCacheSize) {
        lRuCache = new ConcurrentLRuCache<>(maxCacheSize);
    }

    public int getMaxSize() {
        return lRuCache.getMaxSize();
    }

    public int size() {
        reduceEmptyValues();
        return lRuCache.size();
    }

    public boolean containsKey(K key) {
        reduceEmptyValues();
        return lRuCache.containsKey(key);
    }

    public V getBy(K key) {
        reduceEmptyValues();
        SoftReference<V> reference = lRuCache.get(key);
        return reference == null ? null : reference.get();
    }

    public void store(K key, V value) {
        reduceEmptyValues();
        SoftReference<V> reference = new SoftReference<>(value);
        lRuCache.put(key, reference);
    }

    private void reduceEmptyValues() {
        Set<K> keys = lRuCache.getAllKeys();
        for (K key : keys) {
            if (lRuCache.get(key).refersTo(null)) {
                lRuCache.remove(key);
            }
        }
    }

}
