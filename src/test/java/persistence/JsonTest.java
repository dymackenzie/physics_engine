package persistence;

import model.Entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkEntity(double positionX, double positionY,
                               double velocityX, double velocityY,
                               double forceX, double forceY,
                               double restitution, double mass,
                               double radius, double width, double height,
                               String entityType, Entity entity) {
        assertEquals(positionX, entity.getPosition().getComponentX());
        assertEquals(positionY, entity.getPosition().getComponentY());
        assertEquals(velocityX, entity.getVelocity().getComponentX());
        assertEquals(velocityY, entity.getVelocity().getComponentY());
        assertEquals(forceX, entity.getForce().getComponentX());
        assertEquals(forceY, entity.getForce().getComponentY());
        assertEquals(restitution, entity.getRestitution());
        assertEquals(mass, entity.getMass());
        assertEquals(radius, entity.getRadius());
        assertEquals(width, entity.getWidth());
        assertEquals(height, entity.getHeight());
        assertEquals(entityType, entity.getEntityType().name());
    }

}
