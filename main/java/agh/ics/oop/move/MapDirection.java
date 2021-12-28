package agh.ics.oop.move;

import java.util.ArrayList;

public enum MapDirection {
    NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;

    public String toString() {
        switch (this) {
            case EAST:
                return "Wschód";
            case NORTHEAST:
                return "Północny zachód";
            case WEST:
                return "Zachód";
            case NORTHWEST:
                return "Północny wschód";
            case NORTH:
                return "Północ";
            case SOUTH:
                return "Południe";
            case SOUTHEAST:
                return "Południowy wschód";
            case SOUTHWEST:
                return "Południowy zachód";
            default:
                return null;
        }
    }



    public Vector2d toUnitVector() {
        switch (this) {
            case EAST:
                return new Vector2d(1, 0);
            case WEST:
                return new Vector2d(-1, 0);
            case NORTH:
                return new Vector2d(0, 1);
            case SOUTH:
                return new Vector2d(0, -1);
            case NORTHEAST:
                return new Vector2d(1, 1);
            case SOUTHEAST:
                return new Vector2d(1, -1);
            case SOUTHWEST:
                return new Vector2d(-1, -1);
            case NORTHWEST:
                return new Vector2d(-1, 1);
            default:
                return new Vector2d(0, 0);
        }
    }
}
