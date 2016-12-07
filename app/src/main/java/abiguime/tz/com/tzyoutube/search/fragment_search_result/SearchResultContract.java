package abiguime.tz.com.tzyoutube.search.fragment_search_result;

import java.util.List;

import abiguime.tz.com.tzyoutube.BasePresenter;
import abiguime.tz.com.tzyoutube.BaseView;
import abiguime.tz.com.tzyoutube._data.HistoricalItem;
import abiguime.tz.com.tzyoutube._data.Video;

/**
 * Created by abiguime on 2016/12/7.
 */

public class SearchResultContract {

    public interface View extends BaseView<SearchResultContract.Presenter> {

        /* 获取最新的 搜索记录 */
        void showResult(List<Video> items);

    }


    interface Presenter extends BasePresenter {

        // 加载视频
        void searchItemsForItem(String query);
    }
}
