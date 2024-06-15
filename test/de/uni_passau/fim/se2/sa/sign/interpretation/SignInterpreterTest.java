package de.uni_passau.fim.se2.sa.sign.interpretation;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.objectweb.asm.Opcodes.*;

public class SignInterpreterTest {

    @Test
    public void testNewValue() {
        SignInterpreter interpreter = new SignInterpreter();
        assertEquals(SignValue.UNINITIALIZED_VALUE, interpreter.newValue(null));
        assertNull(interpreter.newValue(Type.VOID_TYPE));
        assertEquals(SignValue.TOP, interpreter.newValue(Type.INT_TYPE));
    }

    @Test
    public void testNewOperation() {
        SignInterpreter interpreter = new SignInterpreter();

        AbstractInsnNode instr = new InsnNode(Opcodes.ICONST_0);

            assertEquals(SignValue.ZERO, interpreter.newOperation(instr));

        instr = new InsnNode(Opcodes.ICONST_1);

            assertEquals(SignValue.PLUS, interpreter.newOperation(instr));


        instr = new InsnNode(Opcodes.ICONST_M1);

            assertEquals(SignValue.MINUS, interpreter.newOperation(instr));


        instr = new InsnNode(Opcodes.BIPUSH);

            assertEquals(SignValue.TOP, interpreter.newOperation(instr));


        instr = new InsnNode(Opcodes.SIPUSH);

            assertEquals(SignValue.TOP, interpreter.newOperation(instr));


        LdcInsnNode ldcInsnNode = new LdcInsnNode(0);

            assertEquals(SignValue.ZERO, interpreter.newOperation(ldcInsnNode));

        ldcInsnNode = new LdcInsnNode(1);

            assertEquals(SignValue.PLUS, interpreter.newOperation(ldcInsnNode));


    }

    @Test
    public void testCopyOperation() {
        SignInterpreter interpreter = new SignInterpreter();
        AbstractInsnNode instr = new InsnNode(Opcodes.NOP);
        SignValue value = SignValue.TOP;
        assertEquals(value, interpreter.copyOperation(instr, value));
    }

    @Test
    public void testUnaryOperation() {
        SignInterpreter interpreter = new SignInterpreter();

        AbstractInsnNode instr = new InsnNode(Opcodes.INEG);


            assertEquals(SignValue.MINUS, interpreter.unaryOperation(instr, SignValue.PLUS));
            assertEquals(SignValue.PLUS, interpreter.unaryOperation(instr, SignValue.MINUS));
            assertEquals(SignValue.ZERO, interpreter.unaryOperation(instr, SignValue.ZERO));


        instr = new InsnNode(Opcodes.IINC);

            assertEquals(SignValue.TOP, interpreter.unaryOperation(instr, SignValue.PLUS));



    }
    @Test
    public void NewValue() {
        SignInterpreter interpreter = new SignInterpreter();

        assertEquals(SignValue.UNINITIALIZED_VALUE, interpreter.newValue(null));
        assertEquals(null, interpreter.newValue(Type.VOID_TYPE));
        assertEquals(SignValue.TOP, interpreter.newValue(Type.INT_TYPE));
        assertEquals(SignValue.TOP, interpreter.newValue(Type.LONG_TYPE));
        assertEquals(SignValue.TOP, interpreter.newValue(Type.FLOAT_TYPE));
        assertEquals(SignValue.TOP, interpreter.newValue(Type.DOUBLE_TYPE));
        assertEquals(SignValue.TOP, interpreter.newValue(Type.BOOLEAN_TYPE));
        assertEquals(SignValue.TOP, interpreter.newValue(Type.getObjectType("java/lang/String")));
        // Add more cases to cover other types if necessary
    }

