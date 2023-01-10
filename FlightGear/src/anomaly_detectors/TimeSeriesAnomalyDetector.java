package anomaly_detectors;

import java.util.List;

public interface TimeSeriesAnomalyDetector {
	void learnNormal(TimeSeries ts);
	List<AnomalyReport> detect(TimeSeries ts);
	List<AnomalyReport> detectOnlyByFeature(TimeSeries ts,String feature);
	int detectBy2Or1Parameter(String feature);
	Shape sendShape(String f);
}
