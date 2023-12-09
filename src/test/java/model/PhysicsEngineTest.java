package model;

import model.helpers.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PhysicsEngineTest {

    ArrayList<Entity> entities;
    PhysicsEngine physicsEngine;
    Entity boxA, circleA, circleB, staticBox, boxAway, boxAway2;

    @BeforeEach
    void runBefore() {
        entities = new ArrayList<>();

        circleA = Entity.createCircle(new Vector2(), 1, 1, 2);
        circleB = Entity.createCircle(new Vector2(6, 0), 1, 1, 2);
        boxA = Entity.createBox(new Vector2(10, 0), 1, 1, 2, 2);
        staticBox = Entity.createBox(new Vector2(0, 1.9), 1, 1, 2, 2);
        boxAway = Entity.createBox(new Vector2(100, 0), 1, 1, 2, 2);
        boxAway.setVelocity(new Vector2(0, -100));
        boxAway2 = Entity.createBox(new Vector2(100, 1), 1, 1, 2, 2);
        boxAway2.setVelocity(new Vector2(0, 100));

        entities.add(circleA);
        entities.add(circleB);
        entities.add(boxA);
        entities.add(boxAway);
        entities.add(boxAway2);
        staticBox.setStatic();
        entities.add(staticBox);

        physicsEngine = new PhysicsEngine(entities);
    }

    @Test
    void testConstructor() {
        physicsEngine.setEntities(entities);
        assertEquals(physicsEngine.getEntities(), entities);
    }

    @Test
    void testAddEntity() {
        physicsEngine.addEntity(Entity.EntityType.CIRCLE, 0, 1);
        Entity addedEntity = physicsEngine.getEntities().get(0);
        assertEquals(addedEntity.getEntityType(), Entity.EntityType.CIRCLE);

        physicsEngine.addEntity(Entity.EntityType.BOX, 1, 0);
        addedEntity = physicsEngine.getEntities().get(0);
        assertEquals(addedEntity.getEntityType(), Entity.EntityType.BOX);
    }

    @Test
    void testResetEntities() {
        physicsEngine.resetEntities();
        assertEquals(physicsEngine.getEntities().size(), 1); // due to one static box
        assertEquals(physicsEngine.getEntities().get(0), staticBox);
    }

    @Test
    void testUpdatePhysicsOnce() {

        physicsEngine.updatePhysics();

        Entity c1 = physicsEngine.getEntities().get(0);
        Entity c2 = physicsEngine.getEntities().get(1);
        Entity b = physicsEngine.getEntities().get(2);
        Entity sb = physicsEngine.getEntities().get(3);

        assertTrue(Vector2.equals(c1.getForce(), new Vector2()));
        assertTrue(Vector2.equals(c2.getForce(), new Vector2()));
        assertTrue(Vector2.equals(b.getForce(), new Vector2()));
        assertTrue(Vector2.equals(sb.getForce(), new Vector2()));

        assertEquals(c1.getVelocity().getComponentY(), 0 + (PhysicsEngine.GRAVITY.getComponentY() * PhysicsEngine.DT * 0.5));
        assertEquals(c2.getVelocity().getComponentY(), 0 + (PhysicsEngine.GRAVITY.getComponentY() * PhysicsEngine.DT * 0.5));
        assertEquals(b.getVelocity().getComponentY(), 0 + (PhysicsEngine.GRAVITY.getComponentY() * PhysicsEngine.DT * 0.5));

        assertEquals(c1.getVelocity().getComponentX(), 0);
        assertEquals(c2.getVelocity().getComponentX(), 0);
        assertEquals(b.getVelocity().getComponentX(), 0);

        assertFalse(Vector2.equals(sb.getVelocity(), new Vector2()));

        assertEquals(c1.getPosition().getComponentY(), 0 + (c1.getVelocity().getComponentY() * PhysicsEngine.DT));
        assertEquals(c2.getPosition().getComponentY(), 0 + (c2.getVelocity().getComponentY() * PhysicsEngine.DT));
        assertEquals(b.getPosition().getComponentY(), 0 + (b.getVelocity().getComponentY() * PhysicsEngine.DT));

        assertEquals(c1.getPosition().getComponentX(), 0);
        assertEquals(c2.getPosition().getComponentX(), 6.0);
        assertEquals(b.getPosition().getComponentX(), 10);

        assertFalse(Vector2.equals(sb.getPosition(), new Vector2()));
    }

    @Test
    void testUpdatePhysicsTwice() {
        physicsEngine.updatePhysics();

        Entity c1 = physicsEngine.getEntities().get(0);
        Entity c2 = physicsEngine.getEntities().get(1);
        Entity b = physicsEngine.getEntities().get(2);
        Entity sb = physicsEngine.getEntities().get(3);

        double vel1 = c1.getVelocity().getComponentY();
        double pos1 = c1.getPosition().getComponentY();
        double vel2 = c2.getVelocity().getComponentY();
        double pos2 = c2.getPosition().getComponentY();
        double vel3 = b.getVelocity().getComponentY();
        double pos3 = b.getPosition().getComponentY();

        physicsEngine.updatePhysics();

        assertTrue(Vector2.equals(c1.getForce(), new Vector2()));
        assertTrue(Vector2.equals(c2.getForce(), new Vector2()));
        assertTrue(Vector2.equals(b.getForce(), new Vector2()));
        assertTrue(Vector2.equals(sb.getForce(), new Vector2()));

        assertEquals(c1.getVelocity().getComponentY(), vel1 + (PhysicsEngine.GRAVITY.getComponentY() * PhysicsEngine.DT * 0.5));
        assertEquals(c2.getVelocity().getComponentY(), vel2 + (PhysicsEngine.GRAVITY.getComponentY() * PhysicsEngine.DT * 0.5));
        assertEquals(b.getVelocity().getComponentY(), vel3 + (PhysicsEngine.GRAVITY.getComponentY() * PhysicsEngine.DT * 0.5));

        assertEquals(c1.getVelocity().getComponentX(), 0);
        assertEquals(c2.getVelocity().getComponentX(), 0);
        assertEquals(b.getVelocity().getComponentX(), 0);

        assertFalse(Vector2.equals(sb.getVelocity(), new Vector2()));

        assertEquals(c1.getPosition().getComponentY(), pos1 + (c1.getVelocity().getComponentY() * PhysicsEngine.DT));
        assertEquals(c2.getPosition().getComponentY(), pos2 + (c2.getVelocity().getComponentY() * PhysicsEngine.DT));
        assertEquals(b.getPosition().getComponentY(), pos3 + (b.getVelocity().getComponentY() * PhysicsEngine.DT));

        assertEquals(c1.getPosition().getComponentX(), 0);
        assertEquals(c2.getPosition().getComponentX(), 6.0);
        assertEquals(b.getPosition().getComponentX(), 10);

        assertFalse(Vector2.equals(sb.getPosition(), new Vector2()));
    }


}
