package de.uni_passau.fim.se2.sa.sign.lattice;

import de.uni_passau.fim.se2.sa.sign.interpretation.SignValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SignLatticeTest {

    private final SignLattice signLattice = new SignLattice();

    @Test
    public void testTop() {
        assertEquals(SignValue.TOP, signLattice.top());
    }

    @Test
    public void testBottom() {
        assertEquals(SignValue.BOTTOM, signLattice.bottom());
    }

    @Test
    public void testJoin() {
        assertEquals(SignValue.PLUS_MINUS, signLattice.join(SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.PLUS_MINUS, signLattice.join(SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.TOP, signLattice.join(SignValue.ZERO, SignValue.TOP));
        assertEquals(SignValue.ZERO, signLattice.join(SignValue.ZERO, SignValue.ZERO));
        assertEquals(SignValue.PLUS, signLattice.join(SignValue.PLUS, SignValue.PLUS));
        assertEquals(SignValue.ZERO_PLUS, signLattice.join(SignValue.ZERO, SignValue.PLUS));
        assertEquals(SignValue.ZERO_MINUS, signLattice.join(SignValue.ZERO, SignValue.MINUS));
        assertEquals(SignValue.TOP, signLattice.join(SignValue.TOP, SignValue.PLUS));
    }

    @Test
    public void testIsLessOrEqual() {
        assertTrue(signLattice.isLessOrEqual(SignValue.BOTTOM, SignValue.TOP));
        assertTrue(signLattice.isLessOrEqual(SignValue.ZERO, SignValue.TOP));
        assertTrue(signLattice.isLessOrEqual(SignValue.PLUS, SignValue.TOP));
        assertTrue(signLattice.isLessOrEqual(SignValue.MINUS, SignValue.TOP));
        assertTrue(signLattice.isLessOrEqual(SignValue.ZERO, SignValue.ZERO));
        assertTrue(signLattice.isLessOrEqual(SignValue.PLUS, SignValue.PLUS));
        assertTrue(signLattice.isLessOrEqual(SignValue.MINUS, SignValue.MINUS));
        assertFalse(signLattice.isLessOrEqual(SignValue.PLUS, SignValue.MINUS));
        assertFalse(signLattice.isLessOrEqual(SignValue.ZERO, SignValue.MINUS));
        assertFalse(signLattice.isLessOrEqual(SignValue.MINUS, SignValue.PLUS));
        assertFalse(signLattice.isLessOrEqual(SignValue.TOP, SignValue.BOTTOM));
    }
}
