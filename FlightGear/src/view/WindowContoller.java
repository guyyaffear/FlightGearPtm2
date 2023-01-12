package view;


import java.io.*;
import java.net.URL;
import java.util.*;

import anomaly_detectors.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import view.attributesView.AttributesViewDisplayer;
import view.joystick.JoystickDisplayer;
import view.player.*;
import view.tableClocks.TableClocksDisplayer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import viewModel.ViewModel;
import javafx.scene.control.Alert;



public class WindowController implements Initializable,Observer{
    final HashMap<StringProperty,String> namesAttribute=new HashMap<>();
    final StringProperty txtFilePath=new SimpleStringProperty();
    final StringProperty csvTrainFilePath=new SimpleStringProperty();
    private ViewModel vm;
    StringProperty aileronName,elevatorName,throttleName,rudderName,altimeterName,airspeedName,headingName,rollName,pitchName,yawName;
    public WindowController() {
        txtFilePath.set(new File("resources/last_setting.txt").getAbsolutePath());
        csvTrainFilePath.set(new File("resources/last_train.csv").getAbsolutePath());
        attributesView=new AttributesViewDisplayer();
        joystickDisplayer=new JoystickDisplayer();
        playerDisplayer = new Player();
        tableClocks=new TableClocksDisplayer();
        aileronName=new SimpleStringProperty("");
        elevatorName=new SimpleStringProperty("");
        rudderName=new SimpleStringProperty("");
        throttleName=new SimpleStringProperty("");
        altimeterName=new SimpleStringProperty("");
        airspeedName=new SimpleStringProperty("");
        headingName=new SimpleStringProperty("");
        rollName=new SimpleStringProperty("");
        pitchName=new SimpleStringProperty("");
        yawName=new SimpleStringProperty("");
        namesAttribute.put(aileronName,aileronName.getValue());
        namesAttribute.put(elevatorName,elevatorName.getValue());
        namesAttribute.put(rudderName,rudderName.getValue());
        namesAttribute.put(throttleName,throttleName.getValue());
        namesAttribute.put(altimeterName,altimeterName.getValue());
        namesAttribute.put(airspeedName,airspeedName.getValue());
        namesAttribute.put(headingName,headingName.getValue());
        namesAttribute.put(rollName,rollName.getValue());
        namesAttribute.put(pitchName,pitchName.getValue());
        namesAttribute.put(yawName,yawName.getValue());
    }
    public void setViewModel(ViewModel vm) {
        this.vm=vm;

        vm.bindToProperty("settingsFile", this.txtFilePath,"V2VM");
        String s=txtFilePath.getValue();
        txtFilePath.set("a");
        txtFilePath.set(s);
        vm.bindToProperty("csvTrain", this.csvTrainFilePath,"V2VM");
        s=csvTrainFilePath.getValue();
        csvTrainFilePath.set("a");
        csvTrainFilePath.set(s);
        vm.bindToProperty("csvTest", playerDisplayer.csvTestFilePath,"V2VM");
        vm.bindToProperty("aileronName",aileronName,"VM2V");
        vm.bindToProperty("elevatorName",elevatorName,"VM2V");
        vm.bindToProperty("rudderName",rudderName,"VM2V");
        vm.bindToProperty("throttleName",throttleName,"VM2V");
        vm.bindToProperty("altimeterName",altimeterName,"VM2V");
        vm.bindToProperty("airspeedName",airspeedName,"VM2V");
        vm.bindToProperty("headingName",headingName,"VM2V");
        vm.bindToProperty("rollName",rollName,"VM2V");
        vm.bindToProperty("pitchName",pitchName,"VM2V");
        vm.bindToProperty("yawName",yawName,"VM2V");
        vm.bindToProperty("aileron", joystickDisplayer.aileronValue,"VM2V");
        vm.bindToProperty("elevator", joystickDisplayer.elevatorsValue,"VM2V");
        vm.bindToProperty("rudder", joystickDisplayer.rudderValue,"VM2V");
        vm.bindToProperty("throttle", joystickDisplayer.throttleValue,"VM2V");
        vm.bindToProperty("minAileron",joystickDisplayer.minAileron,"VM2V");
        vm.bindToProperty("maxAileron",joystickDisplayer.maxAileron,"VM2V");
        vm.bindToProperty("minElevator",joystickDisplayer.minElevator,"VM2V");
        vm.bindToProperty("maxElevator", joystickDisplayer.maxElevator, "VM2V");
        vm.bindToProperty("minRudder", joystickDisplayer.minRudder, "VM2V");
        vm.bindToProperty("maxRudder", joystickDisplayer.maxRudder, "VM2V");
        vm.bindToProperty("minThrottle", joystickDisplayer.minThrottle, "VM2V");
        vm.bindToProperty("maxThrottle", joystickDisplayer.maxThrottle, "VM2V");

        vm.bindToProperty("altimeter", tableClocks.altimeterValue,"VM2V");
        vm.bindToProperty("airspeed", tableClocks.airspeedValue,"VM2V");
        vm.bindToProperty("heading", tableClocks.headingValue,"VM2V");
        vm.bindToProperty("roll", tableClocks.rollValue,"VM2V");
        vm.bindToProperty("pitch", tableClocks.pitchValue,"VM2V");
        vm.bindToProperty("yaw", tableClocks.yawValue,"VM2V");
        vm.bindToProperty("minAltimeter",tableClocks.minAltimeter,"VM2V");
        vm.bindToProperty("maxAltimeter", tableClocks.maxAltimeter, "VM2V");    //make exception
        vm.bindToProperty("minAirspeed", tableClocks.minAirspeed, "VM2V");
        vm.bindToProperty("maxAirspeed", tableClocks.maxAirspeed, "VM2V");
        vm.bindToProperty("minHeading", tableClocks.minHeading, "VM2V");
        vm.bindToProperty("maxHeading", tableClocks.maxHeading, "VM2V");
        vm.bindToProperty("minRoll",tableClocks.minRoll,"VM2V");
        vm.bindToProperty("maxRoll",tableClocks.maxRoll,"VM2V");
        vm.bindToProperty("minPitch",tableClocks.minPitch,"VM2V");
        vm.bindToProperty("maxPitch",tableClocks.maxPitch,"VM2V");
        vm.bindToProperty("minYaw",tableClocks.minYaw,"VM2V");
        vm.bindToProperty("maxYaw",tableClocks.maxYaw,"VM2V");
        vm.currentTime.bindBidirectional(playerDisplayer.currentTime);
        vm.setTrainTimeSeries(csvTrainFilePath.get());
        vm.applyValuesMinMax();
        vm.applyNames();
        playerDisplayer.setRate(vm.getRate());
        attributesView.controller.applySetting(vm.getNames());
    }
    //---------------FXML Objects--------------

