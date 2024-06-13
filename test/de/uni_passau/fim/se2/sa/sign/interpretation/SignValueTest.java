package de.uni_passau.fim.se2.sa.sign.interpretation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SignValueTest {

    @Test
    public void testJoin() {
        assertEquals(SignValue.MINUS, SignValue.MINUS.join(SignValue.MINUS));
        assertEquals(SignValue.PLUS_MINUS, SignValue.PLUS.join(SignValue.MINUS));
        assertEquals(SignValue.PLUS_MINUS, SignValue.MINUS.join(SignValue.PLUS_MINUS));
        assertEquals(SignValue.ZERO_PLUS, SignValue.ZERO.join(SignValue.PLUS));
        assertEquals(SignValue.TOP, SignValue.ZERO_PLUS.join(SignValue.MINUS));
    }

    @Test
    public void testIsLessOrEqual() {
        assertTrue(SignValue.ZERO.isLessOrEqual(SignValue.ZERO));
        assertTrue(SignValue.MINUS.isLessOrEqual(SignValue.PLUS_MINUS));
        assertFalse(SignValue.PLUS.isLessOrEqual(SignValue.MINUS));
        assertTrue(SignValue.ZERO_MINUS.isLessOrEqual(SignValue.TOP));
        assertFalse(SignValue.TOP.isLessOrEqual(SignValue.ZERO_MINUS));
    }

    @Test
    public void testIsZero() {
        assertTrue(SignValue.isZero(SignValue.ZERO));
        assertFalse(SignValue.isZero(SignValue.MINUS));
        assertFalse(SignValue.isZero(SignValue.PLUS));
        assertFalse(SignValue.isZero(SignValue.TOP));
    }

    @Test
    public void testIsMaybeZero() {
        assertTrue(SignValue.isMaybeZero(SignValue.ZERO));
        assertTrue(SignValue.isMaybeZero(SignValue.ZERO_MINUS));
        assertTrue(SignValue.isMaybeZero(SignValue.ZERO_PLUS));
        assertTrue(SignValue.isMaybeZero(SignValue.TOP));
        assertFalse(SignValue.isMaybeZero(SignValue.MINUS));
        assertFalse(SignValue.isMaybeZero(SignValue.PLUS));
    }

    @Test
    public void testIsNegative() {
        assertTrue(SignValue.isNegative(SignValue.MINUS));
        assertFalse(SignValue.isNegative(SignValue.ZERO));
        assertFalse(SignValue.isNegative(SignValue.PLUS));
        assertFalse(SignValue.isNegative(SignValue.TOP));
    }

    @Test
    public void testIsMaybeNegative() {
        assertTrue(SignValue.isMaybeNegative(SignValue.MINUS));
        assertTrue(SignValue.isMaybeNegative(SignValue.ZERO_MINUS));
        assertTrue(SignValue.isMaybeNegative(SignValue.PLUS_MINUS));
        assertTrue(SignValue.isMaybeNegative(SignValue.TOP));
        assertFalse(SignValue.isMaybeNegative(SignValue.ZERO));
        assertFalse(SignValue.isMaybeNegative(SignValue.PLUS));
    }
}
