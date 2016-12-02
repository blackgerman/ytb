package abiguime.tz.com.tzyoutube.main.fragment_home;

import java.util.List;

import abiguime.tz.com.tzyoutube.BasePresenter;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube.main.MainFragmentBaseView;

/**
 * Created by abiguime on 2016/11/30.
 */

public class HomePageContract {



    public interface View extends MainFragmentBaseView<Presenter> {

        /* 显示视频列表 */
        void showVideoList(List<Video> videos/*视频列表*/);


        /*附加视频列表*/
        void appendVideosToList(List<Video> videos/*视频列表*/);
    }


    interface Presenter extends BasePresenter {

        // 加载视频
        void loadVideos();

        // 目前已经加载到id，所以需要从id开始加载视频：：：
        void loadVideosFrom(int id);
    }
}
