package abiguime.tz.com.tzyoutube.search.fragment_search_result;

import java.util.List;

import abiguime.tz.com.tzyoutube._data.HistoricalItem;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.source.VideoDataSource;
import abiguime.tz.com.tzyoutube._data.source.local.HistoricalItemDataSource;
import abiguime.tz.com.tzyoutube._data.source.remote.VideoRemoteDataSource;

/**
 * Created by abiguime on 2016/12/7.
 */

public class SearchResultPresenter implements SearchResultContract.Presenter {


    private final VideoRemoteDataSource repo;
    private final SearchResultContract.View view;

    public SearchResultPresenter(SearchResultContract.View view, VideoRemoteDataSource repo) {
    this.view = view;
        this.repo = repo;
    }

    @Override
    public void start() {

    }


    @Override
    public void searchItemsForItem(String query) {

        int lastVideo = 0;
        repo.getHomePageVideos("", lastVideo, new VideoDataSource.GetVideosCallBack() {

            @Override
            public void onVideoLoaded(List<Video> data) {
                view.showResult(data);
            }

            @Override
            public void onDataNotAvailable(String message) {
            }

        });
    }
}
