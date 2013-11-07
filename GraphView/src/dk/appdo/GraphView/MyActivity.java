package dk.appdo.GraphView;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GraphView graph = new GraphView(this);
		Point P = new Point();
		List<Point> points = new ArrayList<Point>();
		getWindowManager().getDefaultDisplay().getSize(P);
		for (int i = 0; i < 20; i++) {
			Point p = new Point(i * P.x/20, i%10 * ((int) (Math.random() * 100)));
			points.add(p);
		}
		graph.addAllPoints(points);
		setContentView(graph);
	}
}
