package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.remotearthsolutions.expensetracker.R;
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

            if (licenseFileName[position].equals(licenseFileName[0])) {
                sendLicenseFileToWebFragment(Constants.RAZERDPANIMATEDPIEVIEW_LICENSE_FILE);
            }
            if (licenseFileName[position].equals(licenseFileName[1])) {
                sendLicenseFileToWebFragment(Constants.WUNDERLISTSLIDINGLAYER_LICENSE_FILE);
            }
            if (licenseFileName[position].equals(licenseFileName[2])) {
                sendLicenseFileToWebFragment(Constants.ROOM_LICENSE_FILE);
            }
            if (licenseFileName[position].equals(licenseFileName[3])) {
                sendLicenseFileToWebFragment(Constants.PURCHASEDCHECKOUT_LICENSE_FILE);
            }
            if (licenseFileName[position].equals(licenseFileName[4])) {
                sendLicenseFileToWebFragment(Constants.DEXTER_LICENSE_FILE);
            }
            if (licenseFileName[position].equals(licenseFileName[5])) {
                sendLicenseFileToWebFragment(Constants.RXJAVA_LICENSE_FILE);
            }
            if (licenseFileName[position].equals(licenseFileName[6])) {
                sendLicenseFileToWebFragment(Constants.PERCELER_LICENSE_FILE);
            }
            if (licenseFileName[position].equals(licenseFileName[7])) {
                sendLicenseFileToWebFragment(Constants.GSON_LICENSE_FILE);
            }
            
        });
        return view;
    }

    private void sendLicenseFileToWebFragment(String filepath)
    {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_URL, filepath);
        webViewFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, webViewFragment, LicenseFragment.class.getName());
        fragmentTransaction.commit();
    }

}
