package de.uni_passau.fim.se2.sa;

import com.google.common.collect.SortedSetMultimap;
import de.uni_passau.fim.se2.sa.sign.AnalysisResult;
import de.uni_passau.fim.se2.sa.sign.SignAnalysisImpl;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SignAnalysisImplTest {


    @Test
    public void testAddMethod() {
        // Create a list of Pair<String, String> for testing
        List<SignAnalysisImpl.Pair<String, String>> elements = List.of(
                new SignAnalysisImpl.Pair<>("key1", "Value 1"),
                new SignAnalysisImpl.Pair<>("key2", "Value 2"),
                new SignAnalysisImpl.Pair<>("key3", "Value 3")
        );

        // Call the add method and capture the result
        String result = SignAnalysisImpl.add(elements);

        // Define the expected result based on the input elements
        String expected = "No warnings or errors found\nValue 1\nValue 2\nValue 3";

        // Assert that the result matches the expected value
        assertEquals(expected, result);
    }

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
}