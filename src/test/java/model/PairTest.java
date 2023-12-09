package model;

import model.helpers.Pair;
import model.helpers.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PairTest {

    Pair pairA;
    Pair pairB;
    Pair pairBothA1;
    Pair pairBothA2;
    Pair pairBothB;
    Pair pairNull;
    Entity a;
    Entity b;

    @BeforeEach
    void runBefore() {
        a = Entity.createBox(new Vector2(), 1, 1, 1, 1);
        b = Entity.createBox(new Vector2(), 1, 1, 2, 2);
        pairA = new Pair(a, a);
        pairB = new Pair(b, b);
        pairBothA1 = new Pair(a, b);
        pairBothA2 = new Pair(a, b);
        pairBothB = new Pair(b, a);
        pairNull = null;
    }

    @Test
    void testConstructor() {
        assertEquals(pairA.getEntityA(), a);
        assertEquals(pairA.getEntityB(), a);
        assertEquals(pairBothA1.getEntityA(), a);
        assertEquals(pairBothA1.getEntityB(), b);
    }

    @Test
    void testEquals() {
        assertNotEquals(pairA, pairNull);
        assertNotEquals(pairA, a);
        assertNotEquals(null, pairA);
        assertNotEquals(pairA, pairB);
        assertNotEquals(pairB, pairA);
        assertNotEquals(pairBothA1, pairB);
        assertNotEquals(pairB, pairBothA1);
        assertNotEquals(pairBothA1, pairA);
        assertNotEquals(pairA, pairBothA1);

        assertEquals(pairA, pairA);
        assertEquals(pairBothA1, pairBothB);
        assertEquals(pairBothB, pairBothA1);
    }

    @Test
    void testHashCode() {
        assertEquals(pairBothA1, pairBothA2);
        assertEquals(pairBothA1.hashCode(), pairBothA2.hashCode());
    }

}
