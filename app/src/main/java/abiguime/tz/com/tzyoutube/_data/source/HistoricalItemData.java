package abiguime.tz.com.tzyoutube._data.source;

import java.util.List;

import abiguime.tz.com.tzyoutube._data.HistoricalItem;

/**
 * Created by abiguime on 2016/12/7.
 */

public interface HistoricalItemData {


    List<HistoricalItem> getLastItems();

    void saveItemToDb(HistoricalItem item);

}
