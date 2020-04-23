package adv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtil {
    private static final Logger log = LoggerFactory.getLogger(CollectionUtil.class);

    public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }

    public static <T> List<T> setToList(Set<T> set) {
        List<T> list = new ArrayList<>(set.size());
        list.addAll(set);
        return list;
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

    public static int[] toIntArray(List<Integer> list){
        int[] ret = new int[list.size()];
        for(int i = 0;i < ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }

    /**
     * Добавить объект obj типа T в множество set или развалиться с понятным сообщением об ошибке
     * <p>
     * ограничения:
     * T имеет equals & hashcode в которых сравнивает только по Integer ключу
     *
     * @param set        коллекция объектов с Integer ключами
     * @param obj        объект для добавления
     * @param idFunction ф-я для получения ключа
     * @param raiseException кидать ли исключение в случае неуспеха
     * @param <T>
     * @return true если удалось добавить объект в коллекцию
     */
    public static <T> boolean addObjectWithIntIdToSet(Set<T> set, T obj, Function<T, Integer> idFunction, boolean raiseException) {
        boolean success = set.add(obj);
        if (!success) {
            Integer newObjId = idFunction.apply(obj);
            Optional<T> oldObjectOpt = set.stream().filter(t -> newObjId.equals(idFunction.apply(t))).findAny();
            String setAsString = set.stream().map(t -> Integer.toString(idFunction.apply(t))).collect(Collectors.joining(","));
            String errMsg = String.format("addSafe(): Found duplcate object with id: %s in set: %s; old Object: %s new Object: %s",
                    newObjId, setAsString, oldObjectOpt, obj);
            log.error(errMsg);
            if (raiseException) {
                throw new IllegalStateException(errMsg);
            }
        }
        return success;
    }
}
