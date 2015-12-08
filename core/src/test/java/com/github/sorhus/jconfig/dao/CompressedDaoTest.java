package com.github.sorhus.jconfig.dao;

import com.github.sorhus.jconfig.compression.Compressor;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class CompressedDaoTest {

    @Test
    public void testGet() {
        DAO dao = mock(DAO.class);
        when(dao.get("id")).thenReturn("compressed");
        Compressor compressor = mock(Compressor.class);
        when(compressor.decompress("compressed")).thenReturn("json");

        CompressedDAO instance = new CompressedDAO(dao, compressor);
        String expected = "json";
        String result = instance.get("id");
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testSet() {
        DAO dao = mock(DAO.class);
        Compressor compressor = mock(Compressor.class);
        when(compressor.compress("json")).thenReturn("compressed");

        CompressedDAO instance = new CompressedDAO(dao, compressor);
        instance.put("id", "json");

        verify(compressor, times(1)).compress("json");
        verify(dao, times(1)).put("id", "compressed");
    }
}
