package abiguime.tz.com.tzyoutube._data;

/**
 * Created by abiguime on 2016/11/30.
 *
 * 用户bean
 */

public class User {

    public String name;

    public static User fake() {
        User useer = new User();
        useer.name = "Here";
        return useer;
    }
}
