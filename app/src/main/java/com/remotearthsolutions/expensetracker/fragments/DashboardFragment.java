package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;
import com.remotearthsolutions.expensetracker.adapters.DashboardAdapter;
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.entities.DashboardModel;
import com.remotearthsolutions.expensetracker.services.FileProcessingServiceImp;
import com.remotearthsolutions.expensetracker.services.InventoryCallback;
import com.remotearthsolutions.expensetracker.services.PurchaseListener;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.viewmodels.DashboardViewModel;
import io.reactivex.disposables.CompositeDisposable;
import org.solovyev.android.checkout.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;


public class DashboardFragment extends BaseFragment implements InAppBillingCallback {

    private ActivityCheckout mCheckout;
    private Inventory mInventory;
    private DashboardViewModel dashboardViewModel;
    private ListView lv;
    private DashboardAdapter adapter;
    private ArrayList<DashboardModel> dashboardlist;

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

        dashboardViewModel = new DashboardViewModel(DatabaseClient
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
        lv = view.findViewById(R.id.dashboardlist);
        loaddashboarddata();
        return view;
    }


    @Override
    public void onPurchaseSuccessListener(Purchase purchase) {
        Toast.makeText(getActivity(), "Thank you for purchasing this item", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseFailedListener(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
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

    private void loaddashboarddata() {
        dashboardlist = new ArrayList<>();
        dashboardlist.add(new DashboardModel(R.drawable.ic_share, Constants.SHARE_T0_EMAIL));
        dashboardlist.add(new DashboardModel(R.drawable.ic_import, Constants.IMPORT_FILE));
        dashboardlist.add(new DashboardModel(R.drawable.ic_cart, Constants.BUY_THE_PRODUCT));
        adapter = new DashboardAdapter(getActivity(), dashboardlist);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {

            switch (position)
            {
                case 0:
                    dashboardViewModel.saveExpenseToCSV(getActivity());
                    dashboardViewModel.shareCSV_FileToMail(getActivity());
                    break;

                case 1:
                    mCheckout.whenReady(new Checkout.EmptyListener() {
                        @Override
                        public void onReady(@Nonnull BillingRequests requests) {

                            requests.purchase(ProductTypes.IN_APP, Constants.TEST_PURCHASED_ITEM, null, mCheckout.getPurchaseFlow());
                        }
                    });
                    break;

                case 2:
                    showAlert(getString(R.string.warning),getString(R.string.buy_message),getString(R.string.ok),null,null);
                    break;

                default:
            }
        });
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
