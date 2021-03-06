package com.example.enigmaapp.activity.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.SettlementViewModel;
import com.example.enigmaapp.model.UserViewModel;
import com.example.enigmaapp.repository.SettlementRepository;
import com.example.enigmaapp.ui.SettlementItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.enigmaapp.activity.MainActivity.actionBar;
import static com.example.enigmaapp.activity.MainActivity.prefs;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.getTodayDate;

public class SettlementFragment extends Fragment {
    private String token;
    private TextView batch;
    private TextView unitary;
    private FloatingActionButton addSettlementBtn;
    private ImageView filterBtn;
    private ImageView uploadBtn;
    private ImageView refreshBtn;

    private View topSection;
    private int page = 1;
    private boolean isBatch;
    public static ProgressBar progressBarSettlement;
    public static SettlementItemAdapter settlementAdapter;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private HashMap<String, String> pageParams = new HashMap<>();
    private ArrayList<SettlementRepository.SettlementSummary> data = new ArrayList<>();

    public static int mSettlementExpandedPosition = -1;
    public static int previousSettlementExpandedPosition = -1;

    public SettlementFragment(boolean isBatch) {
        // Required empty public constructor
        this.isBatch = isBatch;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settlement, container, false);

//        TODO: add back create action button after read only version
//        Batch:
//        addSettlementBtn = v.findViewById(R.id.settlement_create_btn);
//        addSettlementBtn.setOnClickListener(v1 -> openAddScreen(isBatch));

        // Move fo "Filter Settlement" screen:
        filterBtn = v.findViewById(R.id.ic_action_filter);
        filterBtn.setOnClickListener(v12 -> openFilterScreen());

        // Refresh "Settlement" screen:
        refreshBtn = v.findViewById(R.id.ic_action_refresh);
        refreshBtn.setOnClickListener(v13 -> {
            openSettlementScreen(isBatch);
        });

        // Upload "Settlement" screen:
        uploadBtn = v.findViewById(R.id.ic_action_upload);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add upload process
            }
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


        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        token = userViewModel.getCurrentUser().getToken();

        SettlementViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        pageParams.put("current_page", String.valueOf(page));
        if (page == 1 && isBatch) {
            viewModel.resetBatchList();
        }
        if (page == 1 && !isBatch) {
            viewModel.resetUnitaryList();
        }
        viewModel.setBatchParams(pageParams);

        batch = v.findViewById(R.id.settlement_batch);
        unitary = v.findViewById(R.id.settlement_unitary);

        if (isBatch) {
            setSelectedTextView(batch);
            setUnselectedTextView(unitary);
            viewModel.fetchBatch(token);
        } else {
            setSelectedTextView(unitary);
            setUnselectedTextView(batch);
            viewModel.fetchUnitary(token);
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
            data = viewModel.getBatchSettlements();
        } else {
            data = viewModel.getUnitarySettlements();
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
                    viewModel.setBatchParams(pageParams);
                    viewModel.fetchBatch(token);
                } else {
                    viewModel.setUnitaryParams(pageParams);
                    viewModel.fetchUnitary(token);
                }
            }
        });

        return v;
    }

    private void resetExpendedItemPos() {
        mSettlementExpandedPosition = -1;
        previousSettlementExpandedPosition = -1;
    }


    // TODO: add back create action button after read only version
    private void openAddScreen(boolean isBatch) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (isBatch) {
            NewBatchSettFragment fragment = new NewBatchSettFragment();
            transaction.replace(R.id.frame_layout, fragment, "New Batch Settlement");
        } else {
            NewUnitarySettFragment fragment = new NewUnitarySettFragment();
            transaction.replace(R.id.frame_layout, fragment, "New Unitary Settlement");
        }
        transaction.commit();
    }

    private void openFilterScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (isBatch) {
            BatchFilterFragment fragment = new BatchFilterFragment();
            transaction.replace(R.id.frame_layout, fragment, "Filter Settlement");
        } else {
            UnitaryFilterFragment fragment = new UnitaryFilterFragment();
            transaction.replace(R.id.frame_layout, fragment, "Filter Settlement");
        }
        transaction.commit();
    }

    private void setUnselectedTextView(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.textSecondaryColor));
        textView.setBackground(getResources().getDrawable(R.drawable.underline_unselected_tab));
        textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_regular));
    }

    private void setSelectedTextView(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.textColor));
        textView.setBackground(getResources().getDrawable(R.drawable.underline_selected_tab));
        textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semi_bold));
    }

    private void openSettlementScreen(Boolean isBatch) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SettlementFragment fragment = new SettlementFragment(isBatch);
        transaction.replace(R.id.frame_layout, fragment, "Settlement");
        transaction.commit();
    }
}