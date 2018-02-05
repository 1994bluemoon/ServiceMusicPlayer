package vinova.henry.com.servicemusicplayer.model;

import java.io.Serializable;

/**
 * Created by dminh on 2/5/2018.
 */

public class Songs implements Serializable {
    long SongId;
    String Title;
    String Singer;

    public Songs(long songId, String title, String singer) {
        SongId = songId;
        Title = title;
        Singer = singer;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public long getSongId() {
        return SongId;
    }

    public String getSinger() {
        return Singer;
    }
}
