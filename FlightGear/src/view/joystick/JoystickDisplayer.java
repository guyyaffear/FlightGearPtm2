package view.joystick;

import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class JoystickDisplayer extends AnchorPane {
    public final DoubleProperty aileronValue,elevatorsValue,rudderValue,throttleValue;
    public final DoubleProperty minAileron,maxAileron,minElevator,maxElevator,minThrottle,maxThrottle,
            minRudder,maxRudder;
    public final StringProperty throttleName,rudderName,aileronName,elevatorName;
    public final JoystickController controller;

    public JoystickDisplayer() {
        super();
        aileronValue=new SimpleDoubleProperty();
        elevatorsValue=new SimpleDoubleProperty();
        FXMLLoader loader=new FXMLLoader();
        AnchorPane toDisplay=null;
        try {
            toDisplay=loader.load(getClass().getResource("Joystick.fxml").openStream());
            this.getChildren().add(toDisplay);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(toDisplay!=null){
                controller=loader.getController();
                throttleValue=controller.throttle.valueProperty();
                minThrottle=controller.throttle.minProperty();
                maxThrottle=controller.throttle.maxProperty();
                rudderValue=controller.rudder.valueProperty();
                minRudder=controller.rudder.minProperty();
                maxRudder=controller.rudder.maxProperty();
                throttleName=controller.throttleTitle.textProperty();
                aileronName=controller.aileronTitle.textProperty();
                rudderName=controller.rudderTitle.textProperty();
                elevatorName=controller.elevatorTitle.textProperty();

                aileronValue.addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        Platform.runLater(()->{
                            double x=aileronValue.getValue();
                            controller.aileronVal.setText(""+x);
                        });
                    }
                });
                minAileron=controller.minAileron;
                maxAileron=controller.maxAileron;
                elevatorsValue.addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        Platform.runLater(()->{
                            double x=elevatorsValue.getValue();
                            controller.elevatorVal.setText(""+x);
                        });
                    }
                });
                minElevator=controller.minElevator;
                maxElevator=controller.maxElevator;
            }
            else{
                controller=null;
                throttleValue = new SimpleDoubleProperty();
                rudderValue=new SimpleDoubleProperty();
                minThrottle=new SimpleDoubleProperty();
                maxThrottle=new SimpleDoubleProperty();
                minAileron=new SimpleDoubleProperty();
                maxAileron=new SimpleDoubleProperty();
                minRudder=new SimpleDoubleProperty();
                maxRudder=new SimpleDoubleProperty();
                minElevator=new SimpleDoubleProperty();
                maxElevator=new SimpleDoubleProperty();
                throttleName=new SimpleStringProperty();
                rudderName=new SimpleStringProperty();
                aileronName=new SimpleStringProperty();
                elevatorName=new SimpleStringProperty();
            }
        }
    }
}
