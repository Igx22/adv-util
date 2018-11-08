package adv.util;

import org.junit.Test;

import static adv.util.StringUtil.fixBrokenUTF16Surrogates;
import static adv.util.StringUtil.getFirstNWords;
import static org.junit.Assert.assertEquals;

public class StringUtilTest {

    @Test
    public void getFirstCommaSeparatedValue() throws Exception {
        assertEquals(null, StringUtil.getFirstCommaSeparatedValue(null));
        assertEquals("", StringUtil.getFirstCommaSeparatedValue(""));
        assertEquals("192.168.1.1", StringUtil.getFirstCommaSeparatedValue("192.168.1.1"));
        assertEquals("192.168.1.1", StringUtil.getFirstCommaSeparatedValue("192.168.1.1, 192.168.1.2"));
        assertEquals("192.168.1.1", StringUtil.getFirstCommaSeparatedValue("192.168.1.1, 192.168.1.2, 192.168.1.3"));
    }

    @Test
    public void testGetFirstNWords() {
        String sample = "Long sentence with spaces, and punctuation too. And supercalifragilisticexpialidosus words. ";
        String sample2 = "No carriage returns, tho -- since it would seem weird to count the words in a new line as part of the previous paragraph's length.";
        assertEquals("", getFirstNWords(sample, 0));
        assertEquals("", getFirstNWords(sample, 1));
        assertEquals("Long", getFirstNWords(sample, 2));
        assertEquals("Long", getFirstNWords(sample, 3));
        assertEquals("Long", getFirstNWords(sample, 4));
        assertEquals("Long", getFirstNWords(sample, 5));
        assertEquals("Long sentence", getFirstNWords(sample, 6));
        for (int i = 0; i < sample.length(); i++) {
            System.out.println(i + " " + getFirstNWords(sample, i));
        }
        for (int i = 0; i < sample2.length(); i++) {
            System.out.println(i + " " + getFirstNWords(sample2, i));
        }
    }

    @Test
    public void testFixSurrogates() {
        assertEquals("\uD83D\uDCB0", fixBrokenUTF16Surrogates("\uD83D\uDCB0", true));
        assertEquals("", fixBrokenUTF16Surrogates("\uD83D", true));
        assertEquals("", fixBrokenUTF16Surrogates("\uDCB0", true));
        assertEquals("\uD83D\uDCB0", fixBrokenUTF16Surrogates("\uD83D\uDCB0\uDCB0", true));
        assertEquals("\uD83D\uDCB0", fixBrokenUTF16Surrogates("\uDCB0\uD83D\uDCB0", true));
        assertEquals("\uD83D\uDCB0", fixBrokenUTF16Surrogates("\uD83D\uDCB0\uD83D", true));
        assertEquals("\uD83D\uDCB0", fixBrokenUTF16Surrogates("\uD83D\uD83D\uDCB0", true));

        assertEquals("\uD83D\uDCB0", fixBrokenUTF16Surrogates("\uD83D\uDCB0", false));
        assertEquals("�", fixBrokenUTF16Surrogates("\uD83D", false));
        assertEquals("�", fixBrokenUTF16Surrogates("\uDCB0", false));
        assertEquals("\uD83D\uDCB0�", fixBrokenUTF16Surrogates("\uD83D\uDCB0\uDCB0", false));
        assertEquals("�\uD83D\uDCB0", fixBrokenUTF16Surrogates("\uDCB0\uD83D\uDCB0", false));
        assertEquals("\uD83D\uDCB0�", fixBrokenUTF16Surrogates("\uD83D\uDCB0\uD83D", false));
        assertEquals("�\uD83D\uDCB0", fixBrokenUTF16Surrogates("\uD83D\uD83D\uDCB0", false));
    }
}