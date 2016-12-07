package abiguime.tz.com.tzyoutube._data;

import java.util.List;

/**
 * Created by abiguime on 2016/12/7.
 */

public class HistoricalItem {

    public String name;
    public int id;

    public HistoricalItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "HistoricalItem{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

}
