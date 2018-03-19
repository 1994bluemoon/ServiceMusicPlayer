package vinova.henry.com.servicemusicplayer.feature.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vinova.henry.com.servicemusicplayer.R;

public class MiniControlFragment extends Fragment {

    public MiniControlFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mini_control, container, false);

        return v;
    }

}
