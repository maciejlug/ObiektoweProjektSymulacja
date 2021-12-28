package agh.ics.oop;

import agh.ics.oop.gui.App;

import agh.ics.oop.interfaces.IEngine;
import agh.ics.oop.interfaces.IWorldMap;
//import agh.ics.oop.map.MapBoundry;
import agh.ics.oop.map.WorldMap;

import agh.ics.oop.move.SimulationEngine;
import agh.ics.oop.move.Vector2d;
import javafx.application.Application;

import java.util.Arrays;

public class World {
    public static void main(String[] args) {

        try {
            Application.launch(App.class, args);

        } catch(IllegalArgumentException ex) {
            System.out.println("Something went wrong."+ex);
        }
    }
}
