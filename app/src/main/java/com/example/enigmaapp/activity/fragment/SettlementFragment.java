package com.example.enigmaapp.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.activity.FormActivity;
import com.example.enigmaapp.model.SettlementViewModel;
import com.example.enigmaapp.model.LoginViewModel;
import com.example.enigmaapp.repository.SettlementRepository;
import com.example.enigmaapp.ui.SettlementItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.enigmaapp.activity.MainActivity.prefs;
import static com.example.enigmaapp.activity.UserActivity.actionBar;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.getTodayDate;

public class SettlementFragment extends Fragment {
    public static final int BATCH_FILTER_REQUEST_CODE = 2;
    public static final int UNITARY_FILTER_REQUEST_CODE = 3;
    private static final String TAG = "SettlementFragment";
    private String token;
    private TextView batch, unitary;
    private FloatingActionButton addSettlementFab;
    private ImageView filterBtn, uploadBtn, refreshBtn;
    private View topSection;
    private int page = 1;
    private boolean isBatch;
    public static ProgressBar progressBarSettlement;
    public static SettlementItemAdapter settlementAdapter;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private HashMap<String, String> pageParams = new HashMap<>();
    private ArrayList<SettlementRepository.SettlementSummary> data = new ArrayList<>();

    private static SettlementViewModel settlementViewModel;
    public static int mSettlementExpandedPosition = -1;
    public static int previousSettlementExpandedPosition = -1;

    // batch:
    public static String selectedBatchProductId;
    public static String selectedBatchCounterpartyId;
    public static ArrayList<String> selectedBatchStatuses = new ArrayList<>();

    // unitary:
    public static String selectedUnitaryStartDate;
    public static String selectedUnitaryEndDate;
    public static String selectedUnitarySide;
    public static ArrayList<String> selectedUnitaryStatuses = new ArrayList<>();
    public static ArrayList<String> clickedCurrencies = new ArrayList<>();
    public static ArrayList<String> clickedCounterparties = new ArrayList<>();
    public static StringBuilder currencyStringBuilder;
    public static StringBuilder counterpartyStringBuilder;

    public SettlementFragment() {
        this.isBatch = true;
    }

    public SettlementFragment(boolean isBatch) {
        System.out.println("_________________________ SettlementFragment : isBatch: " + isBatch );
        this.isBatch = isBatch;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("fragmentTag", getTag());
        outState.putBoolean("isBatch", isBatch);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.isBatch = savedInstanceState.getBoolean("isBatch");
        }
        Log.d(TAG, "onCreate: got tag ____________________ " + getTag());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settlement, container, false);

