package adv.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtilTest {
    @Test
    public void testCartesian() throws Exception {
        Stream<String> result = CollectionUtil.cartesian(
                (a, b) -> a + b,
                () -> Stream.of("A", "B"),
                () -> Stream.of("K", "L"),
                () -> Stream.of("X", "Y")
        );
        List<String> expected = Stream.of("AKX",
                "AKY",
                "ALX",
                "ALY",
                "BKX",
                "BKY",
                "BLX",
                "BLY").collect(Collectors.toList());
        List<String> actual = result.collect(Collectors.toList());
        Assert.assertEquals(expected, actual);
    }

}