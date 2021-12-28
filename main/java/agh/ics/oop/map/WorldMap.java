package agh.ics.oop.map;
import agh.ics.oop.gui.App;
import agh.ics.oop.interfaces.IPositionChangedObserver;

import agh.ics.oop.move.MapDirection;
import agh.ics.oop.move.Vector2d;
import agh.ics.oop.objects.Animal;
import agh.ics.oop.objects.Grass;

import java.util.*;

public class WorldMap extends AbstractWorldMap {
    public int mapWidth;
    public int mapHeight;
    public Vector2d lowerLeft;
    public Vector2d upperRight;
    public Vector2d randomPosition;
    public MapDirection randomDirection;

    public WorldMap(int mapWidth,int mapHeight, int animalsCount, int startEnergy, int moveEnergy, int plantEnergy) {

        this.lowerLeft = new Vector2d(0, 0);
        int xyInt = (int) Math.sqrt(mapWidth * mapHeight);
        this.upperRight = new Vector2d(mapWidth-1, mapHeight-1);
        int grassCount = 0;
        int animalCount = 0;
        Random rand = new Random();

        while (animalCount < animalsCount) {
            randomPosition = new Vector2d(rand.nextInt(xyInt), rand.nextInt(xyInt));
            randomDirection = MapDirection.values()[new Random().nextInt(MapDirection.values().length)];
            if (objectAt(randomPosition) == null) {
                Animal newAnimal = new Animal(this,randomPosition,randomDirection, startEnergy, moveEnergy, plantEnergy);
                this.place(newAnimal);
                animalCount += 1;
            }
        }

        while (grassCount < (int) (App.returnMapHeight()*App.returnMapHeight())/5) {
            randomPosition = new Vector2d(rand.nextInt(xyInt), rand.nextInt(xyInt));
            if (objectAt(randomPosition) == null) {
                Grass newGrass = new Grass(randomPosition);
                elementsOnMap.computeIfAbsent(randomPosition, k -> new ArrayList<>()).add(newGrass);
                grassCount += 1;
            }
        }
    }


    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRight) & position.follows(lowerLeft);
    }




    public Vector2d lowerLeft(){
        return this.lowerLeft;
      // return new Vector2d(mapBoundry.xList.firstKey(),mapBoundry.yList.firstKey());
    }
    public Vector2d upperRight(){
         return this.upperRight;
       // return new Vector2d(mapBoundry.xList.lastKey(),mapBoundry.yList.lastKey());
    }





}