package com.remotearthsolutions.expensetracker.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.remotearthsolutions.expensetracker.R;

import java.io.File;

import static android.provider.CalendarContract.CalendarCache.URI;


public class Tab2Fragment extends Fragment {

    public Tab2Fragment() {
    }

    private Button shareButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        shareButton = view.findViewById(R.id.sendMail);


        shareButton.setOnClickListener(v -> {

            String emailAddress = "abircoxsbazar@gmail.com";
            String emailSubject = "Reports From Expense Tracker";
            //Uri path = Uri.parse("android.resource://com.remotearthsolutions.expensetracker/" + R.drawable.ic_facebook);

            try {

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.setType("image/*");
                String imagePath = Environment.getExternalStorageDirectory() + "/01682148802.jpg";
                File imageFileToShare = new File(imagePath);
                Uri uri = Uri.fromFile(imageFileToShare);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { emailAddress });
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
                this.startActivity(Intent.createChooser(emailIntent, "Choose Email Client To Send Report"));

            } catch (Throwable t) {
                Toast.makeText(getActivity(),"Report Sending Failed Please Try Again Later " + t.toString(), Toast.LENGTH_LONG).show();
            }

        });


        return view;
    }



}
