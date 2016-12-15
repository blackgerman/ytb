package abiguime.tz.com.tzyoutube._commons.core;

import android.app.Application;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;

import java.io.IOException;

/**
 * Created by abiguime on 2016/12/12.
 *
 * 作为app 的application对象
 *
 */
public class YtbApplication extends Application {



    private static final long WAITING_TIME = 1000;
    MediaPlayer mp;
    private MediaPlayerThread mpthread;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public MediaPlayer getMediaPlayer () {
        if (mp == null){
            new Throwable("No currently playing video!");
        }
        return  mp;
    }


    public void getMediaPlayerFor(final String path, final GetMediaPlayer callback) {

        if (mpthread != null)
            mpthread.stopThread();
        mpthread = new MediaPlayerThread(callback, path);
        mpthread.start();
    }


    // 判断有没有在播放音乐
    public boolean getIsplaying() {
        if (mp == null || !mp.isPlaying())
            return false;
        return true;
    }

    public interface GetMediaPlayer{
        void MediaPlayerLoaded (MediaPlayer mp, boolean isWorking);
        void MediaPlayerError ();
    }

    public void release() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }


    // 获取视频的子线程
    private class MediaPlayerThread extends Thread{


        private final GetMediaPlayer callback;
        private final String path;

        private boolean isWorking;

        public MediaPlayerThread(GetMediaPlayer callback, String path) {
            this.callback = callback;
            this.path = path;
            isWorking = true;
        }

        @Override
        public void run() {
            if (mp != null)
                try {
                /* 释放mediaplayer*/
                    mp.release();
                    mp = null;
                    // 等一段时间让释放完毕
                    Thread.sleep(WAITING_TIME);
                    if (!isWorking) { /*停止线程与释放mediaplayer*/
                        mp.release();
                        mp = null;
                        return;
                    }
                    mp = MediaPlayer.create(YtbApplication.this, Uri.parse(path));
                    if (!isWorking) {
                        mp.release();
                        mp = null;
                        return;
                    }
                    callback.MediaPlayerLoaded(mp, isWorking);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.MediaPlayerError();
                }
            else {
                try {
                    mp = MediaPlayer.create(YtbApplication.this, Uri.parse(path));
                    if (!isWorking) {
                        mp.release();
                        mp = null;
                        return;
                    }
                    callback.MediaPlayerLoaded(mp, isWorking);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.MediaPlayerError();
                }
            }
        }

        /*停止子线程*/
        public void stopThread() {
            isWorking = false;
        }
    }
}
