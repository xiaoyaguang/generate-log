package com.mokylin.sink.tools.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A tool we use to interact with IO streams.
 * @author yaguang.xiao
 *
 */
public class IOUtils {

    private static Logger logger = LoggerFactory.getLogger(IOUtils.class);

    // FEFF because this is the Unicode char represented by the UTF-8 byte order mark (EF BB BF).
    public static final String UTF8_BOM = "\uFEFF";

    /**
     * Read a file to a byte array.
     *
     * @param f
     * @return
     */
    public static byte[] readAndClose(File f) {
        try (FileInputStream fis = new FileInputStream(f)) {
            return readAndClose(fis);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read byte array from a InputStream.
     *
     * <p><b>This method will not close the InputStream.</b>
     *
     * @param is
     * @return
     */
    public static byte[] readAndClose(InputStream is) {
        try {
            return readBytes(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Just read byte array from a InputStream.
     * @param is
     * @return
     * @throws IOException
     */
    private static byte[] readBytes(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] data = new byte[is.available()];
        int count;
        while ((count = is.read(data)) != -1) {
            os.write(data, 0, count);
        }

        return os.toByteArray();
    }

    /**
     * Read the content in the InputStream and close it after reading.<p>
     * default:add "\n" in each line
     * @param in The InputStream
     * @param charset
     * @return the content in the InputStream.
     * @throws IOException
     */
    public static String readAndClose(InputStream in, String charset) throws IOException {
        return readAndClose(in, charset, true);
    }

    /**
     * Read the content in the InputStream and close it after reading.
     *
     * <p>The method will delete the BOM header when it is reading from the InputStream.
     *
     * @param in The InputStream
     * @param charset
     * @param isNeedLineBreak
     * @return
     * @throws IOException
     */
    public static String readAndClose(InputStream in, String charset, boolean isNeedLineBreak)
            throws
            IOException {
        try (InputStreamReader isr = new InputStreamReader(in, charset);
                BufferedReader br = new BufferedReader(isr)) {
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                line = removeUTF8BOM(line);
                builder.append(line);
                if (isNeedLineBreak) {
                    builder.append("\n");
                }
            }

            return builder.toString();
        } catch (IOException e) {
            throw e;
        } finally {
            closeInputStream(in);
        }
    }

    /**
     * 删除BOM字符
     * @param s
     * @return
     */
    private static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }

    /**
     * Close the InputStream
     * @param in the InputStream to close.
     * @throws IOException
     */
    static void closeInputStream(Closeable in) throws IOException {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ignore) {
                logger.error("Exception occurred when close a closeable thing!", ignore);
            }
        }
    }

    /**
     * Wirte the byte array content to a file, the file name and path is
     * specified by the name.
     *
     * @param name
     * @param content
     */
    public static void writeToFile(String name, byte[] content) {
        try (FileOutputStream fos = new FileOutputStream(new File(name))) {
            fos.write(content);
            fos.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
