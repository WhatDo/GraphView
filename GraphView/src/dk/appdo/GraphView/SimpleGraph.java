package dk.appdo.GraphView;

import android.graphics.PointF;

/**
 * Created with IntelliJ IDEA.
 * User: jonas
 * Date: 11/13/13
 * Time: 1:12 PM
 */
public class SimpleGraph extends Graph<PointF> {

	@Override
	public void getPoint(PointF point, PointF resultPoint) {
		resultPoint.set(point);
	}
}
