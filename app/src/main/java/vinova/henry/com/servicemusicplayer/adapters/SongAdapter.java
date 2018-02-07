package vinova.henry.com.servicemusicplayer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vinova.henry.com.servicemusicplayer.R;
import vinova.henry.com.servicemusicplayer.model.Song;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context mContext;
    private List<Song> songs;
    private IClickEvent iClickEvent;

    public SongAdapter(Context mContext, IClickEvent event) {
        this.mContext = mContext;
        this.iClickEvent = event;
        songs = new ArrayList<>();
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_song, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvTitle.setText(songs.get(position).getTitle());
        holder.tvSinger.setText(songs.get(position).getSinger());
        holder.cvSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickEvent.onItemClicked(songs, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_singer)
        TextView tvSinger;
        @BindView(R.id.cv_song)
        CardView cvSong;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
