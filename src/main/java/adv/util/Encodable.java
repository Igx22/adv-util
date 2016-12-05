package adv.util;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Encodable {

    String token();

    static <E extends Enum<E> & Encodable> E forToken(Class<E> cls, String tok) {
        final String t = tok.trim().toUpperCase();
        return Stream.of(cls.getEnumConstants())
                .filter(e -> e.token().equals(t))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown token '" +
                        tok + "' for enum " + cls.getName()));
    }

    static <E extends Encodable> Map<String, E> tokenMap(Class<E> cls) {
        return Stream.of(cls.getEnumConstants()).collect(Collectors.toMap(Encodable::token, Function.identity()));
    }

    /*
    это не работает - похоже на баг компилятора
    https://bugs.openjdk.java.net/browse/JDK-8141508

    public static <E extends Enum<E> & Encodable> Map<String, E> tokenMap(Class<E> cls) {
        return Stream.of(cls.getEnumConstants()).collect(Collectors.toMap(Encodable::token, Function.identity()));
    }
    */
}
