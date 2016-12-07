package abiguime.tz.com.tzyoutube.search.fragment_search_history;

import java.util.List;

import abiguime.tz.com.tzyoutube.BasePresenter;
import abiguime.tz.com.tzyoutube.BaseView;
import abiguime.tz.com.tzyoutube._data.HistoricalItem;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube.main.MainFragmentBaseView;

/**
 * Created by abiguime on 2016/12/7.
 */

public class SearchPageContract {

    public interface View extends BaseView<SearchPageContract.Presenter> {

        /* 获取最新的 搜索记录 */
        void showHistoric(List<HistoricalItem> items);


        /* 保存搜索记录 */
//        void saveHistoricalItem(HistoricalItem item);
    }


    interface Presenter extends BasePresenter {

        // 加载视频
        void getLastItems();

        // 目前已经加载到id，所以需要从id开始加载视频：：：
        void saveHistoricalItem(int id);
    }
}
