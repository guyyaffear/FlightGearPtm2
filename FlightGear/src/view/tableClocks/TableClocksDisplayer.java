package view.tableClocks;

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
import javafx.scene.layout.Pane;
import model.ListOfAttributes;

public class TableClocksDisplayer extends Pane {
    public DoubleProperty altimeterValue, airspeedValue, headingValue,
            rollValue,pitchValue,yawValue;
    public DoubleProperty minAltimeter,maxAltimeter,minAirspeed,maxAirspeed,minHeading,maxHeading,
            minRoll,maxRoll,minPitch,maxPitch,minYaw,maxYaw;
    public StringProperty altimeterName,airspeedName,headingName,rollName,pitchName,yawName;
    public TableClocksController controller;

    public TableClocksDisplayer() {
        super();
        altimeterValue = new SimpleDoubleProperty();
        airspeedValue = new SimpleDoubleProperty();
        headingValue = new SimpleDoubleProperty();
        rollValue=new SimpleDoubleProperty();
        pitchValue=new SimpleDoubleProperty();
        yawValue=new SimpleDoubleProperty();
        minAltimeter = new SimpleDoubleProperty();
        maxAltimeter = new SimpleDoubleProperty();
        minAirspeed = new SimpleDoubleProperty();
        maxAirspeed = new SimpleDoubleProperty();
        minHeading = new SimpleDoubleProperty();
        maxHeading = new SimpleDoubleProperty();
        minRoll = new SimpleDoubleProperty();
        maxRoll = new SimpleDoubleProperty();
        minPitch = new SimpleDoubleProperty();
        maxPitch = new SimpleDoubleProperty();
        minYaw = new SimpleDoubleProperty();
        maxYaw = new SimpleDoubleProperty();
        altimeterName=new SimpleStringProperty();
        airspeedName=new SimpleStringProperty();
        headingName=new SimpleStringProperty();
        rollName=new SimpleStringProperty();
        pitchName=new SimpleStringProperty();
        yawName=new SimpleStringProperty();
        FXMLLoader loader=new FXMLLoader();
        try {
            Pane toDisplay=loader.load(getClass().getResource("TableClocks.fxml").openStream());
            controller=loader.getController();
            this.getChildren().add(toDisplay);
            altimeterName.addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    controller.gAltimeter.setTitle(altimeterName.getValue());
                }
            });
            altimeterValue.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    Platform.runLater(()->{
                        double x=altimeterValue.getValue();
                        controller.gAltimeter.setValue(x);
                    });
                }
            });
            minAltimeter.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=minAltimeter.getValue();
                    controller.gAltimeter.setMinValue(x);
                }
            });
            maxAltimeter.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=maxAltimeter.getValue();
                    controller.gAltimeter.setMaxValue(x);
                }
            });
            airspeedName.addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    controller.gAirspeed.setTitle(airspeedName.getValue());
                }
            });
            airspeedValue.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    Platform.runLater(()->{
                        double x=airspeedValue.getValue();
                        controller.gAirspeed.setValue(x);
                    });
                }
            });
            minAirspeed.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=minAirspeed.getValue();
                    controller.gAirspeed.setMinValue(x);
                }
            });
            maxAirspeed.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=maxAirspeed.getValue();
                    controller.gAirspeed.setMaxValue(x);
                }
            });
            headingName.addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    controller.gHeading.setTitle(headingName.getValue());
                }
            });
            headingValue.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    Platform.runLater(()->{
                        double x=headingValue.getValue();
                        controller.gHeading.setValue(x);
                    });
                }
            });
            minHeading.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=minHeading.getValue();
                    controller.gHeading.setMinValue(x);
                }
            });
            maxHeading.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=maxHeading.getValue();
                    controller.gHeading.setMaxValue(x);
                }
            });
            pitchName.addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    controller.gPitch.setTitle(pitchName.getValue());
                }
            });
            pitchValue.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    Platform.runLater(()->{
                        double x=pitchValue.getValue();
                        controller.gPitch.setValue(x);
                    });
                }
            });
            minPitch.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=minPitch.getValue();
                    controller.gPitch.setMinValue(x);
                }
            });
            maxPitch.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=maxPitch.getValue();
                    controller.gPitch.setMaxValue(x);
                }
            });
            rollName.addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    controller.gRoll.setTitle(rollName.getValue());
                }
            });
            rollValue.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    Platform.runLater(()->{
                        double x=rollValue.getValue();
                        controller.gRoll.setValue(x);
                    });
                }
            });
            minRoll.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=minRoll.getValue();
                    controller.gRoll.setMinValue(x);
                }
            });
            maxRoll.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=maxRoll.getValue();
                    controller.gRoll.setMaxValue(x);
                }
            });
            yawName.addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    controller.gYaw.setTitle(yawName.getValue());
                }
            });
            yawValue.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    Platform.runLater(()->{
                        double x=yawValue.getValue();
                        controller.gYaw.setValue(x);
                    });
                }
            });
            minYaw.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=minYaw.getValue();
                    controller.gYaw.setMinValue(x);
                }
            });
            maxYaw.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double x=maxYaw.getValue();
                    controller.gYaw.setMaxValue(x);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}