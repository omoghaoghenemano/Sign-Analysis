package de.uni_passau.fim.se2.sa.sign;

/** Represents the potential results of the analysis. */
public enum AnalysisResult {
  DIVISION_BY_ZERO("ERROR: Division by Zero detected"),
  MAYBE_DIVISION_BY_ZERO("WARNING: Division by Zero detected"),
  NEGATIVE_ARRAY_INDEX("ERROR: Negative Array Index detected"),
  MAYBE_NEGATIVE_ARRAY_INDEX("WARNING: Negative Array Index detected");

  private final String value;

  AnalysisResult(final String pValue) {
    value = pValue;
  }

  /**
   * Retrieve the string message for this result.
   *
   * @return The string message for this result
   */
  public String getValue() {
    return value;
  }
}
