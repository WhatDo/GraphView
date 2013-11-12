package dk.appdo.GraphView;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
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
		List<PointF> points = new ArrayList<PointF>();
		getWindowManager().getDefaultDisplay().getSize(P);
		for (int i = 0; i < 10; i++) {
			int amp = ((int) (Math.random() * 10));
			for (int j = -10; j < 10; j++) {
				PointF p = new PointF((j + 10 + i * 20) * P.x/200, -j*j * amp + amp*100);
//				Log.d("points", "Added (" + p.x + ", " + p.y + ")");
				points.add(p);
			}

		}
//		points.add(new Point(0, 70));
//		points.add(new Point(300, 72));
//		points.add(new Point(600, 64));
//		graph.addAllPoints(points);
		graph.setDrawDataPoints(true);
		setContentView(graph);
	}
}
