package view.player;

import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class Player extends AnchorPane {

    public StringProperty speedPlayer;
    public StringProperty csvTestFilePath;
    public DoubleProperty currentTime;
    public final PlayerDisplayerController controller;
    private int length;
    private int rate=1;
    private boolean sliderMoved=true;

    public void setRate(int r){rate=r;}
    public void setLength(int l){length=l;}

    public Player() {
        super();
        speedPlayer=new SimpleStringProperty();
        FXMLLoader fxl = new FXMLLoader();
        AnchorPane toDisplay=null;
        try {
            toDisplay = fxl.load(getClass().getResource("Player.fxml").openStream());
            this.getChildren().add(toDisplay);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(toDisplay!=null){
                controller=fxl.getController();
                controller.options.valueProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        speedPlayer.setValue(controller.options.valueProperty().getValue());
                    }
                });

                speedPlayer.set(controller.options.getValue());
                csvTestFilePath=controller.csvTestFilePath;
                currentTime=new SimpleDoubleProperty();
                currentTime.addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        Platform.runLater(()->{
                            double minValue=controller.timeLine.getMin();
                            double maxValue=controller.timeLine.getMax();
                            sliderMoved=false;
                            controller.timeLine.valueProperty().setValue(minValue+((maxValue-minValue)*currentTime.getValue()/length));
                            int hour=(int)(currentTime.get())/(rate)/60/60;
                            int minute=(int)((currentTime.get())/(rate)/60)%60;
                            int second=(int)(currentTime.get())/(rate)%60;
                            String newTimeLabel="";
                            if(hour<10)
                                newTimeLabel+="0"+hour+":";
                            else
                                newTimeLabel+=hour+":";
                            if(minute<10)
                                newTimeLabel+="0"+minute+":";
                            else
                                newTimeLabel+=minute+":";
                            if(second<10)
                                newTimeLabel+="0"+second;
                            else
                                newTimeLabel+=second;
                            controller.timeLabel.setText(newTimeLabel);
                        });
                    }
                });
                controller.timeLine.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if(sliderMoved) {
                            double minValue = controller.timeLine.getMin();
                            double maxValue = controller.timeLine.getMax();

                            currentTime.setValue(
                                    (Math.round((Math.round(controller.timeLine.valueProperty().getValue()) - minValue)
                                            * length
                                            / (maxValue - minValue))));
                        }
                        else
                            sliderMoved=true;
                    }
                });
            }
            else
                controller=null;
        }
    }
}