package com.alukyanau.nysestocks.infrastructure.cache;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

class ConcurrentLRuCache<K, V> {

    private final ConcurrentHashMap<K, V> storage;
    private final Queue<K> queue;
    private final int maxCacheSize;

    ConcurrentLRuCache(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        storage = new ConcurrentHashMap<>(maxCacheSize);
        queue = new ConcurrentLinkedQueue<>();
    }

    public int getMaxSize() {
        return maxCacheSize;
    }

    public int size() {
        return storage.size();
    }

    public boolean containsKey(K key) {
        return storage.containsKey(key);
    }

    public V get(K key) {
        V value = storage.get(key);
        if (value == null) {
            return null;
        }
        queue.remove(key);
        queue.add(key);
        return value;
    }

    public void put(K key, V value) {
        if (storage.size() >= maxCacheSize) {
            K lruKey = queue.poll();
            if (lruKey != null) {
                storage.remove(lruKey);
            }
        }
        storage.put(key, value);
        queue.remove(key);
        queue.add(key);
    }

    public void remove(K key) {
        storage.remove(key);
        queue.remove(key);
    }

    public Set<K> getAllKeys() {
        return storage.keySet();
    }
}

