package abiguime.tz.com.tzyoutube.search.fragment_search_result;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._data.HistoricalItem;
import abiguime.tz.com.tzyoutube._data.Video;

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
        return inflater.inflate(R.layout.fragment_searchhistoryitem_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
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

        public MySearchResultItemRecyclerViewAdapter(List<Video> items, SearchResultItemFragment.OnListFragmentInteractionListener listener) {
            mValues = items;
            mListener = listener;
        }

        @Override
        public void onBindViewHolder(final MySearchResultItemRecyclerViewAdapter.ViewHolder holder, int position) {
//            holder.mItem = mValues.get(position);
//            holder.mSearchContent.setText(mValues.get(position).name);
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

            public ViewHolder(View view) {
                super(view);
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
        void onListFragmentInteraction(HistoricalItem item);
    }

}
