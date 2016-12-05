package abiguime.tz.com.tzyoutube.main;

import abiguime.tz.com.tzyoutube.BaseView;

/**
 * Created by abiguime on 2016/11/30.
 *
 * - 把main actiivty 里的fragment 的共工需求集合
 */
public interface MainFragmentBaseView<U> extends BaseView {

    public void showEmptyVideoError();
    public void showVideosLoadingOnGoing();
    public void showVideosLoadingComplete();
}
