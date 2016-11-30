package abiguime.tz.com.tzyoutube._data.source;

import android.support.annotation.Nullable;

import abiguime.tz.com.tzyoutube._data.Video;

/**
 * Created by abiguime on 2016/11/30.
 *
 * 获取视频请求的方法
 * */
public interface VideoDataSource {

    interface GetVideosCallBack {
        void onVideoLoaded();
        void onDataNotAvailable();
    }

    /* 主页面获取视频*/
    void getHomePageVideos (
            @Nullable String usertoken,
            @Nullable int lastOneId,
            GetVideosCallBack callBack
    );

    /* 通过分类获取视频*/
    void getHomePageVideosByCategory(@Nullable Video.Category category,
                                     @Nullable String usertoken,
                                     @Nullable int lastOneId,
                                     GetVideosCallBack callBack);
}
