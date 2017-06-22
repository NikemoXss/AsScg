package com.ys.www.asscg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ys.www.asscg.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyListView extends ListView implements OnScrollListener, OnClickListener {

	private enum DlistViewState {
		LV_NORMAL, // 普通状态
		LV_PULL_REFRESH, // 下拉状态
		LV_RELEASE_REFRESH, // 松开可刷新状态
		LV_LOADING;// 加载状态
	}

	private enum DListViewLoadingMore {
		LV_NORMAL, // 普通状态
		LV_LOADING, // 加载状态
		LV_OVER;// 结束状态
	}

	private DlistViewState mlistViewState = DlistViewState.LV_NORMAL;

	private View mHeadView;// 头部View
	private TextView mRefreshInfoView;// 下拉更新
	private TextView mLastRefreshTimeView;// 下拉更新
	private ProgressBar mHeadProgressBarView;// 刷新进度条
	private ImageView mArrowView;// 下拉图片

	private int mHeadViewWidth;// 头部View宽度
	private int mHeadViewHeight;// 头部View高度

	private View mFootView;// 脚View
	private TextView mLoadMoreView;// 加载更多
	private ProgressBar mFootProgressBarView;// 加载进度条

	private boolean mIsRecord;// 确保只记录一次
	private int mStartY, mMoveY;// 起点和移动距离

	private int mFirstItemIndex;// 第一项的索引

	private boolean mIsScroller = true;
	private boolean mBack = true;// 下拉返回状态

	private LoadMoreInfo mLoadMoreInfoListener;// 加载监听

	private Animation animation, reverseAnimation;// 箭头动画

	private Context mContext;

	public MyListView(Context context) {
		super(context);
		initInfo(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initInfo(context);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initInfo(context);

	}

	public void initInfo(Context context) {
		mContext = context;
		initHeadView(context);
		initAnimation();

	}

	public void showFootView(boolean show) {
		if (show) {
			initFootView(mContext);
		} else {
		}
	}

	public void initHeadView(Context context) {
		mHeadView = LayoutInflater.from(context).inflate(R.layout.list_head, null);
		mArrowView = (ImageView) mHeadView.findViewById(R.id.image);
		mHeadProgressBarView = (ProgressBar) mHeadView.findViewById(R.id.progressbar);
		mRefreshInfoView = (TextView) mHeadView.findViewById(R.id.info);
		mLastRefreshTimeView = (TextView) mHeadView.findViewById(R.id.time);

		measureView(mHeadView);

		mHeadViewWidth = mHeadView.getMeasuredWidth();
		mHeadViewHeight = mHeadView.getMeasuredHeight();

		mLastRefreshTimeView.setText("上次更新时间：2011年9月2日");

		addHeaderView(mHeadView);
		mHeadView.setPadding(0, 0 - mHeadViewHeight, 0, 0);
	}

	public void initAnimation() {
		animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(250);
		reverseAnimation.setFillAfter(true);
	}

	public void initFootView(Context context) {
		mFootView = LayoutInflater.from(context).inflate(R.layout.list_foot, null);
		mLoadMoreView = (TextView) mFootView.findViewById(R.id.loadmore);
		mFootView.setOnClickListener(this);
		mFootProgressBarView = (ProgressBar) mFootView.findViewById(R.id.progressbarfoot);

		addFooterView(mFootView);
	}

	public void setOnLoadMoreInfo(LoadMoreInfo loadmore) {
		mLoadMoreInfoListener = loadmore;
	}

	/***
	 * 作用：测量 headView的宽和高.
	 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
			doActionMove(ev);
			break;
		case MotionEvent.ACTION_DOWN:
			doActionDown(ev);
			break;
		case MotionEvent.ACTION_UP:
			doActionUp(ev);
			break;
		}
		return super.onTouchEvent(ev);
	}

	public void doActionDown(MotionEvent ev) {
		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) ev.getY();
			mIsRecord = true;
		}
	}

	public void doActionMove(MotionEvent ev) {
		mMoveY = (int) ev.getY();
		//
		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) ev.getY();
			mIsRecord = true;
		}
		// 如果不是触摸模式或者已经是加载状态 返回
		if (mIsRecord == false || mlistViewState == DlistViewState.LV_LOADING) {
			return;
		}

		int off = (mMoveY - mStartY) / 2;
		// Log.d("listview", getFirstVisiblePosition() +"  off =" + off + " mMoveY=" + mMoveY + " mStartY="
		// + mStartY);
		switch (mlistViewState) {
		case LV_NORMAL:// 正常状态
			// Log.d("list", "off = "+off+",mMoveY ="+mMoveY+",mStartY ="+mStartY);
			if (getFirstVisiblePosition() == 0) {
				if (off > 0) {
					mHeadView.setPadding(0, off - mHeadViewHeight, 0, 0);
					switchViewState(DlistViewState.LV_PULL_REFRESH);
				}
			}
			break;
		case LV_PULL_REFRESH:// 下拉更新
			setSelection(0);
			mHeadView.setPadding(0, off - mHeadViewHeight, 0, 0);
			if (off < 0) {
				mIsScroller = false;
				switchViewState(DlistViewState.LV_NORMAL);
			} else if (off > mHeadViewHeight) {
				switchViewState(DlistViewState.LV_RELEASE_REFRESH);
			}
			break;
		case LV_RELEASE_REFRESH:// 松手刷新
			setSelection(0);
			mHeadView.setPadding(0, off - mHeadViewHeight, 0, 0);
			if (off >= 0 && off <= mHeadViewHeight) {
				mBack = true;
				switchViewState(DlistViewState.LV_PULL_REFRESH);
			} else if (off < 0) {
				switchViewState(DlistViewState.LV_NORMAL);
			}

			break;
		case LV_LOADING:// 加载状态

			break;
		}

	}

	public void doActionUp(MotionEvent ev) {
		mIsRecord = false;
		mIsScroller = true;
		mBack = false;
		if (mlistViewState == DlistViewState.LV_LOADING) {
			return;
		}
		// 处理相应状态
		switch (mlistViewState) {
		case LV_NORMAL:
			break;
		case LV_PULL_REFRESH:
			mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			switchViewState(DlistViewState.LV_NORMAL);
			break;
		case LV_RELEASE_REFRESH:
			mHeadView.setPadding(0, 0, 0, 0);
			switchViewState(DlistViewState.LV_LOADING);
			onRefresh();
			break;
		}
	}

	public void onRefresh() {
		if (mLoadMoreInfoListener != null) {
			mLoadMoreInfoListener.onRefresh();
		}
	}

	public void onRefreshComplete() {
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
		switchViewState(DlistViewState.LV_NORMAL);

		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = date.format(new Date());
		mLastRefreshTimeView.setText("上次更新时间：" + time);

		// mArrowView.setVisibility(View.VISIBLE);
		// mHeadProgressBarView.setVisibility(View.GONE);
		// mRefreshInfoView.setText("下拉可以刷新");
	}

	public void onLoadMoreComplete() {
		if (mLoadMoreView != null) {
			mLoadMoreView.setVisibility(View.VISIBLE);
		}
		if (mFootProgressBarView != null) {
			mFootProgressBarView.setVisibility(View.GONE);
		}
	}

	public void switchViewState(DlistViewState state) {
		switch (state) {
		case LV_NORMAL:// 正常状态
			mArrowView.clearAnimation();
			// mArrowView.setImageResource(R.drawable.p_down);
			break;
		case LV_LOADING:// 加载状态
			mHeadProgressBarView.setVisibility(View.VISIBLE);
			mArrowView.setVisibility(View.GONE);
			mRefreshInfoView.setText("载入中....");
			mArrowView.clearAnimation();
			break;
		case LV_PULL_REFRESH:// 下拉更新
			mHeadProgressBarView.setVisibility(View.GONE);
			mArrowView.setVisibility(View.VISIBLE);
			mRefreshInfoView.setText("下拉可以刷新");
			mArrowView.clearAnimation();
			if (mBack) {
				mBack = false;
				mArrowView.clearAnimation();
				mArrowView.startAnimation(reverseAnimation);
			}
			break;
		case LV_RELEASE_REFRESH:// 松手刷新
			mHeadProgressBarView.setVisibility(View.GONE);
			mArrowView.setVisibility(View.VISIBLE);
			mRefreshInfoView.setText("松手获取更多");

			mArrowView.clearAnimation();
			mArrowView.startAnimation(animation);

			break;
		}
		mlistViewState = state;
	}

	public interface LoadMoreInfo {
		// 加载
		public void onLoadMore();

		// 下拉刷新
		public void onRefresh();
	}

	@Override
	public void onClick(View v) {
		if (mLoadMoreInfoListener != null) {
			mLoadMoreInfoListener.onLoadMore();
			mFootProgressBarView.setVisibility(View.VISIBLE);
			mLoadMoreView.setVisibility(View.GONE);
		}

	}

}
