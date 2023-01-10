package anomaly_detectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZScoreAnomalyDetector implements TimeSeriesAnomalyDetector {
	
	private List<Float> thArr;

	@Override
	public void learnNormal(TimeSeries ts) {
		thArr= new ArrayList<>();
		ts.getInfo().forEach(col->{
			float[] colArr=ts.getLineAsArray(ts.getInfo().indexOf(col));
			thArr.add(zScore(colArr,colArr.length-1));
		});
	}

	private float zScore(float[] col,int lastIndex) {
		float th=0;
		float z;
		float[] tmp=new float[lastIndex+1];
		System.arraycopy(col,0,tmp,0,lastIndex+1);
		float var=(float)Math.sqrt(StatLib.var(tmp));
		float avg=StatLib.avg(tmp);
		for(int i=0;i<=lastIndex;i++)
		{
			z=(Math.abs(col[i]-avg)/ var);
			if(th<z)
				th=z;
		}
		return th;
	}
	
	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> detected = new ArrayList<AnomalyReport>();
		ArrayList<Float> curr;
		float[] curr2;
		ArrayList<Float> info;
		
		for ( int i=0; i<ts.getLength();i++) {
			curr = new ArrayList<>();
			info = ts.getInfo().get(i);
			curr.addAll(info);
			float[] arr=StatLib.toFloat(curr);
			for (int j=0; j<info.size(); j++)
			{
				float z = zScore(arr,j);
				if (z>thArr.get(i))
				{
					detected.add(new AnomalyReport(ts.getTitles().get(i),j+1));
				}//if
			}//for
		}//for
		return detected;
	}
	@Override
	public List<AnomalyReport> detectOnlyByFeature(TimeSeries ts,String feature){
		List<AnomalyReport> res=new ArrayList<>();
		if(!ts.getTitles().contains(feature))
			return res;
		ArrayList<Float> curr;
		ArrayList<Float> info;
		int i=ts.getTitles().indexOf(feature);
		curr = new ArrayList<>();
		info = ts.getInfo().get(i);
		curr.addAll(info);
		float[] arr=StatLib.toFloat(curr);
		for (int j=0; j<info.size(); j++)
		{
			float z = zScore(arr,j);
			if (z>thArr.get(i))
			{
				res.add(new AnomalyReport(ts.getTitles().get(i),j));
			}//if
		}//for
		return res;
	}
	@Override
	public Shape sendShape(String feature) {
		return null;
	}
	@Override
	public int detectBy2Or1Parameter(String feature){return 1;}
}
