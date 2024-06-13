package de.uni_passau.fim.se2.sa;

import com.google.common.collect.SortedSetMultimap;
import de.uni_passau.fim.se2.sa.sign.AnalysisResult;
import de.uni_passau.fim.se2.sa.sign.SignAnalysisImpl;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SignAnalysisImplTest {


   

    @Test
    public void testConcatenateWarningsAndErrors_NullList() {
        List<SignAnalysisImpl.Pair<String, String>> elements = null;
        String result = SignAnalysisImpl.add(elements);
        assertEquals("No warnings or errors found", result);
    }


    @Test
    public void testAnalyse() throws AnalyzerException, IOException {
        String className = "de.uni_passau.fim.se2.sa.sign.SignAnalysisImpl";
        String methodName = "isDivByZero";

        SignAnalysisImpl analysis = new SignAnalysisImpl();
        SortedSetMultimap<Integer, AnalysisResult> results = analysis.analyse(className, methodName);



        // Assert that no division by zero or negative array index issues are found
        assertFalse(results.containsKey(-1), "No issues should be reported for line -1");
        assertFalse(results.containsValue(AnalysisResult.DIVISION_BY_ZERO), "No division by zero issues should be found");
        assertFalse(results.containsValue(AnalysisResult.MAYBE_DIVISION_BY_ZERO), "No maybe division by zero issues should be found");
        assertFalse(results.containsValue(AnalysisResult.NEGATIVE_ARRAY_INDEX), "No negative array index issues should be found");
        assertFalse(results.containsValue(AnalysisResult.MAYBE_NEGATIVE_ARRAY_INDEX), "No maybe negative array index issues should be found");
    }
    @Test
    public void testConcatenateWarningsAndErrors_emptyList() {
        List<SignAnalysisImpl.Pair<String, String>> emptyList = new ArrayList<>();
        String result = SignAnalysisImpl.add(emptyList);
        assertEquals("No warnings or errors found", result);
    }

    @Test
    public void testConcatenateWarningsAndErrors_nullList() {
        String result = SignAnalysisImpl.add(null);
        assertEquals("No warnings or errors found", result);
    }

    @Test
    public void testConcatenateWarningsAndErrors_withWarningsAndErrors() {
        List<SignAnalysisImpl.Pair<String, String>> issues = new ArrayList<>();
        issues.add(new SignAnalysisImpl.Pair<>("Line 24", "ERROR: Division by Zero detected"));
        issues.add(new SignAnalysisImpl.Pair<>("Line 25", "WARNING: Division by Zero detected"));
        issues.add(new SignAnalysisImpl.Pair<>("Line 26", "WARNING: Division by Zero detected"));
        issues.add(new SignAnalysisImpl.Pair<>("Line 27", "WARNING: Division by Zero detected"));
        issues.add(new SignAnalysisImpl.Pair<>("Line 30", "ERROR: Negative Array Index detected"));
        issues.add(new SignAnalysisImpl.Pair<>("Line 31", "WARNING: Negative Array Index detected"));
        issues.add(new SignAnalysisImpl.Pair<>("Line 34", "WARNING: Division by Zero detected"));

        String expected = "Line 24: ERROR: Division by Zero detected\n" +
                "Line 25: WARNING: Division by Zero detected\n" +
                "Line 26: WARNING: Division by Zero detected\n" +
                "Line 27: WARNING: Division by Zero detected\n" +
                "Line 30: ERROR: Negative Array Index detected\n" +
                "Line 31: WARNING: Negative Array Index detected\n" +
                "Line 34: WARNING: Division by Zero detected";

        String result = SignAnalysisImpl.add(issues);
        assertEquals(expected, result);
    }

    @Test
    public void testConcatenateWarningsAndErrors_singleWarning() {
        List<SignAnalysisImpl.Pair<String, String>> issues = new ArrayList<>();
        issues.add(new SignAnalysisImpl.Pair<>("Line 10", "WARNING: Possible null pointer dereference"));

        String expected = "Line 10: WARNING: Possible null pointer dereference";

        String result = SignAnalysisImpl.add(issues);
        assertEquals(expected, result);
    }

    @Test
    public void testConcatenateWarningsAndErrors_singleError() {
        List<SignAnalysisImpl.Pair<String, String>> issues = new ArrayList<>();
        issues.add(new SignAnalysisImpl.Pair<>("Line 15", "ERROR: Array index out of bounds"));

        String expected = "Line 15: ERROR: Array index out of bounds";

        String result = SignAnalysisImpl.add(issues);
        assertEquals(expected, result);
    }

    @Test
    public void testConcatenateWarningsAndErrors_multipleErrorsSameLine() {
        List<SignAnalysisImpl.Pair<String, String>> issues = new ArrayList<>();
        issues.add(new SignAnalysisImpl.Pair<>("Line 42", "ERROR: Division by Zero detected"));
        issues.add(new SignAnalysisImpl.Pair<>("Line 42", "ERROR: Negative Array Index detected"));

        String expected = "Line 42: ERROR: Division by Zero detected\n" +
                "Line 42: ERROR: Negative Array Index detected";

        String result = SignAnalysisImpl.add(issues);
        assertEquals(expected, result);
    }
}