package com.example.eklavya.ui.adminforgotpassword;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eklavya.R;

public class AdminForgotPasswordFragment extends Fragment {

    private AdminForgotPasswordViewModel mViewModel;

    public static AdminForgotPasswordFragment newInstance() {
        return new AdminForgotPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_forgot_password_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdminForgotPasswordViewModel.class);
        // TODO: Use the ViewModel
    }

}
