package abiguime.tz.com.tzyoutube._commons.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by abiguime on 2016/12/14.
 */

public class CustomSeekbar extends AppCompatSeekBar {
    public CustomSeekbar(Context context) {
        super(context);
    }

    public CustomSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
    }

    @Override
    public void setProgressDrawable(Drawable d) {
        super.setProgressDrawable(d);
        Log.d("xxx", "setProgressDrawable "+d);

        ClipDrawable lfront = (ClipDrawable) ((LayerDrawable) d).findDrawableByLayerId(android.R.id.progress);
        lfront.setLevel(10000);
    }


    @Override
    public synchronized void setSecondaryProgress(int secondaryProgress) {
        super.setSecondaryProgress(secondaryProgress);
    }

}
