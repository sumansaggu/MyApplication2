package com.example.saggu.myapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ImportActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "ImportActivity";
    private String[] filePathString;
    private String[] fileNameString;
    private File[] listFiles;
    File file;

    Button btnListStorageForSTB, btnListStorageForCust;

    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;
    ArrayList<ImportValues> uploadData;
    ListView listStorage;
    DbHendler dbHendler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        dbHendler = new DbHendler(this, null, null, 1);
        listStorage = (ListView) findViewById(R.id.lvIntenalStorage);

        btnListStorageForCust = (Button) findViewById(R.id.importCust);
        btnListStorageForSTB = (Button) findViewById(R.id.importSTB);
        uploadData = new ArrayList<>();
        dbHendler.copyDbToExternalStorage(this.getApplicationContext());
        checkFilePermissions();
        dbHendler.copyDbToExternalStorage(this.getApplicationContext());






        btnListStorageForCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                pathHistory = new ArrayList<String>();
                pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
                Log.d(TAG, "btn sd card: " + pathHistory.get(count));
                checkInternalStorage();
            }
        });
        listStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastDirectory = pathHistory.get(count);
                if (lastDirectory.equals(parent.getItemAtPosition(position))) {
                    Log.d(TAG, " selected item for uplaod: " + lastDirectory);
                    //execute method to read exel data
                    readExelData(lastDirectory);


                } else {
                    count++;
                    pathHistory.add(count, (String) parent.getItemAtPosition(position));
                    checkInternalStorage();
                    Log.d(TAG, "lvInternalstorage: " + pathHistory.get(count));
                }

            }
        });
        btnListStorageForSTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    Log.d(TAG, "up Directory : you have reached the highest level directory");
                } else {
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage();
                    Log.d(TAG, "up Directory: " + pathHistory.get(count));
                }
            }
        });


    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.importCust){

        }
        if(v.getId()==R.id.importSTB){

        }

    }
    private void readExelData(String filePath) {
        Log.d(TAG, "readExelData...");
        //declare input file
        File inputFile = new File(filePath);
        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowscount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();

            //loops through the rows
            for (int r = 0; r < rowscount; r++) {
                Row row = sheet.getRow(r);
                int cellscount = row.getPhysicalNumberOfCells();
                //loop through the columns
                for (int c = 0; c < cellscount; c++) {
                    //handle if there are two many columns in the sheet
                    if (c > 6) {
                        toast("format not supported");
                        break;
                    } else {
                        String value = getCellsAtString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        Log.d(TAG, "readExelData: data from row: " + cellInfo);
                        sb.append(value + ", ");
                    }
                }
                sb.append(":");
            }
            Log.d(TAG, "readExelData: " + sb.toString());
            parseStingBuilder(sb);
            toast("Upload Success");
        } catch (FileNotFoundException ex) {
            Log.e(TAG, "readExelData: file not found " + ex.getMessage());
        } catch (IOException ex) {
            Log.e(TAG, "readExelData: Error reading inputstream " + ex.getMessage());
        }
    }

    //method for parsing data and storing in ArrayList<ImportedValues>
    private void parseStingBuilder(StringBuilder mStingBuilder) {
        Log.d(TAG, "parseStingBuilder: parsing started");
        //splits the sb into rows
        String[] rows = mStingBuilder.toString().split(":");
        //add to the arraylist row by row
        for (int i = 0; i < rows.length; i++) {
            //split the columns of rows
            String[] columns = rows[i].split(",");
            //use try catch to make sure there are no "" that try to parse into doubles.
            try {
                //   double x = Double.parseDouble(columns[0]);
                //   double y = Double.parseDouble(columns[1]);
                //    double z = Double.parseDouble(columns[2]);

                String name = columns[0];
                String mob = columns[1];
                double conNo = Double.parseDouble(columns[2]);
                float conno = (float) conNo;
                int fees = (int) Double.parseDouble(columns[3]);
                int balance = (int) Double.parseDouble(columns[4]);
                String nName = columns[5];
                dbHendler.addPersonImport(new PersonInfo(name, mob, conno, fees, balance, nName), this);
                //    String cellInfo = "Rows.. | " + name + " | " + mob + " | " + conNo + " | " + fees + " | " + balance + " | " + nName;
                //   Log.d(TAG, "parseStingBuilder: data from row: " + cellInfo);


                //add the uploaddata ArrayList
                //  uploadData.add(new ImportValues(x,y));
                uploadData.add(new ImportValues(name, mob, conNo, fees, balance, nName));
            } catch (NumberFormatException ex) {
                Log.e(TAG, "parseStringBuilder: NUMBERFORMATEXCEPTION " + ex.getMessage());
            }
        }

    }


    //returns the cell as a string from the excel file
    private String getCellsAtString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellvalue = formulaEvaluator.evaluate(cell);
            switch (cellvalue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellvalue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericvalue = cellvalue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellvalue.getNumberValue();
                        SimpleDateFormat dateformatter =
                                new SimpleDateFormat("YYYY/MM/DD");
                        value = dateformatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericvalue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellvalue.getStringValue();
                    break;
                default:
            }

        } catch (NullPointerException ex) {
            Log.e(TAG, "getCellsAtString: NULLPOINTEREXEPTION " + ex.getMessage());
        }
        return value;
    }

    private void checkInternalStorage() {
        Log.d(TAG, "checkInternalStorage started..");
        try {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                toast("sd card not found");
            } else {
                // locate the folder
                file = new File(pathHistory.get(count));
            }
            listFiles = file.listFiles();

            //create a string array for file path string
            filePathString = new String[listFiles.length];

            //creat s string array for file names string
            fileNameString = new String[listFiles.length];

            for (int i = 0; i < listFiles.length; i++) {
                //get the path of the image file
                filePathString[i] = listFiles[i].getAbsolutePath();
                //get the name image file
                fileNameString[i] = listFiles[i].getName();
            }

            for (int i = 0; i < listFiles.length; i++) {
                Log.d(TAG, "Files..File Name: " + listFiles[i].getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filePathString);
            listStorage.setAdapter(adapter);

        } catch (NullPointerException ex) {
            Log.e(TAG, "null pointer exexption " + ex.getMessage());
        }
    }


    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }
        } else {
            Log.d(TAG, "checkFilePermissions: no need to check permissions");
        }
    }

    private void toast(String massage) {
        Toast.makeText(this, massage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ViewAll.class);
        startActivity(i);
    }


}
