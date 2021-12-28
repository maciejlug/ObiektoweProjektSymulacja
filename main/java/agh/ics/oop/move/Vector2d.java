package agh.ics.oop.move;
import java.util.Objects;

public class Vector2d {
    public final int x, y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean precedes(Vector2d vector) {
        return vector.x >= this.x && vector.y >= this.y;
    }

    public boolean follows(Vector2d vector) {
        return vector.x <= this.x && vector.y <= this.y;
    }

    public Vector2d upperRight(Vector2d vector) {
        return new Vector2d(Math.max(this.x, vector.x), Math.max(this.y, vector.y));
    }

    public Vector2d lowerLeft(Vector2d vector) {
        return new Vector2d(Math.min(this.x, vector.x), Math.min(this.y, vector.y));
    }

    public Vector2d add(Vector2d vector) {
        return new Vector2d(this.x + vector.x, this.y + vector.y);
    }

    public Vector2d subtract(Vector2d vector) {
        return new Vector2d(this.x - vector.x, this.y - vector.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) obj;
        return that.x == this.x && that.y == this.y;

    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}