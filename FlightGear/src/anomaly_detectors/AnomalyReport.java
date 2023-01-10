package anomaly_detectors;

public class AnomalyReport {
	public final String description;
	public final  long timeStep;
	public AnomalyReport(String description, long timeStep){
		this.description=description;
		this.timeStep=timeStep;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(!(obj instanceof AnomalyReport))
			return false;
		AnomalyReport a=(AnomalyReport)obj;
		return a.description.equals(this.description)&&a.timeStep==this.timeStep;
	}
}
