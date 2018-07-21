package com.searcher.searcher.Spider.Util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

import java.nio.file.Files;
public class Util {
    public static String getImage(String url) throws IOException {
        URL imgurl = new URL(url);
        InputStream in = imgurl.openStream();
        byte[] imgData = IOUtils.toByteArray(in);
        return Base64.getEncoder().encodeToString(imgData);
    }
}
