package vinova.henry.com.servicemusicplayer.model;

import java.io.Serializable;

public class Song implements Serializable {
    private long SongId;
    private String Title;
    private String Singer;

    public Song(long songId, String title, String singer) {
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
