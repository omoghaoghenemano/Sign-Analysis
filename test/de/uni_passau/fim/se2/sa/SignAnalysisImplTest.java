package de.uni_passau.fim.se2.sa;

import com.google.common.collect.SortedSetMultimap;
import de.uni_passau.fim.se2.sa.sign.AnalysisResult;
import de.uni_passau.fim.se2.sa.sign.SignAnalysisImpl;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SignAnalysisImplTest {



    @Test
    public void testAnalyse() throws AnalyzerException, IOException {
        String className = "de.uni_passau.fim.se2.sa.sign.SignAnalysisImpl";
        String methodName = "add";

        SignAnalysisImpl analysis = new SignAnalysisImpl();
        SortedSetMultimap<Integer, AnalysisResult> results = analysis.analyse(className, methodName);

        assertEquals( 3, analysis.add() );

        // Assert that no division by zero or negative array index issues are found
        assertFalse(results.containsKey(-1), "No issues should be reported for line -1");
        assertFalse(results.containsValue(AnalysisResult.DIVISION_BY_ZERO), "No division by zero issues should be found");
        assertFalse(results.containsValue(AnalysisResult.MAYBE_DIVISION_BY_ZERO), "No maybe division by zero issues should be found");
        assertFalse(results.containsValue(AnalysisResult.NEGATIVE_ARRAY_INDEX), "No negative array index issues should be found");
        assertFalse(results.containsValue(AnalysisResult.MAYBE_NEGATIVE_ARRAY_INDEX), "No maybe negative array index issues should be found");
    }
}