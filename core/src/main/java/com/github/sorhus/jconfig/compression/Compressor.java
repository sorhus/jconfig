package com.github.sorhus.jconfig.compression;

/**
 * @author: anton.sorhus@gmail.com
 */
public interface Compressor {

    public String decompress(String input);
    public String compress(String input);
}
