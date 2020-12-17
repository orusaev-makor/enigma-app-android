package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.SettlementViewModel;
import com.example.enigmaapp.model.UserViewModel;
import com.example.enigmaapp.repository.SettlementRepository;
import com.example.enigmaapp.ui.SettlementItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SettlementFragment extends Fragment {
    private String token;
    private TextView batch;
    private TextView unitary;
    private boolean isBatch;
    private FloatingActionButton addSettlementBtn;
    private ImageView filterBtn;
    private ImageView uploadBtn;
    private ImageView refreshBtn;

    public SettlementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Show navbar on "Settlement" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settlement, container, false);

        Batch:
        addSettlementBtn = v.findViewById(R.id.settlement_create_btn);
        addSettlementBtn.setOnClickListener(v1 -> openAddScreen(isBatch));

        // Move fo "Filter Settlement" screen:
        filterBtn = v.findViewById(R.id.ic_action_filter);
        filterBtn.setOnClickListener(v12 -> openFilterScreen(isBatch));

        // Refresh "Settlement" screen:
        refreshBtn = v.findViewById(R.id.ic_action_refresh);
        refreshBtn.setOnClickListener(v13 -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            SettlementFragment fragment = new SettlementFragment();
            transaction.replace(R.id.frame_layout, fragment, "Settlement");
            transaction.commit();
        });

        // Upload "Settlement" screen:
        uploadBtn = v.findViewById(R.id.ic_action_upload);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add upload process
            }
        });

        RecyclerView recyclerView = v.findViewById(R.id.settlement_fragment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final SettlementItemAdapter adapter = new SettlementItemAdapter(requireContext());
        recyclerView.setAdapter(adapter);

        SettlementViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        viewModel.getSettlements().observe(requireActivity(), new Observer<List<SettlementRepository.SettlementSummary>>() {
            @Override
            public void onChanged(List<SettlementRepository.SettlementSummary> settlementItems) {
                adapter.submitList(settlementItems);
            }
        });

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchBatchSettlements(token);
        isBatch = true;

        batch = v.findViewById(R.id.settlement_batch);
        unitary = v.findViewById(R.id.settlement_unitary);

        batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.fetchBatchSettlements(token);
                setSelectedTextView(batch);
                setUnselectedTextView(unitary);
                isBatch = true;
            }
        });

        unitary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.fetchUnitarySettlements(token);
                setSelectedTextView(unitary);
                setUnselectedTextView(batch);
                isBatch = false;
            }
        });

        return v;
    }

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

    private void openFilterScreen(boolean isBatch) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (isBatch) {
            SettBatchFilterFragment fragment = new SettBatchFilterFragment();
            transaction.replace(R.id.frame_layout, fragment, "Filter Settlement");
        } else {
            SettUnitaryFilterFragment fragment = new SettUnitaryFilterFragment();
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
}