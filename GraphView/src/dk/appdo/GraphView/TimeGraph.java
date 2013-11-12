package dk.appdo.GraphView;

import android.graphics.Canvas;
import android.graphics.PointF;

public class TimeGraph extends Graph<TimeGraph.TimePoint> {

	public TimeGraph(int width, int height) {
		super(width, height);
	}

	@Override
	public void draw(Canvas canvas) {
	}

	@Override
	public void getPoint(TimePoint point, PointF resultPoint) {
	}

	public static class TimePoint {

	}
}
