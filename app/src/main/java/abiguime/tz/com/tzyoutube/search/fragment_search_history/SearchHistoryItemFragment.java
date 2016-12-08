package abiguime.tz.com.tzyoutube.search.fragment_search_history;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._data.HistoricalItem;


/* 显示历史的fragment */
public class SearchHistoryItemFragment extends Fragment implements SearchPageContract.View {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    public static final String TAG = SearchHistoryItemFragment.class.getName();
    private RecyclerView.Adapter adapter;

    SearchPageHistoricPresenter repo;
    private RecyclerView recyclerView;

    public SearchHistoryItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SearchHistoryItemFragment newInstance() {
        SearchHistoryItemFragment fragment = new SearchHistoryItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
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
            // 获取最新的数据
            repo.getLastItems();
        }
    }



    @Override
    public void setPresenter(SearchPageContract.Presenter presenter) {
        this.repo = (SearchPageHistoricPresenter) presenter;
    }

    @Override
    public void showHistoric(List<HistoricalItem> items) {
        adapter = new MySearchHistoryItemRecyclerViewAdapter(items, mListener);
        recyclerView.setAdapter(adapter);
    }


    class MySearchHistoryItemRecyclerViewAdapter extends RecyclerView.Adapter<MySearchHistoryItemRecyclerViewAdapter.ViewHolder> {

        private final List<HistoricalItem> mValues;
        private final SearchHistoryItemFragment.OnListFragmentInteractionListener mListener;

        public MySearchHistoryItemRecyclerViewAdapter(List<HistoricalItem> items, SearchHistoryItemFragment.OnListFragmentInteractionListener listener) {
            mValues = items;
            mListener = listener;
        }

        @Override
        public void onBindViewHolder(final MySearchHistoryItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mSearchContent.setText(mValues.get(position).name);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.search(holder.mItem.name);
                }
            });
            holder.mright_suggestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.suggest(holder.mItem.name);
                }
            });
        }


        @Override
        public MySearchHistoryItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_search_history_item, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final ImageView mleft_search;
            public final ImageView mright_suggestion;
            public final TextView mSearchContent;
            private final View mView;
            public HistoricalItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mleft_search = (ImageView) view.findViewById(R.id.iv_search_type_icon);
                mright_suggestion = (ImageView) view.findViewById(R.id.iv_suggestion);
                mSearchContent = (TextView) view.findViewById(R.id.text);
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
//        void onListFragmentInteraction(HistoricalItem item);
//        void onQuerySent(String query);
        void suggest(String suggest);
        void search(String search);
    }

}
