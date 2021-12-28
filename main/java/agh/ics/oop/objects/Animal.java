package agh.ics.oop.objects;
import agh.ics.oop.interfaces.IMapElement;
import agh.ics.oop.interfaces.IPositionChangedObserver;
import agh.ics.oop.interfaces.IWorldMap;
import agh.ics.oop.move.MapDirection;
import agh.ics.oop.move.Vector2d;
import java.util.ArrayList;
import java.util.Random;

public class Animal implements IMapElement {
    public Vector2d position;
    public MapDirection direction;
    public IWorldMap map;
    public ArrayList<IPositionChangedObserver> observers = new ArrayList<>();
    public ArrayList<Integer> genesList;
    public double energy;
    public int moveEnergy;
    public boolean isDead = false;
    public int plantEnergy;
    Random rand = new Random();



    public Animal(IWorldMap map, Vector2d initialPosition, MapDirection direction, double startEnergy, int moveEnergy, int plantEnergy){
        this.map = map;
        this.position = initialPosition;
        this.direction = direction;
        this.genesList = insertGens();
        this.energy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, MapDirection direction, double startEnergy, int moveEnergy, int plantEnergy, ArrayList<Integer>genes){
        this.map = map;
        this.position = initialPosition;
        this.direction = direction;
        this.genesList = genes;
        this.energy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
    }


    public String toString() {

        switch (this.direction){
            case EAST:
                return "E";
            case NORTHEAST:
                return "NE";
            case SOUTHEAST:
                return "SE";
            case WEST:
                return "W";
            case SOUTHWEST:
                return "SW";
            case NORTHWEST:
                return "NW";
            case NORTH:
                return "N";
            case SOUTH:
                return "S";
            default:
                break;
        }
        return null;
    }

    public Vector2d getPosition() {
        return this.position;
    }


    public boolean isMovable() {
        return true;
    }



    public void addObserver(IPositionChangedObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangedObserver observer) {
        this.observers.remove(observer);
    }

    public void move(){
        if(!isDead){
            int random = genesList.get(rand.nextInt(genesList.size()));
            switch (random) {
                case 0:
                    moveFunc(0);
                    this.direction = MapDirection.NORTH;
                    break;
                case 1:
                    this.direction = MapDirection.NORTHEAST;

                    break;
                case 2:
                    this.direction = MapDirection.EAST;
                    break;
                case 3:
                    this.direction = MapDirection.SOUTHEAST;
                    break;
                case 4:
                    moveFunc(4);
                    this.direction = MapDirection.SOUTH;
                    break;
                case 5:
                    this.direction = MapDirection.SOUTHWEST;
                    break;
                case 6:
                    this.direction = MapDirection.WEST;
                    break;
                case 7:
                    this.direction = MapDirection.NORTHWEST;

                    break;
            }
        }

    }

    public void checkIfDead(){
        if((this.energy - this.moveEnergy)>0) {
            this.energy = this.energy - this.moveEnergy;
        }else{
            this.isDead = true;
        }
    }

    public void moveFunc(int move){

        if(move == 0){
            if(map.canMoveTo(this.position.add(this.direction.toUnitVector()))){
                Vector2d oldPosition = this.position;
                Vector2d newPosition = this.position.add(this.direction.toUnitVector());
                this.positionChanged(oldPosition, newPosition, this);
                this.position = newPosition;
            }
        }else{
            if(map.canMoveTo(this.position.subtract(this.direction.toUnitVector()))){
                Vector2d oldPosition = this.position;
                Vector2d newPosition = this.position.subtract(this.direction.toUnitVector());
                this.positionChanged(oldPosition, newPosition, this);
                this.position = newPosition;
            }
        }

    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        for (IPositionChangedObserver observer : this.observers) {
            observer.positionChanged(oldPosition, newPosition, this);

        }
    }


    public String getImagePath() {
        int movementsLeft = (int) this.energy/moveEnergy;
        if(movementsLeft>=10){
            return "src/main/resources/ANIMALhealthy.png";
        }
        if(movementsLeft<10 && movementsLeft>=5){
            return "src/main/resources/ANIMALmedium.png";
        }
        if(movementsLeft<5 && movementsLeft>=0){
            return "src/main/resources/ANIMAL.png";
        }
        return "";
    }


    public ArrayList<Integer> insertGens(){
        ArrayList<Integer> genelist = new ArrayList<>();
        for(int i=0;i<32;i++){
            genelist.add(rand.nextInt(8));
        }
        genelist.sort((a, b) -> a - b);
        return genelist;
    }

    public Animal getAnimal(){
        return this;
    }

    @Override
    public double getEnergy() {
        return this.energy;
    }

}
