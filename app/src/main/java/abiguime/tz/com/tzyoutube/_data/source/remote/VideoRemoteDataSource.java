package abiguime.tz.com.tzyoutube._data.source.remote;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.constants.API;
import abiguime.tz.com.tzyoutube._data.source.VideoDataSource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by abiguime on 2016/11/30.
 *
 * - 实现获取视频的接口
 */
public class VideoRemoteDataSource implements VideoDataSource {

    String TAG = VideoRemoteDataSource.class.getName();

    private final Context ctx;
    Gson gson = new Gson();

    public VideoRemoteDataSource(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void getHomePageVideos(@Nullable String usertoken,
                                  @Nullable int lastOneId,
                                  final GetVideosCallBack callBack) {
        /* 通过网络请求获取视频信息*/
        final String link = API.VIDEOS+"";
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(link)
                        .build();
                Response response = null;
                try {
                        response = client.newCall(request).execute();
                        // 服务器返回的json数据
                        String json_result = response.body().string();
                    Log.d(TAG, json_result);
                    return json_result;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String json) {

                if (json == null) {
                    callBack.onVideoLoaded(null);
                } else {
                    List<Video> videos = new ArrayList<Video>();
                    videos = gson.fromJson(json,
                            new TypeToken<List<Video>>(){}.getType()
                            );
                    callBack.onVideoLoaded(videos);
                }
            }
        }.execute(link);

    }


    @Override
    public void getHomePageVideosByCategory(@Nullable Video.Category category,
                                            @Nullable String usertoken,
                                            @Nullable int lastOneId,
                                            GetVideosCallBack callBack) {
        // okhttp

    }



/*
    // task to load video
    class VideoLoadingAsyntask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

    }*/
}
