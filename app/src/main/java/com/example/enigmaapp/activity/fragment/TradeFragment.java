package com.example.enigmaapp.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import com.example.enigmaapp.activity.FormActivity;
import com.example.enigmaapp.model.TradeViewModel;
import com.example.enigmaapp.ui.TradeItemAdapter;
import com.example.enigmaapp.web.trade.TradeItemResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.enigmaapp.activity.MainActivity.prefs;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.getTodayDate;
import static com.example.enigmaapp.repository.LoginRepository.mCurrentUser;

public class TradeFragment extends Fragment {
    public static final int TRADE_FILTER_REQUEST_CODE = 1;
    private FloatingActionButton addTradeFab;
    private ImageView filterBtn, uploadBtn, refreshBtn;
    private TextView fromDate, toDate;
    private View topSection;
    public static int tradesCurrentPage = 1;
    public static ProgressBar progressBarTrade;
    public static TradeItemAdapter tradeAdapter;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private HashMap<String, String> pageParams = new HashMap<>();

    private static TradeViewModel tradeViewModel;

    public static int mTradeExpandedPosition = -1;
    public static int previousTradeExpandedPosition = -1;

    public static String enteredTradeId;
    public static String selectedProductId;
    public static String selectedExecutionType;
    public static String selectedBatched;
    public static String selectedStartDate;
    public static String selectedEndDate;
    public static ArrayList<String> selectedStatuses = new ArrayList<>();

    public TradeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resetExpendedItemPos();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_trade, container, false);

        // TODO: add back create action button after read only version
        // Move fo "New Trade" screen:
//        addTradeFab = v.findViewById(R.id.trade_create_fab);
//        addTradeFab.setOnClickListener(v12 -> openNewTradeFragment());

        // Move fo "Filter Trade" screen:
        filterBtn = v.findViewById(R.id.ic_action_filter);
        filterBtn.setOnClickListener(v1 -> startFormActivity());

        // Refresh "Trade" screen:
        refreshBtn = v.findViewById(R.id.ic_action_refresh);
        refreshBtn.setOnClickListener(v13 -> openTradeFragment());

        // Upload "Trade" screen:
        uploadBtn = v.findViewById(R.id.ic_action_upload);
        uploadBtn.setOnClickListener(v14 -> {
            // TODO: add upload process
        });

        topSection = v.findViewById(R.id.layout_trade_top_section);
        fromDate = topSection.findViewById(R.id.trade_from_date);
        fromDate.setText(prefs.getString("startDateTradeFilter", "-"));
        toDate = topSection.findViewById(R.id.trade_to_date);
        toDate.setText(prefs.getString("endDateTradeFilter", getTodayDate()));

        // getting token only after fetch request was made
//        LoginViewModel loginViewModel =new ViewModelProvider(this).get(LoginViewModel.class);
//        loginViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
//            @Override
//            public void onChanged(List<User> users) {
//                tokenTest = users.get(0).getToken();
//                System.out.println("TOKENNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN _ TEST ___________ " + tokenTest);
//            }
//        });
        String token = mCurrentUser.getToken();

        tradeViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeViewModel.class);

        pageParams.put("current_page", String.valueOf(tradesCurrentPage));
        if (tradesCurrentPage == 1) {
            tradeViewModel.resetTradesList();
        }

//        setTradeParams();
        tradeViewModel.setParams(pageParams);
        tradeViewModel.fetchTrades(token);

        nestedScrollView = v.findViewById(R.id.scroll_trade);
        progressBarTrade = v.findViewById(R.id.progress_bar_trade);
        recyclerView = v.findViewById(R.id.trade_fragment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<TradeItemResult> data = tradeViewModel.getTrades();
        tradeAdapter = new TradeItemAdapter(requireContext(), data);
        recyclerView.setAdapter(tradeAdapter);

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v12, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    // check if scrolled till bottom
                    if (scrollY == v12.getChildAt(0).getMeasuredHeight() - v12.getMeasuredHeight()) {
                        // when reach last item position
                        tradesCurrentPage++;
                        pageParams.put("current_page", String.valueOf(tradesCurrentPage));
                        tradeViewModel.setParams(pageParams);
                        progressBarTrade.setVisibility(View.VISIBLE);
                        tradeViewModel.fetchTrades(token);
                    }
                });
        return v;
    }

    public static void setTradeParams() {
        HashMap<String, String> map = new HashMap<>();

        // build params map
        if (enteredTradeId != null) {
            map.put("trade_id", enteredTradeId);
        } else {
            tradeViewModel.removeFromParams("trade_id");
        }

        if (selectedProductId != null) {
            map.put("product_id", selectedProductId);
        } else {
            tradeViewModel.removeFromParams("product_id");
        }

        if (selectedExecutionType != null) {
            map.put("execution_type", selectedExecutionType);
        } else {
            tradeViewModel.removeFromParams("execution_type");
        }

        if (selectedBatched != null) {
            map.put("already_batched", selectedBatched);
        } else {
            tradeViewModel.removeFromParams("already_batched");
        }

        if (selectedStartDate != null) {
            map.put("start_date", selectedStartDate);
            map.put("end_date", selectedEndDate);
        } else {
            tradeViewModel.removeFromParams("start_date");
            tradeViewModel.removeFromParams("end_date");
        }

        // clear any old selected statuses:
        for (int i = 0; i < 5; i++) {
            tradeViewModel.removeFromParams("status[" + i + "]");
        }
        // set new selected statuses:
        if (selectedStatuses.size() > 0) {
            for (int i = 0; i < selectedStatuses.size(); i++) {
                map.put("status[" + i + "]", selectedStatuses.get(i));
            }
        }

//        map.put("current_page", String.valueOf(page));

        tradeViewModel.setParams(map);
    }

    private void resetExpendedItemPos() {
        mTradeExpandedPosition = -1;
        previousTradeExpandedPosition = -1;
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Fragment frg = null;
                    frg = getParentFragmentManager().findFragmentByTag("Trade");
                    FragmentTransaction ft = getParentFragmentManager().beginTransaction();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        ft.detach(frg).commitNow();
                        ft.attach(frg).commitNow();
                    } else {
                        ft.detach(frg).attach(frg).commit();
                    }

                }
            });

    private void startFormActivity() {
        // The launcher with the Intent you want to start
        mStartForResult.launch(new Intent(getContext(), FormActivity.class).putExtra("formTypeExtra", "filterTrade"));

//        Intent intent = new Intent(getContext(), FormActivity.class);
//        intent.putExtra("formTypeExtra", "filterTrade");
//        getActivity().startActivityForResult(intent, TRADE_FILTER_REQUEST_CODE);
    }

    private void openTradeFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        TradeFragment fragment = new TradeFragment();
        ft.replace(R.id.frame_layout, fragment, "Trade");
        ft.commit();
    }

    // TODO: add back after read only version
    private void openNewTradeFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        NewTradeCreationFragment fragment = new NewTradeCreationFragment();
        ft.replace(R.id.frame_layout, fragment, "New Trade");
        ft.commit();
    }

    public void refresh() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }
}