    @Test
    public void NewOperation() {
        SignInterpreter interpreter = new SignInterpreter();

        AbstractInsnNode iconstM1 = new InsnNode(ICONST_M1);
        assertEquals(SignValue.MINUS, interpreter.newOperation(iconstM1));

        AbstractInsnNode iconst0 = new InsnNode(ICONST_0);
        assertEquals(SignValue.ZERO, interpreter.newOperation(iconst0));

        AbstractInsnNode iconst1 = new InsnNode(ICONST_1);
        assertEquals(SignValue.PLUS, interpreter.newOperation(iconst1));

        AbstractInsnNode bipush = new IntInsnNode(BIPUSH, 42);
        assertEquals(SignValue.TOP, interpreter.newOperation(bipush));

        AbstractInsnNode sipush = new IntInsnNode(SIPUSH, 32767);
        assertEquals(SignValue.TOP, interpreter.newOperation(sipush));

        AbstractInsnNode ldcInt = new LdcInsnNode(123);
        assertEquals(SignValue.PLUS, interpreter.newOperation(ldcInt));

        AbstractInsnNode ldcMinusInt = new LdcInsnNode(-456);
        assertEquals(SignValue.MINUS, interpreter.newOperation(ldcMinusInt));

        AbstractInsnNode ldcZero = new LdcInsnNode(0);
        assertEquals(SignValue.ZERO, interpreter.newOperation(ldcZero));

        AbstractInsnNode ldcString = new LdcInsnNode("Hello");
        assertEquals(SignValue.TOP, interpreter.newOperation(ldcString));
    }

    @Test
    public void UnaryOperation() {
        SignInterpreter interpreter = new SignInterpreter();

        AbstractInsnNode ineg = new InsnNode(INEG);
        assertEquals(SignValue.MINUS, interpreter.unaryOperation(ineg, SignValue.PLUS));

        AbstractInsnNode iinc = new IincInsnNode(1, 1);
        assertEquals(SignValue.TOP, interpreter.unaryOperation(iinc, SignValue.ZERO));
    }

