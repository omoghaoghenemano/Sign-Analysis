package de.uni_passau.fim.se2.sa.sign.interpretation;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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
        try {
            assertEquals(SignValue.ZERO, interpreter.newOperation(instr));
        } catch (AnalyzerException e) {
            fail("AnalyzerException should not be thrown");
        }

        instr = new InsnNode(Opcodes.ICONST_1);
        try {
            assertEquals(SignValue.PLUS, interpreter.newOperation(instr));
        } catch (AnalyzerException e) {
            fail("AnalyzerException should not be thrown");
        }

        instr = new InsnNode(Opcodes.ICONST_M1);
        try {
            assertEquals(SignValue.MINUS, interpreter.newOperation(instr));
        } catch (AnalyzerException e) {
            fail("AnalyzerException should not be thrown");
        }

        instr = new InsnNode(Opcodes.BIPUSH);
        try {
            assertEquals(SignValue.TOP, interpreter.newOperation(instr));
        } catch (AnalyzerException e) {
            fail("AnalyzerException should not be thrown");
        }

        instr = new InsnNode(Opcodes.SIPUSH);
        try {
            assertEquals(SignValue.TOP, interpreter.newOperation(instr));
        } catch (AnalyzerException e) {
            fail("AnalyzerException should not be thrown");
        }

        LdcInsnNode ldcInsnNode = new LdcInsnNode(0);
        try {
            assertEquals(SignValue.ZERO, interpreter.newOperation(ldcInsnNode));
        } catch (AnalyzerException e) {
            fail("AnalyzerException should not be thrown");
        }

        ldcInsnNode = new LdcInsnNode(1);
        try {
            assertEquals(SignValue.PLUS, interpreter.newOperation(ldcInsnNode));
        } catch (AnalyzerException e) {
            fail("AnalyzerException should not be thrown");
        }

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
        try {
            assertEquals(SignValue.MINUS, interpreter.unaryOperation(instr, SignValue.PLUS));
            assertEquals(SignValue.PLUS, interpreter.unaryOperation(instr, SignValue.MINUS));
            assertEquals(SignValue.ZERO, interpreter.unaryOperation(instr, SignValue.ZERO));
        } catch (AnalyzerException e) {
            fail("AnalyzerException should not be thrown");
        }

        instr = new InsnNode(Opcodes.IINC);
        try {
            assertEquals(SignValue.TOP, interpreter.unaryOperation(instr, SignValue.PLUS));
        } catch (AnalyzerException e) {
            fail("AnalyzerException should not be thrown");
        }


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
