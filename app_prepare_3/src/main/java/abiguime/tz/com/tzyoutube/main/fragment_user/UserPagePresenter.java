package abiguime.tz.com.tzyoutube.main.fragment_user;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import abiguime.tz.com.tzyoutube._data.User;
import abiguime.tz.com.tzyoutube._data.source.UserDataSource;
import abiguime.tz.com.tzyoutube._data.source.remote.UserRemoteDataSource;

/**
 * Created by abiguime on 2016/11/30.
 */

public class UserPagePresenter implements UserPageContract.Presenter{


    private final UserPageContract.View view;
    UserRemoteDataSource model;

    public UserPagePresenter(UserPageContract.View view, UserRemoteDataSource model) {
        this.view = view;
        this.model = model;
    }


    @Override
    public void login(String name, String password) {

        /* 检查数据*/

        view.onLoginProgress(); // 显示进度条 。。。

        /* --数据在服务器上- */
        model.login(name, password, new UserDataSource.LoginCallBack() {
            @Override
            public void onLoginSuccess(User user) {
                view.showUser(user);
            }

            @Override
            public void onLoginError(String message) {
                view.onLoginError(message); // 显示进度条 。。。
            }
        });
    }

    @Override
    public void start() {

    }
}
