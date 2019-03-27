package com.remotearthsolutions.expensetracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.remotearthsolutions.expensetracker.activities.MainActivity;
import com.remotearthsolutions.expensetracker.adapters.DashboardAdapter;
import com.remotearthsolutions.expensetracker.contracts.DashboardContract;
import com.remotearthsolutions.expensetracker.databaseutils.AppDatabase;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.entities.DashboardModel;
import com.remotearthsolutions.expensetracker.services.FileProcessingServiceImp;
import com.remotearthsolutions.expensetracker.utils.CheckoutUtils;
import com.remotearthsolutions.expensetracker.utils.FabricAnswersUtils;
import com.remotearthsolutions.expensetracker.utils.PermissionUtils;
import com.remotearthsolutions.expensetracker.viewmodels.DashboardViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.DashBoardViewModelFactory;
import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.ProductTypes;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends BaseFragment implements DashboardContract.View {

    private DashboardViewModel dashboardViewModel;
    private ListView lv;
    private String productId;
    private CheckoutUtils checkoutUtils;
    private Context context;
    private Resources resources;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        resources = context.getResources();
        checkoutUtils = CheckoutUtils.getInstance((Activity) context);
    }

    public DashboardFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productId = ((ApplicationObject) ((Activity) context).getApplication()).getAdProductId();
        AppDatabase db = DatabaseClient.getInstance(context).getAppDatabase();
        dashboardViewModel = ViewModelProviders.of(this, new DashBoardViewModelFactory(this, db.categoryExpenseDao(), db.expenseDao(),
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkoutUtils = CheckoutUtils.getInstance((Activity) context);
    }

    private void loaddashboarddata() {

        ArrayList<DashboardModel> dashboardlist = new ArrayList<>();
        dashboardlist.add(new DashboardModel(R.drawable.ic_share, resources.getString(R.string.export_data)));
        dashboardlist.add(new DashboardModel(R.drawable.ic_import, resources.getString(R.string.import_data)));
        dashboardlist.add(new DashboardModel(R.drawable.ic_cart, resources.getString(R.string.buy_product)));
        DashboardAdapter adapter = new DashboardAdapter(context, dashboardlist);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {

            switch (position) {
                case 0:
                    dashboardViewModel.saveExpenseToCSV((Activity) context);
                    break;

                case 1:
                    showAlert(resources.getString(R.string.attention),
                            resources.getString(R.string.will_replace_your_current_entries),
                            resources.getString(R.string.yes),
                            resources.getString(R.string.cancel), new Callback() {
                                @Override
                                public void onOkBtnPressed() {

                                    new PermissionUtils().writeExternalStoragePermission((Activity) context, new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse response) {

                                            List<String> allCsvFile = dashboardViewModel.getAllCsvFile();

                                            if (allCsvFile == null || allCsvFile.size() == 0) {
                                                Toast.makeText(context, resources.getString(R.string.no_supported_file), Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            final CharSequence[] csvList = allCsvFile.toArray(new String[allCsvFile.size()]);
                                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                                            dialogBuilder.setTitle(resources.getString(R.string.select_csv));
                                            dialogBuilder.setItems(csvList, (dialog, item) -> {
                                                String selectedText = csvList[item].toString();
                                                String filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), selectedText).getAbsolutePath();
                                                dashboardViewModel.importDataFromFile(filePath);
                                                FabricAnswersUtils.logCustom("Data Imported");
                                                showProgress(resources.getString(R.string.please_wait));

                                                new Handler().postDelayed(() -> {
                                                    ((MainActivity) context).updateSummary();
                                                    ((MainActivity) context).refreshChart();
                                                    hideProgress();
                                                }, 3000);
                                            });

                                            AlertDialog alertDialogObject = dialogBuilder.create();
                                            alertDialogObject.show();

                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse response) {
                                            showAlert("", resources.getString(R.string.read_write_permission_is_needed),
                                                    resources.getString(R.string.ok), null, null);
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                            Toast.makeText(context, resources.getString(R.string.read_write_permission_is_needed_enable_from_settings),
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
                    if (!isDeviceOnline()) {
                        showAlert("", resources.getString(R.string.internet_connection_needed), resources.getString(R.string.ok), null, null);
                        return;
                    }

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
        return context;
    }
}
