package model;

import java.util.HashMap;
import java.util.List;

import anomaly_detectors.*;
import javafx.scene.paint.Color;

public interface Model {
	void start();// connect to flight gear
	void play();
	void pause();
	void stop();
	void forward();
	void rewind();

	boolean checkValidateSettingFile(String txtFile);
	void saveLastSettingFile(String currentTxtFile);
	List<String> getNames();
	void applyValuesMinMax();
	void applyNames();
	int getRate();
	void setSpeedOfFlight(double speed);

	boolean setTrainTimeSeries(String csvTrainFile);
	void saveLastCsvTrainFile(String csvTrainFile);

	boolean setTestTimeSeries(String csvTestFile);
	int getLength();
	void setValues(int timeStep);
	void setCurrentTime(int currentTime);
	void setCurrentTimeWithoutNotify(int currentTime);
	double getMinValueOfColl(String f);
	double getMaxValueOfColl(String f);

	boolean setAnomalyDetector(String path,String name);
	int howManyParameterTheDetectorUse(String f);
	String getMostCorrelated(String parameter);
	List<Point> sendPointOf1Parameter(int endTime, String feature);
	HashMap<Point, Color> sendPointOf2Parameter(int endTime, String feature);
	CorrelatedFeatures getCorrelatedFeatures(String parameter);
	Shape sendShapeDetector(String feature);

	void shutDown();
}
