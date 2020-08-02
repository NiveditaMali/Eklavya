package com.example.eklavya.ui.adminadduserfaculty;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.eklavya.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AdminAddUserFacultyFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "main";
    Workbook workbook = null;
    XSSFWorkbook wBook;
    Sheet sheet=null; //sheet can be used as common for XSSF and HSSF WorkBook
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    Button uploadtbtn, choosefiletbtn;
    TextView pathtview;
    String selecteddept,selectedclass,userid,facid,userdept;
    String facname,facemail,facsubject1,facsubject2,facsubject3;
    private AdminAddUserFacultyViewModel mViewModel;

    public static AdminAddUserFacultyFragment newInstance() {
        return new AdminAddUserFacultyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_add_user_faculty_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdminAddUserFacultyViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadtbtn = view.findViewById(R.id.btnaaufupload);
        choosefiletbtn = view.findViewById(R.id.btnaaufchoosefile);
        pathtview = view.findViewById(R.id.txtaauffilepath);
        fStore= FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();

    /*    //spinner for department selection

        Spinner spindept = view.findViewById(R.id.spinaaufdept);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(Objects.requireNonNull(this.getContext()), R.array.departmentname, R.layout.support_simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spindept.setAdapter(adapter1);
       // spintdept.setOnItemSelectedListener(this);
       // selectdept = spintdept.getSelectedItem().toString();
        spindept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selecteddept=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

     */

        //get current user department
        userid=fAuth.getCurrentUser().getUid();
        fStore.collection("Admin").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userdept= documentSnapshot.getString("Department");

            }
        });


        choosefiletbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new MaterialFilePicker()
                        .withSupportFragment(AdminAddUserFacultyFragment.this)
                        .withRequestCode(1)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            assert data != null;
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file


            pathtview.setText(filePath);
            //Read Excel to File

            uploadtbtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    try {
                        //Create the input stream from the xlsx/xls file
                        FileInputStream fis = new FileInputStream(filePath);
                        //Create Workbook instance for xlsx/xls file input stream
                        Workbook workbook = null;
                        if(filePath.toLowerCase().endsWith("xlsx")){
                            workbook = new XSSFWorkbook(fis);
                        }else if(filePath.toLowerCase().endsWith("xls")){
                            workbook = new HSSFWorkbook(fis);
                        }

                        //Get the number of sheets in the xlsx file

                        //loop through each of the sheets


                            //Get the nth sheet from the workbook
                            Sheet sheet = workbook.getSheetAt(0);

                            //every sheet has rows, iterate over them
                            Iterator<Row> rowIterator = sheet.iterator();
                        int rowno =0;
                        // viewtext.append("\n");
                        while (rowIterator.hasNext()) {
                            Log.e(TAG, " row no "+ rowno );
                            Row myRow = (Row) rowIterator.next();
                            if(rowno !=0) {
                                Iterator<Cell> cellIter = myRow.cellIterator();
                                int colno =0;
                              String[]subjectarray;
                                facname=null;facemail=null;facsubject1=null;facsubject2=null;facsubject3=null;
                                while (cellIter.hasNext()) {
                                    Cell myCell = (Cell) cellIter.next();
                                    if (colno==0){
                                        facemail = myCell.toString();
                                    }else if (colno==1){
                                        facname = myCell.toString();
                                    }else if (colno==2){
                                        facsubject1 = myCell.toString();
                                    }else if (colno==3){
                                        facsubject2 = myCell.toString();
                                    }else if (colno==4){
                                        facsubject3 = myCell.toString();
                                    }
                                   colno++;
                                   Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                                    Log.i("Email1",facemail);

                                }

                            /*   DocumentReference documentReference = fStore.collection("Faculty").document(userdept).collection("Teachers").document();
                                Map<String, Object> FacData = new HashMap<>();
                                //  StudData.put("Roll No",srollno);
                                FacData.put("Name", facname);
                                FacData.put("Email ID", facemail);
                                FacData.put("Department", userdept);
                                FacData.put("Subject", Arrays.asList(facsubject1, facsubject2, facsubject3));

                                documentReference.set(FacData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "File Uploaded successfully ", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "File not uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                });

                             */


                                    fAuth.createUserWithEmailAndPassword(facemail,"123456789").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Log.i("Email2",facemail);
                                            facid = fAuth.getCurrentUser().getUid();
                                            Log.i("user",facid);

                                                Log.i("user", facid);
                                                DocumentReference documentReference = fStore.collection("Faculty").document(facid);
                                                Log.i("user", facid);
                                                Map<String, Object> FacData = new HashMap<>();
                                                Log.i("user", facid);
                                                //  StudData.put("Roll No",srollno);
                                                FacData.put("Name", facname);
                                                FacData.put("EmailID", facemail);
                                                FacData.put("Department", userdept);
                                                if(facsubject1!=null&&facsubject2!=null&&facsubject3!=null) {
                                                    FacData.put("Subject", Arrays.asList(facsubject1, facsubject2, facsubject3));
                                                }else if (facsubject1!=null&&facsubject2!=null&&facsubject3==null){
                                                    FacData.put("Subject", Arrays.asList(facsubject1, facsubject2));
                                                }else if(facsubject1!=null&&facsubject2==null&&facsubject3==null){
                                                    FacData.put("Subject", Arrays.asList(facsubject1));
                                                }
                                                Log.i("user", facid);

                                                documentReference.set(FacData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getContext(), "File Uploaded successfully ", Toast.LENGTH_SHORT).show();

                                                    }

                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "File not uploaded", Toast.LENGTH_SHORT).show();
                                                    }
                                                });



                                            Toast.makeText(getContext(), "Auth successful", Toast.LENGTH_SHORT).show();
                                        }


                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Auth failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            }

                                Log.i("FU", "12");

                                rowno++;

                        }
                        //close file input stream
                        fis.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


        }

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}

