package com.example.eklavya.ui.facultyattendance;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eklavya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FacultyAttendanceFragment extends Fragment  {

    private static final String TAG = "main";
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    Task<DocumentSnapshot> DFSubject;
    Task<QuerySnapshot> DFStudent;
    Button submitbtn;
    String selecteddept;
    protected String selectedclass;
    String selectedsubject;
    String facID;
    String facedept;
    ValueEventListener listener;
    ArrayList<String> spinnersubjectlist;

    String[] listitems;
    boolean[] checkeditems;
    ArrayList<Integer>mUseritems=new ArrayList<>();
    ArrayList<String>mUserid=new ArrayList<>();
    List<String>eventList=new ArrayList<>();
    boolean isFirstTimeClick=true;

    private FacultyAttendanceViewModel mViewModel;


    public static FacultyAttendanceFragment newInstance() {
        return new FacultyAttendanceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.faculty_attendance_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        submitbtn = view.findViewById(R.id.btnfafsubmit);
        fStore= FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();



        //get current user department
        facID=fAuth.getCurrentUser().getUid();
        fStore.collection("Faculty").document(facID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                facedept= documentSnapshot.getString("Department");

            }
        });
        //get current date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        Log.i("BR", thisDate);



        //spinner for class selection
        Spinner spinclass = view.findViewById(R.id.spinfafclass);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(this.getContext()), R.array.classname, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinclass.setAdapter(adapter);
        //spintclass.setOnItemSelectedListener(this);
        spinclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedclass = adapterView.getItemAtPosition(i).toString();
                Log.i("BR", selectedclass);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "Nothing is Selected ", Toast.LENGTH_SHORT).show();
            }
        });



        readData(new FirestoreCallback() {
            @Override
            public void onCallback(List<String> mUserid) {
                Log.i("BR9", String.valueOf(mUserid));
                //taking attendance

                // String[] studlist=new String[mUserid.size()];
                Log.i("BR10", String.valueOf(mUserid));
                listitems = new String[mUserid.size()];
               listitems= mUserid.toArray(listitems);

                for(int i = 0; i < listitems.length ; i++){
                    Log.d("string is",(String)listitems[i]);
                }
               // listitems=getResources().getStringArray(mUserid);
                checkeditems=new boolean[listitems.length];

                //submit button
                submitbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(getContext());
                        mBuilder.setTitle("Take Attendance");
                        mBuilder.setMultiChoiceItems(listitems, checkeditems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b) {
                                    if (!mUseritems.contains(i)) {
                                        mUseritems.add(i);
                                    }
                                    else if(mUseritems.contains(i)){
                                        mUseritems.remove(i);
                                    }
                                }
                            }
                        });

                        //ok button

                        mBuilder.setCancelable(false);
                        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String item="";
                                for (int j=0;j<mUseritems.size();j++){
                                    item=listitems[mUseritems.get(j)];
                                    Log.i("BR5",item);
                                    DocumentReference documentReference = fStore.collection("Attendance").document(selectedsubject).collection(thisDate).document(item);
                                    Map<String, Object> FacData = new HashMap<>();
                                    documentReference.set(FacData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Attendance marked ", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }


                                //Log.i("BR5",item);
                                for(int k=0;k<checkeditems.length;k++){
                                    checkeditems[k]=false;
                                    mUseritems.clear();
                                }


                            }
                        });

                        //dismiss button

                        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        //clear all button

                        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for(int j=0;j<checkeditems.length;j++){
                                    checkeditems[j]=false;
                                    mUseritems.clear();
                                }
                            }
                        });

                        AlertDialog mDialog=mBuilder.create();
                        mDialog.show();;

                    }
                });
            }
        });

        //spinner subject data retrieving

        //facID=fAuth.getCurrentUser().getUid();
         //    DFSubject=fStore.collection("Faculty").document(facID);
             List<String> spinnersubjectlist=new ArrayList<String>();
            Spinner spinsubject = view.findViewById(R.id.spinfafsubject);
            ArrayAdapter<String> adapter3;
            adapter3 = new ArrayAdapter<String>(Objects.requireNonNull(this.getContext()),  R.layout.support_simple_spinner_dropdown_item,spinnersubjectlist);
            adapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinsubject.setAdapter(adapter3);
            //spintclass.setOnItemSelectedListener(this);

        DFSubject=fStore.collection("Faculty").document(facID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                NoteSub notesub=documentSnapshot.toObject(NoteSub.class);
                for(String sub:notesub.getSubject()){
                    spinnersubjectlist.add(sub);
                }
                adapter3.notifyDataSetChanged();
            }
        });

           /* DFSubject.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String sub = document.getId();
                            spinnersubjectlist.add(sub);
                            Log.i("BR", sub);
                        }
                        adapter3.notifyDataSetChanged();
                    }
                }
            });

            */

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


   /*  DFStudent=fStore.collection("Student");
        DFStudent.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        String sub = document.getId();
                        mUserid.add(sub);
                        Log.i("BR7", sub);
                    }
                    Log.i("BR8", String.valueOf(mUserid));
                }
            }
        });

    */





    }
    private void readData(FirestoreCallback firestoreCallback){

    DFStudent=fStore.collection("Student").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
             if (task.isSuccessful()) {
                 List<String> mUserid = new ArrayList<>();
                 Log.i("BRclass",selectedclass);
                 for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                     String studId = document.getId();
                    // String studdept=document.getString("Department");

                     String studyear=document.getString("Class");
                   //  String name = document.getString("Name");
                         String rollno = document.getString("RollNo");
                         mUserid.add(rollno);

                     Log.i("BR7",studyear);

                 }
                 firestoreCallback.onCallback(mUserid);
                 Log.i("BR8", String.valueOf(mUserid));
             }
        }
    });
}

    private interface FirestoreCallback{
        void onCallback(List<String> mstudid);
    }
}
class NoteSub{
    List<String> Subject;
    public  NoteSub(){

    }
    public NoteSub(List<String>Subject){
        this.Subject=Subject;
    }

    public List<String> getSubject() {
        return Subject;
    }

}

class NoteStud{
    List<String> Name;
    public NoteStud(){

    }

    public List<String> getName() {
        return Name;
    }
}


