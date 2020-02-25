package adv.util;

public class LoggerAbbreviator {

    public static String abbreviateThreadName(String threadName, int targetLength) {
        if (threadName == null) {
            return threadName;
        } else if (threadName.length() > targetLength) {
            int cutPosition = threadName.length() - targetLength;
            int dotPosition = threadName.lastIndexOf('.');
            //точка после места обрезки - обрежем и точку тоже
            if (dotPosition > 0 && dotPosition > cutPosition) {
                cutPosition = dotPosition + 1;
            }
            return threadName.substring(cutPosition);
        } else if (threadName.contains("nioEventLoopGroup")) {
            threadName = threadName.replace("nioEventLoopGroup", "nio");
        }
        return threadName;
    }

    public static String abbreviateFullQualifiedClassName(String fqClassName, int targetLength) {
        StringBuilder buf = new StringBuilder(targetLength);
        if (fqClassName == null) {
            throw new IllegalArgumentException("Class name may not be null");
        } else {
            int inLen = fqClassName.length();
            if (inLen < targetLength) {
                return fqClassName;
            } else {
                int[] dotIndexesArray = new int[16];
                int[] lengthArray = new int[17];
                int dotCount = computeDotIndexes(fqClassName, dotIndexesArray);
                if (dotCount == 0) {
                    return fqClassName;
                } else {
                    computeLengthArray(targetLength, fqClassName, dotIndexesArray, lengthArray, dotCount);

                    for(int i = 0; i <= dotCount; ++i) {
                        if (i == 0) {
                            buf.append(fqClassName.substring(0, lengthArray[i] - 1));
                        } else {
                            buf.append(fqClassName.substring(dotIndexesArray[i - 1], dotIndexesArray[i - 1] + lengthArray[i]));
                        }
                    }

                    return buf.toString();
                }
            }
        }
    }

    private static int computeDotIndexes(String className, int[] dotArray) {
        int dotCount = 0;
        int k = 0;

        while(true) {
            k = className.indexOf(46, k);
            if (k == -1 || dotCount >= 16) {
                return dotCount;
            }

            dotArray[dotCount] = k;
            ++dotCount;
            ++k;
        }
    }

    private static void computeLengthArray(int targetLength, String className, int[] dotArray, int[] lengthArray, int dotCount) {
        int toTrim = className.length() - targetLength;

        int i;
        for(i = 0; i < dotCount; ++i) {
            int previousDotPosition = -1;
            if (i > 0) {
                previousDotPosition = dotArray[i - 1];
            }

            int available = dotArray[i] - previousDotPosition - 1;
            int len = available < 1 ? available : 1;
            if (toTrim > 0) {
                len = available < 1 ? available : 1;
            } else {
                len = available;
            }

            toTrim -= available - len;
            lengthArray[i] = len + 1;
        }

        i = dotCount - 1;
        lengthArray[dotCount] = className.length() - dotArray[i];
    }

    private static void printArray(String msg, int[] ia) {
        System.out.print(msg);

        for(int i = 0; i < ia.length; ++i) {
            if (i == 0) {
                System.out.print(ia[i]);
            } else {
                System.out.print(", " + ia[i]);
            }
        }

        System.out.println();
    }

    private static void test(String fqn, int targetLength) {
        String shortName = abbreviateFullQualifiedClassName(LoggerAbbreviator.class.getName(), targetLength);
        System.out.printf("abbr(%s,%d)=%s \n", fqn, targetLength, shortName);
    }

    public static void main(String[] args) {
        test(LoggerAbbreviator.class.getName(), 10);
        test(LoggerAbbreviator.class.getName(), 20);
        test(LoggerAbbreviator.class.getName(), 30);
        test(LoggerAbbreviator.class.getName(), 40);
    }
}
