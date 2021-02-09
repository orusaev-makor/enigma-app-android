package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.SettingsViewModel;
import com.example.enigmaapp.ui.ExposureLimitItemAdapter;
import com.example.enigmaapp.ui.MaxQtyPerTradeItemAdapter;
import com.example.enigmaapp.ui.SettingsConfirmationDialog;
import com.example.enigmaapp.web.settings.ExposureLimitItemResult;
import com.example.enigmaapp.web.settings.MaxQtyPerTradeItemResult;

import java.util.List;

import static com.example.enigmaapp.activity.UserActivity.actionBar;
import static com.example.enigmaapp.repository.LoginRepository.mCurrentUser;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        LinearLayout increaseExposureLimit = v.findViewById(R.id.request_increase_exposure_limit);
        increaseExposureLimit.setOnClickListener(v12 -> {
            // TODO: set "request increase exposure limit" process
            openDialog();
        });

        LinearLayout increaseQuantityLimit = v.findViewById(R.id.request_increase_quantity_limit);
        increaseQuantityLimit.setOnClickListener(v1 -> {
            // TODO: set "request increase quantity limit" process
            openDialog();
        });

        RecyclerView exposureLimitRecyclerView = v.findViewById(R.id.exposure_limit_recycled_view);
        exposureLimitRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ExposureLimitItemAdapter exposureLimitItemAdapter = new ExposureLimitItemAdapter(requireContext());
        exposureLimitRecyclerView.setAdapter(exposureLimitItemAdapter);

        SettingsViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettingsViewModel.class);

        viewModel.getAllLimitExposures().observe(requireActivity(), exposureLimitItemResults ->
                exposureLimitItemAdapter.submitList(exposureLimitItemResults));

        RecyclerView maxQtyPerTradeRecyclerView = v.findViewById(R.id.max_qty_per_trade_recycled_view);
        maxQtyPerTradeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final MaxQtyPerTradeItemAdapter maxQtyPerTradeItemAdapter = new MaxQtyPerTradeItemAdapter(requireContext());
        maxQtyPerTradeRecyclerView.setAdapter(maxQtyPerTradeItemAdapter);

        viewModel.getAllMaxPerTrades().observe(requireActivity(), new Observer<List<MaxQtyPerTradeItemResult>>() {
            @Override
            public void onChanged(List<MaxQtyPerTradeItemResult> maxQtyPerTradeItemResults) {
                maxQtyPerTradeItemAdapter.submitList(maxQtyPerTradeItemResults);
            }
        });

        String token = mCurrentUser.getToken();

        viewModel.fetchLimitExposures(token);
        viewModel.fetchMaxPerTrades(token);

        return v;
    }

    private void openDialog() {
        SettingsConfirmationDialog settingsDialog = new SettingsConfirmationDialog();
        settingsDialog.show(getParentFragmentManager(), "Settings Dialog");
    }
}