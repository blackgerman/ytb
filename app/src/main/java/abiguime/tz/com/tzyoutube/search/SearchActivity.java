package abiguime.tz.com.tzyoutube.search;

import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._commons.customviews.YoutubeLayout;
import abiguime.tz.com.tzyoutube._data.HistoricalItem;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.source.VideoDataSource;
import abiguime.tz.com.tzyoutube._data.source.local.HistoricalItemDataSource;
import abiguime.tz.com.tzyoutube._data.source.remote.VideoRemoteDataSource;
import abiguime.tz.com.tzyoutube.main.MainActivity;
import abiguime.tz.com.tzyoutube.search.fragment_search_history.SearchHistoryItemFragment;
import abiguime.tz.com.tzyoutube.search.fragment_search_history.SearchPageHistoricPresenter;
import abiguime.tz.com.tzyoutube.search.fragment_search_result.SearchResultItemFragment;
import abiguime.tz.com.tzyoutube.search.fragment_search_result.SearchResultPresenter;

public class SearchActivity extends AppCompatActivity implements
        SearchResultItemFragment.OnListFragmentInteractionListener,
        SearchHistoryItemFragment.OnListFragmentInteractionListener,
        SearchView.OnQueryTextListener, View.OnFocusChangeListener {

    private static final String[] SUGGESTIONS = {
            "Bauru", "Sao Paulo", "Rio de Janeiro",
            "Bahia", "Mato Grosso", "Minas Gerais",
            "Tocantins", "Rio Grande do Sul"
    };

    private SimpleCursorAdapter mAdapter;
    private SearchView searchView;
    private Toolbar tb;

    // fragments
    private SearchHistoryItemFragment historicListFragment;
    private SearchResultItemFragment searchResultFragment;

    // historic fragment presenter and remote
    private SearchPageHistoricPresenter historicItempresenter;
    private HistoricalItemDataSource historicItemrepo;

    // result fragment presenter and remote
    private SearchResultPresenter searchResultPresenter;
    private VideoRemoteDataSource videoRepo;


    /* 是否在退出activity */
    private boolean exiting = false;
    private boolean isQuering = false;
    private YoutubeLayout youtubelayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        setSupportActionBar(tb);

        // searchview 默认已经打开而且可以直接输入内容
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener(this);

        ImageButton bt = (ImageButton) findViewById(R.id.iv_backtohome);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exiting = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });

        /* 更改底部的颜色 。*/
        View searchPlate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlate.setBackgroundColor(Color.TRANSPARENT);

        // material design activity 共享视图
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(MainActivity.DURATION);
            getWindow().setEnterTransition(fade);
        }

        // retrieve the data from the database and save put it in the fragment
        initRepo();
        initFragments();
        initYoutubeLayout();
    }

    private void initYoutubeLayout() {
        if (youtubelayout.getVisibility()!= View.VISIBLE) {
            youtubelayout.setVisibility(View.VISIBLE);
            youtubelayout.requestHeaderContent();
            youtubelayout.setVideo(Video.fakeVideos(1).get(0), false);
            youtubelayout.smoothSlideTo(1f);
        }
    }

    private void initRepo() {
        videoRepo = new VideoRemoteDataSource(SearchActivity.this);
    }

    private void mT(String s) {
        Toast.makeText(SearchActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    private void initFragments() {

        if (historicListFragment == null) {
            historicListFragment = SearchHistoryItemFragment.newInstance();
            historicItemrepo = new HistoricalItemDataSource(getApplicationContext());
            historicItempresenter = new SearchPageHistoricPresenter(historicListFragment, historicItemrepo);
            historicListFragment.setPresenter(historicItempresenter);
        }
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.add(R.id.frame_layout, historicListFragment, SearchHistoryItemFragment.TAG);
        trans.commit();
    }


    private void initViews() {
        tb = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView) findViewById(R.id.mysearchview);
        youtubelayout= (YoutubeLayout) findViewById(R.id.youtubelayout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS[i]});
        }
        mAdapter.changeCursor(c);
    }



    /* 提交搜搜的操作*/
    @Override
    public boolean onQueryTextSubmit(final String query) {
        if (isQuering)
            return true;
        isQuering = true;
        final FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        videoRepo.getHomePageVideos("", 20, new VideoDataSource.GetVideosCallBack() {

            @Override
            public void onVideoLoaded(List<Video> data) {
                if (searchResultFragment == null) {
                    searchResultFragment = SearchResultItemFragment.newInstance(data);
                    searchResultPresenter = new SearchResultPresenter(searchResultFragment, videoRepo);
                    searchResultFragment.setPresenter(searchResultPresenter);
                    trans.add(R.id.frame_layout, searchResultFragment, SearchResultItemFragment.TAG);
                    trans.hide(historicListFragment);
                    trans.commit();
                } else {
                    searchResultFragment.sendRequest(query);
//            searchResultFragment.showResult();
                    trans.show(searchResultFragment);
                    trans.hide(historicListFragment);
                    trans.commit();
                }
            }

            @Override
            public void onDataNotAvailable(String message) {
            }
        });

        searchView.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchView.clearFocus();
            }
        }, 1000);
        return true;
    }


    /* 当输入框字符串有变化*/
    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void suggest(String suggest) {
        searchView.setQuery(suggest, false);
    }

    @Override
    public void search(String search) {
        searchView.setQuery(search, true);
    }

    @Override
    public void onVideoResultSelected(Video video) {
        // play video sent by the fragment
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        /*由于在finish过程中有时候会调用onfocuschange方法，就应该判断一下、
        * 本方法只能在还没有finish之前运行*/
        if (hasFocus && !exiting) {
            // show the historic fragment
            final FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            if (searchResultFragment!=null)
                trans.hide(searchResultFragment);
            trans.show(historicListFragment);
            trans.commit();
            isQuering = false;
        }
    }
}
