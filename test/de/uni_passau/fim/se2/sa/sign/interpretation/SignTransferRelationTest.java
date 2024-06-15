package de.uni_passau.fim.se2.sa.sign.interpretation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SignTransferRelationTest {

    @Test
    public void testEvaluateInt() {
        SignTransferRelation transferRelation = new SignTransferRelation();

        assertEquals(SignValue.MINUS, transferRelation.evaluate(-10));
        assertEquals(SignValue.ZERO, transferRelation.evaluate(0));
        assertEquals(SignValue.PLUS, transferRelation.evaluate(10));
    }




    @Test
    public void testEvaluateUnaryOperation() {
        SignTransferRelation transferRelation = new SignTransferRelation();

        assertEquals(SignValue.MINUS, transferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.PLUS));
        assertEquals(SignValue.PLUS, transferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.MINUS));
        assertEquals(SignValue.ZERO, transferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.ZERO));
        assertEquals(SignValue.ZERO_MINUS, transferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.ZERO_PLUS));
        assertEquals(SignValue.ZERO_PLUS, transferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.ZERO_MINUS));
        assertEquals(SignValue.PLUS_MINUS, transferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.PLUS_MINUS));
        assertEquals(SignValue.TOP, transferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.TOP));
        assertEquals(SignValue.BOTTOM, transferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.BOTTOM));
        assertEquals(SignValue.UNINITIALIZED_VALUE, transferRelation.evaluate(TransferRelation.Operation.NEG, SignValue.UNINITIALIZED_VALUE));
    }

    @Test
    public void testEvaluateBinaryOperationAdd() {
        SignTransferRelation transferRelation = new SignTransferRelation();

        assertEquals(SignValue.BOTTOM, transferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.ZERO, transferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO, SignValue.ZERO));
        assertEquals(SignValue.PLUS, transferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.PLUS));
        assertEquals(SignValue.MINUS, transferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.TOP, transferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.TOP, transferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS_MINUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO_PLUS, transferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_PLUS, SignValue.ZERO_PLUS));
        assertEquals(SignValue.ZERO_MINUS, transferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_MINUS, SignValue.ZERO_MINUS));
    }

    @Test
    public void testEvaluateBinaryOperationSub() {
        SignTransferRelation transferRelation = new SignTransferRelation();

        assertEquals(SignValue.BOTTOM, transferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.PLUS_MINUS, transferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS_MINUS, SignValue.ZERO));
        assertEquals(SignValue.MINUS, transferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.PLUS));
        assertEquals(SignValue.PLUS, transferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.MINUS));
        assertEquals(SignValue.ZERO_MINUS, transferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.ZERO_PLUS));
        assertEquals(SignValue.ZERO_PLUS, transferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.ZERO_MINUS));
        assertEquals(SignValue.PLUS, transferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS, SignValue.ZERO));
        assertEquals(SignValue.PLUS, transferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.TOP, transferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.MINUS, SignValue.MINUS));
    }

    @Test
    public void testEvaluateBinaryOperationMul() {
        SignTransferRelation transferRelation = new SignTransferRelation();

        assertEquals(SignValue.BOTTOM, transferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.ZERO, transferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO, SignValue.PLUS));
        assertEquals(SignValue.PLUS_MINUS, transferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS_MINUS, SignValue.PLUS));
        assertEquals(SignValue.MINUS, transferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.PLUS));
        assertEquals(SignValue.PLUS, transferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO_PLUS, transferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_PLUS, SignValue.PLUS));
        assertEquals(SignValue.ZERO_MINUS, transferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_PLUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO, transferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO, SignValue.ZERO));
    }

    @Test
    public void testEvaluateBinaryOperationDiv() {
        SignTransferRelation transferRelation = new SignTransferRelation();

        assertEquals(SignValue.BOTTOM, transferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.ZERO, transferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO, SignValue.PLUS));
        assertEquals(SignValue.ZERO_MINUS, transferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.MINUS, SignValue.PLUS));
        assertEquals(SignValue.ZERO_PLUS, transferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.BOTTOM, transferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS, SignValue.ZERO));
        assertEquals(SignValue.TOP, transferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS_MINUS, SignValue.MINUS));
        assertEquals(SignValue.BOTTOM, transferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO, SignValue.ZERO));
    }
}




