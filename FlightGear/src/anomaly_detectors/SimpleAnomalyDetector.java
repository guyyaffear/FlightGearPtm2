package anomaly_detectors;

import java.util.List;
import java.util.ArrayList;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
		
	public static float thForCorrelation = (float) 0.5;
	public static float thForAnomaly=10;
	private List<CorrelatedFeatures> normalModel, correlatedOnlyForThemself;
	public SimpleAnomalyDetector() {
		super();
		this.normalModel = null;
	}
	public SimpleAnomalyDetector(float thForCorrelation) {
		this.thForCorrelation=thForCorrelation;
	}
	@Override
	public void learnNormal(TimeSeries ts) {
		normalModel = new ArrayList<>();
		correlatedOnlyForThemself =new ArrayList<>();
		float pear, max, maxAbsPear;
		int maxPlace;
		Line l;
		float [] valX, valY;
		Point[] points;
		for (int i=0; i<ts.getSize();i++) {
			maxAbsPear=0;
			maxPlace=-1;
			valX= ts.getLineAsArray(i);
			for (int j=i+1; j<ts.getSize();j++) {
				valY= ts.getLineAsArray(j);
				pear =StatLib.pearson(valX,valY);	
				if (Math.abs(pear)>=thForCorrelation && Math.abs(pear)>maxAbsPear)
				{
					maxAbsPear=Math.abs(pear);
					maxPlace=j;
				}
			}//for j
			if(maxPlace!=-1) {
			valY= ts.getLineAsArray(maxPlace);
			pear =StatLib.pearson(valX,valY);
			points = new Point[valX.length];
			for(int k=0;k<points.length;k++)
				points[k]= new Point(valX[k],valY[k]);
			l =StatLib.linear_reg(points);
			max=0;
			for (Point p : points)
				max = Math.max(max, StatLib.dev(p, l));
			normalModel.add(new CorrelatedFeatures(ts.getTitles().get(i),
					ts.getTitles().get(maxPlace), pear, l, max));
			}//if
			else
				correlatedOnlyForThemself.add(new CorrelatedFeatures(ts.getTitles().get(i),
						ts.getTitles().get(i), 0, null,0));
		}//for i
	}


	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> detected = new ArrayList<AnomalyReport>();
		Point p;
		for ( int i=0; i<ts.getLength();i++) {
			for (CorrelatedFeatures f : normalModel)
			{
				p=new Point(ts.getLine(f.feature1).get(i),
						ts.getLine(f.feature2).get(i));
				if (StatLib.dev(p,f.lin_reg)>thForAnomaly)
				{
					detected.add(new AnomalyReport(f.feature1+"-"+f.feature2,i+1));
				}
			}
		}
		return detected;
	}
	
	public List<CorrelatedFeatures> getNormalModel(){
		return normalModel;
	}
	public List<CorrelatedFeatures> getCorrelatedOnlyForThemself(){
		return correlatedOnlyForThemself;
	}
	@Override
	public List<AnomalyReport> detectOnlyByFeature(TimeSeries ts,String feature){
		List<AnomalyReport> res=new ArrayList<>();
		if(!ts.getTitles().contains(feature))
			return res;
		Point p;
		CorrelatedFeatures relevant=null;
		for(CorrelatedFeatures cf:normalModel){
			if(cf.feature1.equals(feature)||cf.feature2.equals(feature)){
				if(relevant==null)
					relevant=cf;
				else
					if(cf.corrlation>relevant.corrlation)
						relevant=cf;
			}
		}
		if(relevant==null)
			return res;
		for ( int i=0; i<ts.getLength();i++) {
			p=new Point(ts.getLine(relevant.feature1).get(i),
					ts.getLine(relevant.feature2).get(i));
			if (StatLib.dev(p,relevant.lin_reg)>thForAnomaly)
				res.add(new AnomalyReport(relevant.feature1+"-"+relevant.feature2,i));
		}
		return res;
	}
	@Override
	public Shape sendShape(String feature){
		CorrelatedFeatures mostRelevant=null;
		for(CorrelatedFeatures cf:normalModel){
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
		return null;
	}
	@Override
	public int detectBy2Or1Parameter(String feature){return 2;}
}
