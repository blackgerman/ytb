package abiguime.tz.com.tzyoutube.main.fragment_user;

import abiguime.tz.com.tzyoutube.BasePresenter;
import abiguime.tz.com.tzyoutube.BaseView;
import abiguime.tz.com.tzyoutube._data.User;
import abiguime.tz.com.tzyoutube.main.MainFragmentBaseView;

/**
 * Created by abiguime on 2016/11/30.
 */

public class UserPageContract {



    interface View extends BaseView<Presenter> {

        /* presenter 把用户信息传给view(fragment)*/
        void showUser(User user);


        /* 登录中 */
        void onLoginProgress();

        /*登录失败*/
        void onLoginError(String message);

    }


    interface Presenter extends BasePresenter {

        // 登录
        void login(String name, String password);
    }
}
