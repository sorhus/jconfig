package com.github.sorhus.jconfig.compression;

/**
 * @author: anton.sorhus@gmail.com
 */
public class FakeCompressor implements Compressor {

    @Override
    public String decompress(String compressed) {
        return compressed == null ? null : compressed.substring(1,compressed.length()-1);
    }

    @Override
    public String compress(String uncompressed) {
        return uncompressed == null ? null : String.format("!%s!", uncompressed);
    }
}
