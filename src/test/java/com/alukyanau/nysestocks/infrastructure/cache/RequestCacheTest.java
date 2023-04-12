package com.alukyanau.nysestocks.infrastructure.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestCacheTest {

    @Test
    void testGetMaxSize() {
        // given
        int expectedMaxSize = 1000;
        RequestCache<String, String> cache = new RequestCache<>(expectedMaxSize);
        // when
        int maxSize = cache.getMaxSize();
        // then
        assertEquals(expectedMaxSize, maxSize);
    }

    @Test
    void testSizeAndStore() {
        // given
        RequestCache<String, String> cache = new RequestCache<>(2);
        // when
        int startSize = cache.size();
        cache.store("key1", "value1");
        int sizeAfterOneAdding = cache.size();
        cache.store("key2", "value2");
        int sizeAfterTwoAdding = cache.size();
        cache.store("key3", "value3");
        int sizeAfterThreeAdding = cache.size();
        // then
        assertEquals(0, startSize);
        assertEquals(1, sizeAfterOneAdding);
        assertEquals(2, sizeAfterTwoAdding);
        assertEquals(2, sizeAfterThreeAdding);
    }

    @Test
    void testContainsKeyAndStore() {
        // given
        RequestCache<String, String> cache = new RequestCache<>(2);
        String notStoredKey = "not stored key";
        // when
        cache.store("key1", "value1");
        cache.store("key2", "value2");
        boolean containsKey1 = cache.containsKey("key1");
        boolean containsKey2 = cache.containsKey("key2");
        boolean containsNotStoredKey = cache.containsKey(notStoredKey);
        // then
        assertTrue(containsKey1);
        assertTrue(containsKey2);
        assertFalse(containsNotStoredKey);
    }

    @Test
    void testGetByAndStore() {
        // given
        RequestCache<String, String> cache = new RequestCache<>(2);
        // when
        cache.store("key1", "value1");
        cache.store("key2", "value2");
        String valueForKey1 = cache.getBy("key1");
        String valueForKey2 = cache.getBy("key2");
        String valueForNotStoredKey = cache.getBy("not stored key");
        // then
        assertEquals("value1", valueForKey1);
        assertEquals("value2", valueForKey2);
        assertNull(valueForNotStoredKey);
    }
}