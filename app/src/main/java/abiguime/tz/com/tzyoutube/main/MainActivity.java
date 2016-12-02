package abiguime.tz.com.tzyoutube.main;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._data.User;
import abiguime.tz.com.tzyoutube._data.source.remote.UserRemoteDataSource;
import abiguime.tz.com.tzyoutube._data.source.remote.VideoRemoteDataSource;
import abiguime.tz.com.tzyoutube.main.fragment_home.HomePageContract;
import abiguime.tz.com.tzyoutube.main.fragment_home.HomePageFragment;
import abiguime.tz.com.tzyoutube.main.fragment_home.HomePagePresenter;
import abiguime.tz.com.tzyoutube.main.fragment_hot.HotPageFragment;
import abiguime.tz.com.tzyoutube.main.fragment_user.UserPageContract;
import abiguime.tz.com.tzyoutube.main.fragment_user.UserPageFragment;
import abiguime.tz.com.tzyoutube.main.fragment_user.UserPagePresenter;


public class MainActivity extends AppCompatActivity implements
        UserPageFragment.OnFragmentInteractionListener,
        HomePageFragment.OnFragmentInteractionListener,
        HotPageFragment.OnFragmentInteractionListener {

    /* 内部所有fragment的presenter */
    public HomePagePresenter homePagePresenter;
    public UserPagePresenter userPagePresenter;

    // models
    public UserRemoteDataSource userremote;
    private VideoRemoteDataSource homePageRemote;

    /* views*/
    FrameLayout activity_frame;

    //frames
   /* UserPageFragment frg_user;
    HomePageFragment frg_home;
    HotPageFragment frg_hot;*/

    Map<Integer, WeakReference<Fragment>> frg;

    ViewPager vp;
    TabLayout tabs;

    Toolbar toolbar;


    private MainActivityPageAdapter vpadapter;


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

    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initRemote() {
        userremote = new UserRemoteDataSource(this);
        homePageRemote = new VideoRemoteDataSource(this);
    }

    private void initViewPager() {

        vpadapter = new MainActivityPageAdapter(getSupportFragmentManager());
        vp.setAdapter(vpadapter);
        // 把tab 绑定到viewpager上
        tabs.setupWithViewPager(vp);

        int[] sFocusedSelected = {android.R.attr.state_selected};
        int[] sDefaultSelected = { };


        // 默认 -- 白色 图标
        Drawable dhome = getResources().getDrawable(R.drawable.ic_tab_home);
        Drawable dhot = getResources().getDrawable(R.drawable.ic_tab_trending);
        Drawable daccount = getResources().getDrawable(R.drawable.ic_tab_account);


        //  被改色的图标
        Drawable dhome_dark = filter(Color.BLACK, R.drawable.ic_tab_home);
        Drawable dhot_dark  = filter(Color.BLACK, R.drawable.ic_tab_trending);
        Drawable daccount_dark = filter(Color.BLACK, R.drawable.ic_tab_account);


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
        ImageView ihm = new ImageView(this); ihm.setImageDrawable(sdhome);
        ImageView iht = new ImageView(this); iht.setImageDrawable(sdhot);
        ImageView iacc = new ImageView(this); iacc.setImageDrawable(sdacc);
        //
        tabs.getTabAt(0).setCustomView(ihm);
        tabs.getTabAt(1).setCustomView(iht);
        tabs.getTabAt(2).setCustomView(iacc);


        // 更改tab 下线的颜色
        tabs.setSelectedTabIndicatorColor(Color.WHITE);
    }

    private void initViews() {
//        activity_frame = (FrameLayout) findViewById(R.id.activity_frame);
        vp = (ViewPager) findViewById(R.id.vpcontainer);
        tabs = (TabLayout) findViewById(R.id.tabs);
    }

    private void initPresenter() {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public Drawable filter (int color, int drawableId) {
        int iColor = color;

        int red   = (iColor & 0xFF0000) / 0xFFFF;
        int green = (iColor & 0xFF00) / 0xFF;
        int blue  = iColor & 0xFF;

        float[] matrix = { 0, 0, 0, 0, red,
                0, 0, 0, 0, green,
                0, 0, 0, 0, blue,
                0, 0, 0, 1, 0 };

        //
        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setColorFilter(colorFilter);
        return drawable;
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
        public int getCount() {
            return 3;
        }
    }
}
