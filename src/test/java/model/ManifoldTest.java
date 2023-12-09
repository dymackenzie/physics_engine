package model;

import model.helpers.Pair;
import model.helpers.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManifoldTest {

    Entity boxA, boxBa, boxBb, boxBc, boxBd, boxC, circleA, circleB, circleC;
    Entity boxFailX, boxFailY;

    @BeforeEach
    void runBefore() {
        boxA = Entity.createBox(new Vector2(), 1, 1, 2, 2);
        boxBa = Entity.createBox(new Vector2(1, 1), 1, 1, 2, 2);
        boxBb = Entity.createBox(new Vector2(1, -1), 1, 1, 2, 2);
        boxBc = Entity.createBox(new Vector2(1, 0), 1, 1, 2, 2);
        boxBd = Entity.createBox(new Vector2(-1, 0), 1, 1, 2, 2);
        boxC = Entity.createBox(new Vector2(3, 3), 1, 1, 2, 2);

        boxFailX = Entity.createBox(new Vector2(3, 0), 1, 1, 2, 2);
        boxFailY = Entity.createBox(new Vector2(0, 3), 1, 1, 2, 2);

        circleA = Entity.createCircle(new Vector2(), 1, 1, 2);
        circleB = Entity.createCircle(new Vector2(1, 1), 1, 1, 2);
        circleC = Entity.createCircle(new Vector2(3, 3), 1, 1, 2);
    }

    @Test
    void testBoxIntersectsBox() {
        Manifold successA = new Manifold(new Pair(boxA, boxBa));
        Manifold successB = new Manifold(new Pair(boxA, boxBb));
        Manifold successC = new Manifold(new Pair(boxA, boxBc));
        Manifold successD = new Manifold(new Pair(boxA, boxBd));
        Manifold fail = new Manifold(new Pair(boxA, boxC));
        Manifold failX = new Manifold(new Pair(boxA, boxFailX));
        Manifold failY = new Manifold(new Pair(boxA, boxFailY));

        assertTrue(successA.isCollision());
        assertTrue(successB.isCollision());
        assertTrue(successC.isCollision());
        assertTrue(successD.isCollision());
        assertFalse(fail.isCollision());
        assertFalse(failX.isCollision());
        assertFalse(failY.isCollision());

        assertTrue(Vector2.equals(successA.getNormal(), new Vector2(0, 1)));
        assertTrue(Vector2.equals(successB.getNormal(), new Vector2(0, -1)));
        assertTrue(Vector2.equals(successC.getNormal(), new Vector2(1, 0)));
        assertTrue(Vector2.equals(successD.getNormal(), new Vector2(-1, 0)));
        assertNull(fail.getNormal());
        assertNull(failX.getNormal());
        assertNull(failY.getNormal());

        assertEquals(successA.getDepth(), 1);
        assertEquals(successB.getDepth(), 1);
        assertEquals(successC.getDepth(), 1);
        assertEquals(successD.getDepth(), 1);
        assertEquals(fail.getDepth(), 0.0);
        assertEquals(failX.getDepth(), 0.0);
        assertEquals(failY.getDepth(), 0.0);
    }

    @Test
    void testCircleIntersectCircle() {
        Manifold success = new Manifold(new Pair(circleA, circleB));
        Manifold fail = new Manifold(new Pair(circleA, circleC));

        assertTrue(success.isCollision());
        assertFalse(fail.isCollision());

        assertTrue(Vector2.equals(success.getNormal(),
                Vector2.normalize(Vector2.subtract(circleB.getPosition(), circleA.getPosition()))));
        assertNull(fail.getNormal());

        assertEquals(success.getDepth(), 4 - Vector2.distance(circleA.getPosition(), circleB.getPosition()));
        assertEquals(fail.getDepth(), 0.0);
    }

    @Test
    void testBoxIntersectCircle() {

        Entity boxA = Entity.createBox(new Vector2(), 1, 1, 5, 5);
        Entity circleInsideA = Entity.createCircle(new Vector2(1, 2.5), 1, 1, 1);
        Entity circleInsideB = Entity.createCircle(new Vector2(3, 2.5), 1, 1, 1);
        Entity circleInsideC = Entity.createCircle(new Vector2(2.5, 1), 1, 1, 1);
        Entity circleInsideD = Entity.createCircle(new Vector2(2.5, 3), 1, 1, 1);

        Entity circleD = Entity.createCircle(new Vector2(5, 5), 1, 1, 2);

        Manifold successA = new Manifold(new Pair(boxBa, circleA));
        Manifold failA = new Manifold(new Pair(boxBa, circleD));
        Manifold failB = new Manifold(new Pair(circleD, boxBa));

        Manifold successInsideA = new Manifold(new Pair(boxA, circleInsideA));
        Manifold successInsideB = new Manifold(new Pair(boxA, circleInsideB));
        Manifold successInsideC = new Manifold(new Pair(boxA, circleInsideC));
        Manifold successInsideD = new Manifold(new Pair(boxA, circleInsideD));

        assertTrue(successA.isCollision());
        assertFalse(failA.isCollision());
        assertFalse(failB.isCollision());
        assertTrue(successInsideA.isCollision());
        assertTrue(successInsideB.isCollision());
        assertTrue(successInsideC.isCollision());
        assertTrue(successInsideD.isCollision());

        assertTrue(Vector2.equals(successA.getNormal(),
                Vector2.scale(
                        Vector2.normalize(
                                Vector2.subtract(
                                        Vector2.add(circleA.getPosition(), new Vector2(circleA.getRadius(), circleA.getRadius())),
                                        Vector2.add(boxBa.getPosition(), new Vector2(boxBa.getWidth() / 2, boxBa.getHeight() / 2)))),
                        -1)));
        assertNull(failA.getNormal());
        assertNull(failB.getNormal());

        assertEquals(successA.getDepth(),
                2 - Vector2.length(Vector2.subtract(
                        Vector2.add(boxBa.getPosition(), new Vector2(boxBa.getWidth() / 2, boxBa.getHeight() / 2)),
                        Vector2.add(circleA.getPosition(), new Vector2(circleA.getRadius(), circleA.getRadius())))));
        assertEquals(failA.getDepth(), 0.0);
        assertEquals(failB.getDepth(), 0.0);
    }

}
