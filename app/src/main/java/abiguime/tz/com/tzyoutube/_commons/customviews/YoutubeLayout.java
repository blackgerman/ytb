package abiguime.tz.com.tzyoutube._commons.customviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._commons.core.YtbApplication;
import abiguime.tz.com.tzyoutube._commons.utils.ULog;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.constants.Constants;
import abiguime.tz.com.tzyoutube.main.MainActivity;
import abiguime.tz.com.tzyoutube.main.fragment_home.HomePageContract;


/**
 * Created by abiguime on 2016/8/4.
 */

public class YoutubeLayout extends ViewGroup implements
// videos playback interfaces
        SurfaceHolder.Callback,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener ,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnSeekCompleteListener ,
        MediaPlayer.OnInfoListener{

    private static final String TAG = YoutubeLayout.class.getName();

    /* 表示当前缩放大小状态 （是否缩小到下面）*/
    public enum SizeState {
        Minimized, Full;
    }

    // 进度
    private static final int PROGRESS_UPDATE = 1112;

    private final ViewDragHelper myDragerHelper;

    private View  mDescView;
    private MyRelativeLayout mHeaderView;

    private float initialMotionX, initialMotionY;

    /**
     * 视图被滑动的距离 （上到下）
     */
    private int dragRange;

    /**
     * 表示本view的top 或者 y 坐标
     */
    private int mTop;

    /**
     * 表示从上到下滑动的比例：
     * 上 ： 0f  --> 0%
     * 下： 1f  ---》 100%
     */
    private float mDragOffset;
    /*敏感度*/
    private float SENSITIVITY = 1.0f;
    /*刚初始化，还没任何操作*/
    public boolean justStarted = true;

    private MediaPlayer mp;

    private Canvas canvas;

    // 缩放状态
    private SizeState position;
    private int visibility;


    // 是否在播放
    private boolean isPlaying = true;

    // 暂停时候进度
    private int current;


    // 当前播放视频
    private Video currentVideo;

    // 跟着播放进的子线程
    private PlayerProgressThread thread;
    private SurfaceHolder mSurfaceHolder;


    public YoutubeLayout(Context context) {
        this(context, null);
    }


    public YoutubeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YoutubeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myDragerHelper = ViewDragHelper.create(this, SENSITIVITY, new MyDragHelper());
        /* custom attributes are
        * - show bottom / show top
        * - visibility : visible, invisible
        * */
        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs,
                    new int[]{R.styleable.youtube_layout_init_draw_position,
                            R.styleable.youtube_layout_init_visibility});
            position = (attributes.getInt(0, 0) != 0 ? SizeState.Full : SizeState.Minimized);
            visibility = (attributes.getInt(1, 0) == 0 ? View.VISIBLE : View.INVISIBLE);
        } else {
            position = SizeState.Full;
            visibility = View.VISIBLE;
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDescView = findViewById(R.id.viewDesc);
        mHeaderView = (MyRelativeLayout) findViewById(R.id.viewHeader);

        // 根据状态设计显示性
        if (position == SizeState.Minimized) {
            mDescView.setVisibility(INVISIBLE);
            mHeaderView.setVisibility(VISIBLE);
        }
        setVisibility(visibility);
    }

    /* OnLayout is need to tell a view how is he going to draw
        *  himself
        **/
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        dragRange = getScreenHeight() - mHeaderView.getHeight();
        // remove margin bottom
        dragRange -= getContext().getResources().getDimensionPixelSize(R.dimen.item_margin_bottom);
        /*positions of the top and bottom views have to be computed
        * programmatically
        * */

        if (justStarted && position == SizeState.Minimized) {
//            justStarted = false;
            mTop = getScreenHeight() -
                    getVideo16_9Height()/3 -
                    getContext().getResources().getDimensionPixelSize(R.dimen.item_margin_bottom);
            int  mBottom = mTop + getVideo16_9Height()/3;
            int mLeft = getScreenWidth() - getVideo16_9Height()/3 - getContext()
                    .getResources().getDimensionPixelSize(R.dimen.item_margin_bottom);
            int mRight = mLeft + getVideo16_9Height()/3;
            mHeaderView.layout(mLeft, mTop, mRight, mBottom);
            Log.d("xxx", ""+mLeft+" - "+mTop+" - "+mRight+" - "+mBottom);
            Log.d("xxx", "getVideo16_9Height/3 = "+(getVideo16_9Height()/3));
        } else{
            mHeaderView.layout(0, mTop, r, mTop+mHeaderView.getMeasuredHeight());
        }
        mDescView.layout(0,mTop+mHeaderView.getMeasuredHeight(), r, b+mTop);
    }

    private int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    private int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
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
        // 设置视频的介绍图片（cover) （作为正在加载图）
        setLoadingCover(video);
        // 启动播放
        startMediaPlayerPlayBack(video);
        currentVideo = video;
        // 把显示视频的viewgroup 显示成全屏
        if (!isFirst)
            smoothSlideTo(0f);


        if (mp!=null) {
            mp.getCurrentPosition();
        }
        // 运行显现出、时时刻刻的获取进度.
    }


    private void startMediaPlayerPlayBack(Video video) {
        // 因为不想在主线程做mediaplayer的初始化，在另外子线程实现mediaplayer
        // 初始化再实现一下操作
        ((YtbApplication) getContext().getApplicationContext())
                .getMediaPlayerFor(Constants.IP + video.path, new YtbApplication.GetMediaPlayer() {
                    @Override
                    public void MediaPlayerLoaded(MediaPlayer mp, boolean isWorking) {
                        if (!isWorking)  {
                            if (mp != null) {
                                mp.release();
                                mp = null;
                            }
                            return;
                        }
                        YoutubeLayout.this.mp = mp;
                        mp.setOnBufferingUpdateListener(YoutubeLayout.this); // 缓存更新监听
                        mp.setOnCompletionListener(YoutubeLayout.this); // 播放完毕监听
                        mp.setOnPreparedListener(YoutubeLayout.this); // 准备完成监听（prepareAsync）
                        mp.setScreenOnWhilePlaying(true); // 在播放期间屏幕必须保持量的状态
                        mp.setOnVideoSizeChangedListener(YoutubeLayout.this); // 在视频大小手变化时 (重新设置surfaceview 的大小)
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC); // 设置音乐频道
                        mHeaderView.getSv().getHolder().addCallback(YoutubeLayout.this); // 绑定surfaceview和mediaplayer
                    }

                    @Override
                    public void MediaPlayerError() {
                        ((MainActivity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "MediaPlayerError", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }


    /*设置视频主要图片*/
    private void setLoadingCover(Video video) {
        mHeaderView.getSv().setVisibility(INVISIBLE);
        mHeaderView.getSv().getHolder().removeCallback(this);
        Picasso.with(getContext()).load(Constants.IP + video.coverimage)
                .placeholder(R.drawable.loading_black_cover)
                .into(mHeaderView.getIv());
        mHeaderView.getIv().setVisibility(VISIBLE);
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

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mHeaderView.setBufferUpdatePercent(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
        mp = null;
    }

    /* 当prepareAsync 运行完成时，就会调用本方法*/
    @Override
    public void onPrepared(final MediaPlayer mp) {

        if (mp == null) {
            Toast.makeText(getContext(), "Mediaplayer null", Toast.LENGTH_SHORT).show();
            return;
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    // 启动视频播放;
                    mHeaderView.setSvSize(mp.getVideoWidth(), mp.getVideoHeight());
                    ULog.d("xxx", "vheight "+mp.getVideoHeight()+" -- vwidth "+mp.getVideoWidth());
                    // 把进度放在目前播放到的位置
                    mp.seekTo(current);
                    //播放
                    mp.start();
                    if (thread == null) {
                        thread = new PlayerProgressThread();
                        thread.start();
                    }
                    current = 0; // 保持重播一次
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
                        mHeaderView.getIv().setVisibility(INVISIBLE);
                        mHeaderView.getSv().setVisibility(VISIBLE);
                    }
                });
            }
        }, 1000);
    }



    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        Log.d("xxx", "surfaceCreated");
        if (mp == null) {
            // implement a fallback mechanism if it fails, for example if no internet or 404
            Log.w("Layout1", "MediaPlayer was not created");
            return;
        }
        // 绑定mediaplayer 与 surfaceview
        try {
            mp.setDisplay(holder);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("xxx", "surfaceChanged");
        tryDrawing();
    }

    private void tryDrawing() {
        // get canvas from surface holder
        if (!mSurfaceHolder.isCreating() || !mSurfaceHolder.getSurface().isValid())
            return;
        canvas = mSurfaceHolder.lockCanvas();
        ULog.d(TAG, "lockCanvas");
        if (canvas == null) {
            ULog.d(TAG, "canvas is null");
        } else {
            canvas.drawColor(Color.BLACK);
            mSurfaceHolder.unlockCanvasAndPost(canvas);
            ULog.d(TAG, "unlockCanvasAndPost");
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceHolder = null;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
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
            dr.setAlpha(alpha < 0 ? 0 : alpha);
            setBackground(dr);
            requestLayout();
        }


        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (mDragOffset<0.5f)
                smoothSlideTo(0f);
            else
                smoothSlideTo(1f);
        }

        /**
         * 1- allow the vertical scrolling of the views
         */
       /*implements this to allow vertical dragging*/
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {

            final int topBound = getPaddingTop();
            final int bottomBound = getScreenHeight() - mHeaderView.getHeight();
            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop; /* sends back the new top of the view */
        }
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
        // 把事件传给draghelper
        myDragerHelper.processTouchEvent(event);
        final int action = event.getAction();
        // 获取手指位置
        float x = event.getX();
        float y = event.getY();
        // 判断用户点击的位置是否在具体视图一下
        boolean isHeaderViewUnder = myDragerHelper.isViewUnder(mHeaderView, (int)x, (int)y);
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                /* 表示滑动操作开始了--》保存按下的第一个点 */
                initialMotionX = x;
                initialMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
                /* when the user leave the screen,
                * get the leaving point coordinates */
                final float dx = x - initialMotionX;
                final float dy = y - initialMotionY;
                final int slop = myDragerHelper.getTouchSlop();
                /* 判断三个点：
                    1- 用户点击的位置时候 Header 头部
                    2-
                 */
                if (position == SizeState.Full && isHeaderViewUnder && isHeaderTop()) {
                    processClick();
                    return true;
                }
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

    private boolean isHeaderTop() {
        return mHeaderView.getX() <= 1.0f && mHeaderView.getY() <= 1.0f;
    }

    /*按照现在视频播放状态暂停或者播放*/
    private void processClick() {
        if (mp == null) {
            Toast.makeText(getContext(), "processClick", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (mp != null && mp.isPlaying()) {
                mHeaderView.playpauseAnimation(false);
                current = mp.getCurrentPosition();
                mp.pause();
                mp.release();
                mp = null;
                isPlaying = false;
            } else {
                mHeaderView.playpauseAnimation(true);
                if (currentVideo != null)
                    startMediaPlayerPlayBack(currentVideo);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }




    /*判断view有没有出去屏幕能够显示的方位 */
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


    /* 自动把视图通过选快点额效果滑到底部 */
    public boolean smoothSlideTo(float slideOffset) {

        if (slideOffset == .0f)
            position = SizeState.Full;
        else
            position = SizeState.Minimized;

        final int topBound = getPaddingTop();
        int y = (int) (topBound + slideOffset * dragRange);
        int x = mHeaderView.getLeft();
        if (myDragerHelper.smoothSlideViewTo(mHeaderView, x, y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS_UPDATE:
                    int totalProgress = msg.arg1;
                    int progress = msg.arg2;
                    mHeaderView.setProgress(progress, totalProgress);
                    break;
            }
            return false;
        }
    });

    class PlayerProgressThread extends Thread  {
        @Override
        public void run() {
            super.run();
            while (isPlaying) {
                try {
                    Thread.sleep(800);
                    if (mp.isPlaying()) {
                        Message msg = mHandler.obtainMessage();
                        msg.arg1 = mp.getDuration();
                        msg.arg2 = mp.getCurrentPosition();
                        msg.what = PROGRESS_UPDATE;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isPlaying = true;
                }
            }
        }
    }
}
