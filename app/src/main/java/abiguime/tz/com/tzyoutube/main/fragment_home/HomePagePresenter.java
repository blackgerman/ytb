package abiguime.tz.com.tzyoutube.main.fragment_home;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import abiguime.tz.com.tzyoutube.main.fragment_user.UserPageContract;
import abiguime.tz.com.tzyoutube.main.fragment_user.UserPageFragment;

/**
 * Created by abiguime on 2016/11/30.
 */

public class HomePagePresenter implements HomePageContract.Presenter{


    private final Context ctx;
    private final HomePageContract.View view;

    public HomePagePresenter(FragmentManager frg) {
        HomePageFragment frgs = (HomePageFragment) frg.findFragmentByTag(HomePageFragment.class.getName());
        this.view = (HomePageContract.View) frg.findFragmentByTag(UserPageFragment.class.getName());
        ctx = frgs.getContext();
    }

    /* 从网络上获取视频*/
    @Override
    public void loadVideos() {

    }

    /* 从网络通过具体位置获取视频列表*/
    @Override
    public void loadVideosFrom(int id) {

    }

    @Override
    public void start() {
    }
}
