package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;
import com.remotearthsolutions.expensetracker.adapters.DashboardAdapter;
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback;
import com.remotearthsolutions.expensetracker.databaseutils.AppDatabase;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.entities.DashboardModel;
import com.remotearthsolutions.expensetracker.services.FileProcessingServiceImp;
import com.remotearthsolutions.expensetracker.services.InventoryCallback;
import com.remotearthsolutions.expensetracker.services.PurchaseListener;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.viewmodels.DashboardViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.DashBoardViewModelFactory;
import org.solovyev.android.checkout.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;


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
        AppDatabase db = DatabaseClient.getInstance(getContext()).getAppDatabase();
        dashboardViewModel = ViewModelProviders.of(this, new DashBoardViewModelFactory(db.expenseDao(),
                db.categoryDao(), db.accountDao(), new FileProcessingServiceImp())).get(DashboardViewModel.class);
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

            switch (position) {

                case 0:
                    dashboardViewModel.saveExpenseToCSV(getActivity());
                    dashboardViewModel.shareCSV_FileToMail(getActivity());
                    break;

                case 1:

                    List<String> allCsvFile = dashboardViewModel.getAllCsvFile();

                    final CharSequence[] csvList = allCsvFile.toArray(new String[allCsvFile.size()]);
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    dialogBuilder.setTitle("Select a .csv file");
                    dialogBuilder.setItems(csvList, (dialog, item) -> {
                        String selectedText = csvList[item].toString();  //Selected item in listview
                    });

                    AlertDialog alertDialogObject = dialogBuilder.create();
                    alertDialogObject.show();

                    break;

                case 2:
                    mCheckout.whenReady(new Checkout.EmptyListener() {
                        @Override
                        public void onReady(@Nonnull BillingRequests requests) {
                            requests.purchase(ProductTypes.IN_APP, Constants.TEST_PURCHASED_ITEM, null, mCheckout.getPurchaseFlow());
                        }
                    });
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
