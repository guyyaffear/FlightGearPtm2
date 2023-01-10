package anomaly_detectors;

import java.util.ArrayList;
import java.util.List;

public class StatLib {

	public static float[] toFloat(List<Float> l) {
		float[] retArr = new float[l.size()];
		for (int j=0; j<retArr.length;j++)
			retArr[j]=l.get(j);
		return retArr;
	}

	// simple average
	public static float avg(float[] x) {
		float avg = 0;
		for (float item : x) {
			avg += item;
		}
		avg /= x.length;
		return avg;
	}

	public static List<Point> getListPoint(List<Float> x,List<Float> y){
		List<Point> result=new ArrayList<>();
		for(int i=0;i<x.size();i++)
			result.add(new Point(x.get(i),y.get(i)));
		return result;
	}

	// returns the variance of X and Y
	public static float var(float[] x) {
		float avg1, avg2; // avg1 = average square, avg2 = average of squares
		avg1 = avg(x);
		avg1 = avg1 * avg1;
		float[] xSqr = new float[x.length];
		for (int i = 0; i < x.length; i++) {
			xSqr[i] = x[i] * x[i];
		}
		avg2 = avg(xSqr);
		return avg2 - avg1;
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y) {
		// cov(X,Y)=E(X*Y)-E(x)*E(y)
		float avgX = avg(x), avgY = avg(y);
		float[] xy = new float[y.length];// Assuming x & y are the same length
		for (int i = 0; i < xy.length; i++) {
			xy[i] = (x[i] - avgX) * (y[i] - avgY);
		}
		return avg(xy);
	}

	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y) {
		float sdX = (float) Math.sqrt(var(x));
		float sdY = (float) Math.sqrt(var(y));
		float cov = cov(x, y);
		return (cov / (sdX * sdY));
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points) {
		float[] x = new float[points.length];
		float[] y = new float[points.length];
		for (int i = 0; i < points.length; i++) {
			x[i] = points[i].x;
			y[i] = points[i].y;
		}
		float a = cov(x, y) / var(x);
		float b = avg(y) - a * avg(x);
		Line l = new Line(a, b);
		return l;
	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p, Point[] points) {
		return dev(p, linear_reg(points));
	}

	// returns the deviation between point p and the line
	public static float dev(Point p, Line l) {
		return Math.abs(l.f(p.x) - p.y);
	}
}
