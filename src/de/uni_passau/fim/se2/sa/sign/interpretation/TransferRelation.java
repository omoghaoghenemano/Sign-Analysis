package de.uni_passau.fim.se2.sa.sign.interpretation;

/**
 * Computes an abstract transfer for the data-flow analysis.
 *
 * <p>Such a transfer takes an operator and abstract values and computes the effect of the operator
 * on the given values.
 */
public interface TransferRelation {

  /** The supported operations by the analysis. */
  enum Operation {
    /** Addition of two integers. */
    ADD,
    /** Subtraction of two integers. */
    SUB,
    /** Multiplication of two integers. */
    MUL,
    /** Division of two integers. */
    DIV,
    /** Negation of one integer. */
    NEG,
  }

  /**
   * Evaluates a concrete value to its corresponding abstract value.
   *
   * @param pValue The concrete value
   * @return The corresponding abstract value
   */
  SignValue evaluate(final int pValue);

  /**
   * Evaluates a unary operation.
   *
   * <p>The result is the abstract element that is the result of the operation application on the
   * given abstract element.
   *
   * @param pOperation The operation to apply
   * @param pValue The abstract element to apply the operation on
   * @return The result of the operation application
   */
  SignValue evaluate(final Operation pOperation, final SignValue pValue);

  /**
   * Evaluates a binary operation.
   *
   * <p>The result is the abstract element that is the result of the operation application on the
   * given abstract elements.
   *
   * @param pOperation The operation to apply
   * @param pLHS The left-hand side abstract element to apply the operation on
   * @param pRHS The right-hand side abstract element to apply the operation on
   * @return The result of the operation application
   */
  SignValue evaluate(final Operation pOperation, final SignValue pLHS, final SignValue pRHS);
}
