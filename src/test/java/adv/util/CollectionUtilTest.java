package adv.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

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

    @Test
    public void testAddSafe1() {
        HashSet<User> userSet = new HashSet<>();
        User sam = new User(1, "Sam");
        User john = new User(2, "John");
        assertTrue(CollectionUtil.addObjectWithIntIdToSet(userSet, sam, User::getId, false));
        assertTrue(CollectionUtil.addObjectWithIntIdToSet(userSet, john, User::getId, false));
        assertFalse(CollectionUtil.addObjectWithIntIdToSet(userSet, sam, User::getId, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSafe2() {
        HashSet<User> userSet = new HashSet<>();
        User sam = new User(1, "Sam");
        assertTrue(CollectionUtil.addObjectWithIntIdToSet(userSet, sam, User::getId, true));
        assertFalse(CollectionUtil.addObjectWithIntIdToSet(userSet, sam, User::getId, true));
    }

    static class User {
        Integer id;
        String name;

        public User(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return Objects.equals(id, user.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}