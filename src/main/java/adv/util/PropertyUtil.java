package adv.util;

import java.util.*;

public class PropertyUtil {

    @SuppressWarnings("unchecked")
    public static List<String> getKeysSorted(Properties p) {
        List<String> sortedKeys = new ArrayList<>();
        sortedKeys.addAll((Set) p.keySet());
        Collections.sort(sortedKeys);
        return sortedKeys;
    }
}
