package anomaly_detectors;

import java.util.List;

public class Circle extends Shape {
	public Point center;
	public float radius;
	
	public Circle(final Point center, float radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public Circle(float x, float y, float radius) {
		center = new Point(x, y);
		this.radius = radius;
	}
	
	public Circle(final Point p1, final Point p2) {
		center = new Point((float)((p1.x + p2.x) * 0.5), (float)((p1.y + p2.y) * 0.5));
		radius = center.distanceTo(p1);
	}
	
	public Circle(final Point p1, final Point p2, final Point p3) {
		final float P2_MINUS_P1_Y = p2.y - p1.y;
		final float P3_MINUS_P2_Y =  p3.y - p2.y;

		if (P2_MINUS_P1_Y == 0.0 || P3_MINUS_P2_Y == 0.0) {
			center = new Point((float)0,(float)0);
			radius = (float)0;
		}
		else {
			final float A = -(p2.x - p1.x) / P2_MINUS_P1_Y;
			final float A_PRIME = -(p3.x - p2.x) / P3_MINUS_P2_Y;
			final float A_PRIME_MINUS_A = A_PRIME - A;
			
			if (A_PRIME_MINUS_A == 0.0) {
				center = new Point((float)0, (float)0);
				radius = (float)0;
			}
			else {
				final float P2_SQUARED_X = p2.x * p2.x;
				final float P2_SQUARED_Y = p2.y * p2.y;

				
				final float B = (float) ((P2_SQUARED_X - p1.x * p1.x + P2_SQUARED_Y - p1.y * p1.y) /
						         (2.0 * P2_MINUS_P1_Y));
				final float B_PRIME = (float) ((p3.x * p3.x - P2_SQUARED_X + p3.y * p3.y - P2_SQUARED_Y) /
						               (2.0 * P3_MINUS_P2_Y));
				
		
				final float XC = (B - B_PRIME) / A_PRIME_MINUS_A;
				final float YC = A * XC + B;
				
				final float DXC = p1.x - XC;
				final float DYC = p1.y - YC;
				
				center = new Point(XC, YC);
				radius = (float) Math.sqrt(DXC * DXC + DYC * DYC);
			}
		}
	}
	
	public boolean containsAllPoints(final List<Point> points) {
		for (final Point p : points) {
			if (p != center && !containsPoint(p)) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean containsPoint(final Point p) {
		return p.distanceSquaredTo(center) <= radius * radius;
	}
	
	@Override
	public String toString() {
		return center.toString()  +  ", Radius: " + radius; 
	}
}