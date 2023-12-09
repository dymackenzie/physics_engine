package model;

import model.helpers.Pair;
import model.helpers.Vector2;
import model.logs.Event;
import model.logs.EventLog;

import java.util.*;

/**
 * The main engine that loops and processes all the computations necessary
 * to simulate a physics environment.
 *
 * Help taken for the collisions from:
 * <a href="https://code.tutsplus.com/series/how-to-create-a-custom-physics-engine--gamedev-12715">...</a>
 */

public class PhysicsEngine {

    public static final double DT = 1.0 / 60.0;
    public static final double ITERATIONS = 10; // # of solutions for collisions
    public static final double PENETRATION_ALLOWANCE = 0.05;
    public static final double PENETRATION_CORRECTION = 0.4;
    public static final Vector2 GRAVITY = new Vector2(0.0, 200);

    private ArrayList<Entity> entities;

    // MODIFIES: this
    // EFFECTS: constructs and initializes engine
    public PhysicsEngine(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    // MODIFIES: this
    // EFFECTS: adds specified Entity of EntityType to entities
    public void addEntity(Entity.EntityType entityType, int x, int y) {
        Random random = new Random();
        EventLog el = EventLog.getInstance();
        if (entityType == Entity.EntityType.BOX) {
            double width = 20 + random.nextInt(100);
            double height = 20 + random.nextInt(100);
            getEntities().add(0, Entity.createBox(new Vector2(x - (width / 2),
                                    y - (height / 2)),
                            0.5,
                            1,
                            width,
                            height));
            el.logEvent(new Event("Box added."));
        }
        if (entityType == Entity.EntityType.CIRCLE) {
            double radius = 10 + random.nextInt(50);
            getEntities().add(0, Entity.createCircle(new Vector2(x - (radius),
                                    y - (radius)),
                            0.5,
                            1,
                            radius));
            el.logEvent(new Event("Circle added."));
        }
    }

    // MODIFIES: this
    // EFFECTS: removes all Entities in entities
    public void resetEntities() {
        // only keeps the static entities
        ArrayList<Entity> staticEntity = new ArrayList<>();
        for (Entity entity : getEntities()) {
            if (entity.isStatic()) {
                staticEntity.add(entity);
            }
        }
        setEntities(staticEntity);
    }

    // MODIFIES: entity, this
    // EFFECTS: updates physics through forces, collisions, velocities, and positions
    public void updatePhysics() {

        // apply forces
        for (Entity entity : entities) {
            applyForces(entity);
        }

        // generate the list of collisions
        List<Manifold> manifolds = generateCollisions(generatePairs());

        // resolve collisions * iterations
        for (int i = 0; i < ITERATIONS; ++i) {
            for (Manifold manifold : manifolds) {
                collisionResolution(manifold);
            }
        }

        // integrate velocities
        for (Entity entity : entities) {
            applyVelocity(entity);
        }

        // correct positions
        for (Manifold manifold : manifolds) {
            positionalCorrection(manifold);
        }

        // clear all forces
        for (Entity entity : entities) {
            entity.getForce().setComponentX(0);
            entity.getForce().setComponentY(0);
        }

    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    // EFFECTS: creates a list of pairs of possible collisions without duplicates
    private List<Pair> generatePairs() {

        List<Pair> pairs = new ArrayList<>();

        // O(n^2) loop to pair each object together
        for (Entity x : entities) {
            for (Entity y : entities) {

                // skip if checks with self
                if (x.equals(y)) {
                    continue;
                }

                pairs.add(new Pair(x, y));

            }
        }

        // removes duplicates
        Set<Pair> pairSet = new HashSet<>(pairs);

        return new ArrayList<>(pairSet);

    }

    // EFFECTS: creates Collision objects out of pairs if pair is a collision
    private List<Manifold> generateCollisions(List<Pair> pairs) {
        List<Manifold> manifolds = new ArrayList<>();

        for (Pair pair : pairs) {
            Manifold manifold = new Manifold(pair);
            if (manifold.isCollision()) {
                manifolds.add(manifold);
            }
        }

        return manifolds;
    }

    // MODIFIES: entity
    // EFFECTS: applies forces to entity, changing the velocity
    private void applyForces(Entity entity) {
        /*
        v += (1/m * F) * dt
         */
        if (entity.isStatic()) {
            return;
        }
        entity.setVelocity(Vector2.add(entity.getVelocity(),
                Vector2.scale(entity.getForce(), (DT * 0.5 * entity.getInvMass()))));
        entity.setVelocity(Vector2.add(entity.getVelocity(), Vector2.scale(GRAVITY, DT * 0.5)));
    }

    // MODIFIES: entity
    // EFFECTS: applies velocities to entity, changing the position
    private void applyVelocity(Entity entity) {
        /*
        x += v * dt
         */
        if (entity.isStatic()) {
            return;
        }
        entity.setPosition(Vector2.add(entity.getPosition(), Vector2.scale(entity.getVelocity(), DT)));
    }

    // MODIFIES: manifold, entity
    // EFFECTS: resolves collision and applies new position, velocity, and acceleration
    private void collisionResolution(Manifold manifold)  {

        // sets variables from Collision object
        Entity a = manifold.getEntityA();
        Entity b = manifold.getEntityB();
        Vector2 normal = manifold.getNormal();

        // determine relative velocity
        Vector2 relativeVelocity = Vector2.subtract(b.getVelocity(), a.getVelocity());

        // then calculates the velocity along normal vector
        double velocityAlongNormal = Vector2.dotProduct(relativeVelocity, normal);

        // if velocities are separating, do not resolve
        if (velocityAlongNormal > 0) {
            return;
        }

        // calculate the restitution
        double restitution = Math.min(a.getRestitution(), b.getRestitution());

        // if object is resting
        //if (Vector2.lengthSquared(relativeVelocity) < RESTING) {
        //    restitution = 0.0;
        //}

        // calculate the impulse
        // scalar first
        double impulseScalar = (-(1 + restitution) * velocityAlongNormal) / (a.getInvMass() + b.getInvMass());
        // normal * scalar = impulse
        Vector2 impulse = Vector2.scale(normal, impulseScalar);

        // RESOLVES COLLISION
        // subtracts from a velocity by impulse * inverse mass
        a.setVelocity((Vector2.subtract(a.getVelocity(), Vector2.scale(impulse, a.getInvMass()))));
        // adds to b velocity by impulse * inverse mass
        b.setVelocity((Vector2.add(b.getVelocity(), Vector2.scale(impulse, b.getInvMass()))));
    }

    // MODIFIES: manifold, entity
    // EFFECTS: corrects position
    private void positionalCorrection(Manifold manifold) {

        if (manifold.getEntityB().isStatic()) {
            return;
        }

        double correction = StrictMath.max(manifold.getDepth() - PENETRATION_ALLOWANCE, 0.0f)
                / (manifold.getEntityA().getInvMass() + manifold.getEntityB().getInvMass()) * PENETRATION_CORRECTION;

        manifold.getEntityA().setPosition(Vector2.add(manifold.getEntityA().getPosition(),
                Vector2.scale(manifold.getNormal(),-manifold.getEntityA().getInvMass() * correction)));
        manifold.getEntityB().setPosition(Vector2.add(manifold.getEntityB().getPosition(),
                Vector2.scale(manifold.getNormal(),manifold.getEntityA().getInvMass() * correction)));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    // getter
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    // setter
    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

}