    @FXML
    AttributesViewDisplayer attributesView;

    @FXML
    JoystickDisplayer joystickDisplayer;

    @FXML
    TableClocksDisplayer tableClocks;

    @FXML
    Player playerDisplayer;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        playerDisplayer.csvTestFilePath.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                if(!vm.setTestTimeSeries(playerDisplayer.csvTestFilePath.getValue())) {
                    if(!newVal.isEmpty()){
                        Alert message=new Alert(Alert.AlertType.ERROR);
                        message.setContentText("oops!"
                                + " \n this file format is not valid");
                        message.show();
                        playerDisplayer.csvTestFilePath.set("");
                    }
                }
                else{
                    vm.initValues();
                    playerDisplayer.setLength(vm.getLength());
                    Alert message=new Alert(Alert.AlertType.CONFIRMATION);
                    message.setContentText("well done!"
                            + " \n the test file has been saved in the system");
                    message.show();
                }
            }
        });
        playerDisplayer.speedPlayer.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                vm.setSpeedOfFlight(Double.parseDouble(playerDisplayer.speedPlayer.getValue()));
            }
        });
        playerDisplayer.currentTime.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                double oldV=(double)oldVal;
                double newV=(double)newVal;
                if(newV==vm.getLength()-1)
                    playerDisplayer.controller.playIcon.setFill(Color.BLACK);
                if(newV!=0)
                    playerDisplayer.controller.stopIcon.setFill(Color.BLACK);
                Platform.runLater(()->{
                    if(oldV<newV)
                        initGraph();
                    else{
                        attributesView.controller.detections.controller.clearPoints();
                        attributesView.controller.selectedPrameter.controller.clearPoints();
                        attributesView.controller.correlatedPrameter.controller.clearPoints();
                        initGraph();
                    }
                });
            }
        });
        playerDisplayer.controller.playIcon.fillProperty().addListener(new ChangeListener<Paint>() {
            @Override
            public void changed(ObservableValue<? extends Paint> observableValue, Paint paint, Paint t1) {
                if(playerDisplayer.controller.playIcon.getFill()!= Color.BLACK){
                    if(playerDisplayer.csvTestFilePath.get()!=null)
                        vm.play(Double.parseDouble(playerDisplayer.speedPlayer.getValue()));
                    else{
                        Alert message=new Alert(Alert.AlertType.ERROR);
                        message.setContentText("oops!"
                                + " \n you didn't choose any csv test file, please choose one and try again");
                        message.show();
                        playerDisplayer.controller.playIcon.setFill(Color.BLACK);
                    }
                }
            }
        });
        playerDisplayer.controller.stopIcon.fillProperty().addListener(new ChangeListener<Paint>() {
            @Override
            public void changed(ObservableValue<? extends Paint> observableValue, Paint paint, Paint t1) {
                if(playerDisplayer.controller.stopIcon.getFill()!=Color.BLACK){
                    if(playerDisplayer.csvTestFilePath.get()!=null)
                        vm.stop();
                    else{
                        Alert message=new Alert(Alert.AlertType.ERROR);
                        message.setContentText("oops!"
                                + " \n you didn't choose any csv test file, please choose one and try again");
                        playerDisplayer.controller.stopIcon.setFill(Color.BLACK);
                        message.show();
                    }
                }
            }
        });
        playerDisplayer.controller.pauseIcon.fillProperty().addListener(new ChangeListener<Paint>() {
            @Override
            public void changed(ObservableValue<? extends Paint> observableValue, Paint paint, Paint t1) {
                if(playerDisplayer.controller.pauseIcon.getFill()!=Color.BLACK)
                    if(playerDisplayer.csvTestFilePath.get()!=null)
                        vm.pause();
                    else{
                        Alert message=new Alert(Alert.AlertType.ERROR);
                        message.setContentText("oops!"
                                + " \n you didn't choose any csv test file, please choose one and try again");
                        playerDisplayer.controller.pauseIcon.setFill(Color.BLACK);
                        message.show();
                    }
            }
        });
        playerDisplayer.controller.setEventHandlerForForward(()->vm.forward());
        playerDisplayer.controller.setEventHandlerForRewind(()->vm.rewind());
        playerDisplayer.currentTime.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                vm.setCurrentTime((int)playerDisplayer.currentTime.get());
                vm.setValues((int)playerDisplayer.currentTime.get());
            }
        });
        aileronName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                joystickDisplayer.aileronName.setValue(aileronName.getValue());
                namesAttribute.put(aileronName,aileronName.getValue());
                //attributesView.controller.applySetting(namesAttribute.values());
            }
        });
        elevatorName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                joystickDisplayer.elevatorName.setValue(elevatorName.getValue());
                namesAttribute.put(elevatorName,elevatorName.getValue());
                //attributesView.controller.applySetting(namesAttribute.values());
            }
        });
        rudderName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                joystickDisplayer.rudderName.setValue(rudderName.getValue());
                namesAttribute.put(rudderName,rudderName.getValue());
                //attributesView.controller.applySetting(namesAttribute.values());
            }
        });
        throttleName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                joystickDisplayer.throttleName.setValue(throttleName.getValue());
                namesAttribute.put(throttleName,throttleName.getValue());
                //attributesView.controller.applySetting(namesAttribute.values());
            }
        });
        altimeterName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                tableClocks.altimeterName.setValue(altimeterName.getValue());
                namesAttribute.put(altimeterName,altimeterName.getValue());
                //attributesView.controller.applySetting(namesAttribute.values());
            }
        });
        airspeedName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                tableClocks.airspeedName.setValue(airspeedName.getValue());
                namesAttribute.put(airspeedName,airspeedName.getValue());
                //attributesView.controller.applySetting(namesAttribute.values());
            }
        });
        headingName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                tableClocks.headingName.setValue(headingName.getValue());
                namesAttribute.put(headingName,headingName.getValue());
                //attributesView.controller.applySetting(namesAttribute.values());
            }
        });
        rollName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                tableClocks.rollName.setValue(rollName.getValue());
                namesAttribute.put(rollName,rollName.getValue());
                //attributesView.controller.applySetting(namesAttribute.values());
            }
        });
        pitchName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                tableClocks.pitchName.setValue(pitchName.getValue());
                namesAttribute.put(pitchName,pitchName.getValue());
                //attributesView.controller.applySetting(namesAttribute.values());
            }
        });
        yawName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                tableClocks.yawName.setValue(yawName.getValue());
                namesAttribute.put(yawName,yawName.getValue());
                //attributesView.controller.applySetting(namesAttribute.values());
            }
        });

        attributesView.selectedParameter.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (vm != null) {
                    String str = vm.getMostCorrelated(attributesView.selectedParameter.getValue());
                    if (str != null) {
                        attributesView.correlatedPrameter.setValue(str);
                        attributesView.controller.selectedPrameter.controller.clearAll();
                        attributesView.controller.selectedPrameter.controller.changeSetting(0, vm.getLength(),
                                vm.getMinValColl(attributesView.selectedParameter.get()),
                                vm.getMaxValColl(attributesView.selectedParameter.get()));
                        attributesView.controller.correlatedPrameter.controller.clearAll();
                        if (!str.equals("no correlated feature")) {
                            attributesView.controller.correlatedPrameter.controller.changeSetting(0, vm.getLength(),
                                    vm.getMinValColl(str),
                                    vm.getMaxValColl(str));
                        }
                        attributesView.controller.detections.controller.clearAll();
                        CorrelatedFeatures cf=vm.getCorrelatedFeatureObject(t1);
                        if(cf!=null&&vm.howManyParameterTheDetectorUse(t1)==2){
                            if(cf.feature1.equals(t1))
                                attributesView.controller.detections.controller.changeSetting(
                                        vm.getMinValColl(t1),
                                        vm.getMaxValColl(t1),
                                        vm.getMinValColl(str),
                                        vm.getMaxValColl(str));
                            else
                                attributesView.controller.detections.controller.changeSetting(
                                        vm.getMinValColl(str),
                                        vm.getMaxValColl(str),
                                        vm.getMinValColl(t1),
                                        vm.getMaxValColl(t1));
                        }
                        else
                            attributesView.controller.detections.controller.changeSetting(
                                    0, vm.getLength(),
                                    vm.getMinValColl(t1),
                                    vm.getMaxValColl(t1)
                            );
                        attributesView.controller.detections.controller.clearAll();
                        Shape shape=vm.sendShapeDetector(t1);
                        if(shape!=null){
                            if(shape instanceof Line)
                                attributesView.controller.detections.controller.addLine((Line)shape,Color.GREEN);
                            if(shape instanceof anomaly_detectors.Circle)
                                attributesView.controller.detections
                                        .controller.addCircle((Circle) (shape),Color.GREEN);
                        }
                    }
                    if(playerDisplayer.controller.playIcon.getFill()==Color.BLACK){
                        //checking if the player is already in play mode
                        //if it is, the points will be sent automatically
                        initGraph();
                    }
                }
            }
        });
    }
    private void initGraph(){
        String s1=attributesView.selectedParameter.getValue();
        String s2=attributesView.correlatedPrameter.getValue();
        if(!s1.isEmpty()&&!s2.isEmpty()){
            int time=(int)playerDisplayer.currentTime.get();
            attributesView.controller.selectedPrameter.controller.addSetPoints(
                    vm.sendPointOf1Parameter(time
                            ,attributesView.selectedParameter.getValue()),Color.BLUE,1);
            playerDisplayer.currentTime.setValue(time);
            if(!s2.equals("no correlated feature")){
                attributesView.controller.correlatedPrameter.controller.addSetPoints(
                        vm.sendPointOf1Parameter(time
                                ,attributesView.correlatedPrameter.getValue()),Color.BLUE,1);
            }
            vm.initPointsForDetector(s1,time);
            attributesView.controller.detections.controller.addSetPoints(vm.sendNotAnomaliesPointWith2Parameter(s1,time),Color.BLUE,1);
            attributesView.controller.detections.controller.addSetPoints(vm.sendAnomaliesPointWith2Parameter(s1,time),Color.RED,1.5);
        }
    }
    public void loadClassFile(){
        FileChooser fc=new FileChooser();
        fc.setTitle("open anomaly detector class file");
        fc.setInitialDirectory(new File("./bin/anomaly_detectors"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter
                        ("class file", "*.class")
        );
        File chooser=fc.showOpenDialog(null);
        if(chooser!=null) {
            if(vm.setAnomalyDetector(chooser.getParent(),chooser.getName())) {
                //file is valid
                Alert message=new Alert(Alert.AlertType.CONFIRMATION);
                message.setContentText("well done!"
                        + " \n the anomaly detector class file has been saved in the system");
                message.show();
                attributesView.controller.detections.controller.clearAll();
            }			else {
                Alert message=new Alert(Alert.AlertType.ERROR);
                message.setContentText("oops!"
                        + " \n please choose a valid class file that implements the interface TimeSeriesAnomalyDetector");
                message.show();
            }
        }
        else {
            Alert message=new Alert(Alert.AlertType.ERROR);
            message.setContentText("oops!"
                    + " \n please choose a class file");
            message.show();
        }
        //TimeSeriesAnomalyDetector
    }

    public void loadTxtFile() {
        FileChooser fc=new FileChooser();
        fc.setTitle("open txt setting file");
        fc.setInitialDirectory(new File("./resources"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter
                        ("txt file", "*.txt")
        );
        File chooser=fc.showOpenDialog(null);
        if(chooser!=null) {
            if(!vm.checkValidateSettingFile(chooser.getPath())) {
                Alert message=new Alert(Alert.AlertType.ERROR);
                message.setContentText("oops!"
                        + " \n this file format is not valid \n and the file wasn't saved in the system");
                message.show();
                txtFilePath.set(new File("resources/last_setting.txt").getAbsolutePath());
            }
            else {
                txtFilePath.set(chooser.getPath());
                vm.saveLastSettingFile();
                Alert message=new Alert(Alert.AlertType.CONFIRMATION);
                message.setContentText("well done!"
                        + " \n your txt file was saved in the system");
                message.show();
                vm.applyValuesMinMax();
                playerDisplayer.setRate(vm.getRate());
                vm.applyNames();
            }
            //attributesView.controller.applySetting(v);
        }
    }


    public void loadCSVFile() {
        FileChooser fc=new FileChooser();
        fc.setTitle("open csv file");
        fc.setInitialDirectory(new File("./resources"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter
                        ("csv file", "*.csv")
        );
        File chooser=fc.showOpenDialog(null);

        if(chooser!= null)
        {
            if(!vm.setTrainTimeSeries(chooser.getPath()))
            {
                Alert message=new Alert(Alert.AlertType.ERROR);
                message.setContentText("oops!"
                        + " \n this file format is not valid \n and the file wasn't saved in the system");
                message.show();
            }
            else {
                vm.saveLastCsvTrainFile();
                playerDisplayer.setLength(vm.getLength());
                Alert message=new Alert(Alert.AlertType.CONFIRMATION);
                message.setContentText("well done!"
                        + " \n your csv file was saved in the system");
                message.show();
                csvTrainFilePath.set(chooser.getPath());
            }
        }
        joystickDisplayer=new JoystickDisplayer();
        attributesView.controller.applySetting(vm.getNames());
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
