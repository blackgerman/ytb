package abiguime.tz.com.tzyoutube.main.fragment_home;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.List;

import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.source.VideoDataSource;
import abiguime.tz.com.tzyoutube._data.source.remote.UserRemoteDataSource;
import abiguime.tz.com.tzyoutube._data.source.remote.VideoRemoteDataSource;
import abiguime.tz.com.tzyoutube.main.fragment_user.UserPageContract;
import abiguime.tz.com.tzyoutube.main.fragment_user.UserPageFragment;

/**
 * Created by abiguime on 2016/11/30.
 */

public class HomePagePresenter implements HomePageContract.Presenter{

    public String TAG = HomePagePresenter.class.getName();

    private final HomePageContract.View view;
    private final VideoRemoteDataSource model;

//    public UserPagePresenter(UserPageContract.View view, UserRemoteDataSource model) {

    public HomePagePresenter(HomePageContract.View view, VideoRemoteDataSource model) {
        this.view = view;
        this.model = model;
    }

    /* 从网络上获取视频*/
    @Override
    public void loadVideos() {

        view.showVideosLoadingOnGoing(); // 让fragment 显示正在获取数据的效果
        int lastVideo = 0;
        model.getHomePageVideos("", lastVideo, new VideoDataSource.GetVideosCallBack() {

            @Override
            public void onVideoLoaded(List<Video> data) {
                view.showVideoList(data);
                view.showVideosLoadingComplete(); // 让fragment 显示数据加载好。
            }

            @Override
            public void onDataNotAvailable(String message) {
                view.showEmptyVideoError();
            }

        });

    }

    /* 从网络通过具体位置获取视频列表*/
    @Override
    public void loadVideosFrom(int id) {
        int lastVideo = id;
        model.getHomePageVideos("", lastVideo, new VideoDataSource.GetVideosCallBack() {

            @Override
            public void onVideoLoaded(List<Video> data) {
                view.appendVideosToList(data);
                view.showVideosLoadingComplete(); // 让fragment 显示数据加载好。
            }

            @Override
            public void onDataNotAvailable(String message) {
                view.showEmptyVideoError();
            }

        });
    }

    @Override
    public void start() {
    }
}
