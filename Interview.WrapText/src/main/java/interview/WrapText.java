package interview;

/**
 * Implement a function that takes an arbitrary natural language text and wraps
 * it, such that:
 * 
 * <ol>
 * <li>Every line is indented with four spaces</li>
 * <li>No line is longer than 80 characters in length</li>
 * <li>Words are not cut in the middle</li>
 * </ol>
 */

public final class WrapText {
    private static final char[] indent = "    ".toCharArray();
    private static final int maxLength = 80 - indent.length;

    private static final char[] ls = System.lineSeparator().toCharArray();

    public static String wrap(final String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder res = new StringBuilder();
        int lineStart = 0;
        
        while (lineStart < text.length()) {
            // searching for the first non-space character
            while (lineStart < text.length() 
                    && (text.charAt(lineStart) == ' ' || text.charAt(lineStart) == '\t')) {
                lineStart++;
            }
            
            // make sure LS is supported
            // TODO do it on application startup
            checkSystemLineSeparator(ls);

            int lineEnd = lineStart, lsCount = 0, lastSpaceInd = -1;
            
            while (lineEnd < text.length() //
                    && (lineEnd <= lineStart + maxLength //
                            || lastSpaceInd == -1) // check for very long word
                    && lsCount < ls.length) {
                
                if (text.charAt(lineEnd) == ' ' || text.charAt(lineEnd) == '\t') {
                    lastSpaceInd = lineEnd;
                }
                // we assume System#lineSeparator has all unique symbols
                lsCount = text.charAt(lineEnd) == ls[lsCount] ? lsCount + 1 : 0;
                
                lineEnd++;
            }

            // make sure we do not cut words in the middle
            if (lastSpaceInd != -1 && lineEnd > lineStart + maxLength) {
                lineEnd = lastSpaceInd;
            }
            
            // add text with indentation
            res.append(indent).append(text.substring(lineStart, lineEnd));

            // append line separator if not last line
            if (lineEnd < text.length() && lsCount < ls.length) {
                res.append(ls);
            }

            lineStart = lineEnd;
        }

        return res.toString();
    }

    private static void checkSystemLineSeparator(char[] nl) {
        // most systems will have line separator of 1 or 2 chars
        if (nl.length > 2 || (nl.length == 2 && nl[0] == nl[1])) {
            throw new IllegalStateException("Unsupported system line separator.");
        }
    }

}
