package agh.ics.oop.map;
import agh.ics.oop.interfaces.IMapElement;
import agh.ics.oop.interfaces.IPositionChangedObserver;
import agh.ics.oop.interfaces.IWorldMap;
import agh.ics.oop.move.Vector2d;
import agh.ics.oop.objects.Animal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



abstract class AbstractWorldMap implements IWorldMap, IPositionChangedObserver {

    public static Map<Vector2d, ArrayList<IMapElement>> elementsOnMap = new HashMap<>();

    public boolean place(Animal animal){
        Vector2d position = animal.getPosition();
        if(canMoveTo(animal.position)){
            elementsOnMap.computeIfAbsent(position, k -> new ArrayList<>()).add(animal);
            animal.addObserver(this);
            return true;
        }
        throw new IllegalArgumentException(position + " is not legal place position");
    }
    public boolean isOccupied(Vector2d position) {
        return objectAt(position)!=null;
    }

    public ArrayList<IMapElement> objectAt(Vector2d position) {
        if(elementsOnMap.get(position)!=null){
            return elementsOnMap.get(position);
        }else if(elementsOnMap.get(position)!=null){
            return elementsOnMap.get(position);
        }
        return null;
    }


    public abstract Vector2d lowerLeft();
    public abstract Vector2d upperRight();
    public String toString(){
        return new MapVisualizer(this).draw(this.lowerLeft(), this.upperRight());
    }



    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {

        if (canMoveTo(newPosition)) {

            ArrayList<IMapElement> iterArray = new ArrayList<>(elementsOnMap.get(oldPosition));
            for(IMapElement element:iterArray){
                if(element.equals(animal)){
                    elementsOnMap.get(oldPosition).remove(element);
                    if(elementsOnMap.get(newPosition)!=null) {
                        elementsOnMap.get(newPosition).add(element);
                    }else{
                        elementsOnMap.computeIfAbsent(newPosition, k -> new ArrayList<>()).add(element);
                    }
                }
            }
        }
    }


    public Map<Vector2d, ArrayList<IMapElement>> showMap(){
        return elementsOnMap;
    }




}