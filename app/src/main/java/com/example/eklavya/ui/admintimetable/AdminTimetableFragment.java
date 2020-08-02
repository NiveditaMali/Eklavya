package com.example.eklavya.ui.admintimetable;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AdminTimetableFragment extends Fragment  {

    private static final String TAG = "main";

    FirebaseFirestore fStore;
    Button uploadtbtn, choosefiletbtn;
    TextView pathtview;
     String selecteddept,selectedclass;

    private AdminTimetableViewModel mViewModel;

    public static AdminTimetableFragment newInstance() {
        return new AdminTimetableFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_timetable_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdminTimetableViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadtbtn = view.findViewById(R.id.btnttupload);
        choosefiletbtn = view.findViewById(R.id.btnttchoosefile);
        pathtview = view.findViewById(R.id.txtttfilepath);
        fStore= FirebaseFirestore.getInstance();

        //spinner for department selection

        Spinner spindept = view.findViewById(R.id.spinttdept);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(Objects.requireNonNull(this.getContext()), R.array.departmentname, R.layout.support_simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spindept.setAdapter(adapter1);
       // spintdept.setOnItemSelectedListener(this);
       //  selecttdept = spintdept.getSelectedItem().toString();
        spindept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selecteddept=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinner for class selection
        Spinner spinclass = view.findViewById(R.id.spinttclass);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(this.getContext()), R.array.classname, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinclass.setAdapter(adapter);
       // spintclass.setOnItemSelectedListener(this);
       // selecttclass = spintclass.getSelectedItem().toString();
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
                        // .withActivity(this)
                        .withSupportFragment(AdminTimetableFragment.this)
                        .withRequestCode(1)
                        //.withFilter(Pattern.compile(".*\\.txt$")) // Filtering files and directories by file name using regexp
                        //.withFilterDirectories(true) // Set directories filterable (false by default)
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
                        InputStream myInput;
                        // initialize asset manager
                       // AssetManager assetManager = getAssets();
                        //  open excel sheet
//                File file = new File(filePath);

                        // Create a POI File System object
                        POIFSFileSystem myFileSystem = new POIFSFileSystem(new File(filePath));
                        // Create a workbook using the File System
                        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
                        // Get the first sheet from workbook
                        HSSFSheet mySheet = myWorkBook.getSheetAt(0);
                        // We now need something to iterate through the cells.
                        Iterator<Row> rowIter = mySheet.rowIterator();
                        int rowno =0;
                        // viewtext.append("\n");
                        while (rowIter.hasNext()) {
                            Log.e(TAG, " row no "+ rowno );
                            HSSFRow myRow = (HSSFRow) rowIter.next();
                            if(rowno !=0) {
                                Iterator<Cell> cellIter = myRow.cellIterator();
                                int colno =0;
                                String sno="", sub="", batch="";
                                while (cellIter.hasNext()) {
                                    HSSFCell myCell = (HSSFCell) cellIter.next();
                                    if (colno==0){
                                        sno = myCell.toString();
                                    }else if (colno==1){
                                        sub = myCell.toString();
                                    }else if (colno==2){
                                       batch = myCell.toString();
                                    }
                                    colno++;
                                    Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());

                                    // timestamppy.child("Timetable").child("TISTAMP").child(rowno+"").child("ttStamp").child(sno+"").setValue(myCell.toString());
                                    DocumentReference documentReference=fStore.collection(selecteddept).document(selectedclass).collection("Timetable").document("Monday").collection("Time").document(sno);
                                    Map<String,Object> MCA=new HashMap<>();
                                  //  MCA.put("UserId",sno);
                                    MCA.put("SubjectName",sub);
                                    MCA.put("Batch",batch);

                                    documentReference.set(MCA).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(),"File Uploaded successfully ",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                //   viewtext.append( sno + " -- "+ date+ "  -- "+ det+"\n");

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