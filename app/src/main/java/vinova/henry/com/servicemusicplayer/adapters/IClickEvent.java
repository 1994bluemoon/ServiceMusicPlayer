package vinova.henry.com.servicemusicplayer.adapters;

import java.util.List;

import vinova.henry.com.servicemusicplayer.model.Song;

public interface IClickEvent {

        void onItemClicked(List<Song> songs, int position);

}
