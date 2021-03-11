package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.LoginViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

public class ChangePasswordFragment extends Fragment {
    private MaterialButton saveBtn;
    private EditText currentPass, newPass, newPassRetype;
    private HashMap<String, String> changePassParams = new HashMap<>();
    private TextView changePassErrorMsg;

    public ChangePasswordFragment() {
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
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);

        currentPass = v.findViewById(R.id.change_pass_current_pass_edit_text);
        newPass = v.findViewById(R.id.change_pass_new_pass_edit_text);
        newPassRetype = v.findViewById(R.id.change_pass_new_pass_retype_edit_text);
        changePassErrorMsg = v.findViewById(R.id.change_pass_error_message);
        changePassErrorMsg.setText("");

        currentPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // allows focus on other fields only if old password field is not empty:
                newPass.setFocusable(!s.toString().equals(""));
                newPassRetype.setFocusable(!s.toString().equals(""));
                newPass.setFocusableInTouchMode(!s.toString().equals(""));
                newPassRetype.setFocusableInTouchMode(!s.toString().equals(""));
            }
        });

        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(LoginViewModel.class);

        saveBtn = v.findViewById(R.id.change_pass_save_btn);
        saveBtn.setOnClickListener(v1 -> {
            changePassParams.put("old_password", currentPass.getText().toString());
            changePassParams.put("new_password", newPass.getText().toString());
            changePassParams.put("new_password_retype", newPassRetype.getText().toString());
            loginViewModel.changePassword(changePassParams, changePassErrorMsg);
        });
        return v;
    }
}