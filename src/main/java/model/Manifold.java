package model;

import model.helpers.Pair;
import model.helpers.Vector2;

/**
 * A Collision class that, when constructed, takes a pair of Entities and determines if it is a collision
 *
 * Help taken for the collisions from:
 * <a href="https://code.tutsplus.com/series/how-to-create-a-custom-physics-engine--gamedev-12715">...</a>
 */

public class Manifold {

    private final Entity entityA;
    private final Entity entityB;
    private Vector2 normal;
    private double depth;
    private boolean isCollision = false;

    // MODIFIES: this
    // EFFECTS: takes a pair and determines if it is a collision
    public Manifold(Pair pair) {

        this.entityA = pair.getEntityA();
        this.entityB = pair.getEntityB();
        this.normal = null;
        this.depth = 0.0;
        handleIsCollision(pair);

    }

    // EFFECTS: checks type of collision and determines whether it is a collision
    private void handleIsCollision(Pair pair) {

        if (pair.getEntityA().getEntityType() == Entity.EntityType.CIRCLE) {
            if (pair.getEntityB().getEntityType() == Entity.EntityType.CIRCLE) {
                this.isCollision = circleIntersectCircle(entityA, entityB); // circle + circle
            } else {
                // circle + box, box is not first parameter
                this.isCollision = boxIntersectCircle(entityB, entityA, false);
            }
        } else {
            if (pair.getEntityB().getEntityType() == Entity.EntityType.CIRCLE) {
                // box + circle, box is first parameter
                this.isCollision = boxIntersectCircle(entityA, entityB, true);
            } else {
                this.isCollision = boxIntersectsBox(entityA, entityB); // box + box
            }
        }

    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    // MODIFIES: this (specifically normal and depth)
    // EFFECTS: determines whether one box intersects another, calculates normal and depth
    @SuppressWarnings("methodlength")
    private boolean boxIntersectsBox(Entity a, Entity b) {

        // vector from A to B
        Vector2 centerA = Vector2.add(a.getPosition(), new Vector2(a.getWidth() / 2, a.getHeight() / 2));
        Vector2 centerB = Vector2.add(b.getPosition(), new Vector2(b.getWidth() / 2, b.getHeight() / 2));
        Vector2 dist = Vector2.subtract(centerB, centerA);

        // calculate the overlap on the x-axis and y-axis
        double overlapX = ((a.getWidth() + b.getWidth()) / 2) - Math.abs(dist.getComponentX());
        double overlapY = ((a.getHeight() + b.getHeight()) / 2) - Math.abs(dist.getComponentY());

        // if both > 0, there is intersection
        if (overlapX > 0 && overlapY > 0) {
            // find the axis of the least penetration
            if (overlapX < overlapY) {
                if (dist.getComponentX() < 0) { //
                    this.normal = new Vector2(-1, 0);
                } else {
                    this.normal = new Vector2(1,0);
                }
                this.depth = overlapX;
            } else {
                if (dist.getComponentY() < 0) {
                    this.normal = new Vector2(0, -1);
                } else {
                    this.normal = new Vector2(0,1);
                }
                this.depth = overlapY;
            }
            return true;
        }

        // all else fails, therefore not intersecting
        return false;
    }

    // MODIFIES: this (specifically normal and depth)
    // EFFECTS: determines if one circle intersects another, calculates normal and depth
    private boolean circleIntersectCircle(Entity a, Entity b) {

        // distance from one circle to another
        Vector2 dist = Vector2.subtract(Vector2.add(b.getPosition(), new Vector2(b.getRadius(), b.getRadius())),
                Vector2.add(a.getPosition(), new Vector2(a.getRadius(), a.getRadius())));
        double radii = a.getRadius() + b.getRadius();

        // test intersection, false if not intersecting
        if (Vector2.lengthSquared(dist) >= (radii * radii)) {
            return false;
        }

        double distance = Vector2.distance(a.getPosition(), b.getPosition());

        // normal vector is pointing from posA to posB
        this.normal = Vector2.normalize(dist);
        this.normal = Vector2.scale(this.normal, 1);
        // depth = how much we need to move apart
        this.depth = radii - distance;

        return true;
    }

    // MODIFIES: this (specifically normal and depth)
    // EFFECTS: determines whether a circle intersects a box, calculates normal and depth
    @SuppressWarnings("methodlength")
    private boolean boxIntersectCircle(Entity box, Entity circle, boolean isBoxFirst) {
        // vector from box to circle
        Vector2 boxCenter = new Vector2(box.getPosition().getComponentX() + (box.getWidth() / 2),
                box.getPosition().getComponentY() + (box.getHeight() / 2));
        Vector2 circleCenter = new Vector2(circle.getPosition().getComponentX() + circle.getRadius(),
                circle.getPosition().getComponentY() + circle.getRadius());
        Vector2 dist = Vector2.subtract(circleCenter, boxCenter);

        // closest point on box to center of circle
        Vector2 clamped = new Vector2(dist.getComponentX(), dist.getComponentY());

        // clamp the point of circle to the edges of box
        clamped.setComponentX(Math.max(-(box.getWidth() / 2),
                Math.min((box.getWidth() / 2), clamped.getComponentX())));
        clamped.setComponentY(Math.max(-(box.getHeight() / 2),
                Math.min((box.getHeight() / 2), clamped.getComponentY())));

        Vector2 closest = Vector2.add(boxCenter, clamped);
        Vector2 difference = Vector2.subtract(closest, circleCenter);
        double length = Vector2.length(difference);

        // tests intersection, if false then not intersecting
        if (length >= circle.getRadius()) {
            this.normal = null;
            return false;
        }

        // if box is first parameter, reverse the normal vector
        if (isBoxFirst) {
            this.normal = Vector2.scale(Vector2.normalize(difference), -1);
        } else {
            this.normal = Vector2.normalize(difference);
        }
        this.depth = circle.getRadius() - length;

        return true;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    public Entity getEntityA() {
        return entityA;
    }

    public Entity getEntityB() {
        return entityB;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public double getDepth() {
        return depth;
    }

    public boolean isCollision() {
        return isCollision;
    }
}
