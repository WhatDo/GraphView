package dk.appdo.GraphView;

import android.graphics.*;
import android.util.Log;
import android.view.View;

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

	private RectF mWindowRect;
	private RectF mPathRect;

	private Matrix mMatrix;

	private PointF mPrevPoint;
	private PointF mPrevPoint2;
	private PointF mTmpPoint;

	private Paint mPathPaint;

	private int mWidth;
	private int mHeight;

	private float mXFactor = 1;
	private float mYFactor = mGraphYAxisDirection;
	private float mXOffset = 0;
	private float mYOffset = mHeight;

	private GraphView mParent;

	private boolean mGotDimensions = false;

	public Graph() {

		mPoints = new ArrayList<T>();

		mPath = new Path();
		mDrawPath = new Path();
		mPrevPath = new Path();

		mWindowRect = new RectF();
		mPathRect = new RectF();

		mMatrix = new Matrix();

		mTmpPoint = new PointF();
		mPrevPoint = new PointF();
		mPrevPoint2 = new PointF();

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
		Log.d("Graph", "Drawing");
		mPath.computeBounds(mPathRect, false);
		mMatrix.setRectToRect(mPathRect, mWindowRect, Matrix.ScaleToFit.FILL);
		mPath.transform(mMatrix, mDrawPath);

		canvas.drawPath(mDrawPath, mPathPaint);
	}

	public void addAll(List<T> points) {
		for (T lel : points) {
			add(lel);
		}
	}

	public void add(T point) {

		if (mGotDimensions) {
			getPoint(point, mTmpPoint);
			Log.d("Graph", "Added " + mTmpPoint.x + ", " + mTmpPoint.y + ")");
			pathToPoint(mTmpPoint);
		}

		mPoints.add(point);

		if (mParent != null) {
			mParent.notifyDatasetChanged();
		}
	}

	private void pathToPoint(PointF p) {

		float px = (p.x * mXFactor + mXOffset);
		float py = (p.y * mYFactor + mYOffset);

		PointF prev = mPoints.size() == 0 ? mTmpPoint : mPrevPoint;

		float prx = (prev.x * mXFactor + mXOffset);
		float pry = (prev.y * mYFactor + mYOffset);

		// Used for mPrevPath
		float pnx;
		float pny;

		float dx = 0;
		float dy = 0;

		if (mPoints.size() == 0) {
			mPath.moveTo(px, py);
			mPrevPath.moveTo(px, py);
		} else {
			mPath.set(mPrevPath);
			Log.d("Graph", "Start " + prx + ", " + pry + ")" + "; End " + px + ", " + py + ")");
			mPath.cubicTo(prx, pry, px - dx, py - dy, px, py);

			if (mPoints.size() > 1) {
				pnx = px;
				pny = py;

				px = prx;
				py = pry;

				PointF prr = mPoints.size() > 2 ? mPrevPoint2 : mPrevPoint;
				prx = prr.x * mXFactor + mXOffset;
				pry = prr.y * mYFactor + mYOffset;

				dx = (pnx - prx) / 3;
				dy = (pny - pry) / 3;

				mPrevPath.cubicTo(prx, pry, px - dx, py - dy, px, py);
			}
		}

		mPrevPoint2.set(mPrevPoint);
		mPrevPoint.set(mTmpPoint);
	}

	public T get(int i) {
		return mPoints.get(i);
	}

	public int size() {
		return mPoints.size();
	}

	public void setDimensions(int width, int height) {
		mWidth = width;
		mHeight = height;

		mWindowRect.set(0, 0, width, height);

		mYOffset = mHeight;

		if (!mGotDimensions) {
			for (T lel : mPoints) {
				getPoint(lel, mTmpPoint);
				pathToPoint(mTmpPoint);
			}
		}

		mGotDimensions = true;
	}

	public void reset() {
		mPrevPath.reset();
		mPath.reset();
		mDrawPath.reset();
	}

	public void setParent(GraphView parent) {
		mParent = parent;
		mParent.notifyDatasetChanged();
	}

	/**
	 * Transforms the data from point into a PointF, suitable for plotting.
	 *
	 * @param point
	 * @param resultPoint
	 */
	public abstract void getPoint(T point, PointF resultPoint);
}
