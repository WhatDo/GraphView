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

	public static final int DIRECTION_DOWN = 1;
	public static final int DIRECTION_UP = -1;

	private static final int DATA_WIDTH_AUTO = -1;

	private static final int DEFAULT_GRAPH_COLOR = Color.GRAY;
	private static final int DEFAULT_GRAPH_WIDTH = 5;

	private static final int DEFAULT_GRAPH_DIRECTION = DIRECTION_UP;
	//private static final int DEFAULT_GRAPH_DIRECTION = DIRECTION_UP;

	private int mGraphYAxisDirection = DEFAULT_GRAPH_DIRECTION;

	private List<Point> mData = new ArrayList<Point>();

	private RotatingQueue<Point> mQueue;

	private Paint mGraphPaint = new Paint();

	private CanvasView mView;

	private boolean mDrawDataPoints = false;

	private float mMaxVal = Integer.MIN_VALUE;

	private float mMinVal = Integer.MAX_VALUE;

	private boolean mAutoScaleY = true;

	private int mXScale = 1;

	private int mYScale = 1;

	private int mDataWidth = DATA_WIDTH_AUTO;

	private float mGraphXBase = 0.0f;

	private boolean mDrawXAxis = true;

	public GraphView(Context context) {
		this(context, null);
	}

	public GraphView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GraphView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initPaints();

		mView = new CanvasView(context);

		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		addView(mView, params);
	}

	private void initPaints() {
		mGraphPaint.setAntiAlias(true);
		mGraphPaint.setColor(DEFAULT_GRAPH_COLOR);
		mGraphPaint.setStyle(Paint.Style.STROKE);
		mGraphPaint.setStrokeWidth(DEFAULT_GRAPH_WIDTH);
		mGraphPaint.setStrokeJoin(Paint.Join.ROUND);
		mGraphPaint.setPathEffect(new CornerPathEffect(10));
	}

	public void setGraphPositiveYAxis(int direction) {
		mGraphYAxisDirection = direction;
	}

	public void setDrawDataPoints(boolean toDraw) {
		mDrawDataPoints = toDraw;
	}

	public void addPoint(Point p) {
		mData.add(p);
		updateMaxMinVal(p);
		addToQueueIfNotNull(p);
		mView.invalidate();
	}

	public void addAllPoints(List<Point> points) {
		mData.addAll(points);
		updateMaxMinVal(points);
		addToQueueIfNotNull(points);

		mView.invalidate();
	}

	private void addToQueueIfNotNull(Point p) {
		if (mQueue != null) {
			mQueue.insertElement(p);
		}
	}

	private void addToQueueIfNotNull(List<Point> points) {
		for (Point p : points) {
			addToQueueIfNotNull(p);
		}
	}

	private void updateMaxMinVal(Point p) {
		if (p.y > mMaxVal) {
			mMaxVal = p.y;
		}
		if (p.y < mMinVal) {
			mMinVal = p.y;
		}
	}

	private void updateMaxMinVal(List<Point> points) {
		for (Point p : points) {
			updateMaxMinVal(p);
		}
	}


	public void setGraphColor(int color) {
		mGraphPaint.setColor(color);
	}

	public void setXAxisYPos(float percentage) {
		mGraphXBase = percentage;
	}

	public void setShowXAxis(boolean show) {
		mDrawXAxis = show;
	}

	/**
	 * Sets the number of points shown
	 *
	 * @param width
	 */
	public void setDataWidth(int width) {
		mQueue = new RotatingQueue<Point>(width);
		mDataWidth = width;

		for (Point p : mData) {
			mQueue.insertElement(p);
		}
	}

	private class CanvasView extends View {

		private int mHeight;
		private int mWidth;
		private Path mPath = new Path();

		private RectF mPathBounds = new RectF();
		private RectF mViewBounds = new RectF();

		public CanvasView(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {

			float xFactor = 1;
			float yFactor = mGraphYAxisDirection;
			float xOffset = 0;
			float yOffset = (mHeight * (1 - mGraphXBase));

			Matrix mMatrix = new Matrix();

			if (mDrawXAxis) {
				canvas.drawLine(0, yOffset, mWidth, yOffset, mGraphPaint);
			}

			mPath.reset();
			if (mData.size() > 0) {
				mPath.moveTo(mData.get(0).x * xFactor + xOffset, mData.get(0).y * yFactor + yOffset);
				for (int i = 1; i < mData.size(); i++) {
					Point p = mData.get(i);
					Point prev = mData.get(i - 1);
					Point next;
					if (i < mData.size() - 1)
						next = mData.get(i + 1);
					else
						next = p;

					float px = (p.x * xFactor + xOffset);
					float prx = (prev.x * xFactor + xOffset);
					float pnx = (next.x * xFactor + xOffset);
					float py = (p.y * yFactor + yOffset);
					float pry = (prev.y * yFactor + yOffset);
					float pny = (next.y * yFactor + yOffset);

					float dx = (pnx - prx) / 3;
					float dy = (pny - pry) / 3;

					mPath.cubicTo(prx, pry, px - dx, py - dy, px, py);

					if (mDrawDataPoints)
						canvas.drawCircle(px, py, 10, mGraphPaint);
				}
				mPath.computeBounds(mPathBounds, false);
				mMatrix.setRectToRect(mPathBounds, mViewBounds, Matrix.ScaleToFit.FILL);
				mPath.transform(mMatrix);
				canvas.drawPath(mPath, mGraphPaint);
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			mHeight = MeasureSpec.getSize(heightMeasureSpec);
			mWidth = MeasureSpec.getSize(widthMeasureSpec);
			mViewBounds.set(0, 0, mWidth, mHeight);
		}
	}
}
