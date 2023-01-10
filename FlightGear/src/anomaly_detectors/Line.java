package anomaly_detectors;

public class Line extends Shape {
	//ax+b
	public final float a,b;
	public Line(float a, float b){
		this.a=a;
		this.b=b;
	}
	public float f(float x){
		return a*x+b;
	}
}
