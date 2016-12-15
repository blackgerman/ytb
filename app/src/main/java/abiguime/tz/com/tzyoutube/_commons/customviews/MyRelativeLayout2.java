package abiguime.tz.com.tzyoutube._commons.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.constants.Constants;

/**
 * Created by abiguime on 2016/12/9.
 */

public class MyRelativeLayout2 extends LinearLayout {



    private boolean hasFinishInflate = false;
    private Video video;

    // surfaceview
    SurfaceView sv;
    // imageview
    ImageView iv;
    private boolean initialMotion = true;

    public MyRelativeLayout2(Context context) {
        this(context, null);
    }

    public MyRelativeLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRelativeLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    private FrameLayout frame_playback;


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        frame_playback = (FrameLayout) findViewById(R.id.frame_playback);
        iv = (ImageView) findViewById(R.id.iv_videoclub);
        sv = (SurfaceView) findViewById(R.id.sv_videoclub);
        hasFinishInflate = true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getContext().getResources().getDisplayMetrics().widthPixels;
        int h = (9*w/16);
        // 设置布局最开始的大小
        setMeasuredDimension(w, h);
        /*1- 一开始，这个布局里会有很多视图
            但是，当我自动改变本视图大小之后，由于视图在更改的时候
            还没有显示过自己内部的视图的愿意，
            那么这些视图就不改自己的大小。
          2 - 所以需要手动去设置视图的大小
        * */
        if (hasFinishInflate)
            setVideoClubSize ();
    }



    public void setVideoClubSize () {
        if (getHeight() == 0 || getWidth() == 0)
            return;
        MyRelativeLayout2.LayoutParams layoutParams = (LayoutParams) frame_playback.getLayoutParams();
        layoutParams.height = getHeight();
        layoutParams.width = getWidth();//(initialMotion ? 3:1);
        //添加margin
        layoutParams.rightMargin = (int) (getContext().getResources().getDimensionPixelSize(R.dimen.item_margin_bottom)
                * (1-getScaleY() + (initialMotion?1:0))); // 1
        frame_playback.setLayoutParams(layoutParams);

        frame_playback.setScaleX((initialMotion ? (1f/3f): getScaleY()));
        frame_playback.setPivotX(getWidth());

        frame_playback.setScaleY((initialMotion ? (1f/3f): 1));
        frame_playback.setPivotY(getHeight());
    }



    public void setInitialMotion(boolean initialMotion) {
        this.initialMotion = initialMotion;
    }


    public SurfaceView getSv() {
        return sv;
    }

    public ImageView getIv() {
        return iv;
    }

    // 按照视频大小更改surfaceview的大小
    public void setSvSize(int videoWidth, int videoHeight) {
        // 不能超过的高宽
        int maxWidth =  getContext().getResources().getDisplayMetrics().widthPixels;
        int maxHeight = 9* maxWidth /16;
        FrameLayout.LayoutParams fr = (FrameLayout.LayoutParams) sv.getLayoutParams();
        int newHeight = videoHeight, newWidth = videoWidth;
        // 视频时垂直方向显示
        if (videoHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (int) (videoWidth * ((float)maxHeight/ (float)videoHeight));
        }
        fr.height = newHeight;
        fr.width = newWidth;
        fr.gravity = Gravity.CENTER;
        sv.setLayoutParams(fr);
    }

    private int getVideo16_9Height() {
        return 9*getResources().getDisplayMetrics().widthPixels/16;
    }

}
