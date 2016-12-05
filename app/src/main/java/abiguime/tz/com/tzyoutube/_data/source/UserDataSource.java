package abiguime.tz.com.tzyoutube._data.source;

import android.support.annotation.Nullable;

import abiguime.tz.com.tzyoutube._data.User;

/**
 * Created by abiguime on 2016/11/30.
 *
 * 针对用户的网络请求:: Login... Register
 * */
public interface UserDataSource {

    /**/
    interface LoginCallBack {
        void onLoginSuccess(User user);
        void onLoginError(String message);
    }

    /* 登录*/
    void login(
            @Nullable String name,
            @Nullable String password,
            LoginCallBack callBack
    );

    /* 注册 */
    void register(
            @Nullable String name,
            @Nullable String password,
            @Nullable String phonenumber,
            LoginCallBack callBack
    );

}
