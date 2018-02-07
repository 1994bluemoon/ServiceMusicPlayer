package vinova.henry.com.servicemusicplayer.feature.home;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vinova.henry.com.servicemusicplayer.model.Song;

public class HomePresenter implements IHomeContract.IPresenter{

    private IHomeContract.IView iView;
    private Context mContext;
    private List<Song> songs;

    HomePresenter(Context mContext, IHomeContract.IView view) {
        this.mContext = mContext;
        this.iView = view;
        songs = new ArrayList<>();
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public IHomeContract.IView getiView() {
        return iView;
    }

    public Context getmContext() {
        return mContext;
    }

    List<Song> getSongs() {
        return songs;
    }

    @Override
    public void getAllMusic() {

        new doAsync(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //iView.updateRecyclerView();
    }

    static class doAsync extends AsyncTask<Void, Void, List<Song>>{
        HomePresenter presenter;
        Context context;
        private IHomeContract.IView iView;


        public doAsync(HomePresenter presenter) {
            this.presenter = presenter;
            this.context = presenter.getmContext();
            this.iView = presenter.getiView();
        }

        @Override
        protected List<Song> doInBackground(Void... voids) {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            List<Song> songs = new ArrayList<>();

            @SuppressLint("Recycle")
            Cursor cursor = contentResolver.query(
                    uri, // Uri
                    null,
                    null,
                    null,
                    null
            );

            if (cursor == null) {
                Log.d("presenter", "no music");
            } else if (!cursor.moveToFirst()) {
                cursor.moveToFirst();
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
                    songs.add(new Song(songId, SongTitle, singer));

                } while (cursor.moveToNext());

                return songs;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            presenter.setSongs(songs);
            iView.updateRecyclerView();
        }
    }
}


