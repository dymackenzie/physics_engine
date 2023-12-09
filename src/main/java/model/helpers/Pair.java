package model.helpers;

import model.Entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 Simple pair class that just stores two entities as a pair.
 */
public class Pair {

    private final Entity entityA;
    private final Entity entityB;

    // MODIFIES: this
    // EFFECTS: creates a pair with two entities
    public Pair(Entity entityA, Entity entityB) {
        this.entityA = entityA;
        this.entityB = entityB;
    }

    // EFFECTS: checks if pair is equal to another pair
    @Override
    public boolean equals(Object object) {
        // deals with edge cases
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        // checks if pair equals pair
        Pair pair = (Pair) object;
        return (entityA.equals(pair.entityA) && entityB.equals(pair.entityB))
                || (entityA.equals(pair.entityB) && entityB.equals(pair.entityA));
    }

    // EFFECTS: returns hash code
    @Override
    public int hashCode() {
        return Objects.hashCode(new HashSet<>(Arrays.asList(entityA, entityB)));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    public Entity getEntityA() {
        return entityA;
    }

    public Entity getEntityB() {
        return entityB;
    }

}
