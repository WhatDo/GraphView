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

	//private static final int

	private static final int DEFAULT_GRAPH_COLOR = Color.GRAY;
	private static final int DEFAULT_GRAPH_WIDTH = 5;

	private static final int DEFAULT_GRAPH_DIRECTION = DIRECTION_UP;
	//private static final int DEFAULT_GRAPH_DIRECTION = DIRECTION_UP;

	private int mGraphYAxisDirection = DEFAULT_GRAPH_DIRECTION;

	private List<Point> mData = new ArrayList<Point>();

	private Paint mGraphPaint = new Paint();

	private CanvasView mView;

	private boolean mDrawDataPoints = false;

	private int mMaxVal = Integer.MIN_VALUE;

	private boolean mAutoScale = true;

	private int mXScale = 1;

	private int mYScale = 1;

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
	}

	public void setGraphPositiveYAxis(int direction) {
		mGraphYAxisDirection = direction;
	}

	public void setDrawDataPoints(boolean todraw) {
		mDrawDataPoints = todraw;
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

	public void setXAxisYPos(float percentage) {
		mGraphXBase = percentage;
	}

	private class CanvasView extends View {

		private int mHeight;
		private int mWidth;
		private Path mPath = new Path();

		public CanvasView(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {

			int xFactor = 1;
			int yFactor = mGraphYAxisDirection;
			int xOffset = 0;
			int yOffset = (int) (mHeight * (1-mGraphXBase));

			if (mDrawXAxis) {
				canvas.drawLine(0, yOffset, mWidth, yOffset, mGraphPaint);
			}

			mPath.reset();
			if (mData.size() > 0) {
				mPath.moveTo(mData.get(0).x, mData.get(0).y);
				for (int i = 1; i < mData.size(); i++) {
					Point p = mData.get(i);
					Point prev = mData.get((i - 1));

					int px = p.x * xFactor + xOffset;
					int prx = prev.x * xFactor + xOffset;
					int py = p.y * yFactor + yOffset;
					int pry = prev.y * yFactor + yOffset;

					int tmpx = (px + prx) / 2;
					int tmpy = (py + pry) / 2;

					mPath.quadTo((prx + tmpx) / 2, pry, tmpx, tmpy);
					mPath.quadTo((tmpx + px) / 2, py, px, py);

					if (mDrawDataPoints)
						canvas.drawCircle(p.x, p.y, 10, mGraphPaint);
				}
				mPath.lineTo(mData.get(mData.size() - 1).x, mData.get(mData.size() - 1).y);

				canvas.drawPath(mPath, mGraphPaint);
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			mHeight = MeasureSpec.getSize(heightMeasureSpec);
			mWidth = MeasureSpec.getSize(widthMeasureSpec);
		}
	}
}
