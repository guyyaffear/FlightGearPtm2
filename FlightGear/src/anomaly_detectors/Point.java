package anomaly_detectors;

public class Point {
	public final float x,y;
	public Point(float x, float y) {
		this.x=x;
		this.y=y;
	}
	public float distanceSquaredTo(final Point p) {
		final float DX = x - p.x;
		final float DY = y - p.y;
		
		return DX * DX + DY * DY;
	}
	
	public float distanceTo(final Point p) {
		return (float)Math.sqrt(distanceSquaredTo(p));
	}

	@Override
	public String toString() {
		return "X: " + x + ", Y: " + y; 
	}

	@Override
	public boolean equals(Object other){
		if(other==this)
			return true;
		if(!(other instanceof Point))
			return false;
		Point p=(Point) other;
		return this.x==p.x&&this.y==p.y;
	}
}
