package model;


import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Observable;
import java.util.*;
import java.util.stream.Collectors;

import anomaly_detectors.*;
import javafx.scene.paint.Color;

public class MyModel extends Observable implements Model {

	private int currentTime = 0;
	private TimeSeries train, test;
	private TimeSeriesAnomalyDetector detector;
	private ListOfAttributes atrList;
	private HashMap<String, Integer> collsForView;
	private String txtLast;
	private double aileronVal, elevatorVal, rudderVal, throttleVal, altimeterVal, airspeedVal, headingVal, rollVal, pitchVal, yawVal;
	private final String aileronName="aileron", elevatorName="elevator", rudderName="rudder", throttleName="throttle", altimeterName="altimeter", airspeedName="airspeed", headingName="heading", rollName="roll", pitchName="pitch", yawName="yaw";
	private int port = -1;
	private String ip = "";
	private int rate = -1;
	private Socket fg = null;
	private double speed;
	private PrintWriter writeToFlightGear = null;
	private ActiveObject task;
	private HashMap<String,List<Point>> pointsToDisplay;
	private String selectedFeature,correlatedFeature;
	private List<AnomalyReport> detection;
	private boolean wantToSuspend=false;

	public MyModel() {
		this.txtLast = new File("resources/last_setting.txt").getAbsolutePath();
		checkValidateSettingFile(new File("resources/last_setting.txt").getAbsolutePath());
		atrList = new ListOfAttributes(txtLast);
		collsForView = new HashMap<>();
		task=new ActiveObject(1);
	}
	@Override
	public int getRate(){return rate;}
	@Override
	public ArrayList<String> getNames() {
		if(train!=null)
			return train.getTitles();
		return new ArrayList<>();
	}

	@Override
	public void setCurrentTimeWithoutNotify(int currentTime){this.currentTime=currentTime;}

	public void setRate(int rate){
		this.rate=rate;
		setChanged();
		notifyObservers("rate: "+rate);
	}
	public void setAileronVal(double aileronVal) {
		this.aileronVal = aileronVal;
		setChanged();
		notifyObservers("aileronVal: " + aileronVal);
	}
	public void setElevatorVal(double elevatorVal) {
		this.elevatorVal = elevatorVal;
		setChanged();
		notifyObservers("elevatorVal: " + elevatorVal);
	}
	public void setRudderVal(double rudderVal) {
		this.rudderVal = rudderVal;
		setChanged();
		notifyObservers("rudderVal: " + rudderVal);
	}
	public void setThrottleVal(double throttleVal) {
		this.throttleVal = throttleVal;
		setChanged();
		notifyObservers("throttleVal: " + throttleVal);
	}
	public void setAltimeterVal(double altimeterVal) {
		this.altimeterVal = altimeterVal;
		setChanged();
		notifyObservers("altimeterVal: " + altimeterVal);
	}
	public void setAirspeedVal(double airspeedVal) {
		this.airspeedVal = airspeedVal;
		setChanged();
		notifyObservers("airspeedVal: " + airspeedVal);
	}
	public void setHeadingVal(double headingVal) {
		this.headingVal = headingVal;
		setChanged();
		notifyObservers("headingVal: " + headingVal);
	}
	public void setRollVal(double rollVal) {
		this.rollVal = rollVal;
		setChanged();
		notifyObservers("rollVal: " + rollVal);
	}
	public void setPitchVal(double pitchVal) {
		this.pitchVal = pitchVal;
		setChanged();
		notifyObservers("pitchVal: " + pitchVal);
	}
	public void setYawVal(double yawVal) {
		this.yawVal = yawVal;
		setChanged();
		notifyObservers("yawVal: " + yawVal);
	}

