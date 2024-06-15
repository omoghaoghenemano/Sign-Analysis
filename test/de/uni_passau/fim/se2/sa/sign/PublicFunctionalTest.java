package de.uni_passau.fim.se2.sa.sign;
import de.uni_passau.fim.se2.sa.examples.PublicFunctional;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PublicFunctionalTest {

    @Test
    public void testAllCases() {
        PublicFunctional pf = new PublicFunctional();
        assertThrows(ArithmeticException.class, pf::allCases);
    }

    @Test
    public void testTwoErrors() {
        PublicFunctional pf = new PublicFunctional();
        assertThrows(ArrayIndexOutOfBoundsException.class, pf::twoErrors);
    }

    @Test
    public void testIfElse() {
        PublicFunctional pf = new PublicFunctional();
        assertDoesNotThrow(pf::ifelse);
    }

    @Test
    public void testLoop0() {
        PublicFunctional pf = new PublicFunctional();
        assertDoesNotThrow(pf::loop0);
    }

    @Test
    public void testAdd() {
        PublicFunctional pf = new PublicFunctional();
        assertEquals(3, pf.add());
    }

    @Test
    public void testDiv() {
        PublicFunctional pf = new PublicFunctional();
        assertThrows(ArithmeticException.class, pf::div);
    }


    @Test
    public void testFoo() {
        PublicFunctional pf = new PublicFunctional();
        assertThrows(ArrayIndexOutOfBoundsException.class, pf::foo);
    }

    @Test
    public void testBar() {
        PublicFunctional pf = new PublicFunctional();
        assertThrows(ArrayIndexOutOfBoundsException.class, pf::bar);
    }

    @Test
    public void testFirst() {
        PublicFunctional pf = new PublicFunctional();
        assertEquals(4, pf.first());
    }

    @Test
    public void testSecond() {
        PublicFunctional pf = new PublicFunctional();
        assertEquals(6, pf.second(3));
    }

    @Test
    public void testListSize() {
        PublicFunctional pf = new PublicFunctional();
        assertEquals(0, pf.listSize());
    }
}
