package adv.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

// see http://stackoverflow.com/questions/4773778/creating-zip-archive-in-java
public class ZipUtil {
    private static final Logger log = LoggerFactory.getLogger(ZipUtil.class);
    private static Charset CP866 = Charset.forName("CP866");

    public static void readZipStream(InputStream zipByteStream, ZipEntryHandler handler) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(zipByteStream);
        final ZipInputStream is = new ZipInputStream(bis, CP866);
        try {
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                long size = entry.getSize();
                String fileName = new File(entry.getName()).getName();
                log.debug("readZipStream(): fileName: '{}' size: {}, entry: '{}'", fileName, size, entry.getName());
                byte[] data = ZipUtil.extractEntry(entry, is);
                handler.handle(entry.getName(), fileName, data);
            }
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }

    }

    public static String removeNonWordChars(String value) {
        if (value == null || "".equals(value)) {
            return value;
        }
        return value.replaceAll("[^\\w\\-_\\.a-яA-Я\\(\\)\\[\\]\\s]", "_");
    }

    public static void writeZip(String archiveFileName, InputStream sourceBytes, OutputStream target) {
        String entryName = removeNonWordChars(archiveFileName);
        log.debug("creating archive entry: '{}' cleared to => '{}'", archiveFileName, entryName);
        try {
            ZipOutputStream out = null;
            try {
                out = new ZipOutputStream(target, CharsetUtil.CP866);
                out.putNextEntry(new ZipEntry(entryName));
                StreamUtils.copyStream(sourceBytes, out);
            } finally {
                if (out != null) {
                    try {
                        out.closeEntry();
                    } catch (IOException ignored) {
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public interface ZipEntryHandler {
        void handle(String fullName, String fileName, byte[] data);
    }

    public static byte[] extractEntry(final ZipEntry entry, InputStream zipStream) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(20000);
        IOUtils.copy(zipStream, os);
        return os.toByteArray();
    }
}
