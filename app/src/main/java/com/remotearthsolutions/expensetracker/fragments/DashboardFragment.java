package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;
import com.remotearthsolutions.expensetracker.adapters.DashboardAdapter;
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback;
import com.remotearthsolutions.expensetracker.databaseutils.AppDatabase;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.entities.DashboardModel;
import com.remotearthsolutions.expensetracker.services.FileProcessingServiceImp;
import com.remotearthsolutions.expensetracker.services.PurchaseListener;
import com.remotearthsolutions.expensetracker.utils.CheckoutUtils;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.PermissionUtils;
import com.remotearthsolutions.expensetracker.viewmodels.DashboardViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.DashBoardViewModelFactory;
import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends BaseFragment implements InAppBillingCallback {

    private CheckoutUtils checkoutUtils;
    private DashboardViewModel dashboardViewModel;
    private ListView lv;
    private String productId;

    public DashboardFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productId = ((ApplicationObject) getActivity().getApplication()).getAdProductId();

        checkoutUtils = CheckoutUtils.getInstance(getActivity());
        checkoutUtils.start();
        checkoutUtils.createPurchaseFlow(new PurchaseListener(this));

        AppDatabase db = DatabaseClient.getInstance(getContext()).getAppDatabase();
        dashboardViewModel = ViewModelProviders.of(this, new DashBoardViewModelFactory(db.categoryExpenseDao(), db.expenseDao(),
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
        ((ApplicationObject) getActivity().getApplication()).setPremium(true);
        Toast.makeText(getActivity(), "Thank you for purchasing this item", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseFailedListener(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        checkoutUtils.stop();
        super.onDestroy();
    }

    private void loaddashboarddata() {
        ArrayList<DashboardModel> dashboardlist = new ArrayList<>();
        dashboardlist.add(new DashboardModel(R.drawable.ic_share, Constants.SHARE_T0_EMAIL));
        dashboardlist.add(new DashboardModel(R.drawable.ic_import, Constants.IMPORT_FILE));
        dashboardlist.add(new DashboardModel(R.drawable.ic_cart, Constants.BUY_THE_PRODUCT));
        DashboardAdapter adapter = new DashboardAdapter(getActivity(), dashboardlist);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {

            switch (position) {
                case 0:
                    dashboardViewModel.saveExpenseToCSV(getActivity());
                    break;

                case 1:
                    showAlert("Attention",
                            "This will replace your current entries. Are you sure you want to import data?",
                            "Yes",
                            "Cancel", new Callback() {
                                @Override
                                public void onOkBtnPressed() {

                                    new PermissionUtils().writeExternalStoragePermission(getActivity(), new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse response) {

                                            List<String> allCsvFile = dashboardViewModel.getAllCsvFile();

                                            if (allCsvFile == null || allCsvFile.size() == 0) {
                                                Toast.makeText(getActivity(), "No expense tracker supported file is found", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            final CharSequence[] csvList = allCsvFile.toArray(new String[allCsvFile.size()]);
                                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                                            dialogBuilder.setTitle("Select a .csv file");
                                            dialogBuilder.setItems(csvList, (dialog, item) -> {
                                                String selectedText = csvList[item].toString();
                                                String filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), selectedText).getAbsolutePath();
                                                dashboardViewModel.importDataFromFile(filePath);
                                            });

                                            AlertDialog alertDialogObject = dialogBuilder.create();
                                            alertDialogObject.show();

                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse response) {
                                            showAlert("", "Read/write permission on external storage is needed to export/import data.",
                                                    "Ok", null, null);
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                            Toast.makeText(getActivity(), "Read/write permission on external storage is needed to export/import data. Please enable it from device settings.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelBtnPressed() {

                                }
                            });

                    break;

                case 2:
                    checkoutUtils.getCheckout().whenReady(new Checkout.EmptyListener() {
                        @Override
                        public void onReady(@Nonnull BillingRequests requests) {
                            requests.purchase(ProductTypes.IN_APP, productId, null, checkoutUtils.getPurchaseFlow());
                        }
                    });
                    break;
            }
        });
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
