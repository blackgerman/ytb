package abiguime.tz.com.tzyoutube._data.source.remote;

import android.content.Context;
import android.support.annotation.Nullable;

import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.source.VideoDataSource;

/**
 * Created by abiguime on 2016/11/30.
 *
 * - 实现获取视频的接口
 */
public class VideoRemoteDataSource implements VideoDataSource {


    private final Context ctx;

    public VideoRemoteDataSource(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void getHomePageVideos(@Nullable String usertoken,
                                  @Nullable int lastOneId,
                                  GetVideosCallBack callBack) {
        /* 通过网络请求获取视频信息*/
        callBack.onVideoLoaded();
    }


    @Override
    public void getHomePageVideosByCategory(@Nullable Video.Category category,
                                            @Nullable String usertoken,
                                            @Nullable int lastOneId,
                                            GetVideosCallBack callBack) {
        // okhttp

    }
}
