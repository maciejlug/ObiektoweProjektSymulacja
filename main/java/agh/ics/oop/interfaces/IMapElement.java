package agh.ics.oop.interfaces;

import agh.ics.oop.move.Vector2d;
import agh.ics.oop.objects.Animal;

public interface IMapElement {
    /**
     * standard 2D position of element
     * position param should const every class implements IMapElement
     */
    Vector2d getPosition();


    /**
     * Its
     *
     * @return true if elements will use move function to move, else should return false.
     */
    boolean isMovable();


    String getImagePath();


    Animal getAnimal();
    double getEnergy();
}