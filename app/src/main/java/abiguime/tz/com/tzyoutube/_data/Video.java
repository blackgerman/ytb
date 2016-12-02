package abiguime.tz.com.tzyoutube._data;

/**
 * Created by abiguime on 2016/11/30.
 */

public class Video {


    /* 视频分类 */
    public enum Category {
        All/*全部*/, Sport, Music, Cartoon
    }

    public int id;
    public String path;
    public long uploadTime;
    public int likes;
    public int dislikes;
    public float duration;
    public String description;
    public int category;
    public String user;
    public String title;
    public String coverimage;

    public Video(int id, String path, long uploadTime, int likes, int dislikes, int isactive, int duration, String description, int category, String user, String title, String coverimage) {
        this.id = id;
        this.path = path;
        this.uploadTime = uploadTime;
        this.likes = likes;
        this.dislikes = dislikes;
        this.duration = duration;
        this.description = description;
        this.category = category;
        this.user = user;
        this.title = title;
        this.coverimage = coverimage;
    }


}
