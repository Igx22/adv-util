package adv.util;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static <K extends Comparable<? super K>, V> Map<K, V> sortMapByKey(Map<K, V> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public static <T> Stream<T> cartesian(BinaryOperator<T> aggregator,
                                           Supplier<Stream<T>>... streams) {
        return Arrays.stream(streams)
                .reduce((s1, s2) ->
                        () -> s1.get().flatMap(t1 -> s2.get().map(t2 -> aggregator.apply(t1, t2))))
                .orElse(Stream::empty).get();
    }
}