//        TODO: add back create action button after read only version
//        Batch:
//        addSettlementFab = v.findViewById(R.id.settlement_create_fab);
//        addSettlementFab.setOnClickListener(v1 -> openAddScreen(isBatch));

        // Move fo "Filter Settlement" screen:
        filterBtn = v.findViewById(R.id.ic_action_filter);
        filterBtn.setOnClickListener(v12 -> startFormActivity());

        // Refresh "Settlement" screen:
        refreshBtn = v.findViewById(R.id.ic_action_refresh);
        refreshBtn.setOnClickListener(v13 -> openSettlementScreen(isBatch));

        // Upload "Settlement" screen:
        uploadBtn = v.findViewById(R.id.ic_action_upload);
        uploadBtn.setOnClickListener(v16 -> {
            // TODO: add upload process
        });

        topSection = v.findViewById(R.id.layout_settlement_top_section);
        TextView fromDate = topSection.findViewById(R.id.trade_from_date);
        TextView toDate = topSection.findViewById(R.id.trade_to_date);
        if (isBatch) {
            fromDate.setText("-");
            toDate.setText(getTodayDate());
        } else {
            fromDate.setText(prefs.getString("startDateUnitaryFilter", "-"));
            toDate.setText(prefs.getString("endDateUnitaryFilter", getTodayDate()));
        }

        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(LoginViewModel.class);
        token = loginViewModel.getCurrentUser().getToken();

        settlementViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        pageParams.put("current_page", String.valueOf(page));
        if (page == 1 && isBatch) {
            settlementViewModel.resetBatchList();
        }
        if (page == 1 && !isBatch) {
            settlementViewModel.resetUnitaryList();
        }
        settlementViewModel.setBatchParams(pageParams);

        batch = v.findViewById(R.id.settlement_batch);
        unitary = v.findViewById(R.id.settlement_unitary);

        if (isBatch) {
            setSelectedTextView(batch);
            setUnselectedTextView(unitary);
            settlementViewModel.fetchBatch(token);
        } else {
            setSelectedTextView(unitary);
            setUnselectedTextView(batch);
            settlementViewModel.fetchUnitary(token);
        }

        batch.setOnClickListener(v14 -> {
            resetExpendedItemPos();
            openSettlementScreen(true);
        });
        unitary.setOnClickListener(v1 -> {
            resetExpendedItemPos();
            openSettlementScreen(false);
        });

        nestedScrollView = v.findViewById(R.id.scroll_settlement);
        progressBarSettlement = v.findViewById(R.id.progress_bar_settlement);
        recyclerView = v.findViewById(R.id.settlement_fragment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (isBatch) {
            data = settlementViewModel.getBatchSettlements();
        } else {
            data = settlementViewModel.getUnitarySettlements();
        }
        settlementAdapter = new SettlementItemAdapter(requireContext(), data);
        recyclerView.setAdapter(settlementAdapter);

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v15, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // check if scrolled till bottom
            if (scrollY == v15.getChildAt(0).getMeasuredHeight() - v15.getMeasuredHeight()) {
                page++;
                pageParams.put("current_page", String.valueOf(page));
                progressBarSettlement.setVisibility(View.VISIBLE);
                if (isBatch) {
                    settlementViewModel.setBatchParams(pageParams);
                    settlementViewModel.fetchBatch(token);
                } else {
                    settlementViewModel.setUnitaryParams(pageParams);
                    settlementViewModel.fetchUnitary(token);
                }
            }
        });

        return v;
    }

    public static void setUnitaryParams() {
        HashMap<String, String> map = new HashMap<>();

        // build params map
        if (selectedUnitaryStartDate != null) {
            map.put("start_date", selectedUnitaryStartDate);
            map.put("end_date", selectedUnitaryEndDate);
        } else {
            settlementViewModel.removeFromUnitaryParams("start_date");
            settlementViewModel.removeFromUnitaryParams("end_date");
        }

        if (selectedUnitarySide != null) {
            map.put("side", selectedUnitarySide);
        } else {
            settlementViewModel.removeFromUnitaryParams("side");
        }

        //  clear any old selected currencies, then add new selection:
        settlementViewModel.removeFromUnitaryParamsContainsKey("currency_list");
        for (int i = 0; i < clickedCurrencies.size(); i++) {
            map.put("currency_list[" + i + "]", clickedCurrencies.get(i));
        }

        //  clear any old selected counterparties, then add new selection:
        settlementViewModel.removeFromUnitaryParamsContainsKey("counterparty_id_list");
        for (int i = 0; i < clickedCounterparties.size(); i++) {
            map.put("counterparty_id_list[" + i + "]", clickedCounterparties.get(i));
        }

        // clear any old selected statuses, then add new selection:
        settlementViewModel.removeFromUnitaryParamsContainsKey("status");
        if (selectedUnitaryStatuses.size() > 0) {
            for (int i = 0; i < selectedUnitaryStatuses.size(); i++) {
                map.put("status[" + i + "]", selectedUnitaryStatuses.get(i));
            }
        }

        System.out.println("___________ setUnitaryParams - map ___________ : " + map);
        settlementViewModel.setUnitaryParams(map);
    }

    public static void setBatchParams() {
        HashMap<String, String> map = new HashMap<>();

        // build params map
        if (selectedBatchProductId != null) {
            map.put("product_id", selectedBatchProductId);
        } else {
            settlementViewModel.removeFromBatchParams("product_id");
        }

        if (selectedBatchCounterpartyId != null) {
            map.put("counterparty_id", selectedBatchCounterpartyId);
        } else {
            settlementViewModel.removeFromBatchParams("counterparty_id");
        }

        // clear any old selected statuses:
        for (int i = 0; i < 5; i++) {
            settlementViewModel.removeFromBatchParams("status[" + i + "]");
        }
        // set new selected statuses:
        if (selectedBatchStatuses.size() > 0) {
            for (int i = 0; i < selectedBatchStatuses.size(); i++) {
                map.put("status[" + i + "]", selectedBatchStatuses.get(i));
            }
        }

        settlementViewModel.setBatchParams(map);
    }

    private void resetExpendedItemPos() {
        mSettlementExpandedPosition = -1;
        previousSettlementExpandedPosition = -1;
    }


    // TODO: add back create action button after read only version
    private void openAddScreen(boolean isBatch) {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (isBatch) {
            NewBatchSettFragment fragment = new NewBatchSettFragment();
            ft.replace(R.id.frame_layout, fragment, "New Batch Settlement");
        } else {
            NewUnitarySettFragment fragment = new NewUnitarySettFragment();
            ft.replace(R.id.frame_layout, fragment, "New Unitary Settlement");
        }
        ft.commit();
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    String tag = (isBatch) ? "Batch" : "Unitary";
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Fragment frg = null;
                        frg = getParentFragmentManager().findFragmentByTag(tag);
                        FragmentTransaction ft = getParentFragmentManager().beginTransaction();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            ft.detach(frg).commitNow();
                            ft.attach(frg).commitNow();
                        } else {
                            ft.detach(frg).attach(frg).commit();
                        }

                    }
                }
            });

    private void startFormActivity() {
        // start form activity
//        Intent intent = new Intent(getContext(), FormActivity.class);
        if (isBatch) {
//            intent.putExtra("formTypeExtra", "filterBatch");
//            getActivity().startActivityForResult(intent, BATCH_FILTER_REQUEST_CODE);
            mStartForResult.launch(new Intent(getContext(), FormActivity.class).putExtra("formTypeExtra", "filterBatch"));
        } else {
//            intent.putExtra("formTypeExtra", "filterUnitary");
//            getActivity().startActivityForResult(intent, UNITARY_FILTER_REQUEST_CODE);
            mStartForResult.launch(new Intent(getContext(), FormActivity.class).putExtra("formTypeExtra", "filterUnitary"));
        }
    }

    private void setUnselectedTextView(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.textSecondaryColor));
        textView.setBackground(getResources().getDrawable(R.drawable.underline_unselected_tab));
    }

    private void setSelectedTextView(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.textColor));
        textView.setBackground(getResources().getDrawable(R.drawable.underline_selected_tab));
    }

    private void openSettlementScreen(Boolean isBatch) {
        String tag = (isBatch) ? "Batch" : "Unitary";
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        SettlementFragment fragment = new SettlementFragment(isBatch);
        ft.replace(R.id.frame_layout, fragment, tag);
        ft.commit();
    }
}