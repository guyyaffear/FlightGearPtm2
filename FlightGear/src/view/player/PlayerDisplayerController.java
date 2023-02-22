package view.player;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import model.ListOfAttributes;

public class PlayerDisplayerController  implements Initializable {

    StringProperty csvTestFilePath;
    @FXML
    Slider timeLine;
    @FXML
    ComboBox<String> options;
    @FXML
    public FontAwesomeIcon playIcon,pauseIcon,stopIcon,forwardIcon,rewindIcon;
    @FXML
    Label timeLabel;
    public PlayerDisplayerController() {
        options = new ComboBox<>();
        csvTestFilePath = new SimpleStringProperty();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ObservableList<String> list=FXCollections.observableArrayList
                ("0.25","0.5","0.75","1","1.25","1.5","1.75","2");
        options.setItems(list);
        if(options.getValue()==null)
            options.setValue("1");
        timeLine.setValue(0);
        playIcon.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
            playIcon.setFill(Color.rgb(3,187,116));
            pauseIcon.setFill(Color.BLACK);
            stopIcon.setFill(Color.BLACK);
            forwardIcon.setFill(Color.BLACK);
            rewindIcon.setFill(Color.BLACK);
        });
        pauseIcon.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
            playIcon.setFill(Color.BLACK);
            pauseIcon.setFill(Color.rgb(210,65,65));
            stopIcon.setFill(Color.BLACK);
            forwardIcon.setFill(Color.BLACK);
            rewindIcon.setFill(Color.BLACK);
        });
        stopIcon.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
            playIcon.setFill(Color.BLACK);
            pauseIcon.setFill(Color.BLACK);
            stopIcon.setFill(Color.rgb(114,12,12));
            forwardIcon.setFill(Color.BLACK);
            rewindIcon.setFill(Color.BLACK);
        });
    }

    public void openCSVFile() {
        FileChooser fc=new FileChooser();
        fc.setTitle("open csv file");
        fc.setInitialDirectory(new File("./resources"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter
                        ("csv file", "*.csv")
        );
        File chooser=fc.showOpenDialog(null);
        if(chooser!=null){
            csvTestFilePath.set(chooser.getPath());
            timeLine.valueProperty().set(0);
        }
    }

    public void setEventHandlerForForward(Runnable r){
        forwardIcon.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->r.run());
    }

    public void setEventHandlerForRewind(Runnable r){
        rewindIcon.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->r.run());
    }
}