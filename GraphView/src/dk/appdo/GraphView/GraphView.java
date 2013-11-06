package dk.appdo.GraphView;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jonas Pedersen
 * Date: 11/4/13
 * Time: 16:00
 */
public class GraphView extends FrameLayout {

	private static final int DEFAULT_GRAPH_COLOR = Color.GRAY;
	private static final int DEFAULT_GRAPH_WIDTH = 5;

	private List<Point> mData = new ArrayList<Point>();

	Paint mGraphPaint = new Paint();

	private CanvasView mView;

	public GraphView(Context context) {
		this(context, null);
	}

	public GraphView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GraphView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mGraphPaint.setAntiAlias(true);
		mGraphPaint.setColor(DEFAULT_GRAPH_COLOR);
		mGraphPaint.setStyle(Paint.Style.STROKE);
		mGraphPaint.setStrokeWidth(DEFAULT_GRAPH_WIDTH);
		mGraphPaint.setStrokeJoin(Paint.Join.ROUND);

		mView = new CanvasView(context);

		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		addView(mView, params);
	}

	public void addPoint(Point p) {
		mData.add(p);
		mView.invalidate();
	}

	public void addAllPoints(List<Point> points) {
		mData.addAll(points);
		mView.invalidate();
	}

	public void setGraphColor(int color) {
		mGraphPaint.setColor(color);
	}

	private class CanvasView extends View {

		private int mHeight;
		private int mWidth;
		private Path mPath = new Path();
		private Point mTmpPoint = new Point();

		public CanvasView(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			mPath.reset();
			if (mData.size() > 0) {
				mPath.moveTo(mData.get(0).x, mData.get(0).y);
				for (int i = 1; i < mData.size(); i++) {
					Point p = mData.get(i);
					Point prev = mData.get((i-1));
					mTmpPoint.set((p.x + prev.x) / 2, (p.y + prev.y) / 2);
					mPath.quadTo((prev.x + mTmpPoint.x) / 2, prev.y, mTmpPoint.x, mTmpPoint.y);
					mPath.quadTo((mTmpPoint.x + p.x) / 2, p.y, p.x, p.y);
					canvas.drawCircle(p.x, p.y, 10, mGraphPaint);
				}
				mPath.lineTo(mData.get(mData.size()-1).x, mData.get(mData.size()-1).y);

				canvas.drawPath(mPath, mGraphPaint);
			}
		}
	}
}