    @Test
    public void BinaryOperation() {
        SignInterpreter interpreter = new SignInterpreter();

        AbstractInsnNode iadd = new InsnNode(IADD);
        assertEquals(SignValue.PLUS, interpreter.binaryOperation(iadd, SignValue.PLUS, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, interpreter.binaryOperation(iadd, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.TOP, interpreter.binaryOperation(iadd, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));
        assertEquals(SignValue.ZERO_PLUS, interpreter.binaryOperation(iadd, SignValue.ZERO_PLUS, SignValue.ZERO_PLUS));
        assertEquals(SignValue.MINUS, interpreter.binaryOperation(iadd, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.MINUS, interpreter.binaryOperation(iadd, SignValue.MINUS, SignValue.ZERO_MINUS));
        assertEquals(SignValue.TOP, interpreter.binaryOperation(iadd, SignValue.MINUS, SignValue.PLUS_MINUS));

        AbstractInsnNode isub = new InsnNode(ISUB);
        assertEquals(SignValue.TOP, interpreter.binaryOperation(isub, SignValue.ZERO, SignValue.UNINITIALIZED_VALUE));
        assertEquals(SignValue.TOP, interpreter.binaryOperation(isub, SignValue.PLUS, SignValue.PLUS));
        assertEquals(SignValue.TOP, interpreter.binaryOperation(isub, SignValue.PLUS, SignValue.ZERO_PLUS));
        assertEquals(SignValue.TOP, interpreter.binaryOperation(isub, SignValue.ZERO_PLUS, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, interpreter.binaryOperation(isub, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.TOP, interpreter.binaryOperation(isub, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));
        assertEquals(SignValue.TOP, interpreter.binaryOperation(isub, SignValue.ZERO_PLUS, SignValue.ZERO_PLUS));
        assertEquals(SignValue.TOP, interpreter.binaryOperation(isub, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.TOP, interpreter.binaryOperation(isub, SignValue.MINUS, SignValue.ZERO_MINUS));
        assertEquals(SignValue.TOP, interpreter.binaryOperation(isub, SignValue.MINUS, SignValue.PLUS_MINUS));
        assertEquals(SignValue.ZERO_MINUS, interpreter.binaryOperation(isub, SignValue.ZERO_MINUS, SignValue.ZERO_PLUS));

        AbstractInsnNode imul = new InsnNode(IMUL);
        assertEquals(SignValue.ZERO, interpreter.binaryOperation(imul, SignValue.ZERO, SignValue.UNINITIALIZED_VALUE));
        assertEquals(SignValue.PLUS, interpreter.binaryOperation(imul, SignValue.PLUS, SignValue.PLUS));
        assertEquals(SignValue.ZERO_PLUS, interpreter.binaryOperation(imul, SignValue.PLUS, SignValue.ZERO_PLUS));
        assertEquals(SignValue.ZERO_PLUS, interpreter.binaryOperation(imul, SignValue.ZERO_PLUS, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, interpreter.binaryOperation(imul, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.ZERO, interpreter.binaryOperation(imul, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));
        assertEquals(SignValue.ZERO_PLUS, interpreter.binaryOperation(imul, SignValue.ZERO_PLUS, SignValue.ZERO_PLUS));
        assertEquals(SignValue.PLUS, interpreter.binaryOperation(imul, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO_PLUS, interpreter.binaryOperation(imul, SignValue.MINUS, SignValue.ZERO_MINUS));
        assertEquals(SignValue.PLUS_MINUS, interpreter.binaryOperation(imul, SignValue.MINUS, SignValue.PLUS_MINUS));
        assertEquals(SignValue.MINUS, interpreter.binaryOperation(imul, SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO_MINUS, interpreter.binaryOperation(imul, SignValue.MINUS, SignValue.ZERO_PLUS));
        assertEquals(SignValue.ZERO_MINUS, interpreter.binaryOperation(imul, SignValue.ZERO_PLUS, SignValue.MINUS));
        AbstractInsnNode idiv = new InsnNode(IDIV);
        assertEquals(SignValue.TOP, interpreter.binaryOperation(idiv, SignValue.ZERO, SignValue.UNINITIALIZED_VALUE));
        assertEquals(SignValue.ZERO_PLUS, interpreter.binaryOperation(idiv, SignValue.PLUS, SignValue.PLUS));
        assertEquals(SignValue.ZERO_PLUS, interpreter.binaryOperation(idiv, SignValue.PLUS, SignValue.ZERO_PLUS));
        assertEquals(SignValue.ZERO_PLUS, interpreter.binaryOperation(idiv, SignValue.ZERO_PLUS, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, interpreter.binaryOperation(idiv, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, interpreter.binaryOperation(idiv, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));
        assertEquals(SignValue.ZERO_PLUS, interpreter.binaryOperation(idiv, SignValue.ZERO_PLUS, SignValue.ZERO_PLUS));
        assertEquals(SignValue.ZERO_PLUS, interpreter.binaryOperation(idiv, SignValue.MINUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO_PLUS, interpreter.binaryOperation(idiv, SignValue.MINUS, SignValue.ZERO_MINUS));
        assertEquals(SignValue.TOP, interpreter.binaryOperation(idiv, SignValue.MINUS, SignValue.PLUS_MINUS));
        assertEquals(SignValue.ZERO_MINUS, interpreter.binaryOperation(idiv, SignValue.MINUS, SignValue.PLUS));
        assertEquals(SignValue.ZERO_MINUS, interpreter.binaryOperation(idiv, SignValue.PLUS, SignValue.ZERO_MINUS));
        AbstractInsnNode ineg = new InsnNode(INEG);
        assertEquals(SignValue.TOP, interpreter.binaryOperation(ineg, SignValue.ZERO, SignValue.ZERO));
    }
    @Test
    public void testBinaryOperation() {
        SignInterpreter interpreter = new SignInterpreter();
        AbstractInsnNode instr = new InsnNode(Opcodes.IADD);
        assertEquals(SignValue.TOP, interpreter.binaryOperation(instr, SignValue.PLUS, SignValue.MINUS));
    }

    @Test
    public void testTernaryOperation() {
        SignInterpreter interpreter = new SignInterpreter();
        AbstractInsnNode instr = new InsnNode(Opcodes.NOP);
        assertNull(interpreter.ternaryOperation(instr, SignValue.PLUS, SignValue.MINUS, SignValue.ZERO));
    }

    @Test
    public void testNaryOperation() {
        SignInterpreter interpreter = new SignInterpreter();
        AbstractInsnNode instr = new InsnNode(Opcodes.NOP);
        assertEquals(SignValue.TOP, interpreter.naryOperation(instr, List.of(SignValue.PLUS, SignValue.MINUS)));
    }

    @Test
    public void testReturnOperation() {
        SignInterpreter interpreter = new SignInterpreter();
        AbstractInsnNode instr = new InsnNode(Opcodes.RETURN);
        assertDoesNotThrow(() -> interpreter.returnOperation(instr, SignValue.PLUS, SignValue.MINUS));
    }

    @Test
    public void testMerge() {
        SignInterpreter interpreter = new SignInterpreter();
        assertEquals(SignValue.PLUS_MINUS, interpreter.merge(SignValue.PLUS, SignValue.MINUS));
    }
}
