package abiguime.tz.com.tzyoutube.search.fragment_search_result;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._commons.MDividerDecorationNoTop;
import abiguime.tz.com.tzyoutube._data.Video;
import abiguime.tz.com.tzyoutube._data.constants.Constants;

public class SearchResultItemFragment extends Fragment implements SearchResultContract.View {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    public static final String TAG = SearchResultItemFragment.class.getName();
    private RecyclerView.Adapter adapter;

    SearchResultPresenter repo;
    private RecyclerView recyclerView;
    private List<Video> videos = null;

    public SearchResultItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SearchResultItemFragment newInstance(List<Video> videos) {
        SearchResultItemFragment fragment = new SearchResultItemFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) videos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            videos = getArguments().getParcelableArrayList("key");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_searchresult_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            recyclerView.addItemDecoration(new MDividerDecorationNoTop(ContextCompat.getDrawable(getContext(), R.drawable.search_result_divider_drawable)));
            if (videos != null) {
                adapter = new MySearchResultItemRecyclerViewAdapter(videos, mListener);
                recyclerView.setAdapter(adapter);
            }
        }
    }



    @Override
    public void setPresenter(SearchResultContract.Presenter presenter) {
        this.repo = (SearchResultPresenter) presenter;
    }

    @Override
    public void showResult(List<Video> items) {
        if (recyclerView != null) {
            adapter = new MySearchResultItemRecyclerViewAdapter(videos, mListener);
            recyclerView.setAdapter(adapter);
        }
        this.videos = items;
    }

    public void sendRequest(String query) {
        repo.searchItemsForItem(query);
    }


    class MySearchResultItemRecyclerViewAdapter extends RecyclerView.Adapter<MySearchResultItemRecyclerViewAdapter.ViewHolder> {

        private final List<Video> mValues;
        private final SearchResultItemFragment.OnListFragmentInteractionListener mListener;
        private Drawable emptyCover;

        public MySearchResultItemRecyclerViewAdapter(List<Video> items, SearchResultItemFragment.OnListFragmentInteractionListener listener) {
            mValues = items;
            mListener = listener;
            emptyCover = ContextCompat.getDrawable(getContext(), R.drawable.empty_cover_drawable);
        }

        @Override
        public void onBindViewHolder(final MySearchResultItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.setPosition(position);
            holder.mItem = mValues.get(position);
            holder.tv_title.setText(holder.mItem.title);
            holder.tv_channel.setText(holder.mItem.channel);
            holder.ivpic.setImageDrawable(emptyCover);
            Picasso.with(getContext()).load(Constants.IP + holder.mItem.coverimage).into(holder.ivpic);
        }

        @Override
        public MySearchResultItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_item_result, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public int getItemCount() {
            return mValues == null ? 0 : mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final View mViews;
            public ImageView ivpic;
            public TextView tv_title, tv_channel, tv_uploadtime_views;
            public Video mItem;

            public ViewHolder(View view) {
                super(view);
                mViews = view;
                ivpic = (ImageView) view.findViewById(R.id.imageview);
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_channel = (TextView) view.findViewById(R.id.tv_channel);
                tv_uploadtime_views = (TextView) view.findViewById(R.id.tv_uploadtime_views);
                // compute the size of the
                LinearLayout.LayoutParams pr = (LinearLayout.LayoutParams) ivpic.getLayoutParams();
                pr.gravity = Gravity.TOP;
                pr.height = getContext().getResources().getDimensionPixelSize(R.dimen.search_result_imageview_height);
                pr.width = (pr.height/9)*16;
                ivpic.setLayoutParams(pr);
            }

            public void setPosition(int position) {
                int paddingLeft_Right_Top = getContext().getResources().getDimensionPixelSize(R.dimen.item_margin);
                if (position == 0) {
                    mViews.setPadding(paddingLeft_Right_Top, paddingLeft_Right_Top, paddingLeft_Right_Top, paddingLeft_Right_Top);
                } else {
                    mViews.setPadding(paddingLeft_Right_Top, 0, paddingLeft_Right_Top,paddingLeft_Right_Top);
                }
            }
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onVideoResultSelected(Video video);
    }

}
