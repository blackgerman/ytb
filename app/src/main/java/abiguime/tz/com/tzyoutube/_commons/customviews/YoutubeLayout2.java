package abiguime.tz.com.tzyoutube._commons.customviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._commons.core.YtbApplication;
import abiguime.tz.com.tzyoutube._commons.utils.ULog;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.constants.Constants;


/**
 * Created by abiguime on 2016/8/4.
 */

public class YoutubeLayout2 extends RelativeLayout implements
// videos playback interfaces
        SurfaceHolder.Callback,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener ,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener{

    private static final String TAG = YoutubeLayout2.class.getName();

    private final ViewDragHelper myDragerHelper;

    private View  mDescView;
    private MyRelativeLayout2 mHeaderView;

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
    private MediaPlayer mp;

    public YoutubeLayout2(Context context) {
        this(context, null);
    }

    public YoutubeLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YoutubeLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myDragerHelper = ViewDragHelper.create(this, SENSITIVITY, new MyDragHelper());
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDescView = findViewById(R.id.viewDesc);
        mHeaderView = (MyRelativeLayout2) findViewById(R.id.viewHeader);
    }

    /* OnLayout is need to tell a view how is he going to draw
        *  himself
        **/
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        dragRange = getHeight() - mHeaderView.getHeight();
        // remove margin bottom
        dragRange -= getContext().getResources().getDimensionPixelSize(R.dimen.item_margin_bottom);
        if (justStarted) {
            mTop = dragRange;
            // initial position
            mHeaderView.layout(0, mTop, r, mTop+mHeaderView.getMeasuredHeight());
            mDescView.layout(0,mTop+mHeaderView.getMeasuredHeight(), r, b+mTop);
            return;
        }
        mHeaderView.layout(0, mTop, r, mTop+mHeaderView.getMeasuredHeight());
        mDescView.layout(0,mTop+mHeaderView.getMeasuredHeight(), r, b+mTop);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (justStarted) {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
            int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

            setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                    resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
        }
    }

    /*设置改播放的音乐*/
    public void setVideo(Video video, boolean isFirst) {
        // 设置视频的主要图片 （作为正在加载图）
        setLoadingCover(video);
        // 启动播放
        startMediaPlayerPlayBack(video);
        // 把显示视频的viewgroup 显示成全屏
        if (!isFirst)
            smoothSlideTo(0f);
    }

    /*isfirst表示启动播放以后要不要启动动画*/
    public void continueVideo (boolean isFirst) {
        // 接着mainactivity 播放
        continueMediaPlayerPlayBack();
        if (!isFirst)
            smoothSlideTo(0f);
    }


    private void startMediaPlayerPlayBack(Video video) {
//        ((YtbApplication) getContext().getApplicationContext())
//                .getMediaPlayerFor(Constants.IP + video.path, this);
    }

    private void continueMediaPlayerPlayBack() {
        mp = ((YtbApplication) getContext().getApplicationContext())
                .getMediaPlayer();
        mp.setOnBufferingUpdateListener(this); // 缓存更新监听
        mp.setOnCompletionListener(this); // 播放完毕监听
        mp.setScreenOnWhilePlaying(true); // 在播放期间屏幕必须保持量的状态
        mp.setOnVideoSizeChangedListener(this); // 在视频大小手变化时 (重新设置surfaceview 的大小)
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC); // 设置音乐频道
        mHeaderView.getSv().getHolder().addCallback(this); // 绑定surfaceview和mediaplayer
    }

    private void setLoadingCover(Video video) {
        Picasso.with(getContext()).load(Constants.IP + video.coverimage)
                .placeholder(R.drawable.loading_black_cover)
                .into(mHeaderView.getIv());
        mHeaderView.getIv().setVisibility(VISIBLE);
    }
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    /* 当prepareAsync 运行完成时，就会调用本方法*/
    @Override
    public void onPrepared(final MediaPlayer mp) {
        if (mp == null) {
            Toast.makeText(getContext(), "Mediaplayer null", Toast.LENGTH_SHORT).show();
            return;
        }

        mp.setOnBufferingUpdateListener(YoutubeLayout2.this); // 缓存更新监听
        mp.setOnCompletionListener(YoutubeLayout2.this); // 播放完毕监听
        mp.setOnPreparedListener(YoutubeLayout2.this); // 准备完成监听（prepareAsync）
        mp.setScreenOnWhilePlaying(true); // 在播放期间屏幕必须保持量的状态
        mp.setOnVideoSizeChangedListener(YoutubeLayout2.this); // 在视频大小手变化时 (重新设置surfaceview 的大小)
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC); // 设置音乐频道
        mHeaderView.getSv().getHolder().addCallback(YoutubeLayout2.this); // 绑定surfaceview和mediaplayer

        postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    // 启动视频播放;
                    mHeaderView.setSvSize(mp.getVideoWidth(), mp.getVideoHeight());
                    ULog.d("xxx", "vheight "+mp.getVideoHeight()+" -- vwidth "+mp.getVideoWidth());
                    mp.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                ObjectAnimator anim = ObjectAnimator.ofFloat(mHeaderView.getIv(),
                        View.ALPHA, 1f, 0f);
                anim.setDuration(500/2);
                anim.start();
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mHeaderView.getIv().setVisibility(GONE);
                    }
                });
            }
        }, 500);
    }



    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mp == null) {
            // implement a fallback mechanism if it fails, for example if no internet or 404
            Log.w("Layout1", "MediaPlayer was not created");
            return;
        }
        // 绑定mediaplayer 与 surfaceview
        mp.setDisplay(holder);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        tryDrawing(holder);
    }

    private void tryDrawing(SurfaceHolder surfaceHolder) {
        // get canvas from surface holder
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas == null) {
            ULog.d(TAG, "canvas is null");
        } else {
            canvas.drawColor(Color.RED);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private class MyDragHelper extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mHeaderView == child;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            Log.d("xxx", "onViewPositionChanged");
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
            dr.setAlpha(alpha < 0 ? 0 : alpha);
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
        if (isHeaderViewUnder && justStarted) {
            justStarted = false;
            mHeaderView.setInitialMotion(false);
        }

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
        if (myDragerHelper.smoothSlideViewTo(mHeaderView, x, y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    public boolean slideTo (float slideOffset) {
        final int topBound = getPaddingTop();
        int x = mHeaderView.getLeft();
        int y = (int) (topBound + slideOffset * dragRange);
        mHeaderView.setX(x);
        mHeaderView.setY(y);
        return false;
    }

    public View getmDescView() {
        return mDescView;
    }

    public MyRelativeLayout2 getmHeaderView() {
        return mHeaderView;
    }
}
