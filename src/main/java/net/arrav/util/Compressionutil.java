package net.arrav.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class Compressionutil {

    public static byte[] gunzip(byte[] bytes) {
        try {
            /* create the streams */
            InputStream is = new GZIPInputStream(new ByteArrayInputStream(bytes));
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                try {
                    /* copy data between the streams */
                    byte[] buf = new byte[4096];
                    int len = 0;
                    while ((len = is.read(buf, 0, buf.length)) != -1) {
                        os.write(buf, 0, len);
                    }
                } finally {
                    os.close();
                }

                /* return the uncompressed bytes */
                return os.toByteArray();
            } finally {
                is.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Determines if a byte array is compressed. The java.util.zip GZip
     * implementaiton does not expose the GZip header so it is difficult to determine
     * if a string is compressed.
     *
     * @param bytes an array of bytes
     * @return true if the array is compressed or false otherwise
     * @throws java.io.IOException if the byte array couldn't be read
     */
    public static boolean isGzip(byte[] bytes)
    {
        if ((bytes == null) || (bytes.length < 2))
        {
            return false;
        }
        else
        {
            return ((bytes[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (bytes[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8)));
        }
    }
}
