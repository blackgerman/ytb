package abiguime.tz.com.tzyoutube.main.fragment_player;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._commons.customviews.YoutubeLayout;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube.main.MainActivity;
import abiguime.tz.com.tzyoutube.search.SearchActivity;

/**
 * Created by abiguime on 2016/12/19.
 */

public class PlayerFragment extends Fragment {

    public static final String TAG = PlayerFragment.class.getName();
    private YoutubeLayout ytb;
    private boolean isFirst = false;


    public static PlayerFragment newInstance() {
        Bundle args = new Bundle();
        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContext() instanceof SearchActivity) {
            return inflater.inflate(R.layout.player_fragment_minized, container, false);
        } else  if (getContext() instanceof MainActivity) {
            return inflater.inflate(R.layout.player_fragment_full, container, false);
        }
        return null;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
    }

    private void mT(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void initViews(View view) {
        ytb = (YoutubeLayout) view.findViewById(R.id.youtubelayout);
    }

    public void playVideo(Video video) {
        if (ytb != null && ytb.getVisibility() != View.VISIBLE) {
            ytb.setVisibility(View.VISIBLE);
            ytb.requestHeaderContent();
        }
        ytb.setVideo(video, isFirst);
    }

}
