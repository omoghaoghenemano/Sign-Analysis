package de.uni_passau.fim.se2.sa.sign.interpretation;

import com.google.common.base.Preconditions;
import org.objectweb.asm.tree.analysis.Value;

/** An enum representing the possible abstract values of the sign analysis. */
public enum SignValue implements Value {
  // Important! This implementation is kind of fragile. Don't change the order of enum values!
  // Otherwise, code will break 🤡:)
  BOTTOM("⊥"), // 0
  MINUS("{–}"), // 1
  ZERO("{0}"), // 2
  ZERO_MINUS("{0,–}"), // 3 == ZERO | MINUS
  PLUS("{+}"), // 4 == 4
  PLUS_MINUS("{+,–}"), // 5 == PLUS | MINUS
  ZERO_PLUS("{0,+}"), // 6 == ZERO | PLUS
  TOP("⊤"), // 7 == MINUS | ZERO | PLUS
  UNINITIALIZED_VALUE("∅"); // 8

  private final String repr;

  SignValue(final String pRepr) {
    repr = pRepr;
  }

  @Override
  public int getSize() {
    return 1;
  }

  @Override
  public String toString() {
    return repr;
  }

  public SignValue join(final SignValue pOther) {
    Preconditions.checkState(
        this != UNINITIALIZED_VALUE && pOther != UNINITIALIZED_VALUE,
        "Dummy shall not be used as a value.");

    // TODO Implement me
    throw new UnsupportedOperationException("Implement me");
  }

  public boolean isLessOrEqual(final SignValue pOther) {
    Preconditions.checkState(
        this != UNINITIALIZED_VALUE && pOther != UNINITIALIZED_VALUE,
        "Dummy shall not be used as a value.");

    // TODO Implement me
    throw new UnsupportedOperationException("Implement me");
  }

  public static boolean isZero(final SignValue pValue) {
    // TODO Implement me
    throw new UnsupportedOperationException("Implement me");
  }

  public static boolean isMaybeZero(final SignValue pValue) {
    // TODO Implement me
    throw new UnsupportedOperationException("Implement me");
  }

  public static boolean isNegative(final SignValue pValue) {
    // TODO Implement me
    throw new UnsupportedOperationException("Implement me");
  }

  public static boolean isMaybeNegative(final SignValue pValue) {
    // TODO Implement me
    throw new UnsupportedOperationException("Implement me");
  }
}
