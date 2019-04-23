package adv.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    public static int getInRange(Random rnd, int low, int high) {
        Check.isTrue(high > low);
        return low + rnd.nextInt(high - low);
    }

    public static boolean getTrueWithProbability(Random rnd, double probability) {
        if (probability >= 0.99999d) {
            return true;
        }
        if (probability <= 0.00001d) {
            return false;
        }
        return rnd.nextDouble() <= probability;
    }

    public static boolean getTrueWithProbability(double probability) {
        return getTrueWithProbability(ThreadLocalRandom.current(), probability);
    }
}

