package model;

import model.helpers.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class Vector2Test {

    Vector2 zero;
    Vector2 a;
    Vector2 b;
    Vector2 c;

    @BeforeEach
    void runBefore() {
        zero = new Vector2();
        a = new Vector2(0, 2);
        b = new Vector2(2, 0);
        c = new Vector2(2, 2);
    }

    @Test
    void testConstructor() {
        assertEquals(zero.getComponentX(), 0);
        assertEquals(zero.getComponentY(), 0);
        assertEquals(c.getComponentX(), 2);
        assertEquals(c.getComponentY(), 2);
    }

    @Test
    void testEquals() {
        assertFalse(Vector2.equals(a, b));
        assertFalse(Vector2.equals(b, a));
        assertFalse(Vector2.equals(c, a));
        assertFalse(Vector2.equals(c, b));
        assertTrue(Vector2.equals(c, c));
        assertTrue(Vector2.equals(zero, zero));
    }

    @Test
    void testNormalize() {
        Vector2 test = Vector2.normalize(a);
        assertEquals(test.getComponentX(), 0);
        assertEquals(test.getComponentY(), 1);
        test = Vector2.normalize(zero);
        assertEquals(test.getComponentX(), 0);
        assertEquals(test.getComponentY(), 0);
    }

    @Test
    void testDistance() {
        assertEquals(Vector2.distance(a, b), Math.sqrt(8));
    }

    @Test
    void testDistanceSquared() {
        assertEquals(Vector2.distanceSquared(a, b), 8);
    }

    @Test
    void testLength() {
        assertEquals(Vector2.length(a), 2);
        assertEquals(Vector2.length(c), Math.sqrt(8));
    }

    @Test
    void testLengthSquared() {
        assertEquals(Vector2.lengthSquared(a), 4);
        assertEquals(Vector2.lengthSquared(c), 8);
    }

    @Test
    void testAdd() {
        Vector2 test = Vector2.add(a, b);
        assertEquals(test.getComponentX(), 2);
        assertEquals(test.getComponentY(), 2);
    }

    @Test
    void testSubtract() {
        Vector2 test = Vector2.subtract(a, b);
        assertEquals(test.getComponentX(), -2);
        assertEquals(test.getComponentY(), 2);
    }

    @Test
    void testScale() {
        Vector2 test = Vector2.scale(c, -2);
        assertEquals(test.getComponentX(), -4);
        assertEquals(test.getComponentY(), -4);
    }

    @Test
    void testDotProduct() {
        assertEquals(Vector2.dotProduct(a, b), 0);
        assertEquals(Vector2.dotProduct(c, b), 4);
    }

    @Test
    void testToString() {
        assertEquals(c.toString(), "X: 2.0, Y: 2.0");
    }

}
