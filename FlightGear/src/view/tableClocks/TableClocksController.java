package view.tableClocks;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.TickLabelLocation;
import eu.hansolo.medusa.TickLabelOrientation;
import eu.hansolo.medusa.TickMarkType;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import model.ListOfAttributes;

public class TableClocksController implements Initializable {
    @FXML
    StackPane altimeter,airspeed,heading,roll,pitch,yaw;
    Gauge gAltimeter,gAirspeed,gHeading,gRoll,gPitch,gYaw;

    public TableClocksController() {
        altimeter=new StackPane(); //height of the flight
        airspeed=new StackPane(); //speed of the flight
        heading=new StackPane(); //direction of the flight
        roll=new StackPane();
        pitch=new StackPane();
        yaw=new StackPane();
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        gAltimeter=GaugeBuilder.create()
                .animated(true)
                .autoScale(true)
                .shadowsEnabled(true)
                .ledColor(Color.rgb(250, 50, 0))
                .gradientBarEnabled(true)
                .gradientBarStops(new Stop(0.0, Color.rgb(0, 0, 255, 0.7)),
                        new Stop(0.5, Color.rgb(0, 200, 255, 0.7)),
                        new Stop(1.0, Color.rgb(0, 255, 0, 0.7)))
                .majorTickMarkType(TickMarkType.TRAPEZOID)
                .majorTickMarksVisible(true)
                .mediumTickMarkType(TickMarkType.TRAPEZOID)
                .mediumTickMarksVisible(true)
                .minorTickMarkType(TickMarkType.LINE)
                .minorTickMarksVisible(false)
                .tickLabelsVisible(true)
                .tickLabelOrientation(TickLabelOrientation.HORIZONTAL)
                .tickLabelLocation(TickLabelLocation.INSIDE)
                .title("altimeter")
                .unit("km")
                .foregroundBaseColor(Gauge.DARK_COLOR)
                .build();
        this.altimeter.getChildren().add(gAltimeter);
        gAirspeed=GaugeBuilder.create()
                .animated(true)
                .autoScale(true)
                .shadowsEnabled(true)
                .ledColor(Color.rgb(250, 50, 0))
                .gradientBarEnabled(true)
                .gradientBarStops(new Stop(0.0, Color.rgb(0, 0, 255, 0.7)),
                        new Stop(0.5, Color.rgb(0, 200, 255, 0.7)),
                        new Stop(1.0, Color.rgb(0, 255, 0, 0.7)))
                .majorTickMarkType(TickMarkType.TRAPEZOID)
                .majorTickMarksVisible(true)
                .mediumTickMarkType(TickMarkType.TRAPEZOID)
                .mediumTickMarksVisible(true)
                .minorTickMarkType(TickMarkType.LINE)
                .minorTickMarksVisible(false)
                .tickLabelsVisible(true)
                .tickLabelOrientation(TickLabelOrientation.HORIZONTAL)
                .tickLabelLocation(TickLabelLocation.INSIDE)
                .title("airspeed")
                .unit("km/h")
                .foregroundBaseColor(Gauge.DARK_COLOR)
                .build();
        this.airspeed.getChildren().add(gAirspeed);
        gHeading=GaugeBuilder.create()
                .animated(true)
                .autoScale(true)
                .shadowsEnabled(true)
                .ledColor(Color.rgb(250, 50, 0))
                .gradientBarEnabled(true)
                .gradientBarStops(new Stop(0.0, Color.rgb(0, 0, 255, 0.7)),
                        new Stop(0.5, Color.rgb(0, 200, 255, 0.7)),
                        new Stop(1.0, Color.rgb(0, 255, 0, 0.7)))
                .majorTickMarkType(TickMarkType.TRAPEZOID)
                .majorTickMarksVisible(true)
                .mediumTickMarkType(TickMarkType.TRAPEZOID)
                .mediumTickMarksVisible(true)
                .minorTickMarkType(TickMarkType.LINE)
                .minorTickMarksVisible(false)
                .tickLabelsVisible(true)
                .tickLabelOrientation(TickLabelOrientation.HORIZONTAL)
                .tickLabelLocation(TickLabelLocation.INSIDE)
                .title("heading")
                .unit("degrees")
                .foregroundBaseColor(Gauge.DARK_COLOR)
                .build();
        this.heading.getChildren().add(gHeading);
        gRoll=GaugeBuilder.create()
                .animated(true)
                .autoScale(true)
                .shadowsEnabled(true)
                .ledColor(Color.rgb(250, 50, 0))
                .gradientBarEnabled(true)
                .gradientBarStops(new Stop(0.0, Color.rgb(0, 0, 255, 0.7)),
                        new Stop(0.5, Color.rgb(0, 200, 255, 0.7)),
                        new Stop(1.0, Color.rgb(0, 255, 0, 0.7)))
                .majorTickMarkType(TickMarkType.TRAPEZOID)
                .majorTickMarksVisible(true)
                .mediumTickMarkType(TickMarkType.TRAPEZOID)
                .mediumTickMarksVisible(true)
                .minorTickMarkType(TickMarkType.LINE)
                .minorTickMarksVisible(false)
                .tickLabelsVisible(true)
                .tickLabelOrientation(TickLabelOrientation.HORIZONTAL)
                .tickLabelLocation(TickLabelLocation.INSIDE)
                .title("roll")
                .unit("km")
                .foregroundBaseColor(Gauge.DARK_COLOR)
                .build();
        this.roll.getChildren().add(gRoll);
        gYaw=GaugeBuilder.create()
                .animated(true)
                .autoScale(true)
                .shadowsEnabled(true)
                .ledColor(Color.rgb(250, 50, 0))
                .gradientBarEnabled(true)
                .gradientBarStops(new Stop(0.0, Color.rgb(0, 0, 255, 0.7)),
                        new Stop(0.5, Color.rgb(0, 200, 255, 0.7)),
                        new Stop(1.0, Color.rgb(0, 255, 0, 0.7)))
                .majorTickMarkType(TickMarkType.TRAPEZOID)
                .majorTickMarksVisible(true)
                .mediumTickMarkType(TickMarkType.TRAPEZOID)
                .mediumTickMarksVisible(true)
                .minorTickMarkType(TickMarkType.LINE)
                .minorTickMarksVisible(false)
                .tickLabelsVisible(true)
                .tickLabelOrientation(TickLabelOrientation.HORIZONTAL)
                .tickLabelLocation(TickLabelLocation.INSIDE)
                .title("yaw")
                .unit("km")
                .foregroundBaseColor(Gauge.DARK_COLOR)
                .build();
        this.yaw.getChildren().add(gYaw);
        gPitch=GaugeBuilder.create()
                .animated(true)
                .autoScale(true)
                .shadowsEnabled(true)
                .ledColor(Color.rgb(250, 50, 0))
                .gradientBarEnabled(true)
                .gradientBarStops(new Stop(0.0, Color.rgb(0, 0, 255, 0.7)),
                        new Stop(0.5, Color.rgb(0, 200, 255, 0.7)),
                        new Stop(1.0, Color.rgb(0, 255, 0, 0.7)))
                .majorTickMarkType(TickMarkType.TRAPEZOID)
                .majorTickMarksVisible(true)
                .mediumTickMarkType(TickMarkType.TRAPEZOID)
                .mediumTickMarksVisible(true)
                .minorTickMarkType(TickMarkType.LINE)
                .minorTickMarksVisible(false)
                .tickLabelsVisible(true)
                .tickLabelOrientation(TickLabelOrientation.HORIZONTAL)
                .tickLabelLocation(TickLabelLocation.INSIDE)
                .title("pitch")
                .unit("km")
                .foregroundBaseColor(Gauge.DARK_COLOR)
                .build();
        this.pitch.getChildren().add(gPitch);
    }}