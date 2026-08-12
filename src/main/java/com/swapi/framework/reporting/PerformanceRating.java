package com.swapi.framework.reporting;

/**
 * Classifies an HTTP response time into a human-readable performance tier.
 *
 * <table>
 *   <tr><th>Threshold</th><th>Rating</th></tr>
 *   <tr><td>&lt; 200 ms</td>         <td>Excelente</td></tr>
 *   <tr><td>200 – 1 000 ms</td>      <td>Bueno</td></tr>
 *   <tr><td>1 001 – 2 000 ms</td>    <td>Aceptable</td></tr>
 *   <tr><td>&gt; 2 000 ms</td>       <td>Lento</td></tr>
 * </table>
 *
 * Used both by {@link HtmlReporter} (to populate the Performance column)
 * and by test classes (to assert the response time is not rated Lento).
 */
public final class PerformanceRating {

    public static final long EXCELLENT_MS  =   200;
    public static final long GOOD_MS       = 1_000;
    public static final long ACCEPTABLE_MS = 2_000;

    private PerformanceRating() {}

    /** Returns the rating label for the given elapsed time in milliseconds. */
    public static String classify(long ms) {
        if (ms < EXCELLENT_MS)   return "Excellent";
        if (ms <= GOOD_MS)       return "Good";
        if (ms <= ACCEPTABLE_MS) return "Acceptable";
        return "Slow";
    }

    /** Returns the badge background colour for the given rating label. */
    public static String badgeColor(String rating) {
        return switch (rating) {
            case "Excellent"   -> "#69dd6ee0";
            case "Good"       -> "#338feb";
            case "Acceptable" -> "#eecf7a";
            default           -> "#e25754"; // Slow
        };
    }
}
