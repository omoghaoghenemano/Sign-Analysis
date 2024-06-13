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
