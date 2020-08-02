package com.example.eklavya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class userfaculty extends Fragment {

    FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    RecyclerView mFirestoreList;
    FirestorePagingAdapter adapter;
    String admindept,adminid,facdept,facid;
    Query query;

    private UserfacultyViewModel mViewModel;

    private SharedPreferences sharedPreferences;
    public static userfaculty newInstance() {
        return new userfaculty();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        return inflater.inflate(R.layout.userfaculty_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserfacultyViewModel.class);
        // TODO: Use the ViewModel

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fabuserfaculty);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminAddUserFaculty.class));
            }
        });
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        mFirestoreList = view.findViewById(R.id.RVuserfaculty);

        //facid and fac dept

        readData(new AFacCallback() {
            @Override
            public void onCallback(String admindept) {
                Log.i("Admindept1", admindept);
                //divider decoration
                DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
                dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
                mFirestoreList.addItemDecoration(dividerItemDecoration);

    //Query
    query = fStore.collection("Faculty");


                PagedList.Config config=new PagedList.Config.Builder()
                        .setInitialLoadSizeHint(10)
                        .setPageSize(3)
                        .build();




                //RecyclerOptions
                FirestorePagingOptions<FacultyModelA> options=new FirestorePagingOptions.Builder<FacultyModelA>()
                        .setLifecycleOwner((LifecycleOwner) getContext())
                        .setQuery(query, config, new SnapshotParser<FacultyModelA>() {
                            @NonNull
                            @Override
                            public FacultyModelA parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                FacultyModelA facultyModelA=snapshot.toObject(FacultyModelA.class);
                                // studid=snapshot.getId();
                                //  studentModelA.setStudid(studid);
                                return facultyModelA;
                            }
                        })
                        .build();

                adapter=new FirestorePagingAdapter<FacultyModelA, FacultyViewHolder>(options) {

                    @NonNull
                    @Override
                    public FacultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single_admin_faculty,parent,false);
                        return new FacultyViewHolder(v);
                    }


                    @Override
                    protected void onBindViewHolder(@NonNull FacultyViewHolder holder, int position, @NonNull FacultyModelA model) {


                            holder.list_name.setText(model.getName());

                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull LoadingState state) {
                        super.onLoadingStateChanged(state);
                        switch (state){

                            case LOADING_INITIAL:
                                Log.d("PAGING_LOG","All inintial data");
                                break;
                            case LOADING_MORE:
                                Log.d("PAGING_LOG","All next page");
                                break;
                            case FINISHED:
                                Log.d("PAGING_LOG","All data loaded");
                                break;
                            case ERROR:
                                Log.d("PAGING_LOG","Error Loading Data");
                                break;
                            case LOADED:
                                Log.d("PAGING_LOG","Total Item Loaded: "+getItemCount());
                                break;
                        }
                    }
                };
                mFirestoreList.setHasFixedSize(true);
                mFirestoreList.setLayoutManager(new LinearLayoutManager(getContext()));
                mFirestoreList.setAdapter(adapter);

            }

        });
/*
        //get current user department
        adminid=fAuth.getCurrentUser().getUid();
        fStore.collection("Admin").document(adminid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                admindept= documentSnapshot.getString("Department");
                Log.i("Admindept",admindept);
            }
        });

 */

    }
    class FacultyViewHolder extends RecyclerView.ViewHolder{

        private TextView list_name;


        public FacultyViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name=itemView.findViewById(R.id.list_fname);


        }
    }
    public void readData(AFacCallback aFacCallback){
        adminid=fAuth.getCurrentUser().getUid();
        fStore.collection("Admin").document(adminid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                admindept= documentSnapshot.getString("Department");
                Log.i("Admindept3",admindept);
                aFacCallback.onCallback(admindept);
            }
        });
    }
    public interface AFacCallback {
        void onCallback(String admindept);
    }

}

