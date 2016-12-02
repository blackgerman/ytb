package abiguime.tz.com.tzyoutube.main.fragment_home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._data.Video;


public class HomePageFragment extends Fragment
        implements HomePageContract.View{


    // Presenter
    HomePageContract.Presenter presenter;



    private OnFragmentInteractionListener mListener;
    private RecyclerViewAdapter adapter;

    public HomePageFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance() {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }


    SwipeRefreshLayout swipetorefresh;
    RecyclerView recyclerView;
    ProgressBar pb;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViews(view);
        // 获取数据
        presenter.loadVideos();
    }

    private void getViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        swipetorefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipetorefresh);
        pb = (ProgressBar) view.findViewById(R.id.frg_progressbar_circular);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void showEmptyVideoError() {
    /* - 加载失败
         - 显示失败的内容提示*/
    }

    @Override
    public void showVideosLoadingOnGoing() {
    /* - 加载中
         显示加载进度条*/
        pb.setVisibility(View.GONE);
    }

    @Override
    public void showVideosLoadingComplete() {
        /* - 加载完成
         隐藏加载进度条*/
    }

    @Override
    public void setPresenter(Object presenter) {
        this.presenter = (HomePageContract.Presenter) presenter;
    }

    @Override
    public void showVideoList(List<Video> videos) {
//        this.videos = videos;
        initRecyclerview(videos);
    }

    private void initRecyclerview(List<Video> videos) {

        Log.d("xxx", "Total items "+videos.size());

        adapter = new RecyclerViewAdapter(getContext(), videos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        swipetorefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void appendVideosToList(List<Video> videos) {

    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        private final Context ctx;
        private final List<Video> videos;
        private int sw;
        private int sh;

        public RecyclerViewAdapter(Context ctx, List<Video> videos) {
            this.ctx = ctx;
            this.videos = videos;
            // screen size 16:9
            sw = ctx.getResources().getDisplayMetrics().widthPixels;
            // 减去图片的 margin
            sw -= 2 * getResources().getDimensionPixelSize(R.dimen.main_img_margin);
            sh = sw * 9 /16;
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.main_home_recycler_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
            Video video = videos.get(position);
            holder.tvTitle.setText(video.title);

            // 设置图片大小
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.ivcover.getLayoutParams();
            lp.width = sw;
            lp.height = sh;
            holder.ivcover.setLayoutParams(lp);
        }

        @Override
        public int getItemCount() {
            return videos.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView ivcover, ivUser;
            public TextView tvTitle;

            public ViewHolder(View itemView) {
                super(itemView);
                ivcover = (ImageView) itemView.findViewById(R.id.imageview_cover);
                ivUser = (ImageView) itemView.findViewById(R.id.iv_user);
                tvTitle = (TextView) itemView.findViewById(R.id.tvtitle);
            }
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
