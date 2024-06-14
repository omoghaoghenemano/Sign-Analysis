package de.uni_passau.fim.se2.sa;

import com.google.common.collect.SortedSetMultimap;
import de.uni_passau.fim.se2.sa.sign.SignAnalysisMain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.SortedSet;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

public class SignAnalysisMainTest {

    private ByteArrayOutputStream outputStream;
    private SignAnalysisMain signAnalysisMain;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        signAnalysisMain = new SignAnalysisMain();
    }

    @Test
    public void testCall_withValidArguments() throws Exception {
        // Given

        signAnalysisMain.setClassName("de.uni_passau.fim.se2.sa.sign.SignAnalysisImpl");
        signAnalysisMain.setMethodName("isNegativeArrayIndex");
        

        // When
        Callable<Integer> callable = signAnalysisMain;
        Integer result = callable.call();

        // Then
        assertEquals(0, result);
        String output = outputStream.toString();
        assertTrue(output.contains("No warnings or errors found")); // Assuming no results expected in this basic test
    }


 }
