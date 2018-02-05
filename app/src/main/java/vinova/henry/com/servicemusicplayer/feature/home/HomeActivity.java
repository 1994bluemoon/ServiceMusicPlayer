package vinova.henry.com.servicemusicplayer.feature.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vinova.henry.com.servicemusicplayer.R;
import vinova.henry.com.servicemusicplayer.adapters.SongAdapter;
import vinova.henry.com.servicemusicplayer.service.PlayerService;

public class HomeActivity extends AppCompatActivity implements IHomeContract.IView {

    public static final int RUNTIME_PERMISSION_CODE = 7;
    SongAdapter adapter;
    HomePresenter homePresenter;

    @BindView(R.id.rcv_home)
    RecyclerView rcvHome;
    @BindView(R.id.bt_stop)
    Button btStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        AndroidRuntimePermission();

        adapter = new SongAdapter(this);
        rcvHome.setLayoutManager(new LinearLayoutManager(this));
        rcvHome.setAdapter(adapter);

        homePresenter = new HomePresenter(this, this);
        homePresenter.getAllMusic();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateRecyclerView() {
        adapter.setSongs(homePresenter.getSongs());
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.bt_stop)
    public void onViewClicked() {
        stopService(new Intent(this, PlayerService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case RUNTIME_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
            }
        }
    }

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

                    alert_builder.setNeutralButton("Cancel", null);

                    AlertDialog dialog = alert_builder.create();

                    dialog.show();

                } else {

                    ActivityCompat.requestPermissions(
                            HomeActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            } else {

            }
        }
    }

}
