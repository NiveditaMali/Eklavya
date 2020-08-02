package com.example.eklavya;

import android.content.Intent;
import android.os.Bundle;
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

public class userstudent extends Fragment {

      FirebaseFirestore fStore;
      FirebaseAuth fAuth;
    RecyclerView mFirestoreList;
    FirestorePagingAdapter adapter;
    String adminid, admindept, studdept, studid;
    String[] classlist;
    Query query;

    private UserstudentViewModel mViewModel;

    public static userstudent newInstance() {
        return new userstudent();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.userstudent_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserstudentViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fabuserstudent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminAddUserStudent.class));
            }
        });


        classlist = getResources().getStringArray(R.array.classname);


     /*   //divider decoration
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
       recyclerView.addItemDecoration(dividerItemDecoration);



       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       recyclerView.setAdapter(new Adapter(this,classlist));

    }

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        Context context;
        String[] items;
        public Adapter(Context context){
            this.context=context;
            this.items=items;

        }

        public Adapter(userstudent userstudent, String[] classlist) {
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(context);
            View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single_admin_faculty,parent,false);
            return new ClasslistViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((ClasslistViewHolder)holder).list_name.setText(items[position]);
        }

        @Override
        public int getItemCount() {
            return 0;
        }
        class ClasslistViewHolder extends RecyclerView.ViewHolder {

            private TextView list_name;


            public ClasslistViewHolder(@NonNull View itemView) {
                super(itemView);

                list_name = itemView.findViewById(R.id.list_fname);


            }
        }
    }

      */


        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        mFirestoreList = view.findViewById(R.id.RVuserstudent);

        readData(new AStudCallback() {
            @Override
            public void onCallback(String admindept) {

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
                mFirestoreList.addItemDecoration(dividerItemDecoration);
                //Query
                query = fStore.collection("Student");

                PagedList.Config config = new PagedList.Config.Builder()
                        .setInitialLoadSizeHint(10)
                        .setPageSize(3)
                        .build();


                //RecyclerOptions
                FirestorePagingOptions<StudentModelA> options = new FirestorePagingOptions.Builder<StudentModelA>()
                        .setLifecycleOwner((LifecycleOwner) getContext())
                        .setQuery(query, config, new SnapshotParser<StudentModelA>() {
                            @NonNull
                            @Override
                            public StudentModelA parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                StudentModelA studentModelA = snapshot.toObject(StudentModelA.class);
                                studid = snapshot.getId();
                               // studentModelA.setStudid(studid);
                                return studentModelA;
                            }
                        })
                        .build();

                adapter = new FirestorePagingAdapter<StudentModelA, StudentViewHolder>(options) {

                    @NonNull
                    @Override
                    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single_admin, parent, false);
                        return new StudentViewHolder(v);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull StudentViewHolder holder, int position, @NonNull StudentModelA model) {
                        holder.list_names.setText(model.getName());
                        holder.list_batchs.setText(model.getRollNo());
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
                mFirestoreList.setHasFixedSize(true);
                mFirestoreList.setLayoutManager(new LinearLayoutManager(getContext()));
                mFirestoreList.setAdapter(adapter);

            }
        });
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {

        private TextView list_names;
        private TextView list_batchs;


        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            list_names = itemView.findViewById(R.id.list_sname);
            list_batchs = itemView.findViewById(R.id.list_sbatch);

        }
    }

    public void readData(AStudCallback aStudCallback) {
        adminid = fAuth.getCurrentUser().getUid();
        fStore.collection("Admin").document(adminid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                admindept = documentSnapshot.getString("Department");
                Log.i("Admindept3", admindept);
                aStudCallback.onCallback(admindept);
            }
        });


    }

    public interface AStudCallback {
        void onCallback(String admindept);
    }




}
