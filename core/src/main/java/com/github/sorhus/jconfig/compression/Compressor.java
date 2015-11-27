package com.github.sorhus.jconfig.compression;

/**
 * @author: anton.sorhus@gmail.com
 */
public interface Compressor {

    String decompress(String input);
    String compress(String input);
}
