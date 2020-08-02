package com.example.eklavya.ui.contactus;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.eklavya.R;

public class ContactUsFragment extends Fragment {

    private RadioButton instituteRadio,appRadio;
    private Button submitButton;
    private ContactUsViewModel mViewModel;

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_us_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ContactUsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instituteRadio =view.findViewById(R.id.radio_Institute);
        appRadio =view.findViewById(R.id.radio_App);
        submitButton=view.findViewById(R.id.btnSubmit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (instituteRadio.isChecked()){
                    Toast.makeText(getContext(), "Reported to Institute Successfully", Toast.LENGTH_LONG).show();

                }
                else if (appRadio.isChecked()){
                    Toast.makeText(getContext(), "Reported to App Successfully", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
