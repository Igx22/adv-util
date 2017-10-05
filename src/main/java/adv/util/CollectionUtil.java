package adv.util;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtil {
    public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> merge(Collection<T> c1, Collection<T> c2) {
        List list = new ArrayList(c1.size() + c2.size());
        list.addAll(c1);
        list.addAll(c2);
        return list;
    }

    public static <K, V> Map<K, V> sortMapByKey(Map<K, V> map, Comparator<? super K> keyComparator) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(keyComparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

}
