package com.remotearthsolutions.expensetracker.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;
import com.remotearthsolutions.expensetracker.activities.MainActivity;
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback;
import com.remotearthsolutions.expensetracker.services.InventoryCallback;
import com.remotearthsolutions.expensetracker.services.PurchaseListener;
import com.remotearthsolutions.expensetracker.utils.Constants;
import org.solovyev.android.checkout.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.provider.CalendarContract.CalendarCache.URI;


public class Tab2Fragment extends Fragment implements InAppBillingCallback {

    private final ActivityCheckout mCheckout;
    private Inventory mInventory;

    private Button buyButton;
    private TextView infoTextView;

    public Tab2Fragment() {
        mCheckout = Checkout.forActivity(getActivity(), ApplicationObject.get().getBilling());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        Button shareButton = view.findViewById(R.id.sendMail);
        Button createFile = view.findViewById(R.id.createFile);
        buyButton = view.findViewById(R.id.button_buy);
        infoTextView = view.findViewById(R.id.info_textView);

        mCheckout.start();

        mCheckout.createPurchaseFlow(new PurchaseListener(this));

        mInventory = mCheckout.makeInventory();
        mInventory.load(Inventory.Request.create()
                .loadAllPurchases()
                .loadSkus(ProductTypes.IN_APP, Constants.TEST_PURCHASED_ITEM), new InventoryCallback());


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

        createFile.setOnClickListener(v -> requestStoragePermission());

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_LONG).show();
                mCheckout.whenReady(new Checkout.EmptyListener() {
                    @Override
                    public void onReady(BillingRequests requests) {
                        requests.purchase(ProductTypes.IN_APP, Constants.TEST_PURCHASED_ITEM, null, mCheckout.getPurchaseFlow());
                    }
                });
            }
        });


        return view;
    }

    private void requestStoragePermission() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        String fileName = "RemotearthFile";
                        String fileContent = "";
                        createFile(fileName,fileContent);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void createFile(String filename, String content)
    {
      String fileName = filename+ ".txt";
      File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),fileName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
            Toast.makeText(getActivity(), "File Created Successfully", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_LONG).show();
        }
        catch (IOException io)
        {
            io.printStackTrace();
            Toast.makeText(getActivity(), "Error File Creating", Toast.LENGTH_LONG).show();
        }

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Attention!");
        builder.setMessage("To use this Features Your need Permission For that, You can access it in app settings.");
        builder.setPositiveButton("Go To Setting", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    @Override
    public void onPurchaseSuccessListener(Purchase purchase) {
        buyButton.setVisibility(View.GONE);
        infoTextView.setText("Thank You");
    }

    @Override
    public void onPurchaseFailedListener(String error) {
        Toast.makeText(getContext(), "Failed : "+ error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        mCheckout.stop();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCheckout.onActivityResult(requestCode, resultCode, data);
    }
}
