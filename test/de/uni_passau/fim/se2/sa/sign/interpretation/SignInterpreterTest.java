package de.uni_passau.fim.se2.sa.sign.interpretation;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SignInterpreterTest {

    @Test
    public void testConstructorWithInvalidAPI() {
        assertDoesNotThrow( () -> new SignInterpreter(Opcodes.ASM4));
    }

    @Test
    public void testNewValue() {
        SignInterpreter interpreter = new SignInterpreter();
        assertEquals(SignValue.UNINITIALIZED_VALUE, interpreter.newValue(null));
        assertNull(interpreter.newValue(Type.VOID_TYPE));
        assertEquals(SignValue.TOP, interpreter.newValue(Type.INT_TYPE));
    }

    
    @Test
    public void testMerge() {
        SignInterpreter interpreter = new SignInterpreter();
        assertEquals(SignValue.PLUS_MINUS, interpreter.merge(SignValue.PLUS, SignValue.MINUS));
    }
}
