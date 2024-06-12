package de.uni_passau.fim.se2.sa.sign.interpretation;

import com.google.common.base.Preconditions;

public class SignTransferRelation implements TransferRelation {

  @Override
  public SignValue evaluate(final int pValue) {
    if (pValue < 0) {
      return SignValue.MINUS;
    } else if (pValue == 0) {
      return SignValue.ZERO;
    } else {
      return SignValue.PLUS;
    }
  }

  @Override
  public SignValue evaluate(final Operation pOperation, final SignValue pValue) {
    Preconditions.checkState(pOperation == Operation.NEG);
    Preconditions.checkNotNull(pValue);

      return switch (pValue) {
          case PLUS -> SignValue.MINUS;
          case MINUS -> SignValue.PLUS;
          case ZERO -> SignValue.ZERO;
          case ZERO_PLUS -> SignValue.ZERO_MINUS;
          case ZERO_MINUS -> SignValue.ZERO_PLUS;
          case PLUS_MINUS -> SignValue.PLUS_MINUS;
          case TOP -> SignValue.TOP;
          case BOTTOM -> SignValue.BOTTOM;
          case UNINITIALIZED_VALUE -> SignValue.UNINITIALIZED_VALUE;

          default -> throw new UnsupportedOperationException("Unsupported value: " + pValue);
      };
  }

  @Override
  public SignValue evaluate(
          final Operation pOperation, final SignValue pLHS, final SignValue pRHS) {
    Preconditions.checkState(
            pOperation == Operation.ADD
                    || pOperation == Operation.SUB
                    || pOperation == Operation.MUL
                    || pOperation == Operation.DIV);
    Preconditions.checkNotNull(pLHS);
    Preconditions.checkNotNull(pRHS);

    switch (pOperation) {
      case ADD:
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

      case SUB:
        if (pLHS.equals(SignValue.BOTTOM) || pRHS.equals(SignValue.BOTTOM)) {
          return SignValue.BOTTOM;
        }

        if (pLHS == SignValue.ZERO) {
          if (pRHS == SignValue.ZERO_PLUS) {
            return SignValue.ZERO_MINUS;
          }
          if(pRHS == SignValue.ZERO_MINUS){
            return SignValue.ZERO_PLUS;
          }
          if (pRHS == SignValue.PLUS_MINUS ) {
            return SignValue.PLUS_MINUS;
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

        if (pRHS == SignValue.ZERO) {
          return pLHS;
        }

        if (pLHS == SignValue.MINUS) {
          if (pRHS == SignValue.PLUS || pRHS == SignValue.ZERO_PLUS) {
            return SignValue.MINUS;
          }
          if (pRHS == SignValue.MINUS) {
            return SignValue.TOP;
          }
        }

        if(pLHS == SignValue.ZERO_MINUS){
          if(pRHS == SignValue.PLUS){
            return SignValue.MINUS;
          }
        }
        if (pLHS == SignValue.PLUS && pRHS == SignValue.PLUS) {
          return SignValue.TOP;
        }

        return SignValue.TOP;

      case MUL:
        if(pLHS.equals(SignValue.BOTTOM) || pRHS.equals(SignValue.BOTTOM))
          return  SignValue.BOTTOM;
        if (pLHS == SignValue.MINUS && pRHS == SignValue.PLUS_MINUS) return SignValue.PLUS_MINUS;
        if (pRHS == SignValue.MINUS && pLHS == SignValue.PLUS_MINUS) return SignValue.PLUS_MINUS;
        if (pLHS == SignValue.MINUS && pRHS == SignValue.ZERO_MINUS) return SignValue.ZERO;
        if (pRHS == SignValue.MINUS && pLHS == SignValue.ZERO_MINUS) return SignValue.ZERO;
        if (pLHS == SignValue.ZERO || pRHS == SignValue.ZERO) return SignValue.ZERO;
        if ((pLHS == SignValue.PLUS && pRHS == SignValue.PLUS) ||
                (pLHS == SignValue.MINUS && pRHS == SignValue.MINUS)) return SignValue.PLUS;
        if ((pLHS == SignValue.PLUS && pRHS == SignValue.MINUS) ||
                (pLHS == SignValue.MINUS && pRHS == SignValue.PLUS)) return SignValue.MINUS;

        return SignValue.TOP;

      case DIV:
        if(pLHS.equals(SignValue.BOTTOM) || pRHS.equals(SignValue.BOTTOM))
          return  SignValue.BOTTOM;
        if (pLHS == SignValue.MINUS && pRHS == SignValue.ZERO_MINUS) return SignValue.BOTTOM;
        if (pRHS == SignValue.ZERO) return SignValue.BOTTOM;
        if (pLHS == SignValue.MINUS && pRHS == SignValue.PLUS_MINUS) return SignValue.MINUS;
        if (pLHS == SignValue.ZERO) return SignValue.ZERO;
        if ((pLHS == SignValue.PLUS && pRHS == SignValue.PLUS) ||
                (pLHS == SignValue.MINUS && pRHS == SignValue.MINUS)) return SignValue.PLUS;
        if ((pLHS == SignValue.PLUS && pRHS == SignValue.MINUS) ||
                (pLHS == SignValue.MINUS && pRHS == SignValue.PLUS)) return SignValue.MINUS;

        return SignValue.TOP;

      default:
        throw new UnsupportedOperationException("Unsupported operation: " + pOperation);
    }
  }
}
