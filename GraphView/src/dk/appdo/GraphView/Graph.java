package dk.appdo.GraphView;

import android.graphics.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Graph<T> {

	public static final int DIRECTION_DOWN = 1;
	public static final int DIRECTION_UP = -1;

	private static final int DEFAULT_GRAPH_COLOR = Color.GRAY;
	private static final int DEFAULT_GRAPH_WIDTH = 5;

	private static final int DEFAULT_GRAPH_DIRECTION = DIRECTION_UP;
	//private static final int DEFAULT_GRAPH_DIRECTION = DIRECTION_UP;

	private int mGraphYAxisDirection = DEFAULT_GRAPH_DIRECTION;

	private List<T> mPoints;

	private Path mPrevPath;
	private Path mPath;
	private Path mDrawPath;

	private PointF mPrevPoint;
	private PointF mTmpPoint;

	private Paint mPathPaint;

	private int mWidth;
	private int mHeight;

	private float mXFactor = 1;
	private float mYFactor = mGraphYAxisDirection;
	private float mXOffset = 0;
//	private float mYOffset = (mHeight * (1 - mGraphXBase));

	public Graph(int width, int height) {

		mWidth = width;
		mHeight = height;

		mPoints = new ArrayList<T>();

		mPath = new Path();
		mDrawPath = new Path();
		mPrevPath = new Path();

		mTmpPoint = new PointF();
		mPrevPoint = new PointF();

		mPathPaint = new Paint();
		initPaint();
	}

	private void initPaint() {
		mPathPaint.setColor(DEFAULT_GRAPH_COLOR);
		mPathPaint.setStyle(Paint.Style.STROKE);
		mPathPaint.setAntiAlias(true);
		mPathPaint.setStrokeJoin(Paint.Join.ROUND);
		mPathPaint.setStrokeWidth(DEFAULT_GRAPH_WIDTH);
	}

	public void draw(Canvas canvas) {
		canvas.drawPath(mPath, mPathPaint);
	}

//	public void add(T point) {
//		getPoint(point, mTmpPoint);
//
//		float px = (p.x * mXFactor + mXOffset);
//		float prx = (prev.x * mXFactor + mXOffset);
//		float pnx = (next.x * mXFactor + mXOffset);
//		float py = (p.y * mYFactor + mYOffset);
//		float pry = (prev.y * mYFactor + mYOffset);
//		float pny = (next.y * mYFactor + mYOffset);
//
//		float dx = (pnx - prx) / 3;
//		float dy = (pny - pry) / 3;
//
//		if (mPoints.size() == 0) {
//			mPath.moveTo(px, py);
//			mPrevPath.moveTo(px, py);
//		} else {
//			mPath.set(mPrevPath);
//			mPath.cubicTo(prx, pry, px - dx, py - dy, px, py);
//
//			if (mPoints.size() > 1) {
//
//			}
//		}
//
//
//		mPoints.add(point);
//	}

	public T get(int i) {
		return mPoints.get(i);
	}

	public abstract void getPoint(T point, PointF resultPoint);
}
