package abiguime.tz.com.tzyoutube._data.source.remote;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.IOException;

import abiguime.tz.com.tzyoutube._data.User;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.source.UserDataSource;
import abiguime.tz.com.tzyoutube._data.source.VideoDataSource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by abiguime on 2016/11/30.
 *
 * - 实现获取视频的接口
 */
public class UserRemoteDataSource implements UserDataSource {


    private static final String LOGIN_URL = "http://10.0.2.2/ytb/web/app_dev.php";
    private final Context ctx;

    OkHttpClient client = new OkHttpClient();

    public UserRemoteDataSource(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void login(@Nullable String name, @Nullable String password, final LoginCallBack callBack) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(LOGIN_URL)
                            .build();
                    Response response = client.newCall(request).execute();
                    // 服务器返回的json数据
                    String json_result = response.body().string();
                    // 。。。、、、
                    callBack.onLoginError(json_result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void register(@Nullable String name, @Nullable String password, @Nullable String phonenumber, LoginCallBack callBack) {

    }
}
