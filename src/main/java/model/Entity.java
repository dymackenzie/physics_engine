package model;

import model.helpers.Vector2;
import org.json.JSONObject;
import persistence.Writable;


/**
 * An Entity class that represents the physical objects in the engine
 *
 * Credit for toJson method: JsonSerializationDemo
 */
public class Entity implements Writable {

    public enum EntityType {
        CIRCLE,
        BOX
    }

    // physics related stuff
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 force;

    // properties of entity
    private final double restitution;
    private double mass;
    private double invMass;

    // shape of entity
    private final double radius;
    private final double width;
    private final double height;

    // determines what entity is and whether entity is in a fixed position
    private boolean isStatic;
    private final EntityType entityType;

    // MODIFIES: this
    // EFFECTS: constructs an entity
    public Entity(Vector2 position, double restitution, double mass, double radius,
                  double width, double height, EntityType entityType) {
        this.position = position;
        this.velocity = new Vector2();
        this.force = new Vector2();
        this.restitution = restitution;
        this.mass = mass;
        this.invMass = 1 / mass;
        this.radius = radius;
        this.width = width;
        this.height = height;
        this.entityType = entityType;
        this.isStatic = false;
    }

    // REQUIRES: 0 <= restitution <= 1, mass > 0
    // EFFECTS: creates circle
    public static Entity createCircle(Vector2 position, double restitution,
                                      double mass, double r) {
        return new Entity(position, restitution, mass, r, 0, 0, EntityType.CIRCLE);
    }

    // REQUIRES: 0 <= restitution <= 1, mass > 0
    // EFFECTS: creates box
    public static Entity createBox(Vector2 position, double restitution, double mass,
                                   double width, double height) {
        return new Entity(position, restitution, mass, 0, width, height, EntityType.BOX);
    }

    // EFFECTS: makes entity static (not affected by environment)
    public void setStatic() {
        this.isStatic = true;
        this.mass = 0.0;
        this.invMass = 0.0;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("positionX", getPosition().getComponentX());
        json.put("positionY", getPosition().getComponentY());
        json.put("velocityX", getVelocity().getComponentX());
        json.put("velocityY", getVelocity().getComponentY());
        json.put("forceX", getForce().getComponentX());
        json.put("forceY", getForce().getComponentY());

        json.put("restitution", getRestitution());
        json.put("mass", getMass());
        json.put("radius", getRadius());
        json.put("width", getWidth());
        json.put("height", getHeight());
        json.put("entityType", getEntityType().name());

        return json;
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //


    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getForce() {
        return force;
    }

    public void setForce(Vector2 force) {
        this.force = force;
    }

    public double getMass() {
        return mass;
    }

    public double getInvMass() {
        return invMass;
    }

    public double getRestitution() {
        return restitution;
    }

    public double getRadius() {
        return radius;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public EntityType getEntityType() {
        return entityType;
    }

}
