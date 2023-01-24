package viewModel;

import java.util.Observable;
import java.util.Observer;

import anomaly_detectors.*;
import javafx.beans.property.*;

import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import model.Model;
import model.MyModel;

public class ViewModel extends Observable implements Observer{
	public DoubleProperty currentTime;
	private StringProperty txtFilePath;
	private StringProperty csvTrainFilePath;
	private StringProperty csvTestFilePath;

	private StringProperty aileronName,elevatorName,throttleName,rudderName,altimeterName,airspeedName,headingName,rollName,pitchName,yawName;

	public final Map<String, Property> properties = new HashMap<>();

	//--------values for gauge table----------
	private DoubleProperty altimeterValue, airspeedValue, headingValue,
	rollValue,pitchValue,yawValue;
	private DoubleProperty minAltimeter,maxAltimeter,minAirspeed,maxAirspeed,minHeading,maxHeading,
	minRoll,maxRoll,minPitch,maxPitch,minYaw,maxYaw;

	//----------values for joystick-----------
	private DoubleProperty aileronValue, elevatorsValue
	,throttleValue, rudderValue;
	private DoubleProperty minAileron,maxAileron,minElevator,maxElevator,minThrottle,maxThrottle,
	minRudder,maxRudder;
	
	private Model m;
	private HashMap<Point,Color> pointsForDetector=new HashMap<>();
	public ViewModel(Model m) {
		this.m=m;
		currentTime=new SimpleDoubleProperty();

		txtFilePath=new SimpleStringProperty("");
		csvTrainFilePath=new SimpleStringProperty("");
		csvTestFilePath = new SimpleStringProperty("");

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

		throttleValue = new SimpleDoubleProperty();
		rudderValue=new SimpleDoubleProperty();
		aileronValue=new SimpleDoubleProperty();
		elevatorsValue=new SimpleDoubleProperty();
		minThrottle=new SimpleDoubleProperty();
		maxThrottle=new SimpleDoubleProperty(1);
		minAileron=new SimpleDoubleProperty();
		maxAileron=new SimpleDoubleProperty();
		minRudder=new SimpleDoubleProperty();
		maxRudder=new SimpleDoubleProperty(2);
		minElevator=new SimpleDoubleProperty();
		maxElevator=new SimpleDoubleProperty();
		
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

		properties.put("settingsFile", this.txtFilePath);
		properties.put("csvTrain", this.csvTrainFilePath);
		properties.put("csvTest", this.csvTestFilePath);

		properties.put("aileronName",this.aileronName);
		properties.put("elevatorName",this.elevatorName);
		properties.put("rudderName",this.rudderName);
		properties.put("throttleName",this.throttleName);
		properties.put("altimeterName",this.altimeterName);
		properties.put("airspeedName",this.airspeedName);
		properties.put("headingName",this.headingName);
		properties.put("rollName",this.rollName);
		properties.put("pitchName",this.pitchName);
		properties.put("yawName",this.yawName);
		
		properties.put("altimeter", this.altimeterValue);
		properties.put("airspeed", this.airspeedValue);
		properties.put("heading", this.headingValue);
		properties.put("roll", this.rollValue);
		properties.put("pitch", this.pitchValue);
		properties.put("yaw", this.yawValue);
		properties.put("minAltimeter",this.minAltimeter);
		properties.put("maxAltimeter",this.maxAltimeter);
		properties.put("minAirspeed",this.minAirspeed);
		properties.put("maxAirspeed",this.maxAirspeed);
		properties.put("minHeading",this.minHeading);
		properties.put("maxHeading",this.maxHeading);
		properties.put("minRoll",this.minRoll);
		properties.put("maxRoll",this.maxRoll);
		properties.put("minPitch",this.minPitch);
		properties.put("maxPitch",this.maxPitch);
		properties.put("minYaw",this.minYaw);
		properties.put("maxYaw",this.maxYaw);
		
		properties.put("aileron", this.aileronValue);
		properties.put("elevator", this.elevatorsValue);
		properties.put("rudder", this.rudderValue);
		properties.put("throttle", this.throttleValue);
		properties.put("minAileron",this.minAileron);
		properties.put("maxAileron",this.maxAileron);
		properties.put("minElevator",this.minElevator);
		properties.put("maxElevator",this.maxElevator);
		properties.put("minRudder",this.minRudder);
		properties.put("maxRudder",this.maxRudder);
		properties.put("minThrottle",this.minThrottle);
		properties.put("maxThrottle",this.maxThrottle);
	}
	
