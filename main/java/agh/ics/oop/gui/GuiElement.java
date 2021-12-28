package agh.ics.oop.gui;


import agh.ics.oop.interfaces.IMapElement;
import agh.ics.oop.move.Vector2d;
import agh.ics.oop.objects.Animal;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;

public class GuiElement {
    private String address;
    public Vector2d pos;
    IMapElement e;
    //public VBox vbox;
    public GuiElement(IMapElement el){
        address = el.getImagePath();
        pos = el.getPosition();
        e = el;
    }

    public VBox createVbox() throws Exception {
        Image image = new Image(new FileInputStream(this.address));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        VBox vbox = new VBox();
        vbox.getChildren().add(imageView);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }
}