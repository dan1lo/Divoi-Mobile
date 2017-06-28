package com.example.dennis.divoi.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by dennis on 10/04/17.
 */

public class FileUtil {

    public static String bytesToString(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bytes;
        while((bytes = inputStream.read(buffer)) != -1){
            byteArrayOutputStream.write(buffer, 0, bytes);
        }
        return new String(byteArrayOutputStream.toByteArray(), "ISO-8859-1");
    }
}
