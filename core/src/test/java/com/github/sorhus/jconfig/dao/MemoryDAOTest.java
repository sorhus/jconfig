package com.github.sorhus.jconfig.dao;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class MemoryDAOTest {

    @Test
    public void testPutGetEvict() {
        MemoryDAO instance = new MemoryDAO(2);
        instance.put("key1", "val1");
        instance.put("key2", "val2");

        assertThat(instance.get("key1"), equalTo("val1"));
        assertThat(instance.get("key2"), equalTo("val2"));
        assertThat(instance.get("key3"), equalTo(null));

        instance.put("key3", "val3");
        assertThat(instance.get("key1"), equalTo(null));
        assertThat(instance.get("key2"), equalTo("val2"));
        assertThat(instance.get("key3"), equalTo("val3"));

        instance.put("key3", "val4");
        assertThat(instance.get("key2"), equalTo("val2"));
        assertThat(instance.get("key3"), equalTo("val4"));
    }
}
