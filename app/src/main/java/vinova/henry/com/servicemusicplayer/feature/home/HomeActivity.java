package vinova.henry.com.servicemusicplayer.feature.home;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vinova.henry.com.servicemusicplayer.R;
import vinova.henry.com.servicemusicplayer.adapters.IClickEvent;
import vinova.henry.com.servicemusicplayer.adapters.SongAdapter;
import vinova.henry.com.servicemusicplayer.model.Song;
import vinova.henry.com.servicemusicplayer.service.PlayerServiceImp;

public class HomeActivity extends AppCompatActivity implements IHomeContract.IView, IClickEvent {

    public static final int RUNTIME_PERMISSION_CODE = 7;
    SongAdapter adapter;
    HomePresenterImp homePresenter;
    PlayerServiceImp mService;
    List<Song> songList;
    boolean mBound = false;

    @BindView(R.id.rcv_home)
    RecyclerView rcvHome;
    @BindView(R.id.fm_minicontrol)
    FrameLayout fmMinicontrol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        fmMinicontrol.addView(LayoutInflater.from(this).inflate(R.layout.mini_control_layout, null));
        final ViewHolder holder = new ViewHolder(fmMinicontrol);

        AndroidRuntimePermission();

        adapter = new SongAdapter(this, this);
        rcvHome.setLayoutManager(new LinearLayoutManager(this));
        rcvHome.setAdapter(adapter);

        homePresenter = new HomePresenterImp(this, this);
        homePresenter.getAllMusic();
        songList = homePresenter.getSongs();

        holder.btDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound){
                    unbindService(mConnection);
                    mService.stopSelf();
                    mBound = false;
                }

            }
        });

        holder.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) mService.next();
            }
        });

        holder.btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) mService.resume();
            }
        });

        holder.btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) mService.previous();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, PlayerServiceImp.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        mBound = false;
        mService.stopSelf();
    }

    @Override
    public void updateRecyclerView() {
        adapter.setSongs(homePresenter.getSongs());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(List<Song> songs, int position) {
        Intent serviceIntent = new Intent(this, PlayerServiceImp.class);
        // Pass data to be processed on the Service
        Bundle data = new Bundle();
        data.putInt("position", position);
        data.putSerializable("SongList", (Serializable) songs);
        serviceIntent.putExtras(data);
        // Starting the Service
        this.startService(serviceIntent);
        if (!mBound) {
            Intent intent = new Intent(this, PlayerServiceImp.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            mBound = true;
        }
        fmMinicontrol.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case RUNTIME_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permission", "granted");
                } else {
                    Log.d("permission", "Need confirm permission");
                }
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayerServiceImp.LocalBinder binder = (PlayerServiceImp.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    /*@OnClick({R.id.bt_next, R.id.bt_previous})
    public void onViewClicked(View view) {
        if (mBound) {
            switch (view.getId()) {
                case R.id.bt_next:
                    mService.next();
                    break;
                case R.id.bt_previous:
                    mService.previous();
                    break;
                *//*case R.id.bt_stop:
                    unbindService(mConnection);
                    mService.stopSelf();
                    mBound = false;
                    break;*//*
            }
        }
    }
*/
    // Creating Runtime permission function.
    public void AndroidRuntimePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(HomeActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(
                                    HomeActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RUNTIME_PERMISSION_CODE

                            );
                        }
                    });

                    alert_builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AlertDialog.Builder(HomeActivity.this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Permission")
                                    .setMessage("Please allow read internal storage")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            AndroidRuntimePermission();
                                            //finish();
                                        }
                                    })
                                    .show();
                        }
                    });

                    AlertDialog dialog = alert_builder.create();

                    dialog.show();

                } else {
                    ActivityCompat.requestPermissions(
                            HomeActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            }
        }
    }

    static class ViewHolder {
        @BindView(R.id.bt_play)
        ImageButton btPlay;
        @BindView(R.id.bt_previous)
        ImageButton btPrevious;
        @BindView(R.id.bt_next)
        ImageButton btNext;
        @BindView(R.id.bt_destroy)
        ImageButton btDestroy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
