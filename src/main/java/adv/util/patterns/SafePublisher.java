package adv.util.patterns;

public class SafePublisher<T> {
    private final T value;
    private SafePublisher(T value) { this.value = value; }
    public static <S> S publish(S value) { return new SafePublisher<S>(value).value; }
}
