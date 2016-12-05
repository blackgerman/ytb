package abiguime.tz.com.tzyoutube._data;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import abiguime.tz.com.tzyoutube._data.constants.ImagesRepo;

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


    public static List<Video> fakeVideos(int count) {
        List<Video> vs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Random rd = new Random();
            Video v = new Video((int) SystemClock.currentThreadTimeMillis(),
                    "",
                    0, // upload time
                    rd.nextInt(200000),
                    rd.nextInt(200),
                    1,
                    rd.nextInt(3600),
                    ImagesRepo.descriptions[rd.nextInt(ImagesRepo.descriptions.length)],
                    1, /// category
                    ImagesRepo.ppics[rd.nextInt(ImagesRepo.ppics.length)],
                    ImagesRepo.titles[rd.nextInt(ImagesRepo.titles.length)],
                    ImagesRepo.covers[rd.nextInt(ImagesRepo.covers.length)]
            );
            vs.add(v);
        }
        return vs;
    }

}
