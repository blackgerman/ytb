package abiguime.tz.com.tzyoutube._commons.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._data.Video;


/**
 * Created by abiguime on 2016/8/4.
 */

public class YoutubeLayout extends ViewGroup {

    private final ViewDragHelper myDragerHelper;

    private View  mDescView;
    private MyRelativeLayout mHeaderView;

    private float initialMotionX, initialMotionY;

    /**
     * distance on which we can drag the view
     */
    private int dragRange;

    /**
     *
     */
    private int mTop;

    /**
     *
     */
    private float mDragOffset;


    private float SENSITIVITY = 1.0f;

    public boolean justStarted = true;

    public YoutubeLayout(Context context) {
        this(context, null);
    }

    public YoutubeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YoutubeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myDragerHelper = ViewDragHelper.create(this, SENSITIVITY, new MyDragHelper());
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDescView = findViewById(R.id.viewDesc);
        mHeaderView = (MyRelativeLayout) findViewById(R.id.viewHeader);
        // force the iamgeview to redraw
    }

    /* OnLayout is need to tell a view how is he going to draw
        *  himself
        **/
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        dragRange = getHeight() - mHeaderView.getHeight();
        // remove margin bottom
        dragRange -= getContext().getResources().getDimensionPixelSize(R.dimen.item_margin_bottom);
        /*positions of the top and bottom views have to be computed
        * programmatically
        * */
        if (justStarted) {
            justStarted = false;
            mTop = getHeight() -
                    getVideo16_9Height() -
                    getContext().getResources().getDimensionPixelSize(R.dimen.item_margin_bottom);;
            mHeaderView.setPivotY(getVideo16_9Height());
            mHeaderView.setScaleY(1/3);
        }
        // initial positions.
        mHeaderView.layout(0, mTop, r, mTop+mHeaderView.getMeasuredHeight());
        mDescView.layout(0,mTop+mHeaderView.getMeasuredHeight(), r, b+mTop)
        ;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    /*设置改播放的音乐*/
    public void setVideo(Video video, boolean isFirst) {
        mHeaderView.setVideo(video);
        // 把显示视频的viewgroup 显示成全屏
        if (!isFirst)
            smoothSlideTo(0f);
    }

    public void requestHeaderContent() {
        if (mHeaderView != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHeaderView.setVideoClubSize();
                    mHeaderView.setBackgroundResource(android.R.color.transparent);
                }
            }, 300);
        }
    }

    private class MyDragHelper extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mHeaderView == child;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            mTop = top;
            mDragOffset = (float) top / dragRange;
            mHeaderView.setPivotY(mHeaderView.getHeight());
            mHeaderView.setScaleY((1 - 2*mDragOffset / 3));
            mDescView.setAlpha(1 - mDragOffset);
            mHeaderView.setVideoClubSize();
            // 本布局最后的背景颜色
            // 1 :: 透明
            // 0 :: 黑色
            Drawable dr = new ColorDrawable(Color.BLACK);
            int alpha =  (int) ((1-mDragOffset)*255);
            dr.setAlpha(alpha > 255 ? 255 : alpha);
            setBackground(dr);
            requestLayout();
        }


        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int top = getPaddingTop();
           /* if (yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
                top += dragRange;
            }*/
            if (mDragOffset<0.5f)
                smoothSlideTo(0f);
            else
                smoothSlideTo(1f);
            Log.d("DragLayout", "--- "+mDragOffset+" --- released ");
//            myDragerHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
//            myDragerHelper.settleCapturedViewAt()
        }

        /**
         * 1- allow the vertical scrolling of the views
         */
       /*implements this to allow vertical dragging*/
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {

            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mHeaderView.getHeight();
            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop; /* sends back the new top of the view */
        }


//        @Override
//        public int getViewVerticalDragRange(View child) {
//            return dragRange;
//        }
    }

    private int getVideo16_9Height() {
        return 9*getResources().getDisplayMetrics().widthPixels/16;
    }


    @Override
    public void computeScroll() {
        if (myDragerHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (( action != MotionEvent.ACTION_DOWN)) {
            myDragerHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            myDragerHelper.cancel();
            return false;
        }

        final float x = ev.getX();
        final float y = ev.getY();
        boolean interceptTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                initialMotionX = x;
                initialMotionY = y;
                interceptTap = myDragerHelper.isViewUnder(mHeaderView, (int) x, (int) y);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float adx = Math.abs(x - initialMotionX);
                final float ady = Math.abs(y - initialMotionY);
                final int slop = myDragerHelper.getTouchSlop();
                if (ady > slop && adx > ady) {
                    myDragerHelper.cancel();
                    return false;
                }
            }
        }
        return myDragerHelper.shouldInterceptTouchEvent(ev) || interceptTap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        myDragerHelper.processTouchEvent(event);

        final int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        // check whether the view supplied is under the given point.
        // as a draging action or what so ever...
        boolean isHeaderViewUnder = myDragerHelper.isViewUnder(mHeaderView, (int)x, (int)y);

        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                /* when use starts motion, get the starting the point */
                initialMotionX = x;
                initialMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
                /* when the user leave the screen,
                * get the leaving point coordinates */
                final float dx = x - initialMotionX;
                final float dy = y - initialMotionY;
                final int slop = myDragerHelper.getTouchSlop();
                if (dx * dx + dy * dy < slop * slop && isHeaderViewUnder) {
                    if (mDragOffset == 0) {
                        smoothSlideTo(1f);
                    } else {
                        smoothSlideTo(0f);
                    }
                }
                break;
        }

        return isHeaderViewUnder && isViewHit(mHeaderView, (int) x, (int) y) ||
                isViewHit(mDescView, (int) x, (int) y);
    }


    /*check if the view is still entieryshowing on the screen.
    * return true if yes. */
    private boolean isViewHit(View view, int x, int y) {
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation); // get the actual position onscreen of the view.
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation); // get the actual location of the layout.
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth() &&
                screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
    }


    public boolean smoothSlideTo(float slideOffset) {

        final int topBound = getPaddingTop();
        int y = (int) (topBound + slideOffset * dragRange);
        int x = mHeaderView.getLeft();
        /*if (slideOffset == 1) {
            y -= getContext().getResources().getDimensionPixelSize(R.dimen.item_margin_bottom);
            x -= getContext().getResources().getDimensionPixelSize(R.dimen.item_margin_margin);
        }*/
        if (myDragerHelper.smoothSlideViewTo(mHeaderView, x, y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    public View getmDescView() {
        return mDescView;
    }

    public MyRelativeLayout getmHeaderView() {
        return mHeaderView;
    }
}