	private ListOfAttributes checkValidationSettingFile(String txtFile) {
		if (txtFile == null)
			return null;
		File f = new File(txtFile);
		if (!f.exists())
			return null;
		HashMap<String, Integer> atrApeared = new HashMap<>();
		ListOfAttributes atrL = new ListOfAttributes();

		//for the ip, port and rate
		atrApeared.put(aileronName, -1);
		atrApeared.put(elevatorName, -1);
		atrApeared.put(rudderName, -1);
		atrApeared.put(throttleName, -1);
		atrApeared.put(altimeterName, -1);
		atrApeared.put(airspeedName, -1);
		atrApeared.put(headingName, -1);
		atrApeared.put(rollName, -1);
		atrApeared.put(pitchName, -1);
		atrApeared.put(yawName, -1);
		atrApeared.put("ip", -1);
		atrApeared.put("port", -1);
		atrApeared.put("rate", -1);

		try {
			BufferedReader read = new BufferedReader(new FileReader((txtFile)));
			String line;
			while ((line = read.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length == 4) {
					if (!atrApeared.containsKey(data[0])) { //only these attributes
						read.close();
						return null;
					}
					if (Double.parseDouble(data[2]) >= Double.parseDouble(data[3])) { //min is smaller than max
						read.close();
						return null;
					}
					if (atrApeared.get(data[0]) != -1) { //attribute is already in use
						read.close();
						return null;
					}
					if (atrApeared.containsValue(Integer.parseInt(data[1]))) { //col is already in use
						read.close();
						return null;
					}
					atrApeared.put(data[0], Integer.parseInt(data[1]));
					String[] arr = {data[1], data[2], data[3]};
					atrL.addAttribute(data[0], new AttributeSettings(arr));
				}//if 4
				else {
					if (data.length == 2) {
						if (!(data[0].equals("ip") || data[0].equals("port") || data[0].equals("rate"))) {
							read.close();
							return null;
						} else {
							if (data[0].equals("ip")) {
								if (atrApeared.get("ip") != -1) {
									read.close();
									return null;
								} else
									atrL.ip = data[1];
							}
							if (data[0].equals("port")) {
								if (atrApeared.get("port")!=-1) {
									read.close();
									return null;
								} else
									atrL.port = Integer.parseInt(data[1]);
							}
							if (data[0].equals("rate")) {
								if (atrApeared.get("rate")!=-1) {
									read.close();
									return null;
								} else
									atrL.rate = Integer.parseInt(data[1]);
							}
							atrApeared.put(data[0], -2);
						}
					}//if 2
					else {
						read.close();
						return null;
					}//else
				}//else
			}//while
			read.close();
			return atrL;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void saveLastSettingFile(String currentTxtFile) {
		copyFile(currentTxtFile, new File("resources/last_setting.txt").getAbsolutePath());
		atrList = new ListOfAttributes(txtLast);
	}

	@Override
	public boolean checkValidateSettingFile(String txtFile) {
		ListOfAttributes atrL = checkValidationSettingFile(txtFile);
		if (atrL == null){
			atrList=new ListOfAttributes();
			return false;
		}
		atrList = atrL;
		return true;
	}

	@Override
	public void applyValuesMinMax() {
		if (atrList != null) {
			for (String key : atrList.getAttributesNames()) {
				AttributeSettings a = atrList.getList().get(key);
				setChanged();
				notifyObservers(key + "Min: " + a.getMinValue());
				setChanged();
				notifyObservers(key + "Max: " + a.getMaxValue());
			}
			ip = atrList.ip;
			port = atrList.port;
			rate=atrList.rate;
		}
	}

	@Override
	public void setSpeedOfFlight(double speed){
		this.speed=speed;
	}

	@Override
	public void applyNames() {
		if (atrList != null) {
			if(!atrList.isEmpty()){
				try {
					collsForView.put(aileronName,atrList.getList().get(aileronName).colInCSV);
					collsForView.put(elevatorName,atrList.getList().get(elevatorName).colInCSV);
					collsForView.put(rudderName, atrList.getList().get(rudderName).colInCSV);
					collsForView.put(throttleName, atrList.getList().get(throttleName).colInCSV);
					collsForView.put(altimeterName,atrList.getList().get(altimeterName).colInCSV);
					collsForView.put(airspeedName,atrList.getList().get(airspeedName).colInCSV);
					collsForView.put(headingName,atrList.getList().get(headingName).colInCSV);
					collsForView.put(rollName,atrList.getList().get(rollName).colInCSV);
					collsForView.put(pitchName,atrList.getList().get(pitchName).colInCSV);
					collsForView.put(yawName,atrList.getList().get(yawName).colInCSV);
					setChanged();
					notifyObservers("aileronName: "+aileronName);
					setChanged();
					notifyObservers("elevatorsName: "+elevatorName);
					setChanged();
					notifyObservers("rudderName: "+rudderName);
					setChanged();
					notifyObservers("throttleName: "+throttleName);
					setChanged();
					notifyObservers("altimeterName: "+altimeterName);
					setChanged();
					notifyObservers("airspeedName: "+airspeedName);
					setChanged();
					notifyObservers("headingName: "+headingName);
					setChanged();
					notifyObservers("rollName: "+rollName);
					setChanged();
					notifyObservers("pitchName: "+pitchName);
					setChanged();
					notifyObservers("yawName: "+yawName);

				}catch(Exception e){collsForView.clear();}
			}
		}
	}
	@Override
	public int howManyParameterTheDetectorUse(String f){
		try {
			return detector.detectBy2Or1Parameter(f);
		}catch(Exception e){return 2;}
	}

	private TimeSeries checkValidationCSV(String csv) {
		TimeSeries ts = new TimeSeries(csv);
		if (ts.isEmpty())
			return null;
		if(atrList.isEmpty())
			return ts;
		int atrLen = 0;
		ArrayList<Float> thisLine;

		Map<Integer, AttributeSettings> atrCols =
				atrList.getList().values().stream().
						collect(Collectors.toMap
								(atr -> atr.colInCSV, atr -> atr));

		for (int i = 0; i < ts.getSize(); i++) {
			thisLine = ts.getLine(ts.getTitles().get(i));
			if (thisLine.size() != ts.getLength()) //if all cols are in same length
				return null;
			if (atrCols.containsKey(i)) {//this col must be in csv
				atrLen++;
				for (float f : thisLine) { //if all values are between the min and max that was defined in the setting file
					if (f > atrCols.get(i).getMaxValue() || f < atrCols.get(i).getMinValue())
						return null;
				}
			}
		}
		if (atrLen != atrList.getLength()) // if all the settings appear in the setting file
			return null;

		return ts;
	}

	@Override
	public boolean setTrainTimeSeries(String csvTrainFile) {
		TimeSeries ts = checkValidationCSV(csvTrainFile);
		if (ts != null) {
			train = ts;
			if (detector != null)
				detector.learnNormal(train);
			return true;
		}
		return false;
	}

	@Override
	public boolean setTestTimeSeries(String csvTestFile) {
		TimeSeries ts = checkValidationCSV(csvTestFile);
		if (ts != null) {
			test = ts;
			pointsToDisplay=new HashMap<>();
			pointsToDisplay.put(aileronName,new ArrayList<>());
			pointsToDisplay.put(elevatorName,new ArrayList<>());
			pointsToDisplay.put(rudderName,new ArrayList<>());
			pointsToDisplay.put(throttleName,new ArrayList<>());
			pointsToDisplay.put(altimeterName,new ArrayList<>());
			pointsToDisplay.put(airspeedName,new ArrayList<>());
			pointsToDisplay.put(headingName,new ArrayList<>());
			pointsToDisplay.put(rollName,new ArrayList<>());
			pointsToDisplay.put(pitchName,new ArrayList<>());
			pointsToDisplay.put(yawName,new ArrayList<>());
			return true;
		}
		return false;
	}

	@Override
	public void saveLastCsvTrainFile(String currentCsvTrainFile) {
		copyFile(currentCsvTrainFile, new File("resources/last_train.txt").getAbsolutePath());
	}

	private void copyFile(String originalPath, String copyPath) {
		File lastFile = new File(copyPath);
		File currentFile = new File(originalPath);
		try {
			if (!lastFile.exists())
				lastFile.createNewFile();
			PrintWriter write = new PrintWriter(lastFile);
			BufferedReader read = new BufferedReader(new FileReader(currentFile));
			String line;
			while ((line = read.readLine()) != null) {
				write.println(line);
				write.flush();
			}
			write.close();
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public double getMaxValueOfColl(String f) {
		if(test!=null){
			if(atrList.contains(f))
				return test.maxValueOfColl(test.getTitles().get(test.getTitles().indexOf(f)));
			return test.maxValueOfColl(f);
		}
		return Double.MAX_VALUE;
	}
	@Override
	public Shape sendShapeDetector(String f) {
		if(detector!=null)
			return detector.sendShape(f);
		return null;
	}
	@Override
	public double getMinValueOfColl(String f) {
		if(test!=null){
			if(atrList.contains(f))
				return test.minValueOfColl(test.getTitles().get(test.getTitles().indexOf(f)));
			return test.minValueOfColl(f);
		}
		return Double.MIN_VALUE;
	}

	@Override
	public void play() {
		wantToSuspend=false;
		task.execute(() -> {
			wantToSuspend=false;
			while(!wantToSuspend) {
				if (currentTime + 1 < test.getLineAsList(0).size()) {
					setCurrentTime(currentTime + 1);
					try {
						Thread.sleep((long) (1000.0 / (speed * rate)));
					} catch (InterruptedException e) {
						System.out.println("didn't succeed to connect to the flight gear simulator");
					}
				} else
					break;
			}
			});
	}

	@Override
	public void pause() {
		if(test!=null)
			wantToSuspend=true;
	}

	@Override
	public void stop() {
		if(test!=null){
			pause();
			setCurrentTime(0);
		}
	}

	@Override
	public void forward() {
		if(test!=null)
			setCurrentTime(Math.min(test.getLength()-1,currentTime+5*rate));
	}

	@Override
	public void rewind(){
		if(test!=null)
			setCurrentTime(Math.max(0,currentTime-5*rate));
	}

	@Override
	public boolean setAnomalyDetector(String path, String name) {
		String[] paths = getPaths(path, name);
		URLClassLoader urlClassLoader;
		try {
			urlClassLoader = URLClassLoader.newInstance(
					new URL[]{new URL("file://" + paths[0])});
			Class<?> c = urlClassLoader.loadClass(paths[1]);
			if (c == null)
				return false;
			detector = (TimeSeriesAnomalyDetector) c.newInstance();
			detector.learnNormal(train);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private String[] getPaths(String path, String name) {
		Character backs = 92;
		String pattern = "\\W";
		String backslash = (backs).toString();
		String bin = "bin" + backslash;
		String[] ret = path.split("bin");
		ret[0] += bin;
		String[] packagePath = ret[1].split(pattern);
		ret[1] = "";
		for (String s : packagePath)
			if (!s.isEmpty())
				ret[1] += s + ".";
		ret[1] += name;//this string ends with ".class"
		ret[1] = ret[1].split(".class")[0];
		return ret;
	}

	@Override
	public void start() {
		try {
			fg = new Socket("localhost", port);
			writeToFlightGear = new PrintWriter(fg.getOutputStream());
		} catch (IOException e) {
			System.out.println("the connection with simulator doesn't succeed");
		}
	}

	@Override
	public void setValues(int timeStep) {
		if (test != null) {
			if (timeStep >= 0 && test.getLength() > timeStep) {
				double aileron = test.getLineAsList(atrList.getList().get(aileronName).colInCSV).get(timeStep);
				double elevator = test.getLineAsList(atrList.getList().get(elevatorName).colInCSV).get(timeStep);
				double throttle = test.getLineAsList(atrList.getList().get(throttleName).colInCSV).get(timeStep);
				double rudder = test.getLineAsList(atrList.getList().get(rudderName).colInCSV).get(timeStep);
				double altimeter = test.getLineAsList(atrList.getList().get(altimeterName).colInCSV).get(timeStep);
				double airspeed = test.getLineAsList(atrList.getList().get(airspeedName).colInCSV).get(timeStep);
				double heading = test.getLineAsList(atrList.getList().get(headingName).colInCSV).get(timeStep);
				double roll = test.getLineAsList(atrList.getList().get(rollName).colInCSV).get(timeStep);
				double pitch = test.getLineAsList(atrList.getList().get(pitchName).colInCSV).get(timeStep);
				double yaw = test.getLineAsList(atrList.getList().get(yawName).colInCSV).get(timeStep);
				setAileronVal(aileron);
				setElevatorVal(elevator);
				setThrottleVal(throttle);
				setRudderVal(rudder);
				setAltimeterVal(altimeter);
				setAirspeedVal(airspeed);
				setHeadingVal(heading);
				setRollVal(roll);
				setPitchVal(pitch);
				setYawVal(yaw);
				if (fg == null || writeToFlightGear == null)
					start();
				if(writeToFlightGear!=null){
					String line = "";
					for (int j = 0; j < test.getTitles().size(); j++)
						if (j != 0)
							line = line + "," + test.getLineAsList(j).get(timeStep);
						else
							line = line + test.getLineAsList(j).get(timeStep);
					writeToFlightGear.println(line);
					writeToFlightGear.flush();
				}
			}
		}
	}

	@Override
	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
		setChanged();
		notifyObservers("currentTime: " + currentTime);
	}
	@Override
	public int getLength() {
		try{
		return this.test.getLength();}
		catch (Exception e){return Integer.MAX_VALUE;}
	}

	@Override
	public String getMostCorrelated(String parameter) {
		selectedFeature=parameter;
		CorrelatedFeatures mostRelevant = getCorrelatedFeatures(parameter);
		if(detector!=null&&test!=null)
			this.detection=detector.detectOnlyByFeature(test,selectedFeature);
		if (mostRelevant == null)
			return (correlatedFeature="no correlated feature");
		if (mostRelevant.feature1.equals(parameter))
			return (correlatedFeature=mostRelevant.feature2);
		return (correlatedFeature=mostRelevant.feature1);
	}
	@Override
	public CorrelatedFeatures getCorrelatedFeatures(String parameter) {
		if (!train.getTitles().contains(parameter))
			return null;
		SimpleAnomalyDetector d = new SimpleAnomalyDetector((float)0.5);
		d.learnNormal(train);
		List<CorrelatedFeatures> a = d.getNormalModel();
		CorrelatedFeatures mostRelevant = null;
		for (CorrelatedFeatures c : a) {
			if (c.feature1.equals(parameter)||c.feature2.equals(parameter))
				if (mostRelevant == null)
					mostRelevant = c;
				else if (mostRelevant.corrlation < c.corrlation)
					mostRelevant = c;
		}
		return mostRelevant;
	}
	@Override
	public List<Point> sendPointOf1Parameter(int endTime,String feature){
		List<Point> result = new ArrayList<>();
		if (endTime < 0)
			return result;
		if (test == null)
			return result;
		if(atrList.getList().containsKey(feature)){
			for(int i=0;i<endTime;i++)
				result.add(new Point(i,test.getLineAsList(atrList.getList().get(feature).colInCSV).get(i)));
		}
		else{
			for(int i=0;i<endTime;i++)
				result.add(new Point(i,test.getLine(feature).get(i)));
		}
		return result;
	}
	@Override
	public HashMap<Point, Color> sendPointOf2Parameter(int endTime, String feature) {
		HashMap<Point,Color> result=new HashMap<>();
		if(test!=null){
			CorrelatedFeatures cf=getCorrelatedFeatures(feature);
			if(detector==null){
				for(int i=0;i<endTime;i++){
					if(cf==null)
						result.put(new Point(i,test.getLine(feature).get(i)),Color.BLUE);
					else
						result.put(new Point(test.getLine(cf.feature1).get(i),test.getLine(cf.feature2).get(i)),Color.BLUE);
				}
			}
			else{
				if(detection==null)
					detection=detector.detectOnlyByFeature(test,selectedFeature);
				for(int i=0;i<endTime;i++){
					if(detector.detectBy2Or1Parameter(selectedFeature)==1||cf==null){
						String description=feature;
						if(detection.contains(new AnomalyReport(description,i)))
							result.put(new Point(i,test.getLine(feature).get(i)),Color.RED);
						else
							result.put(new Point(i,test.getLine(feature).get(i)),Color.BLUE);
					}
					else{
						String description=cf.feature1+"-"+cf.feature2;
						if(detection.contains(new AnomalyReport(description,i)))
							result.put(new Point(test.getLine(cf.feature1).get(i),test.getLine(cf.feature2).get(i))
									,Color.RED);
						else
							result.put(new Point(test.getLine(cf.feature1).get(i),test.getLine(cf.feature2).get(i))
									,Color.BLUE);
					}
				}
			}
		}
		return result;
	}

	@Override
	public void shutDown(){
		wantToSuspend=true;
		task.shutDown();
		if(writeToFlightGear!=null)
			writeToFlightGear.close();
		if(fg!=null) {
			try {
				fg.close();
			} catch (IOException e) { }
		}
	}
}