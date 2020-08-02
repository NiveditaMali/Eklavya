package com.example.eklavya.ui.adminadduserstudent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AdminAddUserStudentFragment extends Fragment  {


    private static final String TAG = "main";

    Workbook wb_xssf; //Declare XSSF WorkBook
    Workbook wb_hssf; //Declare HSSF WorkBook
    Sheet sheet=null; //sheet can be used as common for XSSF and HSSF WorkBook

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    Button uploadtbtn, choosefiletbtn;
    TextView pathtview;
    String selecteddept,selectedclass,admindept,adminid,studID,sname,semail,sbatch,srollno;


    private AdminAddUserStudentViewModel mViewModel;

    public static AdminAddUserStudentFragment newInstance() {
        return new AdminAddUserStudentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_add_user_student_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdminAddUserStudentViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadtbtn = view.findViewById(R.id.btnaausupload);
        choosefiletbtn = view.findViewById(R.id.btnaauschoosefile);
        pathtview = view.findViewById(R.id.txtaausfilepath);
        fStore= FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();

//get current user department
        adminid=fAuth.getCurrentUser().getUid();
        fStore.collection("Admin").document(adminid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               admindept= documentSnapshot.getString("Department");

            }
        });


    /*    //spinner for department selection

        Spinner spindept = view.findViewById(R.id.spinaausdept);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(Objects.requireNonNull(this.getContext()), R.array.departmentname, R.layout.support_simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spindept.setAdapter(adapter1);
       // spindept.setOnItemSelectedListener(this);
      //  selecteddept = spindept.getSelectedItem().toString();
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

        //spinner for class selection
        Spinner spinclass = view.findViewById(R.id.spinaausclass);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(this.getContext()), R.array.classname, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinclass.setAdapter(adapter);
        //spintclass.setOnItemSelectedListener(this);
        spinclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedclass=adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        choosefiletbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new MaterialFilePicker()
                        .withSupportFragment(AdminAddUserStudentFragment.this)
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
                @Override
                public void onClick(View view) {
                    try {


                       if(filePath.toLowerCase().endsWith("xls")){
                           POIFSFileSystem myFileSystem = new POIFSFileSystem(new File(filePath));

                            wb_hssf = new HSSFWorkbook(myFileSystem);
                            sheet = wb_hssf.getSheetAt(0);
                        }else if (filePath.toLowerCase().endsWith("xlsx")){
                           FileInputStream myInput=new FileInputStream(filePath);
                            wb_xssf = new XSSFWorkbook(myInput);
                            sheet = wb_xssf.getSheetAt(0);
                           Log.i("BR1",filePath);
                        }


                        // Create a POI File System object
                    /*    POIFSFileSystem myFileSystem = new POIFSFileSystem(new File(filePath));
                        // Create a workbook using the File System
                        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
                        XSSFWorkbook myWorkbook1=new
                        // Get the first sheet from workbook
                        HSSFSheet mySheet = myWorkBook.getSheetAt(0);

                     */
                      //  Workbook wb = WorkbookFactory.create(new File(filePath));
                      //  sheet = wb.getSheetAt(0);
                        // We now need something to iterate through the cells.
                        Iterator<Row> rowIter =sheet.rowIterator();
                        int rowno =0;
                        // viewtext.append("\n");
                        while (rowIter.hasNext()) {
                            Log.e(TAG, " row no "+ rowno );
                            Row myRow = (Row) rowIter.next();
                            if(rowno !=0) {
                                Iterator<Cell> cellIter = myRow.cellIterator();
                                int colno =0;
                                 sname=""; semail=""; sbatch="";srollno="";
                               //int srollno=0;
                                while (cellIter.hasNext()) {
                                    Cell myCell = (Cell) cellIter.next();
                                    if (colno == 0) {
                                        semail = myCell.toString();
                                    } else if (colno == 1) {
                                        srollno =myCell.getStringCellValue();
                                    } else if (colno == 2) {
                                        sname = myCell.toString();
                                    } else if (colno == 3) {
                                        sbatch = myCell.toString();
                                    }
                                    colno++;
                                    Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                                    fAuth.createUserWithEmailAndPassword(semail,"123456789").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            studID = fAuth.getCurrentUser().getUid();
                                            DocumentReference documentReference=fStore.collection("Student").document(studID);
                                            Map<String,Object> StudData=new HashMap<>();
                                            StudData.put("RollNo",srollno);
                                            StudData.put("Name",sname);
                                            StudData.put("EmailID",semail);
                                            StudData.put("Batch",sbatch);
                                            StudData.put("Department",admindept);
                                            StudData.put("Class",selectedclass);

                                            documentReference.set(StudData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext(),"File Uploaded successfully ",Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(),"File Not Uploaded  ",Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Auth failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }


                                Log.i("BR",filePath);
                            }

                            rowno++;
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "error "+ e.toString());
                    }
                }
            });


        }
    }

}
