package model.helpers;

/**
 A Vector class that represents a vector and involves many useful functions pertaining to vectors
 */
public class Vector2 {

    private double componentX;
    private double componentY;

    // MODIFIES: this
    // EFFECTS: constructs a 0,0 vector
    public Vector2() {
        this.componentX = 0.0;
        this.componentY = 0.0;
    }

    // MODIFIES: this
    // EFFECTS: constructs a vector
    public Vector2(double componentX, double componentY) {
        this.componentX = componentX;
        this.componentY = componentY;
    }

    // ~~~~~~~~~~~~~ USEFUL FUNCTIONS ~~~~~~~~~~~~~~ //

    // EFFECTS: determines if both vectors are equal
    public static boolean equals(Vector2 v1, Vector2 v2) {
        return v1.componentX == v2.componentX && v1.componentY == v2.componentY;
    }

    // EFFECTS: normalizes the vector
    public static Vector2 normalize(Vector2 v) {
        double length = Math.sqrt((v.componentX * v.componentX) + (v.componentY * v.componentY));

        // avoids dividing by zero
        if (length != 0.0) {
            return new Vector2(v.componentX / length, v.componentY / length);
        }

        return v;
    }

    // EFFECTS: returns the distance between two vectors
    public static double distance(Vector2 v1, Vector2 v2) {
        double d1 = v2.componentX - v1.componentX;
        double d2 = v2.componentY - v1.componentY;
        return Math.sqrt((d1 * d1) + (d2 * d2));
    }

    // EFFECTS: returns the distance squared between two vectors
    public static double distanceSquared(Vector2 v1, Vector2 v2) {
        double d1 = v2.componentX - v1.componentX;
        double d2 = v2.componentY - v1.componentY;
        return ((d1 * d1) + (d2 * d2));
    }

    // EFFECTS: returns length of vector
    public static double length(Vector2 v) {
        return Math.sqrt((v.componentX * v.componentX) + (v.componentY * v.componentY));
    }

    // EFFECTS: returns length of vector squared
    public static double lengthSquared(Vector2 v) {
        return (v.componentX * v.componentX) + (v.componentY * v.componentY);
    }

    // EFFECTS: adds two vectors
    public static Vector2 add(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.componentX + v2.componentX, v1.componentY + v2.componentY);
    }

    // EFFECTS: subtracts two vectors
    public static Vector2 subtract(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.componentX - v2.componentX, v1.componentY - v2.componentY);
    }

    // EFFECTS: scales the vector by a real
    public static Vector2 scale(Vector2 v, double c) {
        return new Vector2(v.componentX * c, v.componentY * c);
    }

    // EFFECTS: returns dot product of two vectors
    public static double dotProduct(Vector2 v1, Vector2 v2) {
        return (v1.componentX * v2.componentX) + (v1.componentY * v2.componentY);
    }

    // EFFECTS: returns formatted String of Vector2 class
    @Override
    public String toString() {
        return "X: " + this.componentX + ", Y: " + this.componentY;
    }

    public double getComponentX() {
        return componentX;
    }

    public void setComponentX(double componentX) {
        this.componentX = componentX;
    }

    public double getComponentY() {
        return componentY;
    }

    public void setComponentY(double componentY) {
        this.componentY = componentY;
    }
}
