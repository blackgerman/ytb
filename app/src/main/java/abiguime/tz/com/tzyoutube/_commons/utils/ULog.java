package abiguime.tz.com.tzyoutube._commons.utils;

import android.util.Log;

/**
 * Created by abiguime on 2016/12/12.
 */
public class ULog {

    static boolean debug = true;

    public static void d(String tag, String s) {
    if (debug)
        Log.d(tag, s);
    }
}
