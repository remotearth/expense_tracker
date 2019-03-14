package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.MainActivity;
import com.remotearthsolutions.expensetracker.utils.Constants;

public class LicenseFragment extends Fragment {


    private ListView listView;
    private ArrayAdapter adapter;

    public LicenseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_license, container, false);
        listView = view.findViewById(R.id.licenseFileList);
        String[] licenseFileName = getResources().getStringArray(R.array.license);
        adapter = new ArrayAdapter(getActivity(),R.layout.custom_license,R.id.custom_text_license,licenseFileName);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {

            switch (position){
                case 0:
                    sendLicenseFileToWebFragment(Constants.RAZERDPANIMATEDPIEVIEW_LICENSE_FILE);
                    break;
                case 1:
                    sendLicenseFileToWebFragment(Constants.ROOM_LICENSE_FILE);
                    break;
                case 2:
                    sendLicenseFileToWebFragment(Constants.PURCHASEDCHECKOUT_LICENSE_FILE);
                    break;
                case 3:
                    sendLicenseFileToWebFragment(Constants.DEXTER_LICENSE_FILE);
                    break;
                case 4:
                    sendLicenseFileToWebFragment(Constants.RXJAVA_LICENSE_FILE);
                    break;
                case 5:
                    sendLicenseFileToWebFragment(Constants.PERCELER_LICENSE_FILE);
                    break;
                case 6:
                    sendLicenseFileToWebFragment(Constants.GSON_LICENSE_FILE);
                    break;
                case 7:
                    sendLicenseFileToWebFragment(Constants.FACEBOOK_LICENSE_FILE);
                    break;
                case 8:
                    sendLicenseFileToWebFragment(Constants.FIREBASE_LICENSE_FILE);
                    break;
                case 9:
                    sendLicenseFileToWebFragment(Constants.RXANROID_LICENSE_FILE);
                    break;
                case 10:
                    sendLicenseFileToWebFragment(Constants.MULTIDEX_LICENSE_FILE);
                    break;
                case 11:
                    sendLicenseFileToWebFragment(Constants.ADMOB_LICENSE_FILE);
                    break;


            }
        });
        return view;
    }

    private void sendLicenseFileToWebFragment(String filepath)
    {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
        }

        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_SCREEN,getString(R.string.license_details));
        bundle.putString(Constants.KEY_URL, filepath);
        webViewFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.framelayout, webViewFragment, WebViewFragment.class.getName());
        fragmentTransaction.addToBackStack(WebViewFragment.class.getName());
        fragmentTransaction.commit();
    }

}
