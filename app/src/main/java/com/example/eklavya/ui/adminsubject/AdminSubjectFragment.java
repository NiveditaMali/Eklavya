package com.example.eklavya.ui.adminsubject;

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

import com.example.eklavya.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
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

public class AdminSubjectFragment extends Fragment {

    private static final String TAG = "main";

    Workbook wb_xssf; //Declare XSSF WorkBook
    Workbook wb_hssf; //Declare HSSF WorkBook
    Sheet sheet=null; //sheet can be used as common for XSSF and HSSF WorkBook

    FirebaseFirestore fStore;
    Button uploadtbtn, choosefiletbtn;
    TextView pathtview;
    String selecteddept,selectedclass;

    private AdminSubjectViewModel mViewModel;

    public static AdminSubjectFragment newInstance() {
        return new AdminSubjectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_subject_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadtbtn = view.findViewById(R.id.btnasfupload);
        choosefiletbtn = view.findViewById(R.id.btnasfchoosefile);
        pathtview = view.findViewById(R.id.txtasffilepath);
        fStore= FirebaseFirestore.getInstance();

        //spinner for department selection

        Spinner spindept = view.findViewById(R.id.spinasfdept);
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
        Spinner spinclass = view.findViewById(R.id.spinasfclass);
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
                        .withSupportFragment(AdminSubjectFragment.this)
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


                        if (filePath.toLowerCase().endsWith("xls")) {
                            POIFSFileSystem myFileSystem = new POIFSFileSystem(new File(filePath));

                            wb_hssf = new HSSFWorkbook(myFileSystem);
                            sheet = wb_hssf.getSheetAt(0);
                        } else if (filePath.toLowerCase().endsWith("xlsx")) {
                            FileInputStream myInput = new FileInputStream(filePath);
                            wb_xssf = new XSSFWorkbook(myInput);
                            sheet = wb_xssf.getSheetAt(0);
                            Log.i("BR1", filePath);
                        }


                        // We now need something to iterate through the cells.
                        Iterator<Row> rowIter = sheet.rowIterator();
                        int rowno = 0;
                        // viewtext.append("\n");
                        while (rowIter.hasNext()) {
                            Log.e(TAG, " row no " + rowno);
                            Row myRow = (Row) rowIter.next();
                            if (rowno != 0) {
                                Iterator<Cell> cellIter = myRow.cellIterator();
                                int colno = 0;
                                String sname = "";

                                while (cellIter.hasNext()) {
                                    Cell myCell = (Cell) cellIter.next();
                                    if (colno == 0) {
                                        sname = myCell.toString();
                                    }
                                    colno++;
                                    Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());

                                    DocumentReference documentReference = fStore.collection(selecteddept).document(selectedclass).collection("Subject").document(sname);
                                    Map<String, Object> StudData = new HashMap<>();

                                    documentReference.set(StudData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "File Uploaded successfully ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                Log.i("BR", filePath);
                            }

                            rowno++;
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "error " + e.toString());
                    }
                }
            });

        }
    }
}
