package adv.util;

import ua_parser.*;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class CachingParser extends Parser {
    private Logger log = LoggerFactory.getLogger(CachingParser.class);

    private static Cache<Integer, Client> cache = CacheBuilder.newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .initialCapacity(13_500)
            .maximumSize(150_000)
            .build();


    public CachingParser() throws IOException {
        super();
    }

    public CachingParser(InputStream regexYaml) {
        super(regexYaml);
    }

    @Override
    public Client parse(String agentString) {
        try {
            return cache.get(agentString.hashCode(), () -> super.parse(agentString));
        } catch (ExecutionException e) {
            log.error("CachingParser.parse(): {}", e);
            return null;
        }
    }

    @Override
    public Device parseDevice(String agentString) {
        return parse(agentString).device;
    }

    @Override
    public OS parseOS(String agentString) {
        return parse(agentString).os;
    }

    @Override
    public UserAgent parseUserAgent(String agentString) {
        return parse(agentString).userAgent;
    }
}
