package adv.util;

import java.util.Random;

public class RandomUtil {

    public static int getInRange(Random rnd, int low, int high) {
        Check.isTrue(high > low);
        return low + rnd.nextInt(high - low);
    }
}

