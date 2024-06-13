package de.uni_passau.fim.se2.sa.sign.interpretation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransferRelationTest {

    private final SignTransferRelation signTransferRelation = new SignTransferRelation();

    @Test
    public void testEvaluateConcreteValue() {
        assertEquals(SignValue.MINUS, signTransferRelation.evaluate(-1));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(0));
        assertEquals(SignValue.PLUS, signTransferRelation.evaluate(1));
    }

    @Test
    public void testEvaluateUnaryOperation() {
        assertEquals(SignValue.MINUS, signTransferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.PLUS));
        assertEquals(SignValue.PLUS, signTransferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.MINUS));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.ZERO));
        assertEquals(SignValue.ZERO_MINUS, signTransferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.ZERO_PLUS));
        assertEquals(SignValue.ZERO_PLUS, signTransferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.ZERO_MINUS));
        assertEquals(SignValue.PLUS_MINUS, signTransferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.PLUS_MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.TOP));
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.BOTTOM));
        assertEquals(SignValue.UNINITIALIZED_VALUE, signTransferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.UNINITIALIZED_VALUE));
    }

    @Test
    public void testEvaluateBinaryOperationAdd() {
        assertEquals(SignValue.PLUS, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.PLUS));
        assertEquals(SignValue.MINUS, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO, SignValue.ZERO));
        assertEquals(SignValue.PLUS, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO, SignValue.PLUS));
        assertEquals(SignValue.MINUS, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO, SignValue.MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.TOP, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.MINUS));
    }

    @Test
    public void testEvaluateBinaryOperationSub() {
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS, SignValue.PLUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.PLUS, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.ZERO));
        assertEquals(SignValue.MINUS, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.PLUS));
        assertEquals(SignValue.PLUS, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.TOP, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.BOTTOM, SignValue.MINUS));
    }

    @Test
    public void testEvaluateBinaryOperationMul() {
        assertEquals(SignValue.PLUS, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS, SignValue.PLUS));
        assertEquals(SignValue.PLUS, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.MINUS, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO, SignValue.ZERO));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO, SignValue.PLUS));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO, SignValue.MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.TOP, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.BOTTOM, SignValue.MINUS));
    }

    @Test
    public void testEvaluateBinaryOperationDiv() {
        assertEquals(SignValue.ZERO_PLUS, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS, SignValue.PLUS));
        assertEquals(SignValue.ZERO_PLUS, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO_MINUS, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO, SignValue.ZERO));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO, SignValue.PLUS));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO, SignValue.MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.TOP, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.BOTTOM, SignValue.MINUS));
    }
}
