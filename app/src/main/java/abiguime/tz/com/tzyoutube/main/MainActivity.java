package abiguime.tz.com.tzyoutube.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Visibility;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._commons.core.YtbApplication;
import abiguime.tz.com.tzyoutube._commons.customviews.YoutubeLayout;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.source.remote.UserRemoteDataSource;
import abiguime.tz.com.tzyoutube._data.source.remote.VideoRemoteDataSource;
import abiguime.tz.com.tzyoutube.main.fragment_home.HomePageContract;
import abiguime.tz.com.tzyoutube.main.fragment_home.HomePageFragment;
import abiguime.tz.com.tzyoutube.main.fragment_home.HomePagePresenter;
import abiguime.tz.com.tzyoutube.main.fragment_hot.HotPageFragment;
import abiguime.tz.com.tzyoutube.main.fragment_player.PlayerFragment;
import abiguime.tz.com.tzyoutube.main.fragment_user.UserPageContract;
import abiguime.tz.com.tzyoutube.main.fragment_user.UserPageFragment;
import abiguime.tz.com.tzyoutube.main.fragment_user.UserPagePresenter;
import abiguime.tz.com.tzyoutube.search.SearchActivity;


public class MainActivity extends AppCompatActivity implements
        UserPageFragment.OnFragmentInteractionListener,
        HomePageFragment.OnFragmentInteractionListener,
        HotPageFragment.OnFragmentInteractionListener
{

    public static final long DURATION = 300;
    /* 内部所有fragment的presenter */
    public HomePagePresenter homePagePresenter;
    public UserPagePresenter userPagePresenter;

    // models
    public UserRemoteDataSource userremote;
    private VideoRemoteDataSource homePageRemote;

    /* views*/
    FrameLayout activity_frame;

    Map<Integer, WeakReference<Fragment>> frg;

    ViewPager vp;
    TabLayout tabs;

    AppBarLayout appbar;
    Toolbar toolbar;

    // YoutubeLayout
//    YoutubeLayout ytb;


    private MainActivityPageAdapter vpadapter;
    private PlayerFragment playerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化view
        initViews();
        initActionBar();
        // 初始化内部的所有fragment 。。。 （ViewPager)
        initViewPager();
        // 初始化presenter
        initRemote();
        initPresenter();
    }


    @Override
    protected void onPause() {
        super.onPause();
        // 1 - 如果用户突然退出本activity， 应该把当前的请求都停止。
        // 2 - 如果在播放视频，那么把视频也停止，并且释放资源
        ((YtbApplication)getApplication()).release();
    }

    /* 当回到activity是，就获取到本数据 */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /* 当退出activity时，可以把数据存到这里*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 如果
    }


    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.home_tab));
        setSupportActionBar(toolbar);
    }

    private void initRemote() {
        userremote = new UserRemoteDataSource(this);
        homePageRemote = new VideoRemoteDataSource(this);
    }

    private void initViewPager() {

        vpadapter = new MainActivityPageAdapter(getSupportFragmentManager());
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(vpadapter);
        // 把tab 绑定到viewpager上
        tabs.setupWithViewPager(vp);

        int[] sFocusedSelected = {
                android.R.attr.state_selected
        };
        int[] sDefaultSelected = {};


        // 默认 -- 白色 图标
        Drawable dhome = ContextCompat.getDrawable(this, R.drawable.ic_tab_home);
        Drawable dhot = ContextCompat.getDrawable(this, R.drawable.ic_tab_trending);
        Drawable daccount = ContextCompat.getDrawable(this, R.drawable.ic_tab_account);


        //  被改色的图标
        Drawable dhome_dark = filter(R.drawable.ic_tab_home);
        Drawable dhot_dark = filter(R.drawable.ic_tab_trending);
        Drawable daccount_dark = filter(R.drawable.ic_tab_account);


        // 创建selector
        StateListDrawable sdhome = new StateListDrawable();
        sdhome.addState(sFocusedSelected, dhome);
        sdhome.addState(sDefaultSelected, dhome_dark);


        StateListDrawable sdhot = new StateListDrawable();
        sdhot.addState(sFocusedSelected, dhot);
        sdhot.addState(sDefaultSelected, dhot_dark);


        StateListDrawable sdacc = new StateListDrawable();
        sdacc.addState(sFocusedSelected, daccount);
        sdacc.addState(sDefaultSelected, daccount_dark);

        //
        ImageView ihm = new ImageView(this);
        ihm.setImageDrawable(sdhome);
        ImageView iht = new ImageView(this);
        iht.setImageDrawable(sdhot);
        ImageView iacc = new ImageView(this);
        iacc.setImageDrawable(sdacc);

        int padding = getResources().getDimensionPixelSize(R.dimen.tab_padding);
        // 有于本view还没加入改布局的原因， 可以通过本方法添加padding
        ViewCompat.setPaddingRelative(ihm, padding, padding, padding, padding);
        ViewCompat.setPaddingRelative(iht, padding, padding, padding, padding);
        ViewCompat.setPaddingRelative(iacc, padding, padding, padding, padding);

        //
        tabs.getTabAt(0).setCustomView(ihm);
        tabs.getTabAt(1).setCustomView(iht);
        tabs.getTabAt(2).setCustomView(iacc);


        // 更改tab 下线的颜色
        ihm.setSelected(true);
        tabs.setSelectedTabIndicatorColor(Color.WHITE);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 切换activity 标题
                toolbar.setTitle(vpadapter.getPageTitle(tab.getPosition()));

                // 绑定viewpager和当前点击的tab
                vp.setCurrentItem(tab.getPosition());

                // 之后再第一次切换到本fragment 的时候才会开始加载数据
                if (tab.getPosition() == 1) {
                    ((HotPageFragment)frg.get(1).get()).setToLoad();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                appbar.setExpanded(true, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initViews() {
//        activity_frame = (FrameLayout) findViewById(R.id.activity_frame);
        vp = (ViewPager) findViewById(R.id.vpcontainer);
        tabs = (TabLayout) findViewById(R.id.tabs);
        appbar = (AppBarLayout) findViewById(R.id.appbar);

        // hide fragment
        playerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player_fragment);
//        getSupportFragmentManager().beginTransaction().hide(playerFragment).commit();
    }


    private void initPresenter() {
    }


    public Drawable filter(int drawableId) {
        int iColor = Color.parseColor("#FF5E1E1C");
        return new ColouredDrawable(BitmapFactory.decodeResource(getResources(),
                drawableId),
                iColor,
                getResources());

    }

    // menus creationg

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main2, menu);
        menu.findItem(R.id.search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                jumpToSearch();
                return true;
            }
        });
        return true;
    }


    private void jumpToSearch() {



        Intent in = new Intent(this, SearchActivity.class);
        in.putExtra("isplaying", ((YtbApplication)getApplication()).getIsplaying());
        // pass the toolbar between activities

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade explode = new Fade();
            explode.setDuration(DURATION);
            getWindow().setExitTransition(explode);
            getWindow().setReenterTransition(explode);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    appbar, "trans_to_search");
            // put the items to transfer
//            ActivityCompat.startActivity(this, in, options.toBundle());
            appbar.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        startActivity(in, options.toBundle());
        } else {
            startActivity(in);
            overridePendingTransition(0, 0);
        }
    }



    private void goSearch() {
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Toast.makeText(this, "Searching for "+query, Toast.LENGTH_LONG).show();
        }
    }

    // 是否第一次播放视频
    boolean isFirst = false;



    @Override
    public void playVideo(Video video, View v) {
        Toast.makeText(this, "Play "+video.toString(), Toast.LENGTH_SHORT).show();
        if (playerFragment.isHidden()) {
            getSupportFragmentManager().beginTransaction().show(playerFragment).commit();
        }
        playerFragment.playVideo(video);
    }


    class MainActivityPageAdapter extends FragmentPagerAdapter {

        public MainActivityPageAdapter(FragmentManager fm) {
            super(fm);
        }

        /* 返回fragment方法 */
        @Override
        public Fragment getItem(int position) {

            if (frg == null) {
                frg = new HashMap<>();
            }
            switch (position) {
                case 0:
                    //
                    if (frg.get(0) == null) {
                        Fragment f0 = HomePageFragment.newInstance();
                        frg.put(position, new WeakReference<Fragment>(f0));
                        homePagePresenter = new HomePagePresenter((HomePageContract.View) frg.get(position).get(), homePageRemote);
                        ((HomePageContract.View) ( frg.get(position).get())).setPresenter(homePagePresenter);
                    }
                    break;
                case 1:
                    //
                    if (frg.get(position) == null) {
                        Fragment f0 = HotPageFragment.newInstance();
                        frg.put(position, new WeakReference<Fragment>(f0));
                    }
                    break;
                case 2:
                    //
                    if (frg.get(position) == null) {
                        Fragment f0 = UserPageFragment.newInstance();
                        frg.put(position, new WeakReference<Fragment>(f0));
                        userPagePresenter = new UserPagePresenter((UserPageContract.View) frg.get(position).get(), userremote);
                        ((UserPageContract.View) ( frg.get(position).get())).setPresenter(userPagePresenter);
                    }
                    break;
            };
            return frg.get(position).get();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //
            String title = "";
            switch (position){
                case 0:
                    title = getString(R.string.home_tab);
                    break;
                case 1:
                    title = getString(R.string.hot_tab);
                    break;
                case 2:
                    title = getString(R.string.user_tab);
                    break;
            }
            return title;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


    /* 通过bitmap创建drawable：
    * 主要原理就是：
    *   1- 创建画板
    *   2- 给画板添加颜色过滤器，颜色过滤器作用就是把画板上的颜色都处理。我们这只需要把板上的颜色
    *       都改成黑色。
    * */
    public class ColouredDrawable extends BitmapDrawable {

        private ColorFilter mColorFilter;

        public ColouredDrawable(Bitmap toTransform, int toColour, Resources resources) {
            super(resources, toTransform);
            float[] matrix = {
                    0, 0, 0, 0, ((toColour & 0xFF0000) / 0xFFFF),
                    0, 0, 0, 0, ((toColour & 0xFF00) / 0xFF),
                    0, 0, 0, 0, (toColour & 0xFF),
                    0, 0, 0, 1, 0};
            mColorFilter = new ColorMatrixColorFilter(matrix);
        }

        @Override
        public void draw(Canvas canvas) {
            // 给canvas （画板） 添加过滤器
            setColorFilter(mColorFilter);
            super.draw(canvas);
        }
    }

}
