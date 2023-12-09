package model;

import model.helpers.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EntityTest {

    Entity circle;
    Entity box;

    @BeforeEach
    void runBefore() {
        circle = Entity.createCircle(new Vector2(1, 1), 1, 10, 5);
        box = Entity.createBox(new Vector2(2, 2), 1, 1, 5, 5);
    }

    @Test
    void testCreateCircle() {
        Vector2 vel = circle.getVelocity();
        Vector2 force = circle.getForce();
        Vector2 pos = circle.getPosition();
        assertTrue(Vector2.equals(pos, new Vector2(1, 1)));
        assertTrue(Vector2.equals(vel, new Vector2()));
        assertTrue(Vector2.equals(force, new Vector2()));
        assertEquals(circle.getRestitution(), 1);
        assertEquals(circle.getMass(), 10);
        assertEquals(circle.getInvMass(), 1.0/10);
        assertEquals(circle.getRadius(), 5);
        assertEquals(circle.getWidth(), 0);
        assertEquals(circle.getHeight(), 0);
        assertEquals(circle.getEntityType(), Entity.EntityType.CIRCLE);
    }

    @Test
    void testCreateBox() {
        Vector2 vel = box.getVelocity();
        Vector2 force = box.getForce();
        Vector2 pos = box.getPosition();
        assertTrue(Vector2.equals(pos, new Vector2(2, 2)));
        assertTrue(Vector2.equals(vel, new Vector2()));
        assertTrue(Vector2.equals(force, new Vector2()));
        assertEquals(box.getRestitution(), 1);
        assertEquals(box.getMass(), 1);
        assertEquals(box.getInvMass(), 1.0);
        assertEquals(box.getRadius(), 0);
        assertEquals(box.getWidth(), 5);
        assertEquals(box.getHeight(), 5);
        assertEquals(box.getEntityType(), Entity.EntityType.BOX);
    }

    @Test
    void testSetStatic() {
        circle.setStatic();
        box.setStatic();
        assertEquals(circle.getMass(), 0.0);
        assertEquals(circle.getInvMass(), 0.0);
        assertEquals(box.getMass(), 0.0);
        assertEquals(box.getInvMass(), 0.0);
    }

}
