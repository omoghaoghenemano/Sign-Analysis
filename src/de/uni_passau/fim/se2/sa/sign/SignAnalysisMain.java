package de.uni_passau.fim.se2.sa.sign;

import com.google.common.collect.SortedSetMultimap;
import java.util.SortedSet;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

/** Main entry point of the analysis. */
public class SignAnalysisMain implements Callable<Integer> {

  @Spec CommandSpec spec;

  private String className;
  private String methodName;

  public static void main(String[] pArgs) {
    int exitCode = new CommandLine(new SignAnalysisMain()).execute(pArgs);
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    final SignAnalysis analysis = new SignAnalysisImpl();
    final SortedSetMultimap<Integer, AnalysisResult> results =
            analysis.analyse(className, methodName);
    printAnalysisResults(results);
    return 0;
  }

  private void printAnalysisResults(
          final SortedSetMultimap<Integer, AnalysisResult> pAnalysisResults) {
    if (pAnalysisResults.isEmpty()) {
      System.out.println("No warnings or errors found");
      return;
    }

    for (final Integer lineNumber : pAnalysisResults.keySet()) {
      final SortedSet<AnalysisResult> analysisResults = pAnalysisResults.get(lineNumber);
      for (final AnalysisResult analysisResult : analysisResults) {
        System.out.printf("Line %d: %s%n", lineNumber, analysisResult.getValue());
      }
    }
  }

  // @formatter:off
  @Option(
          names = {"-c", "--class"},
          description = "The class to analyze.",
          required = true)
  // @formatter:on
  public void setClassName(String pClassName) {
    className = pClassName;
  }

  // @formatter:off
  @Option(
          names = {"-m", "--method"},
          description = "The name of the method to start the analysis with.",
          required = true)
  // @formatter:on
  public void setMethodName(String pMethodName) {
    methodName = pMethodName;
  }
}
