package adv.util;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Утилита которая
 * 1) сохраняет и загружает атомарно файл с диска
 * 2) ведет архив старых версий файла (файлы заданной давности удаляются)
 * 3) прямой доступ к файлу заменяется на synchronized + callbacks
 * <p>
 * singleton!!!, только один инстанс на файл.
 * <p>
 * todo можно лочить файл средствами ос если нужно
 * <p>
 * атомарность реализована след образом
 * --------------
 * 1) пишем в tmp файл, потом "атомарно" переименовываем в основной
 * 2) основной файл не удаляем а переименовываем в архивный
 * 3) все файлы старше х дней - удаляем
 * <p>
 * все попытки rename/move производятся много раз с *большими* таймаутами
 */
public class DiskSerializer {
    private static final Logger log = LoggerFactory.getLogger(DiskSerializer.class);
    // сколько мы храним бэкапы
    private final long keepOldFilePeriod;
    // todo это ужасный костыль, но т.к. в production с файлами творится неизвестно что
    // "повторы" гарантируют что с высокой вероятностью нам удастся сделать с файлом то что нужно в конечном итоге
    private static final int FILE_OP_RETRY_COUNT = 15;
    private static final int FILE_OP_RETRY_DELAY = 2000;
    private static final int FILE_BUFFER_SIZE = 50000;
    protected final File storageDir;
    protected final File storageFile;


    public DiskSerializer(String storageDirPath, String fileName) {
        this(storageDirPath, fileName, 0, TimeUnit.MILLISECONDS);
    }

    public DiskSerializer(String storageDirPath, String fileName, long keepOldFilePeriod, TimeUnit timeUnit) {
        try {
            this.keepOldFilePeriod = timeUnit.toMillis(keepOldFilePeriod);
            this.storageDir = FileUtil.mkDirOrFail(storageDirPath);
            this.storageFile = new File(storageDir, fileName);
            log.debug("using directory: {}", storageDir.getAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public synchronized void write(WriterCallback writerCallback) {
        File tempFile = new File(storageDir, storageFile.getName() + "." + System.currentTimeMillis());
        doSave(writerCallback, tempFile);

        if (storageFile.isFile() && storageFile.exists()) {
            File archiveFile = new File(storageDir, storageFile.getName() + "." + DateUtil.formatShortNoSpaces(System.currentTimeMillis()));
            FileUtil.safeRename(storageFile, archiveFile, FILE_OP_RETRY_COUNT, FILE_OP_RETRY_DELAY, TimeUnit.MILLISECONDS);
        }
        FileUtil.safeRename(tempFile, storageFile, FILE_OP_RETRY_COUNT, FILE_OP_RETRY_DELAY, TimeUnit.MILLISECONDS);
        // удаляем архивы, которые старше заданного периода
        FileUtil.removeFiles(storageDir, storageFile.getName() + ".", System.currentTimeMillis() - keepOldFilePeriod);
    }

    public synchronized void read(ReaderCallback reader) {
        log.info("reading file: {}", storageFile.getAbsolutePath());
        StopWatch clock = new StopWatch();
        InputStream is = null;
        try {
            clock.start();
            if (!storageFile.exists()) {
                log.info("missing file {}", storageFile);
            } else {
                is = new BufferedInputStream(new FileInputStream(storageFile), FILE_BUFFER_SIZE);
                reader.read(is);
                log.info("reading {} Kib from file: {} time: {}s",
                        String.format("%3.1f", storageFile.length() / 1024.0), storageFile.getAbsolutePath(),
                        String.format("%3.3f", clock.getTime() / 1000.0));
            }
        } catch (Exception e) {
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

    public void write(byte[] data) {
        write(os -> os.write(data));
    }

    public byte[] read() {
        AtomicReference<byte[]> result = new AtomicReference<>(null);
        read(is -> {
            result.set(StreamUtils.read(is));
        });
        return result.get();
    }

    public File getStorageFile() {
        return storageFile;
    }

    // сохраняем данные
    private void doSave(WriterCallback writerCallback, File f) {
        StopWatch clock = new StopWatch();
        OutputStream os = null;
        try {
            clock.start();
            os = new BufferedOutputStream(new FileOutputStream(f), FILE_BUFFER_SIZE);
            writerCallback.write(os);
            log.info("writing {} Kib to file: {} time: {}s",
                    String.format("%3.1f", f.length() / 1024.0), f.getAbsolutePath(),
                    String.format("%3.3f", clock.getTime() / 1000.0));
        } catch (Exception e) {
            log.error("Failed to save file: {}", f, e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public interface WriterCallback {
        void write(OutputStream os) throws Exception;
    }

    public interface ReaderCallback {
        void read(InputStream is) throws Exception;
    }
}
