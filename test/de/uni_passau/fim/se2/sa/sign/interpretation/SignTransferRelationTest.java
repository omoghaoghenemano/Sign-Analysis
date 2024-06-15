package de.uni_passau.fim.se2.sa.sign.interpretation;
import de.uni_passau.fim.se2.sa.sign.interpretation.SignTransferRelation;
import de.uni_passau.fim.se2.sa.sign.interpretation.SignValue;
import de.uni_passau.fim.se2.sa.sign.interpretation.TransferRelation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Preconditions;

public class SignTransferRelationTest {

    private final SignTransferRelation signTransferRelation = new SignTransferRelation();


    @Test
    public void testEvaluateInt() {
        assertEquals(SignValue.MINUS, signTransferRelation.evaluate(-1));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(0));
        assertEquals(SignValue.PLUS, signTransferRelation.evaluate(1));
    }

    @Test
    public void testEvaluateAdd() {
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));
        assertEquals(SignValue.ZERO_PLUS, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_PLUS, SignValue.ZERO_PLUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO, SignValue.UNINITIALIZED_VALUE));
        assertEquals(SignValue.PLUS, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.ZERO));
        assertEquals(SignValue.MINUS, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.PLUS_MINUS));
        assertEquals(SignValue.MINUS, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.ZERO_MINUS));
        assertEquals(SignValue.ZERO_MINUS, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_MINUS, SignValue.ZERO_MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.PLUS));
    }

    @Test
    public void testEvaluateSub() {
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));
        assertEquals(SignValue.PLUS_MINUS, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS_MINUS, SignValue.ZERO));
        assertEquals(SignValue.ZERO_MINUS, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.ZERO_PLUS));
        assertEquals(SignValue.ZERO_PLUS, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.ZERO_MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS_MINUS, SignValue.MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.MINUS, SignValue.PLUS_MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.MINUS, SignValue.ZERO_MINUS));
        assertEquals(SignValue.PLUS, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.SUB, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));

    }

    @Test
    public void testEvaluateMul() {
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));
        assertEquals(SignValue.PLUS_MINUS, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS_MINUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO, SignValue.ZERO_PLUS));
        assertEquals(SignValue.MINUS, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.PLUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.UNINITIALIZED_VALUE));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS, SignValue.UNINITIALIZED_VALUE));
        assertEquals(SignValue.ZERO, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_PLUS, SignValue.ZERO));
        assertEquals(SignValue.ZERO_MINUS, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_MINUS, SignValue.ZERO_PLUS));
        assertEquals(SignValue.MINUS, signTransferRelation.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS, SignValue.MINUS));
    }

    @Test
    public void testEvaluateDiv() {
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS_MINUS, SignValue.ZERO));
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.MINUS, SignValue.ZERO));
        assertEquals(SignValue.ZERO_MINUS, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.MINUS, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS, SignValue.ZERO));
        assertEquals(SignValue.BOTTOM, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO_PLUS, SignValue.ZERO));
        assertEquals(SignValue.ZERO_MINUS, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.MINUS, SignValue.PLUS));
        assertEquals(SignValue.ZERO_PLUS, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO_MINUS, SignValue.MINUS));
        assertEquals(SignValue.TOP, signTransferRelation.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS, SignValue.PLUS_MINUS));
    }

    @Test
    public void testUnsupportedOperation() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            signTransferRelation.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS);
        });
        assertNull(exception.getMessage());

    }
}
