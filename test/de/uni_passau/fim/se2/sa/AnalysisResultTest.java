package de.uni_passau.fim.se2.sa;


import de.uni_passau.fim.se2.sa.sign.AnalysisResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnalysisResultTest {

    @Test
    public void testDivisionByZero() {
        assertEquals("ERROR: Division by Zero detected", AnalysisResult.DIVISION_BY_ZERO.getValue(), "The message for DIVISION_BY_ZERO should be 'ERROR: Division by Zero detected'.");
    }

    @Test
    public void testMaybeDivisionByZero() {
        assertEquals("WARNING: Division by Zero detected", AnalysisResult.MAYBE_DIVISION_BY_ZERO.getValue(), "The message for MAYBE_DIVISION_BY_ZERO should be 'WARNING: Division by Zero detected'.");
    }

    @Test
    public void testNegativeArrayIndex() {
        assertEquals("ERROR: Negative Array Index detected", AnalysisResult.NEGATIVE_ARRAY_INDEX.getValue(), "The message for NEGATIVE_ARRAY_INDEX should be 'ERROR: Negative Array Index detected'.");
    }

    @Test
    public void testMaybeNegativeArrayIndex() {
        assertEquals("WARNING: Negative Array Index detected", AnalysisResult.MAYBE_NEGATIVE_ARRAY_INDEX.getValue(), "The message for MAYBE_NEGATIVE_ARRAY_INDEX should be 'WARNING: Negative Array Index detected'.");
    }
}

