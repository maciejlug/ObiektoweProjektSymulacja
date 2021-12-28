package agh.ics.oop.objects;

import agh.ics.oop.interfaces.IMapElement;
import agh.ics.oop.move.Vector2d;

public class Grass implements IMapElement {

    public Vector2d position;

    public Grass(Vector2d position){
        this.position = position;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    @Override
    public String toString(){
        return "*";
    }



    public boolean isMovable() {
        return false;
    }


    public String getImagePath() {
        return "src/main/resources/grass.png";
    }

    @Override
    public Animal getAnimal() {
        return null;
    }

    @Override
    public double getEnergy() {
        return 0;
    }


}
