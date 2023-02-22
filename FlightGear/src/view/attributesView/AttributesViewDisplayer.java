package view.attributesView;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import model.ListOfAttributes;
import view.joystick.JoystickController;

public class AttributesViewDisplayer extends AnchorPane {
    public final AttributesViewController controller;
    public final StringProperty selectedParameter,correlatedPrameter;
    public AttributesViewDisplayer() {
        super();
        FXMLLoader loader=new FXMLLoader();
        AnchorPane toDisplay=null;
        try {
            toDisplay=loader.load(getClass().getResource("AttributesView.fxml").openStream());
            this.getChildren().add(toDisplay);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(toDisplay!=null){
                controller=loader.getController();
                selectedParameter=controller.selectedPrameter.title;
                correlatedPrameter=controller.correlatedPrameter.title;
            }
            else{
                controller=null;
                selectedParameter=new SimpleStringProperty();
                correlatedPrameter=new SimpleStringProperty();
            }
        }
    }
}