	public boolean bindToProperty(String name, Property p,String direction) {
		//if the direction is VM2V: property_from_view.bind(property_form_view_model)
		//if the direction is V2VM: property_from_view_model.bind(property_from_view)
		if((!(properties.containsKey(name)))||(!(direction.equals("V2VM")||direction.equals("VM2V"))))
			return false;
		Property prop = properties.get(name);
		properties.remove(name);

		if(direction.equals("V2VM")){	//the View change the ViewModel
			p.addListener(new ChangeListener() {
				@Override
				public void changed(ObservableValue observableValue, Object o, Object t1) {
					Object obj=p.getValue();
					prop.setValue(obj);
				}
			});
			p.setValue(p.getValue());
		}
		else{	//the ViewModel change the View
			p.bind(prop);
			Object obj=prop.getValue();
			prop.setValue(null);
			prop.setValue(obj);
		}

		properties.put(name, prop);

		return true;
	}
	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof String){
			String s=(String)arg;
			String[] arr=s.split(" ");
			if(arr[0].contains("Name")){
				String toUpdate=arr[1];
				if(arr[0].equals("aileronName:"))
					aileronName.setValue(toUpdate);
				if(arr[0].equals("elevatorsName:"))
					elevatorName.setValue(toUpdate);
				if(arr[0].equals("throttleName:"))
					throttleName.setValue(toUpdate);
				if(arr[0].equals("rudderName:"))
					rudderName.setValue(toUpdate);
				if(arr[0].equals("altimeterName:"))
					altimeterName.setValue(toUpdate);
				if(arr[0].equals("airspeedName:"))
					airspeedName.setValue(toUpdate);
				if(arr[0].equals("headingName:"))
					headingName.setValue(toUpdate);
				if(arr[0].equals("rollName:"))
					rollName.setValue(toUpdate);
				if(arr[0].equals("pitchName:"))
					pitchName.setValue(toUpdate);
				if(arr[0].equals("yawName:"))
					yawName.setValue(toUpdate);
			}
			else{
				double toUpdate=Double.parseDouble(arr[1]);
				if(arr[0].equals("currentTime:"))
					currentTime.setValue(toUpdate);
				if(arr[0].equals("aileronMin:"))
					minAileron.setValue(toUpdate);
				if(arr[0].equals("aileronMax:"))
					maxAileron.setValue(toUpdate);
				if(arr[0].equals("aileronVal:"))
					aileronValue.setValue(toUpdate);
				if(arr[0].equals("elevatorMin:"))
					minElevator.setValue(toUpdate);
				if(arr[0].equals("elevatorMax:"))
					maxElevator.setValue(toUpdate);
				if(arr[0].equals("elevatorVal:"))
					elevatorsValue.setValue(toUpdate);
				if(arr[0].equals("rudderMin:"))
					minRudder.setValue(toUpdate);
				if(arr[0].equals("rudderMax:"))
					maxRudder.setValue(toUpdate);
				if(arr[0].equals("rudderVal:"))
					rudderValue.setValue(toUpdate);
				if(arr[0].equals("throttleMin:"))
					minThrottle.setValue(toUpdate);
				if(arr[0].equals("throttleMax:"))
					maxThrottle.setValue(toUpdate);
				if(arr[0].equals("throttleVal:"))
					throttleValue.setValue(toUpdate);
				if(arr[0].equals("yawMin:"))
					minYaw.setValue(toUpdate);
				if(arr[0].equals("yawMax:"))
					maxYaw.setValue(toUpdate);
				if(arr[0].equals("yawVal:"))
					yawValue.setValue(toUpdate);
				if(arr[0].equals("airspeedMin:"))
					minAirspeed.setValue(toUpdate);
				if(arr[0].equals("airspeedMax:"))
					maxAirspeed.setValue(toUpdate);
				if(arr[0].equals("airspeedVal:"))
					airspeedValue.setValue(toUpdate);
				if(arr[0].equals("altimeterMin:"))
					minAltimeter.setValue(toUpdate);
				if(arr[0].equals("altimeterMax:"))
					maxAltimeter.setValue(toUpdate);
				if(arr[0].equals("altimeterVal:"))
					altimeterValue.setValue(toUpdate);
				if(arr[0].equals("rollMin:"))
					minRoll.setValue(toUpdate);
				if(arr[0].equals("rollMax:"))
					maxRoll.setValue(toUpdate);
				if(arr[0].equals("rollVal:"))
					rollValue.setValue(toUpdate);
				if(arr[0].equals("pitchMin:"))
					minPitch.setValue(toUpdate);
				if(arr[0].equals("pitchMax:"))
					maxPitch.setValue(toUpdate);
				if(arr[0].equals("pitchVal:"))
					pitchValue.setValue(toUpdate);
				if(arr[0].equals("headingMin:"))
					minHeading.setValue(toUpdate);
				if(arr[0].equals("headingMax:"))
					maxHeading.setValue(toUpdate);
				if(arr[0].equals("headingVal:"))
					headingValue.setValue(toUpdate);
			}
		}
	}
	public boolean checkValidateSettingFile(String txtFile){
		return m.checkValidateSettingFile(txtFile);
	}
	public void saveLastSettingFile(){
		m.saveLastSettingFile(txtFilePath.getValue());
	}
	public void applyValuesMinMax(){ m.applyValuesMinMax();}
	public void applyNames(){m.applyNames();}
	public List<String> getNames() {
		return m.getNames();
	}
	public boolean setTrainTimeSeries(String csvTrainFile) {
		if(!m.setTrainTimeSeries(csvTrainFile))
			return false;
		return true;
	}
	public void saveLastCsvTrainFile(){
		m.saveLastCsvTrainFile(csvTrainFilePath.getValue());
	}
	public boolean setTestTimeSeries(String csvTestFile) {
		if(!m.setTestTimeSeries(csvTestFile))
			return false;
		csvTestFilePath.setValue(csvTestFile);
		return true;
	}
	public int howManyParameterTheDetectorUse(String f){return m.howManyParameterTheDetectorUse(f);}
	public void initValues(){
		m.setValues(0);
	}
	public void setValues(int timestep){m.setValues(timestep);}
	public int getLength() {return m.getLength();}

	public void play(double speed){
		m.setSpeedOfFlight(speed);
		m.play();
	}
	public void stop(){
		m.stop();
	}
	public void pause(){m.pause();}
	public void forward(){m.forward();}
	public void rewind(){m.rewind();}
	public void setSpeedOfFlight(double speed){m.setSpeedOfFlight(speed);}

	public void setCurrentTime(int currentTime){m.setCurrentTimeWithoutNotify(currentTime);}
	public boolean setAnomalyDetector(String path,String name){
		return m.setAnomalyDetector(path, name);
	}
	public String getMostCorrelated(String parameter){
		return m.getMostCorrelated(parameter);
	}

	public List<Point> sendPointOf1Parameter(int endTime, String feature){
		return m.sendPointOf1Parameter(endTime,feature);
	}

	public void initPointsForDetector(String feature,int endTime){
		pointsForDetector=m.sendPointOf2Parameter(endTime,feature);
	}
	public List<Point> sendAnomaliesPointWith2Parameter(String feature,int endTime){
		List<Point> res=new ArrayList<>();
		for(Point key:pointsForDetector.keySet())
			if(pointsForDetector.get(key)==Color.RED)
				res.add(key);
		return res;
	}
	public List<Point> sendNotAnomaliesPointWith2Parameter(String feature,int endTime){
		List<Point> res=new ArrayList<>();
		for(Point key:pointsForDetector.keySet())
			if(pointsForDetector.get(key)==Color.BLUE)
				res.add(key);
		return res;
	}
	public CorrelatedFeatures getCorrelatedFeatureObject(String feature){
		return m.getCorrelatedFeatures(feature);
	}
	public int getRate(){return m.getRate();}
	public double getMaxValColl(String f){return m.getMaxValueOfColl(f);}
	public double getMinValColl(String f){return m.getMinValueOfColl(f);}
	public Shape sendShapeDetector(String feature) {return m.sendShapeDetector(feature);}
}
