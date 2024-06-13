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
  PLUS("{+}"), // 4 == PLUS
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

    int thisOrdinal = this.ordinal();
    int otherOrdinal = pOther.ordinal();

    // The join operation corresponds to the bitwise OR operation of their ordinal values
    return SignValue.values()[thisOrdinal | otherOrdinal];
  }

  public boolean isLessOrEqual(final SignValue pOther) {
    Preconditions.checkState(
            this != UNINITIALIZED_VALUE && pOther != UNINITIALIZED_VALUE,
            "Dummy shall not be used as a value.");
    return (this.ordinal() & pOther.ordinal()) == this.ordinal();
  }

  public static boolean isZero(final SignValue pValue) {
    Preconditions.checkState(pValue != UNINITIALIZED_VALUE, "Dummy shall not be used as a value.");
    return pValue == ZERO;
  }

  public static boolean isMaybeZero(final SignValue pValue) {
    Preconditions.checkState(pValue != UNINITIALIZED_VALUE, "Dummy shall not be used as a value.");
    return pValue == ZERO || pValue == ZERO_MINUS || pValue == ZERO_PLUS || pValue == TOP;
  }

  public static boolean isNegative(final SignValue pValue) {
    Preconditions.checkState(pValue != UNINITIALIZED_VALUE, "Dummy shall not be used as a value.");
    return pValue == MINUS;
  }

  public static boolean isMaybeNegative(final SignValue pValue) {
    Preconditions.checkState(pValue != UNINITIALIZED_VALUE, "Dummy shall not be used as a value.");
    return pValue == MINUS || pValue == ZERO_MINUS || pValue == PLUS_MINUS || pValue == TOP;
  }
}
