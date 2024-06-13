package de.uni_passau.fim.se2.sa.sign.lattice;

import de.uni_passau.fim.se2.sa.sign.interpretation.SignValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LatticeTest {

    private final SignLattice signLattice = new SignLattice();

    @Test
    public void testTop() {
        assertEquals(SignValue.TOP, signLattice.top(), "The top value should be TOP.");
    }

    @Test
    public void testBottom() {
        assertEquals(SignValue.BOTTOM, signLattice.bottom(), "The bottom value should be BOTTOM.");
    }

    @Test
    public void testJoin() {
        // Basic join operations
        assertEquals(SignValue.PLUS_MINUS, signLattice.join(SignValue.PLUS, SignValue.MINUS), "PLUS join MINUS should be TOP.");
        assertEquals(SignValue.ZERO, signLattice.join(SignValue.ZERO, SignValue.ZERO), "ZERO join ZERO should be ZERO.");
        assertEquals(SignValue.PLUS, signLattice.join(SignValue.PLUS, SignValue.PLUS), "PLUS join PLUS should be PLUS.");

        // Mixed join operations
        assertEquals(SignValue.TOP, signLattice.join(SignValue.ZERO, SignValue.TOP), "ZERO join TOP should be TOP.");
        assertEquals(SignValue.ZERO_PLUS, signLattice.join(SignValue.ZERO, SignValue.PLUS), "ZERO join PLUS should be ZERO_PLUS.");
        assertEquals(SignValue.ZERO_MINUS, signLattice.join(SignValue.ZERO, SignValue.MINUS), "ZERO join MINUS should be ZERO_MINUS.");
    }

    @Test
    public void testIsLessOrEqual() {
        // Check less or equal relationships
        assertTrue(signLattice.isLessOrEqual(SignValue.BOTTOM, SignValue.TOP), "BOTTOM should be less or equal to TOP.");
        assertTrue(signLattice.isLessOrEqual(SignValue.ZERO, SignValue.TOP), "ZERO should be less or equal to TOP.");
        assertTrue(signLattice.isLessOrEqual(SignValue.PLUS, SignValue.TOP), "PLUS should be less or equal to TOP.");
        assertTrue(signLattice.isLessOrEqual(SignValue.MINUS, SignValue.TOP), "MINUS should be less or equal to TOP.");
        assertTrue(signLattice.isLessOrEqual(SignValue.ZERO, SignValue.ZERO), "ZERO should be less or equal to ZERO.");
        assertTrue(signLattice.isLessOrEqual(SignValue.PLUS, SignValue.PLUS), "PLUS should be less or equal to PLUS.");
        assertTrue(signLattice.isLessOrEqual(SignValue.MINUS, SignValue.MINUS), "MINUS should be less or equal to MINUS.");

        // Check non less or equal relationships
        assertFalse(signLattice.isLessOrEqual(SignValue.PLUS, SignValue.MINUS), "PLUS should not be less or equal to MINUS.");
        assertFalse(signLattice.isLessOrEqual(SignValue.ZERO, SignValue.MINUS), "ZERO should not be less or equal to MINUS.");
        assertFalse(signLattice.isLessOrEqual(SignValue.MINUS, SignValue.PLUS), "MINUS should not be less or equal to PLUS.");
        assertFalse(signLattice.isLessOrEqual(SignValue.TOP, SignValue.BOTTOM), "TOP should not be less or equal to BOTTOM.");
    }
}
