package com.example.eklavya;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewAttendaceFac extends AppCompatActivity {
    TextView txtsub, txtdate;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    RecyclerView mlist;
    FirestorePagingAdapter adapter;
    Query query;
    String studid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance_fac);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        mlist = findViewById(R.id.RVview_attendance_fac);
        txtsub = findViewById(R.id.vaftxtsubject);
        txtdate = findViewById(R.id.vaftxtdate);


        Intent intent1 = getIntent();
        String getsub = intent1.getStringExtra("extra_subject");
        String getdate = intent1.getStringExtra("extra_date");

        txtsub.setText(getsub);
        txtdate.setText(getdate);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        mlist.addItemDecoration(dividerItemDecoration);
        //Query
        query = fStore.collection("Attendance").document(getsub).collection(getdate);

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();


        //RecyclerOptions
        FirestorePagingOptions<StudentModelA> options = new FirestorePagingOptions.Builder<StudentModelA>()
                .setLifecycleOwner((LifecycleOwner) this)
                .setQuery(query, config, new SnapshotParser<StudentModelA>() {
                    @NonNull
                    @Override
                    public StudentModelA parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        StudentModelA studentModelA = snapshot.toObject(StudentModelA.class);
                        studid = snapshot.getId();
                         studentModelA.setStudid(studid);
                        return studentModelA;
                    }
                })
                .build();

        adapter = new FirestorePagingAdapter<StudentModelA, StudentViewHolder>(options) {

            @NonNull
            @Override
            public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single_admin_faculty, parent, false);
                return new StudentViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull StudentViewHolder holder, int position, @NonNull StudentModelA model) {
               // holder.list_names.setText(model.getName());
                holder.list_studid.setText(model.getStudid());
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state) {

                    case LOADING_INITIAL:
                        Log.d("PAGING_LOG", "All inintial data");
                        break;
                    case LOADING_MORE:
                        Log.d("PAGING_LOG", "All next page");
                        break;
                    case FINISHED:
                        Log.d("PAGING_LOG", "All data loaded");
                        break;
                    case ERROR:
                        Log.d("PAGING_LOG", "Error Loading Data");
                        break;
                    case LOADED:
                        Log.d("PAGING_LOG", "Total Item Loaded: " + getItemCount());
                        break;
                }
            }
        };
        mlist.setHasFixedSize(true);
        mlist.setLayoutManager(new LinearLayoutManager(this));
        mlist.setAdapter(adapter);


    }

    class StudentViewHolder extends RecyclerView.ViewHolder {

       // private TextView list_names;
        private TextView list_studid;


        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            //list_names = itemView.findViewById(R.id.list_sname);
            list_studid = itemView.findViewById(R.id.list_fname);

        }
    }
}
