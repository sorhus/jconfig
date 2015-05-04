package com.github.sorhus.jconfig.compression;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GZipCompressorTest {

    @Test
    public void testCompression() {
        GZipCompressor instance = new GZipCompressor();
        String input = "repetetive strings are not working which is a bug";
        String compressed = instance.compress(input);
        String result = instance.decompress(compressed);
        assertThat(result, equalTo(input));
    }
}
