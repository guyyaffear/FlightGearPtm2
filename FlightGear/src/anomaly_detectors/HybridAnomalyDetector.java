package anomaly_detectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.*;

public class HybridAnomalyDetector implements TimeSeriesAnomalyDetector {
	
	private HashMap<String,HashSet<CorrelatedFeatures>> featuresToAlgorithm;
	private HashMap<CorrelatedFeatures,Circle> welzlCircleModel=new HashMap<>();//storing circles for each
	//pair of correlated features
	private SimpleAnomalyDetector regressionDetector=new SimpleAnomalyDetector();
	private ZScoreAnomalyDetector zScoreDetector=new ZScoreAnomalyDetector();

	@Override
	public void learnNormal(TimeSeries ts) {
		regressionDetector=new SimpleAnomalyDetector((float)0.5);
		regressionDetector.learnNormal(ts);
		List<CorrelatedFeatures> mostCorrelated=regressionDetector.getNormalModel();
		WelzlAlgorithm algorithm=new WelzlAlgorithm();
		/* ----------sorting the features by the correlation value */
		featuresToAlgorithm=new HashMap<>();
		featuresToAlgorithm.put("ZScore", new HashSet<>());
		featuresToAlgorithm.put("Regression", new HashSet<>());
		featuresToAlgorithm.put("Welzl", new HashSet<>());
		featuresToAlgorithm.get("ZScore").addAll(regressionDetector.getCorrelatedOnlyForThemself());
		for(CorrelatedFeatures c:mostCorrelated) {
			if(Math.abs(c.corrlation)>=0.95)
				featuresToAlgorithm.get("Regression").add(c);
			else {
				/*if(Math.abs(c.corrlation)<0.5)
					featuresToAlgorithm.get("ZScore").add(c);
				else*/
					featuresToAlgorithm.get("Welzl").add(c);
			}
		}
		for(CorrelatedFeatures c:featuresToAlgorithm.get("Welzl")) {
			welzlCircleModel.put(c,algorithm.miniDisk(
					StatLib.getListPoint(ts.getLine(c.feature1), ts.getLine(c.feature2))));
		}
		zScoreDetector.learnNormal(ts);
		regressionDetector=new SimpleAnomalyDetector((float) 0.95);
		regressionDetector.learnNormal(ts);
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> detected=new ArrayList<>();
		detected.addAll(regressionDetector.detect(ts));
		TimeSeries testZScoreAlgorithm=new TimeSeries();
		// ------build new TimeSeries-----
		for(CorrelatedFeatures c:featuresToAlgorithm.get("Welzl")) {
			int count=1;
			List<Point> dataFeature=StatLib.getListPoint(ts.getLine(c.feature1), ts.getLine(c.feature2));
			for(Point p:dataFeature) {
				if(!welzlCircleModel.get(c).containsPoint(p))
					detected.add(new AnomalyReport(c.feature1+"-"+c.feature2, count));
				++count;
			}
		}
		for(CorrelatedFeatures c:featuresToAlgorithm.get("ZScore")) {
			testZScoreAlgorithm.addCol(c.feature1,ts.getLine(c.feature1));
			testZScoreAlgorithm.addCol(c.feature2,ts.getLine(c.feature2));
		}
		detected.addAll(zScoreDetector.detect(testZScoreAlgorithm));
		return detected;
	}
	@Override
	public List<AnomalyReport> detectOnlyByFeature(TimeSeries ts,String feature){
		if(!ts.getTitles().contains(feature))
			return new ArrayList<>();
		for(CorrelatedFeatures cf:featuresToAlgorithm.get("Regression"))
			if(cf.feature1.equals(feature)||cf.feature2.equals(feature))
				return regressionDetector.detectOnlyByFeature(ts,feature);
		CorrelatedFeatures relevant=null;
		List<AnomalyReport> res=new ArrayList<>();
		for(CorrelatedFeatures cf:featuresToAlgorithm.get("Welzl")){
			if(cf.feature1.equals(feature)||cf.feature2.equals(feature)){
				if(relevant==null)
					relevant=cf;
				else
					if(cf.corrlation>relevant.corrlation)
						relevant=cf;
			}
		}
		if(relevant!=null) {
			int count = 0;
			List<Point> dataFeature = StatLib.getListPoint(ts.getLine(relevant.feature1), ts.getLine(relevant.feature2));
			for (Point p : dataFeature) {
				if (!welzlCircleModel.get(relevant).containsPoint(p))
					res.add(new AnomalyReport(relevant.feature1 + "-" + relevant.feature2, count));
				++count;
			}
			return res;
		}
		for(CorrelatedFeatures cf:featuresToAlgorithm.get("ZScore"))
			if(cf.feature1.equals(feature)||cf.feature2.equals(feature))
				return zScoreDetector.detectOnlyByFeature(ts,feature);
		return res;
	}
	@Override
	public Shape sendShape(String feature) {
		CorrelatedFeatures mostRelevant=null;
		for(CorrelatedFeatures cf:featuresToAlgorithm.get("Regression")){
			if(cf.feature1.equals(feature)||cf.feature2.equals(feature)){
				if(mostRelevant==null)
					mostRelevant=cf;
				else{
					if(mostRelevant.corrlation<cf.corrlation)
						mostRelevant=cf;
				}
			}
		}
		if(mostRelevant!=null)
			return mostRelevant.lin_reg;
		for(CorrelatedFeatures cf:featuresToAlgorithm.get("Welzl")){
			if(cf.feature1.equals(feature)||cf.feature2.equals(feature)){
				if(mostRelevant==null)
					mostRelevant=cf;
				else{
					if(mostRelevant.corrlation<cf.corrlation)
						mostRelevant=cf;
				}
			}
		}
		if(mostRelevant!=null)
			return welzlCircleModel.get(mostRelevant);
		return null;
	}
	@Override
	public int detectBy2Or1Parameter(String feature){
		for(CorrelatedFeatures cf:featuresToAlgorithm.get("Regression")){
			if(cf.feature1.equals(feature)||cf.feature2.equals(feature))
				return 2;
		}
		for(CorrelatedFeatures cf:featuresToAlgorithm.get("Welzl")){
			if(cf.feature1.equals(feature)||cf.feature2.equals(feature))
				return 2;
		}
		return 1;
	}
}
