package view.attributesView;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import anomaly_detectors.Point;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.ListOfAttributes;
import view.attributesView.coordinateSystem.CoordinateSystemController;
import view.attributesView.coordinateSystem.CoordinateSystemDisplayer;

public class AttributesViewController implements Initializable {
    final ObservableList<String> list = FXCollections.observableArrayList();

    @FXML
    ListView<String> listAttributes=new ListView<String>();

    @FXML
    public CoordinateSystemDisplayer detections,selectedPrameter,correlatedPrameter;

    public AttributesViewController() {
        File lastSetting=new File(new File("resources/last_setting.txt").getAbsolutePath());
        detections=new CoordinateSystemDisplayer();
        selectedPrameter=new CoordinateSystemDisplayer();
        correlatedPrameter=new CoordinateSystemDisplayer();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        listAttributes.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                selectedPrameter.title.setValue(listAttributes.getSelectionModel().getSelectedItem());
            }
        });
        detections.title.setValue("anomaly detection");
    }

    public void applySetting(Collection<String> c) {
        listAttributes.getItems().removeAll(listAttributes.getItems());
        list.removeAll(list);
        list.addAll(c);
        list.sort((s1,s2)->s1.compareTo(s2));
        listAttributes.getItems().addAll(list);
    }
}