package adv.util;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * File utils.
 */
public class FileUtil {
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
    public static final String USER_DIR = new File(System.getProperty("user.dir")).getAbsolutePath();

    public static String getUserDirectory() {
        return USER_DIR;
    }

    public static String createLocalPath(String... pathItem) {
        final StringBuilder sb = new StringBuilder();
        sb.append(getUserDirectory());
        for (String pathStr : pathItem) {
            if (StringUtil.isNotEmpty(pathStr)) {
                sb.append(File.separator);
                sb.append(pathStr);
            }
        }
        return sb.toString();
    }

    public static String calculateMd5(String filePath) throws IOException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            // This should never happen
            throw new RuntimeException("Standard MessageDigest algorithm is absent", ex);
        }

        try (ReadableByteChannel chan = Files.newByteChannel(Paths.get(filePath), StandardOpenOption.READ)) {
            ByteBuffer buf = ByteBuffer.allocate(1024);
            while (chan.read(buf) >= 0) {
                buf.flip();
                md.update(buf);
                buf.clear();
            }
        }

        return BitUtil.toBase16(md.digest());
    }

    private static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    public static void removeFiles(File directory, String filePrefix, long cutOff) {
        log.debug("removing files in dir: {} matching: {} older than: {}", directory.getAbsolutePath(), filePrefix, cutOff);
        if (!(directory.exists() && directory.isDirectory())) {
            log.debug("no files in directory: " + directory.getAbsolutePath());
            return;
        }
        File[] filesToDelete = directory.listFiles(new OldFileFilter(cutOff, filePrefix));
        log.debug("found {} files, removing..", filesToDelete.length);
        for (File f : filesToDelete) {
            try {
                safeRemoveFile(f, 3, 300, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.error("failed to delete file: {} modification date: {}", f.getAbsolutePath(), f.lastModified(), e);
            }
        }
    }

    public static void safeRemoveFile(@Nullable File f, int retryCount, long delay, TimeUnit timeUnit) {
        if (f == null) {
            return;
        }
        boolean success = false;
        for (int retries = 0; !success && retries < retryCount; retries++) {
            if (retries > 0) {
                try {
                    Thread.sleep(timeUnit.toMillis(delay));
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
            try {
                success = f.delete();
            } catch (Exception e) {
                log.error("failed to delete file: {} ", e);
            }
            if (success) {
                log.debug("safeRemoveFile(): sucessfully deleted file: {} modification date: {}", f.getAbsolutePath(), DateUtil.formatShort(f.lastModified()));
            } else {
                log.error("safeRemoveFile(): failed to delete file: {} modification date: {}", f.getAbsolutePath(), DateUtil.formatShort(f.lastModified()));
            }
        }
        if (!success) {
            throw new IllegalStateException(
                    String.format("failed to safeRemoveFile: %s , tried %d times.",
                            f.getAbsolutePath(), retryCount));
        }
    }

    public static void safeRename(File src, File dst, int retryCount, long delay, TimeUnit timeUnit) {
        try {
            delay = timeUnit.toMillis(delay);
            int count = 0;
            boolean success = false;
            while (count++ < retryCount) {
                try {
                    success = src.renameTo(dst);
                } catch (Exception e) {
                    log.warn("safeRename():failed to rename: {}", e.getMessage());
                }
                if (success) {
                    break;
                } else {
                    log.warn("safeRename(): failed to safeRename: {} to {} , iteration #{}",
                            src.getAbsolutePath(), dst.getAbsolutePath(), delay);
                }
                Thread.sleep(delay);
            }
            if (!success) {
                throw new IllegalStateException(
                        String.format("failed to safeRename: %s to %s , tried %d times.",
                                src.getAbsolutePath(), dst.getAbsolutePath(), retryCount));
            } else {
                log.debug("safeRename(): successfully moved {} to {}", src.getAbsolutePath(), dst.getAbsolutePath());
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public static File mkDirOrFail(String directoryName) throws IOException {
        File file = new File(directoryName);
        return mkDirOrFail(file);
    }

    public static boolean isFile(File f) {
        return f != null && f.isFile() && f.exists();
    }

    public static boolean isDir(File dir) {
        return dir != null && dir.isDirectory() && dir.exists();
    }

    public static File mkDirOrFail(File file) throws IOException {
        if (!file.exists()) {
            boolean success = file.mkdirs();
            if (!success) {
                throw new IOException("Failed to create directory:" + file.getAbsolutePath());
            }
        }
        if (!file.isDirectory()) {
            throw new IOException("Invalid directory: " + file.getAbsolutePath());
        }
        return file;
    }


    public static byte[] readFileFast(String file) throws IOException {
        return Files.readAllBytes(Paths.get(file));
    }

    public static byte[] readFile(File file) throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength) {
                throw new IOException("File size >= 2 GB");
            }
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        }
    }

    public static String readFileAsString(File f) {
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(new FileInputStream(f), CharsetUtil.UTF8);
            return StreamUtils.readString(is);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static void writeFile(File file, byte[] data) throws IOException {
        Path path = file.toPath();
        Files.write(path, data);
    }

    public static void writeFile(File file, String str) throws IOException {
        final File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IllegalStateException("Failed to create a directory: " + parentDir.getAbsolutePath());
            }
        }
        if (!parentDir.isDirectory()) {
            throw new IllegalStateException("not a directory: " + parentDir.getAbsolutePath());
        }
        Path path = file.toPath();
        Files.write(path, StringUtil.toUtfBytes(str));
    }

    public static byte[] read(InputStream is) throws IOException {
        return IOUtils.toByteArray(is);
    }

    private static class OldFileFilter implements FileFilter {
        private final long cutOff;
        private final String prefix;

        private OldFileFilter(long cutOff, String prefix) {
            this.cutOff = cutOff;
            this.prefix = prefix;
        }

        @Override
        public boolean accept(File f) {
            // находим файл если он
            // 1) файл
            // 2) совпадает по имени
            // 3) старше чем cutOff момент времени
            return f.isFile()
                    && (prefix == null || f.getName().startsWith(prefix))
                    && cutOff > f.lastModified();
        }
    }

    // FileSystemUtils.copyRecursively(new File(repoExample), importRepoPath.toFile());
}
