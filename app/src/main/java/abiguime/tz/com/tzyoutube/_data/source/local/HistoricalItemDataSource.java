package abiguime.tz.com.tzyoutube._data.source.local;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import abiguime.tz.com.tzyoutube._data.HistoricalItem;
import abiguime.tz.com.tzyoutube._data.source.HistoricalItemData;
import abiguime.tz.com.tzyoutube.search.SearchActivity;
import abiguime.tz.com.tzyoutube.search.fragment_search_history.SearchHistoryItemFragment;
import abiguime.tz.com.tzyoutube.search.fragment_search_history.SearchPageHistoricPresenter;

/**
 * Created by abiguime on 2016/12/7.
 */

public class HistoricalItemDataSource implements HistoricalItemData {



    public HistoricalItemDataSource(Context ctx) {
    }

    @Override
    public List<HistoricalItem> getLastItems() {

        int count = 20;
        List<HistoricalItem> smp = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            smp.add(new HistoricalItem(i, "Historical "+i));
        }
        return smp;
    }

    @Override
    public void saveItemToDb(HistoricalItem item) {

    }


}
