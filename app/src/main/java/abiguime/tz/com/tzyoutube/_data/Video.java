package abiguime.tz.com.tzyoutube._data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import abiguime.tz.com.tzyoutube._data.constants.ImagesRepo;

/**
 * Created by abiguime on 2016/11/30.
 */

public class Video implements Parcelable {


    protected Video(Parcel in) {
        id = in.readInt();
        path = in.readString();
        uploadTime = in.readLong();
        likes = in.readInt();
        dislikes = in.readInt();
        duration = in.readFloat();
        description = in.readString();
        category = in.readInt();
        user = in.readString();
        title = in.readString();
        coverimage = in.readString();
        channel = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
        dest.writeLong(uploadTime);
        dest.writeInt(likes);
        dest.writeInt(dislikes);
        dest.writeFloat(duration);
        dest.writeString(description);
        dest.writeInt(category);
        dest.writeString(user);
        dest.writeString(title);
        dest.writeString(coverimage);
        dest.writeString(channel);
    }

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
    public String channel;

    public Video(int id, String path, long uploadTime, int likes, int dislikes, int isactive, int duration, String description, int category, String user, String title, String coverimage, String channel) {
        this.id = id;
       this.channel = channel;
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
                    ImagesRepo.videos[rd.nextInt(ImagesRepo.videos.length)],
                    0, // upload time
                    rd.nextInt(200000),
                    rd.nextInt(200),
                    1,
                    rd.nextInt(3600),
                    ImagesRepo.descriptions[rd.nextInt(ImagesRepo.descriptions.length)],
                    1, /// category
                    ImagesRepo.ppics[rd.nextInt(ImagesRepo.ppics.length)],
                    ImagesRepo.titles[rd.nextInt(ImagesRepo.titles.length)],
                    ImagesRepo.covers[rd.nextInt(ImagesRepo.covers.length)],
                    ImagesRepo.channel[rd.nextInt(ImagesRepo.channel.length)]
            );
            vs.add(v);
        }
        return vs;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", uploadTime=" + uploadTime +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", user='" + user + '\'' +
                ", title='" + title + '\'' +
                ", coverimage='" + coverimage + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
