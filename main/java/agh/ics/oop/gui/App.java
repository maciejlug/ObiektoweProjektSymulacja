package agh.ics.oop.gui;

import agh.ics.oop.interfaces.IMapElement;
import agh.ics.oop.map.WorldMap;

import agh.ics.oop.move.SimulationEngine;
import agh.ics.oop.move.Vector2d;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Set;

public class App extends Application {
    private static Thread engineThread;
    private static SimulationEngine engine;
    private static Stage stage;
    private static WorldMap map;
    private static GridPane gridPane = new GridPane();
    private static int width;
    private static int height;
    private static int cellHeight = 30;
    private static int cellWidth = 30;
    private static int jungleRatio;
    private static ArrayList<Vector2d> jungleGrass = new ArrayList<>();
    private static int startEnergy;
    private static int animalsCount;
    private static int moveEnergy;
    private static int plantEnergy;


    public static int returnMapHeight(){
        return height;
    }

    public static int returnMapWidth(){
        return width;
    }

    public static int returnStartEnergy(){
        return startEnergy;
    }

    public static void checkValues(){
        if(animalsCount > height*width - height*width/5){
            System.out.println("too many animals");
            return;
        }
        if(startEnergy<1){
            System.out.println("too small start energy");
            return;
        }
        if(height<1 || width<1){
            System.out.println("too small width or height");
            return;
        }
        if(moveEnergy<1){
            System.out.println("too small move energy");
            return;
        }
    }

    public static void drawGrid(){
        gridPane.add(new Label("y/x"), 0, 0, 1, 1);
        gridPane.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        for(int i = 0; i < width+1; i++){
            Label yIndex = new Label(String.valueOf(i));
            GridPane.setHalignment(yIndex, HPos.CENTER);
            gridPane.addRow(0, yIndex);
            gridPane.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }

        gridPane.getRowConstraints().add(new RowConstraints(cellHeight));
        for(int i = height-1; i >= 0; i--){
            Label xIndex = new Label(String.valueOf(i));
            GridPane.setHalignment(xIndex, HPos.CENTER);
            gridPane.addColumn(0, xIndex);
            gridPane.getRowConstraints().add(new RowConstraints(cellHeight));
        }
        drawJungle();
    }

    public static void drawJungle(){

        int jungleHeight = (int) height/jungleRatio;
        int jungleWidth = (int) width/jungleRatio;
        int leftStart = (int) (width/2 - jungleWidth/2);
        int rightEnd = (int) width - leftStart;
        int upperStart = (int) (height/2 - jungleHeight/2);
        int upperEnd = (int) height - upperStart;
        for (int i = upperStart+1; i < upperEnd+1; i++) {
            for (int j = leftStart+1; j < rightEnd+1; j++) {
                Rectangle r = new Rectangle(cellWidth, cellHeight, cellWidth, cellHeight);
                Vector2d jungleTile = new Vector2d(j,i);
                jungleGrass.add(jungleTile);
                r.setFill(Color.LIGHTGREEN);
                gridPane.add(r, j, i);
            }
        }
    }

    public static void runAgain(){
        engineThread = new Thread(engine);
        engineThread.setDaemon(true);
        engineThread.start();
    }

    public static void renderItems(){
        Set<Vector2d> animalKeys = map.showMap().keySet();
        animalKeys.forEach(key -> {
            for(IMapElement obj:map.showMap().get(key)){
                GuiElement el = new GuiElement(obj);
                try {
                    gridPane.add(el.createVbox(),el.pos.x+1,  width-(el.pos.y));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void clearScene(){
        gridPane.getChildren().clear();
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Pane root = new Pane();
            Scene scene = new Scene(root, 325, 350);
            GridPane grid = new GridPane();

            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));

            Label heightLabel = new Label("Map height:");
            grid.add(heightLabel, 0, 1);
            TextField heightTextField = new TextField("10");
            grid.add(heightTextField, 1, 1);

            Label widthLabel = new Label("Map width:");
            grid.add(widthLabel, 0, 2);
            TextField widthtTextField = new TextField("10");
            grid.add(widthtTextField, 1, 2);

            Label startEnergyLabel = new Label("Start energy:");
            grid.add(startEnergyLabel, 0, 3);
            TextField startEnergyTextField = new TextField("30");
            grid.add(startEnergyTextField, 1, 3);

            Label moveEnergyLabel = new Label("Move energy:");
            grid.add(moveEnergyLabel, 0, 4);
            TextField moveEnergyTextField = new TextField("1");
            grid.add(moveEnergyTextField, 1, 4);

            Label plantEnergyLabel = new Label("Plant energy:");
            grid.add(plantEnergyLabel, 0, 5);
            TextField plantEnergyTextField = new TextField("3");
            grid.add(plantEnergyTextField, 1, 5);

            Label jungleRatioLabel = new Label("Jungle ratio: (1:number)" );
            grid.add(jungleRatioLabel, 0, 6);
            TextField jungleRatioTextField = new TextField("2");
            grid.add(jungleRatioTextField, 1, 6);

            Label animalsCountLabel = new Label("Starting animals:");
            grid.add(animalsCountLabel, 0, 7);
            TextField animalsCountTextField = new TextField("20");
            grid.add(animalsCountTextField, 1, 7);

            Button btn = new Button("Start");
            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.BOTTOM_LEFT);
            hbBtn.getChildren().add(btn);

            grid.add(hbBtn, 1, 8);
            root.getChildren().add(grid);
            btn.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent e) {

                    height = Integer.parseInt(heightTextField.getText());
                    width = Integer.parseInt(widthtTextField.getText());
                    startEnergy = Integer.parseInt(startEnergyTextField.getText());
                    moveEnergy = Integer.parseInt(moveEnergyTextField.getText());
                    plantEnergy = Integer.parseInt(plantEnergyTextField.getText());
                    jungleRatio = Integer.parseInt(jungleRatioTextField.getText());
                    animalsCount = Integer.parseInt(animalsCountTextField.getText());

                    checkValues();

                    drawGrid();
                    map = new WorldMap(width,height,animalsCount, startEnergy, moveEnergy, plantEnergy);
                    engine = new SimulationEngine(map);
                    engineThread = new Thread(engine);
                    engineThread.start();

                    renderItems();
                    stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(primaryStage);
                    Scene gameScene = new Scene(gridPane, (width+1)*cellWidth, (height+1)*cellHeight);
                    stage.setScene(gameScene);

                    stage.show();

                }

            });
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


