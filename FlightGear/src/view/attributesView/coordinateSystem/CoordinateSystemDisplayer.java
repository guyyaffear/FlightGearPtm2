package view.attributesView.coordinateSystem;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.attributesView.AttributesViewController;

public class CoordinateSystemDisplayer extends Pane {
    public final CoordinateSystemController controller;
    public final StringProperty title;
    public CoordinateSystemDisplayer() {
        super();
        FXMLLoader loader=new FXMLLoader();
        Pane toDisplay=null;
        try {
            toDisplay=loader.load(getClass().getResource("CoordinateSystem.fxml").openStream());
            this.getChildren().add(toDisplay);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(toDisplay!=null){
                controller=loader.getController();
                title=controller.title.textProperty();
            }
            else{
                controller=null;
                title=new SimpleStringProperty();
            }
        }
    }
}