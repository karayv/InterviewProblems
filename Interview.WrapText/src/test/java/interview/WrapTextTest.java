package interview;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class WrapTextTest {
    static final String nl = System.lineSeparator();

    @Test
    void nullOrEmpty() {
        assertNull(WrapText.wrap(null));
        assertEquals("", WrapText.wrap(""));
        assertEquals("    ", WrapText.wrap(" "));
    }

    @Test
    void singleLineIndent() {
        assertEquals("    Single line", WrapText.wrap("Single line"));
        assertEquals("    Single line", WrapText.wrap(" Single line"));
        assertEquals("    Single line", WrapText.wrap("  Single line"));
        assertEquals("    Single line", WrapText.wrap("   Single line"));
        assertEquals("    Single line", WrapText.wrap("    Single line"));
        assertEquals("    Single line", WrapText.wrap("     Single line")); // 5 spaces shrink to 4
        
        // tab converts to 4 spaces
        assertEquals("    Single line", WrapText.wrap("\tSingle line"));
        assertEquals("    Single line", WrapText.wrap("\t Single line"));
        assertEquals("    Single line", WrapText.wrap(" \t Single line"));
    }

    @Test
    void noLineLonger80Chars() {
        String str70 = "This is a test line with 70 characters. \tWe need some more chars here.";
        assertEquals(70, str70.length()); // double check

        // extra spaces removed, no line split
        assertEquals("    " + str70 + "123456", WrapText.wrap("              " + str70 + "123456"));

        // 4 spaces added to existing 77 characters. New line added
        assertEquals("    " + str70 + nl + "    234567", WrapText.wrap(str70 + " 234567"));
        assertEquals("    " + str70 + nl + "    234567 890", WrapText.wrap(str70 + " 234567 890"));
        assertEquals("    " + str70 + " 23456" + nl + "    ", WrapText.wrap(str70 + " 23456 "));

    }

    @Test
    void multipleLinesInput() {
        // lines processed separately
        assertEquals("    Line1" + nl + "    Line2" + nl + "    Line3",
                WrapText.wrap("Line1" + nl + "Line2" + nl + "Line3"));

        // new lines added
        String str10 = "123456789 ";
        String str70 = repeat(str10, 7);
        String str69 = repeat(str10, 6) + "123456789";
        assertEquals("    " + str69 + nl + "    abcdefg" + nl + "    " + str69 + nl + "    hijklmn" + nl,
                WrapText.wrap(str70 + "abcdefg" + nl + str70 + "hijklmn" + nl));

        // split into 3 lines
        assertEquals("    " + str69 + nl + "    " + str69 + nl + "    " + repeat(str10, 2),
                WrapText.wrap(repeat(str10, 16)));
    }

    @Test
    void longerStrings() {
        String str10NoSp = "1234567890";

        // long string with no spaces should not be split
        assertEquals("    " + repeat(str10NoSp, 16), WrapText.wrap(repeat(str10NoSp, 16)));
    }

    private String repeat(String str, int count) {
        return Stream.generate(() -> str).limit(count).collect(Collectors.joining());
    }

}
