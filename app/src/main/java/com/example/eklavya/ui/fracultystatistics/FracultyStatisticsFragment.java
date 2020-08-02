package com.example.eklavya.ui.fracultystatistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.eklavya.R;
import com.example.eklavya.ViewAttendaceFac;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FracultyStatisticsFragment extends Fragment  {


    public static final String extra_subject="com.example.eklavya.extra_subject";
    public static final String extra_date="com.example.eklavya.extra_date";
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    Task<DocumentSnapshot> DFSubject;
    String selectedsubject;
    String facID;
    EditText textdate;
    Button btnsubmit;

    private FracultyStatisticsViewModel mViewModel;

    public static FracultyStatisticsFragment newInstance() {
        return new FracultyStatisticsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fraculty_statistics_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FracultyStatisticsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fStore= FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        //spinner subject data retrieving

        facID=fAuth.getCurrentUser().getUid();
        //    DFSubject=fStore.collection("Faculty").document(facID);
        List<String> spinnersubjectlist=new ArrayList<String>();
        Spinner spinsubject = view.findViewById(R.id.spinfsfsubject);
        ArrayAdapter<String> adapter3;
        adapter3 = new ArrayAdapter<String>(Objects.requireNonNull(this.getContext()),  R.layout.support_simple_spinner_dropdown_item,spinnersubjectlist);
        adapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinsubject.setAdapter(adapter3);
        //spintclass.setOnItemSelectedListener(this);

        DFSubject=fStore.collection("Faculty").document(facID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                NotefsfSub notefsfsub=documentSnapshot.toObject(NotefsfSub.class);
                for(String sub:notefsfsub.getSubject()){
                    spinnersubjectlist.add(sub);
                }
                adapter3.notifyDataSetChanged();
            }
        });
        spinsubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedsubject=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "Nothing is Selected ", Toast.LENGTH_SHORT).show();
            }
        });

        textdate=view.findViewById(R.id.txtdate);
        btnsubmit=view.findViewById(R.id.fsfbutsubmit);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sub=selectedsubject;
                final String date=textdate.getText().toString().trim();
                Intent intent=new Intent(getContext(), ViewAttendaceFac.class);
                intent.putExtra("extra_subject",sub);
                intent.putExtra("extra_date",date);
                startActivity(intent);

            }
        });


    }

}
class NotefsfSub{
    List<String> Subject;
    public  NotefsfSub(){

    }
    public NotefsfSub(List<String>Subject){
        this.Subject=Subject;
    }

    public List<String> getSubject() {
        return Subject;
    }

}
