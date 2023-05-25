package de.uni_passau.fim.se2.sa.sign;

import com.google.common.collect.SortedSetMultimap;
import java.io.IOException;
import org.objectweb.asm.tree.analysis.AnalyzerException;

/** An interface as the basis for the sign analysis. */
public interface SignAnalysis {

  /**
   * Executes the sign analysis.
   *
   * <p>The execution starts with a specific method, whose name and descriptor are given by {@code
   * pMethodName} from a class {@code pClassName}. The method's name and descriptor are expected to
   * be given in the following format: {@code <name>:<descriptor>}, e.g., {@code foo:(I)I}. The
   * class name can be given either as {@code org.example.Foo} or {@code org/example/Foo}.
   *
   * <p>The implementation shall return a {@link SortedSetMultimap} that saves all found violations
   * from {@link AnalysisResult} for a line number in the source code.
   *
   * @param pClassName The class name to analyse
   * @param pMethodName The method to start the analysis with
   * @return A sorted multimap of line numbers and the respective analysis results for these lines
   * @throws AnalyzerException In case of an error during the analysis
   * @throws IOException In case of an I/O error
   */
  SortedSetMultimap<Integer, AnalysisResult> analyse(
      final String pClassName, final String pMethodName) throws AnalyzerException, IOException;
}
