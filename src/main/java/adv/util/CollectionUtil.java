package adv.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
}
