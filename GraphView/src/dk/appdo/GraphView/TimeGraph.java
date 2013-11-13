package dk.appdo.GraphView;

import android.graphics.PointF;

public class TimeGraph extends Graph<TimeGraph.TimePoint> {

	private final static long MILLIS_IN_SECOND = 1000;

	private final static long MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60;

	private final static long MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;

	private final static long MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;

	@Override
	public void getPoint(TimePoint point, PointF resultPoint) {
		long first = size() > 0 ? get(0).getTime() : point.getTime();

		long millisSinceFirst = point.getTime() - first;

		float x = millisSinceFirst / MILLIS_IN_SECOND;
		float y = point.getData().floatValue();

		resultPoint.set(x, y);
	}

	public static class TimePoint<T extends Number> {

		private long mTime;
		private T mData;

		public TimePoint(long time, T data) {
			mTime = time;
			mData = data;
		}

		public long getTime() {
			return mTime;
		}

		public T getData() {
			return mData;
		}
	}
}
