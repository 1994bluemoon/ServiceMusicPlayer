package vinova.henry.com.servicemusicplayer.feature.home;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vinova.henry.com.servicemusicplayer.model.Songs;

/**
 * Created by dminh on 2/5/2018.
 */

public class HomePresenter implements IHomeContract.IPresenter{

    IHomeContract.IView iView;
    Context mContext;
    List<Songs> songs;
    ContentResolver contentResolver;
    Uri uri;
    Cursor cursor;

    public HomePresenter(Context mContext, IHomeContract.IView view) {
        this.mContext = mContext;
        this.iView = view;
        songs = new ArrayList<>();
    }

    public List<Songs> getSongs() {
        return songs;
    }

    @Override
    public void getAllMusic() {
        contentResolver = mContext.getContentResolver();

        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        cursor = contentResolver.query(
                uri, // Uri
                null,
                null,
                null,
                null
        );

        if (cursor == null) {

            Log.d("presenter", "no music");

        } else if (!cursor.moveToFirst()) {

        }
        else {

            int Title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int Id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int Singer = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            //Getting Song ID From Cursor.
            //int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {
                // You can also get the Song ID using cursor.getLong(id).
                //long SongID = cursor.getLong(id);

                String SongTitle = cursor.getString(Title);
                Long songId = cursor.getLong(Id);
                String singer = cursor.getString(Singer);

                // Adding Media File Names to ListElementsArrayList.
                songs.add(new Songs(songId, SongTitle, singer));

            } while (cursor.moveToNext());

            iView.updateRecyclerView();
        }
    }
}
