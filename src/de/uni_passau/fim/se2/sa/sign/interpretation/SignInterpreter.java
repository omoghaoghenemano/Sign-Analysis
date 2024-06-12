package de.uni_passau.fim.se2.sa.sign.interpretation;

import java.util.List;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Interpreter;

public class SignInterpreter extends Interpreter<SignValue> implements Opcodes {

  public SignInterpreter() {
    this(ASM9);
  }

  /**
   * Constructs a new {@link Interpreter}.
   *
   * @param pAPI The ASM API version supported by this interpreter. Must be one of {@link #ASM4},
   *     {@link #ASM5}, {@link #ASM6}, {@link #ASM7}, {@link #ASM8}, or {@link #ASM9}
   */
  protected SignInterpreter(final int pAPI) {
    super(pAPI);
    if (getClass() != SignInterpreter.class) {
      throw new IllegalStateException();
    }
  }

  /** {@inheritDoc} */
  @Override
  public SignValue newValue(final Type pType) {
    if (pType == null) {
      return SignValue.UNINITIALIZED_VALUE;
    }
    if (pType == Type.VOID_TYPE) {
      return null;
    }
    return SignValue.TOP;
  }

  /** {@inheritDoc} */
  @Override
  public SignValue newOperation(final AbstractInsnNode pInstruction) throws AnalyzerException {
    switch (pInstruction.getOpcode()) {
      case ICONST_M1:
        return SignValue.MINUS;
      case ICONST_0:
        return SignValue.ZERO;
      case ICONST_1:
      case ICONST_2:
      case ICONST_3:
      case ICONST_4:
      case ICONST_5:
        return SignValue.PLUS;
      case BIPUSH:
      case SIPUSH:
        return SignValue.TOP;
      case LDC:
        Object cst = ((org.objectweb.asm.tree.LdcInsnNode) pInstruction).cst;
        if (cst instanceof Integer) {
          int val = (Integer) cst;
          if (val == 0) {
            return SignValue.ZERO;
          } else if (val > 0) {
            return SignValue.PLUS;
          } else {
            return SignValue.MINUS;
          }
        }
        throw new AnalyzerException(pInstruction, "Unsupported LDC constant: " + cst);
      default:
        throw new AnalyzerException(pInstruction, "Unsupported instruction: " + pInstruction.getOpcode());
    }
  }

  /** {@inheritDoc} */
  @Override
  public SignValue copyOperation(final AbstractInsnNode pInstruction, final SignValue pValue) {
    return pValue;
  }

  /** {@inheritDoc} */
  @Override
  public SignValue unaryOperation(final AbstractInsnNode pInstruction, final SignValue pValue)
          throws AnalyzerException {
    switch (pInstruction.getOpcode()) {
      case INEG:
        if (pValue == SignValue.PLUS) {
          return SignValue.MINUS;
        } else if (pValue == SignValue.MINUS) {
          return SignValue.PLUS;
        } else {
          return pValue; // For ZERO, ZERO_MINUS, ZERO_PLUS, PLUS_MINUS, TOP
        }
      case IINC:
        return SignValue.TOP;
      default:
        throw new AnalyzerException(pInstruction, "Unsupported instruction: " + pInstruction.getOpcode());
    }
  }

  /** {@inheritDoc} */
  @Override
  public SignValue binaryOperation(
          final AbstractInsnNode pInstruction, final SignValue pValue1, final SignValue pValue2) {
    switch (pInstruction.getOpcode()) {
      case IADD:
      case ISUB:
      case IMUL:
      case IDIV:
      case IREM:
        return SignValue.TOP; // Simplification for now
      default:
        throw new UnsupportedOperationException("Unsupported instruction: " + pInstruction.getOpcode());
    }
  }

  /** {@inheritDoc} */
  @Override
  public SignValue ternaryOperation(
          final AbstractInsnNode pInstruction,
          final SignValue pValue1,
          final SignValue pValue2,
          final SignValue pValue3) {
    return null; // Nothing to do.
  }

  /** {@inheritDoc} */
  @Override
  public SignValue naryOperation(
          final AbstractInsnNode pInstruction, final List<? extends SignValue> pValues) {
    return SignValue.TOP; // Simplification for varargs
  }

  /** {@inheritDoc} */
  @Override
  public void returnOperation(
          final AbstractInsnNode pInstruction, final SignValue pValue, final SignValue pExpected) {
    // Nothing to do.
  }

  /** {@inheritDoc} */
  @Override
  public SignValue merge(final SignValue pValue1, final SignValue pValue2) {
    return pValue1.join(pValue2);
  }
}
