import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class DensePolynomialTest {

    @Test
    void degree() {
        Polynomial p = new DensePolynomial("0");
        assertEquals(0, p.degree());
        Polynomial q = new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        assertEquals(50, q.degree());
        Polynomial s = new DensePolynomial("x^2147483");
        assertEquals(2147483, s.degree());
    }

    @Test
    void getCoefficient() {
        Polynomial q = new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        assertEquals(100, q.getCoefficient(0));
        assertEquals(0, q.getCoefficient(15));
        assertEquals(1, q.getCoefficient(50));
        assertEquals(0, q.getCoefficient(999999));
        assertEquals(0, q.getCoefficient(-1));
    }

    @Test
    void isZero() {
        Polynomial p = new DensePolynomial("0");
        assertTrue(p.isZero());
        Polynomial q = new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        assertFalse(q.isZero());
    }

    @Test
    void add() {
        Polynomial p = new DensePolynomial("0");
        assertThrows(NullPointerException.class, () -> p.add(null));
        Polynomial q = new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        assertEquals(q, p.add(q));
        Polynomial s = new DensePolynomial("x^2147483");
        Polynomial t = new DensePolynomial("x^2147483 + x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        assertEquals(t, q.add(s));
        Polynomial v = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x - 100");
        Polynomial w = new DensePolynomial("2x^50 + 6x^40 + 2x^35 + 130x^10 + 150x");
        assertEquals(w, q.add(v));
        Polynomial x = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x - 100 + x^-1");
        assertThrows(IllegalArgumentException.class, () -> w.add(x));
        Polynomial r = new DensePolynomial("2");
        assertEquals(r, p.add(r));
    }

    @Test
    void multiply() {
        Polynomial p = new DensePolynomial("0");
        Polynomial q = new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        Polynomial s = new DensePolynomial("2x^10 + 5x^7");
        Polynomial t = new SparsePolynomial("x^-2");
        Polynomial x = new DensePolynomial("2x^8 + 5x^5");
        Polynomial y = new DensePolynomial("x^2");
        Polynomial z = new DensePolynomial("1");
        assertEquals(p, p.multiply(q));
        assertThrows(NullPointerException.class, () -> p.multiply(null));
        assertEquals(x, s.multiply(t));
        assertEquals(p, p.multiply(t));
        assertEquals(z, y.multiply(t));
        assertThrows(IllegalArgumentException.class, () -> z.multiply(t));
    }

    @Test
    void subtract() {
        Polynomial p = new DensePolynomial("0");
        Polynomial q = new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        Polynomial x = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x - 100 + x^-1");
        Polynomial s = new DensePolynomial("2x^10 + 5x^7");
        Polynomial z = new DensePolynomial("1");
        Polynomial a = new DensePolynomial("2x^10 + 5x^7 - 1");
        assertThrows(NullPointerException.class, () -> p.multiply(null));
        assertEquals(p, q.subtract(q));
        assertThrows(IllegalArgumentException.class, () -> p.subtract(x));
        assertEquals(a, s.subtract(z));
    }

    @Test
    void minus() {
        Polynomial p = new DensePolynomial("0");
        Polynomial s = new DensePolynomial("2x^10 + 5x^7");
        Polynomial v = new DensePolynomial("-2x^10 - 5x^7");
        assertEquals(p, p.minus());
        assertEquals(v, s.minus());
    }

    @Test
    void wellFormed() {
        Polynomial p = new DensePolynomial("0");
        Polynomial q = new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        Polynomial a = new DensePolynomial("2x^10 + 5x^7 - 1");
        assertTrue(p.wellFormed());
        assertTrue(q.wellFormed());
        assertTrue(a.wellFormed());
        assertThrows(IllegalArgumentException.class, () -> new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x - 100 + x^-1"));
        assertThrows(IllegalArgumentException.class, () -> new DensePolynomial(" "));
    }

    @Test
    void testEquals() {
        Polynomial q = new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        Polynomial a = new DensePolynomial("2x^10 + 5x^7 - 1");
        Polynomial x = new SparsePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        Polynomial y = new DensePolynomial("x^50 + 3x^40 + x^35 + 65x^10 + 75x + 100");
        assertFalse(q.equals(a));
        assertFalse(q.equals(x));
        assertTrue(q.equals(y));
    }
}
