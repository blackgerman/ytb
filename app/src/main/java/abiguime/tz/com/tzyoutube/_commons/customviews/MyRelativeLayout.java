package abiguime.tz.com.tzyoutube._commons.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

public class MyRelativeLayout extends LinearLayout {


    private View videoclub;
    private boolean hasFinishInflate = false;
    private Video video;

    public MyRelativeLayout(Context context) {
        this(context, null);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        videoclub = findViewById(R.id.videoclub);
        hasFinishInflate = true;
        Log.d("xxx", "onFinishInflate");
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
        if (hasFinishInflate)
            setVideoClubSize ();
    }



    public void setVideoClubSize () {
        if (getHeight() == 0 || getWidth() == 0)
            return;
        MyRelativeLayout.LayoutParams layoutParams = (LayoutParams) videoclub.getLayoutParams();
        layoutParams.height = getHeight();
        layoutParams.width = getWidth();
        //添加margin
        layoutParams.rightMargin = (int) (getContext().getResources().getDimensionPixelSize(R.dimen.item_margin_bottom)
                * (1-getScaleY())); // 1
        videoclub.setLayoutParams(layoutParams);
        videoclub.setScaleX(getScaleY());
        videoclub.setPivotX(getWidth());
    }


    public void setVideo(Video video) {
        this.video = video;
        Picasso.with(getContext()).load(Constants.IP + video.coverimage)
                .placeholder(R.drawable.loading_black_cover)
                .into((ImageView) videoclub);
    }


}
