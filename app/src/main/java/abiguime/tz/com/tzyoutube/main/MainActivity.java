package abiguime.tz.com.tzyoutube.main;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._data.User;
import abiguime.tz.com.tzyoutube._data.source.remote.UserRemoteDataSource;
import abiguime.tz.com.tzyoutube.main.fragment_home.HomePagePresenter;
import abiguime.tz.com.tzyoutube.main.fragment_user.UserPageFragment;
import abiguime.tz.com.tzyoutube.main.fragment_user.UserPagePresenter;

public class MainActivity extends AppCompatActivity implements UserPageFragment.OnFragmentInteractionListener {

    /* 内部所有fragment的presenter */
    public HomePagePresenter homePagePresenter;
    public UserPagePresenter userPagePresenter;

    // models
    public UserRemoteDataSource userremote;

    /* views*/
    FrameLayout activity_frame;

    //frames
    UserPageFragment frg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 初始化view
        initViews();
        // 初始化内部的所有fragment 。。。 （ViewPager)

        initPager();

        // 初始化presenter
        initRemote();
        initPresenter();
    }

    private void initRemote() {
        userremote = new UserRemoteDataSource(this);
    }

    private void initPager() {
       frg = UserPageFragment.newinstance();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.activity_frame, frg, UserPageFragment.class.getName());
        fragmentTransaction.commit();
    }

    private void initViews() {
        activity_frame = (FrameLayout) findViewById(R.id.activity_frame);
    }

    private void initPresenter() {
//        homePagePresenter = new HomePagePresenter(getSupportFragmentManager());
        userPagePresenter = new UserPagePresenter(frg, userremote);
        frg.setPresenter(userPagePresenter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
