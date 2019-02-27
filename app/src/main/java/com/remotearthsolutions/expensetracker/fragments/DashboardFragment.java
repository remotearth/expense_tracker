package com.remotearthsolutions.expensetracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.services.FileProcessingServiceImp;
import com.remotearthsolutions.expensetracker.services.InventoryCallback;
import com.remotearthsolutions.expensetracker.services.PurchaseListener;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.viewmodels.Tab2ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import org.solovyev.android.checkout.*;


public class DashboardFragment extends Fragment implements InAppBillingCallback {

    private ActivityCheckout mCheckout;
    private Inventory mInventory;

    private Tab2ViewModel tab2ViewModel;

    private Button shareButton;
    private Button createFile;
    private Button buyButton;
    private TextView infoTextView;

    public DashboardFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCheckout = Checkout.forActivity(getActivity(), ApplicationObject.get().getBilling());
        mCheckout.start();
        mCheckout.createPurchaseFlow(new PurchaseListener(this));

        mInventory = mCheckout.makeInventory();
        mInventory.load(Inventory.Request.create()
                .loadAllPurchases()
                .loadSkus(ProductTypes.IN_APP, Constants.TEST_PURCHASED_ITEM), new InventoryCallback());

        tab2ViewModel = new Tab2ViewModel(DatabaseClient
                .getInstance(getContext())
                .getAppDatabase()
                .expenseDao(),
                new CompositeDisposable(),
                new FileProcessingServiceImp());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_share, container, false);

        shareButton = view.findViewById(R.id.sendMail);
        createFile = view.findViewById(R.id.createFile);
        buyButton = view.findViewById(R.id.button_buy);
        infoTextView = view.findViewById(R.id.info_textView);

        shareButton.setOnClickListener(v -> {
            tab2ViewModel.saveExpenseToCSV(getActivity());
            tab2ViewModel.shareCSV_FileToMail(getActivity());
        });

        createFile.setOnClickListener(v -> {
            // TODO
        });

        buyButton.setOnClickListener(view1 -> mCheckout.whenReady(new Checkout.EmptyListener() {
            @Override
            public void onReady(BillingRequests requests) {
                requests.purchase(ProductTypes.IN_APP, Constants.TEST_PURCHASED_ITEM, null, mCheckout.getPurchaseFlow());
            }
        }));

        return view;
    }


    @Override
    public void onPurchaseSuccessListener(Purchase purchase) {
        buyButton.setVisibility(View.GONE);
        infoTextView.setText("Thank You");
    }

    @Override
    public void onPurchaseFailedListener(String error) {
        Toast.makeText(getContext(), "Failed : " + error, Toast.LENGTH_SHORT).show();
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
