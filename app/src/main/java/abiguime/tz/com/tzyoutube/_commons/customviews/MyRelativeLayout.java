package abiguime.tz.com.tzyoutube._commons.customviews;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.squareup.picasso.Picasso;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.constants.Constants;

/**
 * Created by abiguime on 2016/12/9.
 */

public class MyRelativeLayout extends LinearLayout {


    private FrameLayout frame_playback;

    // surfaceview
    SurfaceView sv;
    // imageview
    ImageView iv;

    AnimationDrawable dr_pause, dr_play;

    private boolean hasFinishInflate = false;
    private Video video;
    private ImageView play_pause_view;

    private CustomSeekbar myseekbar;
    private boolean isInitial = true;
    private YoutubeLayout.SizeState position;


    public MyRelativeLayout(Context context) {
        this(context, null);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dr_pause = (AnimationDrawable) context.getResources().getDrawable(R.drawable.pause_drawable);
        dr_play = (AnimationDrawable) context.getResources().getDrawable(R.drawable.play_drawable);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        frame_playback = (FrameLayout) findViewById(R.id.frame_playback);
        iv = (ImageView) findViewById(R.id.iv_videoclub);
        sv = (SurfaceView) findViewById(R.id.sv_videoclub);
        play_pause_view = (ImageView) findViewById(R.id.iv_play_pause);
        myseekbar = (CustomSeekbar) findViewById(R.id.myseekbar);
        hasFinishInflate = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int w = getContext().getResources().getDisplayMetrics().widthPixels;
        final int h = 9*w/16;
        // 设置布局最开始的大小
        setMeasuredDimension(w, h);
        /*1- 一开始，这个布局里会有很多视图
            但是，当我自动改变本视图大小之后，由于视图在更改的时候
            还没有显示过自己内部的视图的愿意，
            那么这些视图就不改自己的大小。
          2 - 所以需要手动去设置视图的大小
        * */
    }



    public void setVideoClubSize (float dragOffset) {
        if (getHeight() == 0 || getWidth() == 0)
            return;
        MyRelativeLayout.LayoutParams layoutParams = (LayoutParams) frame_playback.getLayoutParams();
        layoutParams.height = getHeight();
        layoutParams.width = getWidth();
        //添加margin
        layoutParams.rightMargin = (int) (getContext().getResources().getDimensionPixelSize(R.dimen.item_margin_bottom)
                *  (isInitial&&position== YoutubeLayout.SizeState.Minimized ? 2f/3f : 1 - getScaleY())); // 1
//
        Log.d("xxx", "isinitial "+isInitial +" -- dragoffset = "+dragOffset +" getheight "+getHeight()+" -- getscale "+getScaleY());
        if (isInitial && position == YoutubeLayout.SizeState.Minimized) {
            setPivotY(getHeight());
            setScaleY(1f/3);
        }
        frame_playback.setLayoutParams(layoutParams);
        frame_playback.setScaleX(getScaleY());
        frame_playback.setPivotX(getWidth());
        requestLayout();
    }



    public SurfaceView getSv() {
        return sv;
    }

    public ImageView getIv() {
        return iv;
    }


    public void playpauseAnimation (boolean play) {
        play_pause_view.setVisibility(VISIBLE);
        if (play) // 启动播放动画
            play_pause_view.setImageDrawable(dr_play);
        else // 启动暂停动画
            play_pause_view.setImageDrawable(dr_pause);
        // 获取动画时间长度
        int duration = dr_pause.getNumberOfFrames()* getResources().getInteger(R.integer.frame_duration);
        // 在动画运行完成 再把视图隐藏
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                play_pause_view.setVisibility(INVISIBLE);
            }
        }, duration);

        // imageview 动画
        PropertyValuesHolder scaleAnimationX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.3f);
        PropertyValuesHolder scaleAnimationY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.3f);
        PropertyValuesHolder fadeOut = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f);

        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(play_pause_view, scaleAnimationX, scaleAnimationY, fadeOut);
        anim.setDuration(duration);

        ((AnimationDrawable)play_pause_view.getDrawable()).start();
        anim.start();
    }

    // 按照视频大小更改surfaceview的大小
    public void setSvSize(int videoWidth, int videoHeight) {
        // 不能超过的高宽
        int maxWidth =  getContext().getResources().getDisplayMetrics().widthPixels;
        int maxHeight=  9* maxWidth /16;
        FrameLayout.LayoutParams fr = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //(FrameLayout.LayoutParams) sv.getLayoutParams();
        int newHeight = videoHeight, newWidth = videoWidth;
        // 视频时垂直方向显示
        if (videoHeight > maxHeight || videoHeight > videoWidth) {
            newHeight = maxHeight;
            newWidth = (int) (videoWidth * ((float)maxHeight/ (float)videoHeight));
        } else {
            newWidth = maxWidth;
            newHeight = (int) (videoHeight * ((float)maxWidth)/ (float)videoWidth);
        }
        fr.height = newHeight;
        fr.width = newWidth;
        fr.gravity = Gravity.CENTER; // 让surfaceview 居中
        sv.setLayoutParams(fr);
    }

    // 设置进度
    public void setProgress(int progress, int totalProgress) {
        myseekbar.setProgress(progress);
        myseekbar.setMax(totalProgress);
    }

    // 设置进度条缓存进度
    public void setBufferUpdatePercent(int percent) {
        myseekbar.setSecondaryProgress(percent);
    /*myseekbar.setOnDragListener(new OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            return false;
        }
    });*/
    }


    public void setIsInitial(boolean b, YoutubeLayout.SizeState position) {
        this.isInitial = b;
        this.position = position;
    }
}
