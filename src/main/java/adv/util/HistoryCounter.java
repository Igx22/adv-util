package adv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class HistoryCounter {
    // maps (tag "transactioncode_ip:port") -> Map(timeSlotId -> counter)
    private Map<String, Map<Long, AtomicLong>> historyCount = new ConcurrentHashMap<>(1000);

    private Map<String, AtomicLong> simpleCount = new HashMap<>(1000);

    private final String name;
    private final Logger log;

    private long startTime;
    private long slotSizeSeconds;

    private long minSlot = -1;
    private long maxSlot = -1;
    public static final String COL_DELIMITER = ";";
    public static final String ROW_DELIMITER = "\r\n";
    private ZoneId zone;

    public HistoryCounter(String name) {
        this.name = name;
        log = LoggerFactory.getLogger(name);
    }

    public void initHistory(long startTime, long slotSizeSeconds, ZoneId zoneForFormatting) {
        this.startTime = startTime;
        this.slotSizeSeconds = slotSizeSeconds;
        this.zone = zoneForFormatting;
    }

    public void add(String element) {
        if (!simpleCount.containsKey(element)) {
            simpleCount.put(element, new AtomicLong(1));
        } else {
            simpleCount.get(element).incrementAndGet();
        }
    }

    public int count(String element) {
        if (!simpleCount.containsKey(element)) {
            return 0;
        }
        return simpleCount.get(element).intValue();
    }


    public void addWithHistory(String tag, long timestamp) {
        final long timeslot = toTimeSlot(timestamp);
        if (minSlot == -1) {
            minSlot = timeslot;
            maxSlot = timeslot;
        } else {
            if (minSlot > timeslot) {
                minSlot = timeslot;
            }
            if (maxSlot < timeslot) {
                maxSlot = timeslot;
            }
        }
        Map<Long, AtomicLong> tagStatMap = historyCount.get(tag);
        if (tagStatMap == null) {
            tagStatMap = new ConcurrentHashMap<>();
            historyCount.put(tag, tagStatMap);
        }
        AtomicLong timeSlotCounter = tagStatMap.get(timeslot);
        if (timeSlotCounter == null) {
            timeSlotCounter = new AtomicLong(1);
            tagStatMap.put(timeslot, timeSlotCounter);
        } else {
            timeSlotCounter.incrementAndGet();
        }
        add(tag);
    }

    public long count(String tag, long timeslot) {
        Map<Long, AtomicLong> tagStatMap = historyCount.get(tag);
        if (tagStatMap == null) {
            return 0;
        }
        AtomicLong timeSlotCounter = tagStatMap.get(timeslot);
        if (timeSlotCounter == null) {
            return 0;
        }
        return timeSlotCounter.get();
    }

    private long toTimeSlot(long timestamp) {
        return (timestamp - startTime) / (slotSizeSeconds * 1000);
    }

    private long toTimeStamp(long timeslot) {
        return startTime + (slotSizeSeconds * 1000 * timeslot);
    }

    public String printReport() {
        StringBuilder sb = new StringBuilder();
        if (name != null) {
            sb.append("Statistics for [" + name + "] counter\n");
        }
        int totalCount = 0;
        ArrayList<String> output = new ArrayList<>();
        for (Map.Entry<String, AtomicLong> entry : simpleCount.entrySet()) {
            int keyCount = entry.getValue().intValue();
            totalCount += keyCount;
            output.add(entry.getKey() + " -> " + keyCount + " \n");
        }
        Collections.sort(output);
        for (String msg : output) {
            sb.append(msg);
        }
        sb.append("-----------------------\n");
        return sb.toString();
    }

    public void buildHistoryReport(String reportFile) {
        Writer out = null;
        try {
            // history report
            final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss").withZone(zone);
            final ArrayList<String> tags = new ArrayList<>(historyCount.keySet());
            Collections.sort(tags);
            try {
                out = new BufferedWriter(new FileWriter(reportFile));
            } catch (IOException e) {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                reportFile = reportFile + formatter.format(ZonedDateTime.now()) + ".csv";
                out = new BufferedWriter(new FileWriter(reportFile));
            }
            out.write("slot time");
            out.write(COL_DELIMITER);
            for (String tag : tags) {
                out.write(tag);
                out.write(COL_DELIMITER);
            }
            out.write(ROW_DELIMITER);
            long slot = minSlot;
            while (slot != maxSlot) {
                String slotTime = fmt.format(Instant.ofEpochMilli(toTimeStamp(slot)));
                out.write(slotTime);
                out.write(COL_DELIMITER);
                for (String tag : tags) {
                    out.write(count(tag, slot) + "");
                    out.write(COL_DELIMITER);
                }
                out.write(ROW_DELIMITER);
                slot++;
            }
            // simple report
            out.write(ROW_DELIMITER);
            out.write("Stats: " + this.name);
            out.write(ROW_DELIMITER);
            out.write(ROW_DELIMITER);
            ArrayList<String> output = new ArrayList<>(simpleCount.entrySet().size());
            for (Map.Entry<String, AtomicLong> entry : simpleCount.entrySet()) {
                int keyCount = entry.getValue().intValue();
                output.add(entry.getKey() + " -> " + keyCount + " ");
            }
            Collections.sort(output);
            for (String msg : output) {
                out.write(msg);
                out.write(ROW_DELIMITER);
                log.info(msg);
            }
            out.close();
            log.info("Creating report: " + reportFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        HistoryCounter cnt = new HistoryCounter("counter1");
        cnt.initHistory(now, 10, ZoneId.of("UTC"));
        cnt.addWithHistory("event1", now + 10000);
        cnt.addWithHistory("event1", now + 10000 * 2);
        cnt.addWithHistory("event1", now + 10000 * 3);
        cnt.addWithHistory("event1", now + 10000 * 4);
        cnt.addWithHistory("event1", now + 10000 * 5);

        cnt.addWithHistory("event2", now + 10000);
        cnt.addWithHistory("event2", now + 10000);
        cnt.addWithHistory("event2", now + 10000);
        cnt.addWithHistory("event2", now + 10000);
        cnt.addWithHistory("event2", now + 10000 * 5);
        cnt.addWithHistory("event2", now + 10000 * 5);
        cnt.addWithHistory("event2", now + 10000 * 5);
        cnt.buildHistoryReport("result.txt");
    }
}
