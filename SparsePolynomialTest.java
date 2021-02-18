import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SparsePolynomialTest {

    @Test
    void degree() {
        Polynomial p = new SparsePolynomial("0");
        assertEquals(0, p.degree());
        Polynomial q = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        assertEquals(50, q.degree());
        Polynomial s = new SparsePolynomial("x^2147483");
        assertEquals(2147483, s.degree());
        Polynomial v = new SparsePolynomial("x^-1 + x^-2");
        assertEquals(-1, v.degree());
    }

    @Test
    void getCoefficient() {
        Polynomial q = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100 + -x^-2");
        assertEquals(100, q.getCoefficient(0));
        assertEquals(0, q.getCoefficient(15));
        assertEquals(1, q.getCoefficient(50));
        assertEquals(0, q.getCoefficient(999999));
        assertEquals(0, q.getCoefficient(-1));
        assertEquals(-1, q.getCoefficient(-2));
        assertEquals(0, q.getCoefficient(-5));
    }

    @Test
    void isZero() {
        Polynomial p = new SparsePolynomial("0");
        assertTrue(p.isZero());
        Polynomial q = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        assertFalse(q.isZero());
    }

    @Test
    void add() {
        Polynomial p = new SparsePolynomial("0");
        Polynomial q = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        Polynomial v = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100 + x^-2 + x^-5");
        Polynomial w = new SparsePolynomial("2x^50 + 6x^40 + 2x^35 + 130x^10 + 150x + 200 + x^-2 + x^-5");
        Polynomial s = new DensePolynomial("x^2147483");
        Polynomial t = new SparsePolynomial("x^2147483 + x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100 + x^-2 + x^-5");
        assertThrows(NullPointerException.class, () -> p.add(null));
        assertEquals(q, p.add(q));
        assertEquals(w, q.add(v));
        assertEquals(t, v.add(s));
    }

    @Test
    void multiply() {
        Polynomial p = new SparsePolynomial("0");
        Polynomial q = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        Polynomial s = new DensePolynomial("2x^10 + 5x^7");
        Polynomial t = new SparsePolynomial("x^-2");
        Polynomial x = new SparsePolynomial("2x^8 + 5x^5");
        Polynomial y = new DensePolynomial("x^2");
        Polynomial z = new SparsePolynomial("1");
        Polynomial a = new SparsePolynomial("x^48 + 3x^38 + x^33 + 65x^8 + 75x^-1 + 100x^-2");
        assertEquals(p, p.multiply(q));
        assertEquals(x, t.multiply(s));
        assertEquals(z, t.multiply(y));
        assertEquals(t, t.multiply(z));
        assertEquals(a, q.multiply(t));
    }

    @Test
    void subtract() {
        Polynomial p = new SparsePolynomial("0");
        Polynomial q = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        Polynomial s = new SparsePolynomial("2x^10 + 5x^7 - x^-2 - x^-3");
        Polynomial z = new SparsePolynomial("1 - x^-2");
        Polynomial y = new SparsePolynomial("1 + x^-2");
        Polynomial a = new SparsePolynomial("2x^10 + 5x^7 - 1 - x^-3");
        Polynomial b = new SparsePolynomial("2x^10 + 5x^7 - 1 - 2x^-2 - x^-3");
        assertThrows(NullPointerException.class, () -> p.multiply(null));
        assertEquals(p, q.subtract(q));
        assertEquals(q, q.subtract(p));
        assertEquals(a, s.subtract(z));
        assertEquals(b, s.subtract(y));
    }

    @Test
    void minus() {
        Polynomial p = new SparsePolynomial("0");
        Polynomial s = new SparsePolynomial("2x^10 + 5x^7");
        Polynomial v = new SparsePolynomial("-2x^10 - 5x^7");
        Polynomial a = new SparsePolynomial("2x^10 + 5x^7 - x^-2 - x^-3");
        Polynomial b = new SparsePolynomial("-2x^10 - 5x^7 + x^-2 + x^-3");
        assertEquals(p, p.minus());
        assertEquals(v, s.minus());
        assertEquals(b, a.minus());
        assertThrows(ArithmeticException.class, () -> new DensePolynomial("-2x^10 - 5x^7 + x^-2 + x^-3"));
    }

    @Test
    void wellFormed() {
        Polynomial a = new SparsePolynomial("2x^10 + 5x^7 - x^-2 - x^-3");
        Polynomial b = new SparsePolynomial("-2x^10 - 5x^7 + x^-2 + x^-3");
        assertTrue(a.wellFormed());
        assertTrue(b.wellFormed());
        assertThrows(IllegalArgumentException.class, () -> new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x - 100 + x^10"));
        assertThrows(IllegalArgumentException.class, () -> new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x - 100 + x^-2 + x^-3 + x^-3"));
        assertThrows(IllegalArgumentException.class, () -> new DensePolynomial(" "));
    }

    @Test
    void testEquals() {
        Polynomial q = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        Polynomial a = new DensePolynomial("2x^10 + 5x^7 - 1");
        Polynomial x = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        Polynomial y = new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        Polynomial c = new SparsePolynomial("2x^10 + 5x^7 - x^-2 - x^-3");
        Polynomial b = new SparsePolynomial("-2x^10 - 5x^7 + x^-2 + x^-3");
        assertFalse(q.equals(a));
        assertFalse(q.equals(y));
        assertTrue(q.equals(x));
        assertTrue(c.equals(b.minus()));
    }
}