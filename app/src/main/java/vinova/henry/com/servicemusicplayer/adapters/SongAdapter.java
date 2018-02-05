package vinova.henry.com.servicemusicplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import vinova.henry.com.servicemusicplayer.feature.home.HomeActivity;
import vinova.henry.com.servicemusicplayer.feature.home.IHomeContract;
import vinova.henry.com.servicemusicplayer.model.Songs;
import vinova.henry.com.servicemusicplayer.service.PlayerService;

/**
 * Created by dminh on 2/5/2018.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    Context mContext;
    List<Songs> songs;


    public SongAdapter(Context mContext) {
        this.mContext = mContext;
        songs = new ArrayList<>();
    }

    public void setSongs(List<Songs> songs) {
        this.songs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_song, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTitle.setText(songs.get(position).getTitle());
        holder.tvSinger.setText(songs.get(position).getSinger());
        holder.cvSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(mContext, PlayerService.class);
// Pass data to be processed on the Service
                Bundle data = new Bundle();
                data.putSerializable("Song", songs.get(position));
                serviceIntent.putExtras(data);
// Starting the Service
                mContext.startService(serviceIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_singer)
        TextView tvSinger;
        @BindView(R.id.cv_song)
        CardView cvSong;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
