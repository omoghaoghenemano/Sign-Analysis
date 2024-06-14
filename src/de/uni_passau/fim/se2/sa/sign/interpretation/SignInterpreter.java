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
  public SignValue newOperation(final AbstractInsnNode pInstruction) {
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
        // Handle unsupported constants gracefully
        return SignValue.TOP; // or return SignValue.UNINITIALIZED_VALUE or another appropriate default
      default:
        // Handle unsupported instructions gracefully
        return SignValue.TOP; // or return SignValue.UNINITIALIZED_VALUE or another appropriate default
    }
  }

  /** {@inheritDoc} */
  @Override
  public SignValue copyOperation(final AbstractInsnNode pInstruction, final SignValue pValue) {
    return pValue;
  }

  /** {@inheritDoc} */
  @Override
  public SignValue unaryOperation(final AbstractInsnNode pInstruction, final SignValue pValue) {
    switch (pInstruction.getOpcode()) {
      case INEG:
        if (pValue == SignValue.PLUS) {
          return SignValue.MINUS;
        } else if (pValue == SignValue.MINUS) {
          return SignValue.PLUS;
        } else {
          return pValue; // Handle other cases gracefully
        }
      case IINC:
        return SignValue.TOP; // Handle IINC operation gracefully
      default:
        // Handle other unsupported instructions gracefully
        return pValue; // Return original value or another appropriate default
    }
  }


  /** {@inheritDoc} */
  @Override
  public SignValue binaryOperation(final AbstractInsnNode pInstruction, final SignValue pLHS, final SignValue pRHS) {
    switch (pInstruction.getOpcode()) {
      case IADD:
        if(pLHS.equals(SignValue.BOTTOM) || pRHS.equals(SignValue.BOTTOM))
          return  SignValue.BOTTOM;
        if(pLHS == SignValue.UNINITIALIZED_VALUE && pRHS == SignValue.ZERO) return  SignValue.TOP;
        if(pLHS.equals(SignValue.ZERO_PLUS) && pRHS.equals(SignValue.ZERO_PLUS))
          return  SignValue.ZERO_PLUS;
        if (pLHS == SignValue.ZERO && pRHS == SignValue.UNINITIALIZED_VALUE) return  SignValue.TOP;
        if (pLHS == SignValue.ZERO) return pRHS;
        if (pRHS == SignValue.ZERO) return pLHS;
        if (pLHS == SignValue.PLUS && pRHS == SignValue.PLUS || pLHS == SignValue.PLUS && pRHS == SignValue.ZERO_PLUS || pRHS == SignValue.PLUS && pLHS == SignValue.ZERO_PLUS)return SignValue.PLUS;
        if (pLHS == SignValue.MINUS && pRHS == SignValue.MINUS) return SignValue.MINUS;
        if ((pLHS == SignValue.MINUS && pRHS == SignValue.PLUS_MINUS) || (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.MINUS)) return SignValue.TOP;
        if (pLHS == SignValue.MINUS && pRHS == SignValue.ZERO_MINUS) return SignValue.MINUS;
        if (pRHS == SignValue.MINUS && pLHS == SignValue.ZERO_MINUS) return SignValue.MINUS;
        if(pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.ZERO_MINUS) return SignValue.ZERO_MINUS;
        if(pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.PLUS_MINUS) return SignValue.TOP;
        if (pLHS ==  SignValue.MINUS && pRHS == SignValue.PLUS || pLHS ==  SignValue.MINUS && pRHS == SignValue.ZERO_PLUS || pLHS ==  SignValue.MINUS && pRHS == SignValue.TOP
                || pLHS ==  SignValue.MINUS && pRHS == SignValue.UNINITIALIZED_VALUE || pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.PLUS || pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.PLUS_MINUS || pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.PLUS_MINUS  || pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.ZERO_PLUS || pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.PLUS  ) return SignValue.TOP;

        return  SignValue.TOP;

        case ISUB:
        // Handle subtraction operation
        if (pLHS.equals(SignValue.BOTTOM) || pRHS.equals(SignValue.BOTTOM)) {
          return SignValue.BOTTOM;
        }
        if(pLHS == SignValue.UNINITIALIZED_VALUE && pRHS == SignValue.ZERO) return  SignValue.TOP;

        if(pLHS == SignValue.PLUS_MINUS){
          if(pRHS == SignValue.ZERO){
            return  SignValue.PLUS_MINUS;
          }

        }
        if (pLHS == SignValue.ZERO) {
          if (pRHS == SignValue.ZERO_PLUS) {
            return SignValue.ZERO_MINUS;
          }
          if(pRHS == SignValue.ZERO_MINUS){
            return SignValue.ZERO_PLUS;
          }
          if(pRHS == SignValue.PLUS_MINUS){
            return  SignValue.PLUS_MINUS;
          }


          if(pRHS == SignValue.UNINITIALIZED_VALUE){
            return SignValue.TOP;
          }
          if (pRHS == SignValue.TOP) {
            return SignValue.TOP;
          }
          if (pRHS == SignValue.ZERO) {
            return SignValue.ZERO;
          }


          return pRHS == SignValue.PLUS ? SignValue.MINUS : SignValue.PLUS;
        }


        if (pLHS == SignValue.MINUS) {
          if (pRHS == SignValue.PLUS || pRHS == SignValue.ZERO_PLUS) {
            return SignValue.MINUS;
          }
          if (pRHS == SignValue.MINUS) {
            return SignValue.TOP;
          }
          if(pRHS == SignValue.ZERO){
            return  SignValue.MINUS;
          }
        }

        if(pLHS == SignValue.ZERO_PLUS){
          if(pRHS == SignValue.MINUS){
            return SignValue.PLUS;
          }
          if(pRHS == SignValue.ZERO_MINUS){
            return  SignValue.ZERO_PLUS;
          }
          if(pRHS == SignValue.ZERO){
            return  SignValue.ZERO_PLUS;
          }

        }

        if(pLHS == SignValue.ZERO_MINUS){
          if(pRHS == SignValue.PLUS){
            return SignValue.MINUS;
          }
          if(pRHS == SignValue.ZERO_PLUS){
            return  SignValue.ZERO_MINUS;
          }
          if(pRHS == SignValue.ZERO){
            return  SignValue.ZERO_MINUS;
          }
        }
        if (pLHS == SignValue.PLUS) {
          if(pRHS == SignValue.ZERO_MINUS){
            return  SignValue.PLUS;
          }
          if(pRHS == SignValue.MINUS || pRHS == SignValue.ZERO){
            return  SignValue.PLUS;
          }


        }
        if (pLHS == SignValue.PLUS && pRHS == SignValue.PLUS) {
          return SignValue.TOP;
        }

        return SignValue.TOP;
      case IMUL:
        if (pLHS.equals(SignValue.BOTTOM) || pRHS.equals(SignValue.BOTTOM)) {
          return SignValue.BOTTOM;
        }

        // Handle uninitialized and zero cases


        // Handle PLUS_MINUS cases
        if (pLHS == SignValue.PLUS_MINUS) {
          if (pRHS == SignValue.ZERO) {
            return SignValue.ZERO;
          }
          if(pRHS == SignValue.PLUS_MINUS || pRHS == SignValue.MINUS || pRHS == SignValue.PLUS){
            return  SignValue.PLUS_MINUS;
          }



          // Additional cases can be added here if needed
        }

        // Handle ZERO cases
        if (pLHS == SignValue.ZERO) {
          return  pLHS;
          // Additional cases can be added here if needed
          // Default case
        }

        // Handle MINUS cases
        if (pLHS == SignValue.MINUS) {
          if (pRHS == SignValue.PLUS ) {
            return SignValue.MINUS; // Assuming MINUS as the priority
          }
          if(pRHS == SignValue.ZERO_PLUS){
            return SignValue.ZERO_MINUS;
          }
          if(pRHS == SignValue.MINUS){
            return  SignValue.PLUS;
          }
          if(pRHS == SignValue.ZERO){
            return  SignValue.ZERO;
          }
          if(pRHS == SignValue.PLUS_MINUS){
            return  SignValue.PLUS_MINUS;
          }
          if(pRHS == SignValue.ZERO_MINUS){
            return  SignValue.ZERO_PLUS;
          }
          // Additional cases can be added here if needed
          return SignValue.TOP; // Default case
        }

        // Handle ZERO_PLUS cases
        if (pLHS == SignValue.ZERO_PLUS) {
          if (pRHS == SignValue.MINUS ) {
            return SignValue.ZERO_MINUS;
          }
          if( pRHS == SignValue.ZERO_MINUS ){
            return SignValue.ZERO_MINUS;
          }
          if( pRHS == SignValue.ZERO){
            return SignValue.ZERO;
          }
          if(pRHS == SignValue.PLUS){
            return  SignValue.ZERO_PLUS;
          }
          if(pRHS == SignValue.ZERO_PLUS){
            return  SignValue.ZERO_PLUS;
          }
          // Additional cases can be added here if needed
          return SignValue.TOP; // Default case
        }

        // Handle ZERO_MINUS cases
        if (pLHS == SignValue.ZERO_MINUS) {
          if (pRHS == SignValue.PLUS || pRHS == SignValue.ZERO_PLUS ) {
            return SignValue.ZERO_MINUS; // Assuming ZERO_MINUS as the priority
          }
          if(pRHS == SignValue.MINUS){
            return  SignValue.ZERO_PLUS;
          }

          if( pRHS == SignValue.ZERO){
            return  SignValue.ZERO;
          }
          if(pRHS == SignValue.ZERO_MINUS){
            return  SignValue.ZERO_PLUS;
          }
          // Additional cases can be added here if needed
          return SignValue.TOP; // Default case
        }

        if(pLHS == SignValue.TOP) {// Handle PLUS cases
          if(pRHS == SignValue.ZERO){
            return  SignValue.ZERO;
          }
        }

        if(pLHS == SignValue.UNINITIALIZED_VALUE) {// Handle PLUS cases
          if(pRHS == SignValue.ZERO){
            return  SignValue.ZERO;
          }
        }

        if (pLHS == SignValue.PLUS) {
          if (pRHS == SignValue.ZERO_MINUS ) {
            return SignValue.ZERO_MINUS; // Assuming PLUS as the priority
          }
          if( pRHS == SignValue.MINUS){
            return SignValue.MINUS;
          }
          if(pRHS == SignValue.ZERO){
            return  SignValue.ZERO;
          }
          if(pRHS == SignValue.PLUS_MINUS){
            return  SignValue.PLUS_MINUS;
          }
          if(pRHS == SignValue.ZERO_PLUS){
            return  SignValue.ZERO_PLUS;
          }
          if(pRHS == SignValue.PLUS){
            return SignValue.PLUS;
          }

          // Additional cases can be added here if needed
        }

        // Default case
        return SignValue.TOP;


      case IDIV:
        // Handle bottom cases
        if (pLHS.equals(SignValue.BOTTOM) || pRHS.equals(SignValue.BOTTOM)) {
          return SignValue.BOTTOM;
        }

        // Handle uninitialized and zero cases


        // Handle PLUS_MINUS cases
        if (pLHS == SignValue.PLUS_MINUS) {
          if (pRHS == SignValue.ZERO) {
            return SignValue.BOTTOM;
          }

          return  SignValue.TOP;


          // Additional cases can be added here if needed
        }

        // Handle ZERO cases
        if (pLHS == SignValue.ZERO) {
          if(pRHS == SignValue.ZERO ){
            return SignValue.BOTTOM;
          }
          if(pRHS == SignValue.TOP || pRHS == SignValue.UNINITIALIZED_VALUE){
            return SignValue.TOP;
          }
          return  pLHS;
          // Additional cases can be added here if needed
          // Default case
        }

        // Handle MINUS cases
        if (pLHS == SignValue.MINUS) {
          if (pRHS == SignValue.PLUS ) {
            return SignValue.ZERO_MINUS; // Assuming MINUS as the priority
          }
          if(pRHS == SignValue.ZERO_PLUS){
            return SignValue.ZERO_MINUS;
          }
          if(pRHS == SignValue.MINUS){
            return  SignValue.ZERO_PLUS;
          }
          if(pRHS == SignValue.ZERO){
            return  SignValue.BOTTOM;
          }

          if(pRHS == SignValue.ZERO_MINUS){
            return  SignValue.ZERO_PLUS;
          }
          // Additional cases can be added here if needed
          return SignValue.TOP; // Default case
        }

        // Handle ZERO_PLUS cases
        if (pLHS == SignValue.ZERO_PLUS) {
          if (pRHS == SignValue.PLUS ) {
            return SignValue.ZERO_PLUS; // Assuming ZERO_MINUS as the priority
          }
          if( pRHS == SignValue.ZERO_PLUS){
            return SignValue.ZERO_PLUS;
          }
          if(pRHS == SignValue.MINUS){
            return SignValue.ZERO_MINUS;
          }

          if( pRHS == SignValue.ZERO){
            return  SignValue.BOTTOM;
          }
          if(pRHS == SignValue.ZERO_MINUS){
            return  SignValue.ZERO_MINUS;
          }
          // Additional cases can be added here if needed
          return SignValue.TOP; // Default case
        }

        // Handle ZERO_MINUS cases
        if (pLHS == SignValue.ZERO_MINUS) {
          if (pRHS == SignValue.PLUS ) {
            return SignValue.ZERO_MINUS; // Assuming ZERO_MINUS as the priority
          }
          if( pRHS == SignValue.ZERO_PLUS){
            return SignValue.ZERO_MINUS;
          }
          if(pRHS == SignValue.MINUS){
            return SignValue.ZERO_PLUS;
          }

          if( pRHS == SignValue.ZERO){
            return  SignValue.BOTTOM;
          }
          if(pRHS == SignValue.ZERO_MINUS){
            return  SignValue.ZERO_PLUS;
          }
          // Additional cases can be added here if needed
          return SignValue.TOP; // Default case
        }

        if(pLHS == SignValue.TOP) {// Handle PLUS cases
          if(pRHS == SignValue.ZERO){
            return  SignValue.BOTTOM;
          }
        }

        if(pLHS == SignValue.UNINITIALIZED_VALUE) {// Handle PLUS cases
          if(pRHS == SignValue.ZERO){
            return  SignValue.BOTTOM;
          }
        }

        if (pLHS == SignValue.PLUS) {
          if (pRHS == SignValue.ZERO_MINUS ) {
            return SignValue.ZERO_MINUS; // Assuming PLUS as the priority
          }
          if( pRHS == SignValue.MINUS){
            return SignValue.ZERO_MINUS;
          }
          if(pRHS == SignValue.ZERO){
            return  SignValue.BOTTOM;
          }

          if(pRHS == SignValue.ZERO_PLUS){
            return  SignValue.ZERO_PLUS;
          }
          if(pRHS == SignValue.PLUS){
            return SignValue.ZERO_PLUS;
          }
          return  SignValue.TOP;
        }

        // Default case
        return SignValue.TOP;


      default:
        throw new UnsupportedOperationException("Unsupported operation: " );

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
