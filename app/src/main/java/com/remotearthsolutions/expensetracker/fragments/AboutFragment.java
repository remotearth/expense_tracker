package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.remotearthsolutions.expensetracker.BuildConfig;
import com.remotearthsolutions.expensetracker.R;

public class AboutFragment extends Fragment {

    public AboutFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView versionnumber = view.findViewById(R.id.versionno);
        String getVersion = BuildConfig.VERSION_NAME;
        versionnumber.setText(getString(R.string.version)+getVersion);

        return view;
    }
}
