package view.joystick;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;

public class JoystickController implements Initializable {

    DoubleProperty minAileron=new SimpleDoubleProperty();
    DoubleProperty maxAileron=new SimpleDoubleProperty();
    DoubleProperty minElevator=new SimpleDoubleProperty();
    DoubleProperty maxElevator=new SimpleDoubleProperty();
    private double heightToMove,widthToMove;
    private double centerX,centerY;
    @FXML
    Label throttleTitle,rudderTitle,aileronTitle,elevatorTitle;
    @FXML
    Slider rudder;
    @FXML
    Circle insideCircle,outsideCircle;
    @FXML
    Slider throttle;
    @FXML
    Label aileronVal, elevatorVal;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        heightToMove=outsideCircle.getRadius()-insideCircle.getRadius();
        widthToMove=outsideCircle.getRadius()-insideCircle.getRadius();
        centerX=outsideCircle.getCenterX();
        centerY=outsideCircle.getCenterY();
        aileronVal.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                insideCircle.setCenterX(centerX+Double.parseDouble(aileronVal.getText())*widthToMove);
            }
        });
        elevatorVal.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                insideCircle.setCenterY(centerY-Double.parseDouble(elevatorVal.getText())*heightToMove);
            }
        });
    }
}
