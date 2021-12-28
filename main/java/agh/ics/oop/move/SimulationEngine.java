package agh.ics.oop.move;

import agh.ics.oop.gui.App;
import agh.ics.oop.interfaces.IEngine;

import agh.ics.oop.interfaces.IMapElement;
import agh.ics.oop.interfaces.IWorldMap;
import agh.ics.oop.objects.Animal;
import agh.ics.oop.objects.Grass;
import javafx.application.Platform;

import java.util.*;


public class SimulationEngine implements IEngine, Runnable {
    public IWorldMap map;
    public int day;
    public int interval = 500;
    public int animalsNumber;
    public int grassCount;
    public int grassToCreate = (int) Math.sqrt(App.returnMapHeight()*App.returnMapWidth())/10 + 1;

    public SimulationEngine(IWorldMap map) {
        this.map = map;
    }

    public void moveAnimals() {
        Map<Vector2d, ArrayList<IMapElement>> hashMapIter = new HashMap<>(map.showMap());
        Set<Vector2d> animalKeys = hashMapIter.keySet();
        animalsNumber = 0;
        animalKeys.forEach(key -> {
            for (int i = 0; i < hashMapIter.get(key).size(); i++) {
                if (hashMapIter.get(key).get(i) instanceof Animal) {
                    ((Animal) map.showMap().get(key).get(i)).move();
                    animalsNumber += 1;
                }
            }

        });
    }

    public void removeDeadAnimals() {
        Map<Vector2d, ArrayList<IMapElement>> hashMapIter = new HashMap<>(map.showMap());
        Set<Vector2d> animalKeys = hashMapIter.keySet();
        animalKeys.forEach(key -> {
            for (int i = 0; i < hashMapIter.get(key).size(); i++) {
                if (hashMapIter.get(key).get(i) instanceof Animal) {
                    ((Animal) map.showMap().get(key).get(i)).checkIfDead();
                    if (((Animal) map.showMap().get(key).get(i)).isDead) {
                        map.showMap().get(key).remove(hashMapIter.get(key).get(i));
                    }
                }
            }
            if(hashMapIter.get(key).size()==0){
                map.showMap().remove(key);
            }
        });
    }

    public void eatFood() {
        grassCount=0;
        Map<Vector2d, ArrayList<IMapElement>> hashMapIter = new HashMap<>(map.showMap());
        Set<Vector2d> animalKeys = hashMapIter.keySet();
        animalKeys.forEach(key -> {
            ArrayList<IMapElement> animals = new ArrayList<>();
            IMapElement grass = null;
            for (int i = 0; i < hashMapIter.get(key).size(); i++) {
                if (map.showMap().get(key).get(i) instanceof Animal) {
                    animals.add(map.showMap().get(key).get(i));
                }
                if (map.showMap().get(key).get(i) instanceof Grass) {
                    grass = map.showMap().get(key).get(i);
                    grassCount+=1;
                }
            }
            if (animals.size() > 0 && grass != null) {
                map.showMap().get(key).remove(grass);
                animals.sort((a, b) -> (int) (b.getEnergy() - a.getEnergy()));
                int biggestEnergy = (int) animals.get(0).getEnergy();
                int animalsWithSameEnergy = 0;
                for (IMapElement animal : animals) {
                    animalsWithSameEnergy +=1;
                    if(animal.getEnergy()!=biggestEnergy){
                        break;
                    }
                }
                for(int i=0;i<animalsWithSameEnergy;i++){
                    animals.get(i).getAnimal().energy += animals.get(i).getAnimal().plantEnergy / animals.size();
                }

            }
        });
    }

    public void copulate() {
        Map<Vector2d, ArrayList<IMapElement>> hashMapIter = new HashMap<>(map.showMap());
        Set<Vector2d> animalKeys = hashMapIter.keySet();
        animalKeys.forEach(key -> {
            ArrayList<IMapElement> animals = new ArrayList<>();
            for (int i = 0; i < hashMapIter.get(key).size(); i++) {
                if (map.showMap().get(key).get(i) instanceof Animal) {
                    animals.add(map.showMap().get(key).get(i));
                }
            }
            if (animals.size() >= 2) {
                animals.sort((a, b) -> (int) (b.getEnergy() - a.getEnergy()));
                Animal parent1 = animals.get(0).getAnimal();
                Animal parent2 = animals.get(1).getAnimal();
                if(parent1.energy >= App.returnStartEnergy()/2 && parent2.energy >= App.returnStartEnergy()/2){
                    ArrayList<Integer> parent75 = parent1.getAnimal().genesList;
                    ArrayList<Integer> parent25 = parent2.getAnimal().genesList;
                    ArrayList<Integer> child = new ArrayList<>();
                    for (int i = 0; i < parent75.size(); i++) {
                        if (i < 24) {
                            child.add(parent75.get(i));
                        } else {
                            child.add(parent25.get(i));
                        }
                    }
                    MapDirection randomDirection = MapDirection.values()[new Random().nextInt(MapDirection.values().length)];
                    double childEnergy = parent1.energy * 0.25 + parent2.energy * 0.25;
                    Animal newAnimal = new Animal(map, key, randomDirection, childEnergy, parent1.moveEnergy, parent1.plantEnergy, child);
                    map.place(newAnimal);

                }
            }
        });
    }

    public void createNewGrass(){
        int i = 0;
        int emptyTiles =  App.returnMapHeight()*App.returnMapWidth() - grassCount - animalsNumber;
        while(grassToCreate>i && emptyTiles>0) {
            int maxH = App.returnMapHeight();
            int maxW = App.returnMapWidth();
            int randX = (int) Math.round((Math.random() * (maxW)));
            int randY = (int) Math.round((Math.random() * (maxH-1)));
            Vector2d randomVector = new Vector2d(randX,randY);
            if(map.showMap().get(randomVector)==null){
                Grass newGrass = new Grass(randomVector);
                map.showMap().computeIfAbsent(randomVector, k -> new ArrayList<>()).add(newGrass);
                i+=1;
            }

        }
    }


    public void run() {
            System.out.println("Day:" + day);
            System.out.println("Grass count:" + grassCount);
            System.out.println("Animal count:" + animalsNumber);

            if(animalsNumber==0){
                System.out.println("Koniec");
            }
        if (animalsNumber > 0 || day==0 ) {
            Platform.runLater(() -> {
                day += 1;


                try {
                    Thread.sleep(interval);
                    removeDeadAnimals();
                    moveAnimals();
                    eatFood();
                    copulate();
                    createNewGrass();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    App.clearScene();
                    App.drawGrid();
                    App.runAgain();
                    App.renderItems();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}




