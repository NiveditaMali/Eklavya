package com.example.eklavya.ui.studentschedule;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eklavya.R;
import com.example.eklavya.StudentSchedule;
import com.example.eklavya.ui.timetable.TimetableFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class StudentScheduleFragment extends Fragment {

    private FloatingActionButton fab;

    private StudentScheduleViewModel mViewModel;

    public static StudentScheduleFragment newInstance() {
        return new StudentScheduleFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab=view.findViewById(R.id.fabstudschedule);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent intent=new Intent(getContext(),com.example.eklavya.timetable.class);
             startActivity(intent);
             /*TimetableFragment timetableFragment=new TimetableFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.fabstudschedule,timetableFragment,timetableFragment.getTag())

                        .commit();

              */
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.student_schedule_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StudentScheduleViewModel.class);
        // TODO: Use the ViewModel
    }


}
