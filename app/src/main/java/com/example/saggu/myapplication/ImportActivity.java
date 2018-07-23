package com.example.saggu.myapplication;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ImportActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "ImportActivity";
    private String[] filePathString;
    private String[] fileNameString;
    private File[] listFiles;
    File file;

    Button btnListStorageForCust, btnListStorageForSTB;

    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;
    ArrayList<ImportValues> uploadCust;
    ArrayList<STB> uploadSTB;
    ListView listStorage;
    TextView title;
    DbHendler dbHendler;
    TextView helpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        dbHendler = new DbHendler(this, null, null, 1);
        title = (TextView) findViewById(R.id.txtviewimport);
        listStorage = (ListView) findViewById(R.id.lvIntenalStorage);
        helpText = (TextView) findViewById(R.id.txtViewHelp);
        btnListStorageForCust = (Button) findViewById(R.id.importCust);
        btnListStorageForSTB = (Button) findViewById(R.id.importSTB);

        uploadCust = new ArrayList<>();
        uploadSTB = new ArrayList<>();
        // checkFilePermissions();
        dbHendler.copyDbToExternalStorage(this.getApplicationContext());


        btnListStorageForCust.setOnClickListener(this);
        btnListStorageForSTB.setOnClickListener(this);
        helpText.setOnClickListener(this);
        //  registerForContextMenu(listStorage);
     /*  backButton.setOnClickListener(new View.OnClickListener() {
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
        });*/


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.importCust) {
            title.setText("SELECT CUSTOMER DATA");
            count = 0;
            pathHistory = new ArrayList<String>();
            pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
            Log.d(TAG, "btn sd card: " + pathHistory.get(count));
            checkInternalStorage();
            listStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    lastDirectory = pathHistory.get(count);
                    if (lastDirectory.equals(parent.getItemAtPosition(position))) {
                        Log.d(TAG, " selected item for uplaod: " + lastDirectory);
                        //execute method to read exel data
                        readExelDataCustmer(lastDirectory);
                    } else {
                        count++;
                        pathHistory.add(count, (String) parent.getItemAtPosition(position));
                        checkInternalStorage();
                        Log.d(TAG, "lvInternalstorage: " + pathHistory.get(count));
                    }
                }
            });
        }
        if (v.getId() == R.id.importSTB) {
            title.setText("SELECT STB DATA");
            count = 0;
            pathHistory = new ArrayList<String>();
            pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
            Log.d(TAG, "btn sd card: " + pathHistory.get(count));
            checkInternalStorage();
            listStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    lastDirectory = pathHistory.get(count);
                    if (lastDirectory.equals(parent.getItemAtPosition(position))) {
                        Log.d(TAG, " selected item for uplaod: " + lastDirectory);
                        //execute method to read exel data
                        readExelDataSTB(lastDirectory);

                    } else {
                        count++;
                        pathHistory.add(count, (String) parent.getItemAtPosition(position));
                        checkInternalStorage();
                        Log.d(TAG, "lvInternalstorage: " + pathHistory.get(count));
                    }
                }
            });
        }
        if (v.getId() == R.id.txtViewHelp) {
            Log.d(TAG, "onClick: help clicked");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/watch?v=vcNw1Pt0uMI"));
            startActivity(intent);
        }

    }

    private void readExelDataCustmer(String filePath) {
        Log.d(TAG, "readExelData...");
        //declare input file
        File inputFile = new File(filePath);
        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(1);
            int rowscount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();

            //loops through the rows
            for (int r = 1; r < rowscount; r++) {
                Row row = sheet.getRow(r);
                int cellscount = row.getPhysicalNumberOfCells();
                //loop through the columns
                for (int c = 0; c < cellscount; c++) {
                    //handle if there are two many columns in the sheet
                    if (c > 6) {
                        toast("format not supported");
                        break;
                    } else {
                        String valueCust = getCellsAtString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + valueCust;
                        Log.d(TAG, "readExelData: data from row: " + cellInfo);
                        sb.append(valueCust + ", ");
                    }
                }
                sb.append(":");
            }
            Log.d(TAG, "readExelData: " + sb.toString());
            parseStringBuilderCust(sb);
            toast("Upload Success");
        } catch (FileNotFoundException ex) {
            Log.e(TAG, "readExelData: file not found " + ex.getMessage());
        } catch (IOException ex) {
            Log.e(TAG, "readExelData: Error reading inputstream " + ex.getMessage());
        }
    }

    //method for parsing data and storing in ArrayList<ImportedValues>
    private void parseStringBuilderCust(StringBuilder mStingBuilder) {
        Log.d(TAG, "parseStringBuilderCust: parsing started");
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
                //   double z = Double.parseDouble(columns[2]);

                String name = columns[0];
                String mob = columns[1];
                double conNo = Double.parseDouble(columns[2]);
                float conno = (float) conNo;
                int fees = (int) Double.parseDouble(columns[3]);
                int balance = (int) Double.parseDouble(columns[4]);
                int stbid = (int) conNo;
                String nName = columns[5];
                dbHendler.addPersonImport(new PersonInfo(name, mob, conno, fees, balance, stbid, nName), this);
                String cellInfo = "Rows.. | " + name + " | " + mob + " | " + conNo + " | " + fees + " | " + balance + " | " + stbid + " | " + nName;
                Log.d(TAG, "parseStringBuilderCust: data from row: " + cellInfo);


                //add the uploaddata ArrayList
                //  uploadData.add(new ImportValues(x,y));
                uploadCust.add(new ImportValues(name, mob, conNo, fees, balance, stbid, nName));
            } catch (NumberFormatException ex) {
                Log.e(TAG, "parseStringBuilder: NUMBERFORMATEXCEPTION " + ex.getMessage());
            }
        }

    }

    private void readExelDataSTB(String filePath) {
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
            for (int r = 1; r < rowscount; r++) {
                Row row = sheet.getRow(r);
                int cellscount = row.getPhysicalNumberOfCells();
                //loop through the columns
                for (int c = 0; c < cellscount; c++) {
                    //handle if there are two many columns in the sheet
                    if (c > 2) {
                        toast("format not supported");
                        break;
                    } else {
                        String valueSTB = getCellsAtString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + valueSTB;
                        Log.d(TAG, "readExelData: data from row: " + cellInfo);
                        sb.append(valueSTB + ", ");
                    }
                }
                sb.append("%");
            }
            Log.d(TAG, "readExelData: " + sb.toString());
            parseStringBuilderSTB(sb);
            toast("Upload Success");
        } catch (FileNotFoundException ex) {
            Log.e(TAG, "readExelData: file not found " + ex.getMessage());
        } catch (IOException ex) {
            Log.e(TAG, "readExelData: Error reading inputstream " + ex.getMessage());
        }
    }


    private void parseStringBuilderSTB(StringBuilder mStingBuilder) {
        Log.d(TAG, "parseStringBuilderCust:STB parsing started");
        //splits the sb into rows
        String[] rows = mStingBuilder.toString().split("%");
        //add to the arraylist row by row
        for (int i = 0; i < rows.length; i++) {
            //split the columns of rows
            String[] columns = rows[i].split(",");
            //use try catch to make sure there are no "" that try to parse into doubles.
            try {
                String sn = columns[0];
                String vc_mac = columns[1];
                String status = "ACTIVE";
                dbHendler.AddNewStb(new STB(sn, vc_mac, status));
                String cellInfo = "Rows.." + i + " | " + sn + " | " + vc_mac + " | ";
                Log.d(TAG, "parseStringBuilderCust: data from row: " + cellInfo);


                //    add the data to ArrayList

                //    uploadSTB.add(new STB(sn, vc_mac));
            } catch (NumberFormatException ex) {
                Log.e(TAG, "parseStringBuilder STB: NUMBERFORMATEXCEPTION " + ex.getMessage());
            } catch (SQLiteConstraintException ex) {
                toast("" + ex);
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
                    BigDecimal bd = new BigDecimal(numericvalue); //convert exponetial value to simple value

                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellvalue.getNumberValue();
                        SimpleDateFormat dateformatter =
                                new SimpleDateFormat("YYYY/MM/DD");
                        value = dateformatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + bd;
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
        // Intent i = new Intent(this, ViewAll.class);
        // startActivity(i);
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Import");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Import") {
            toast("selected" + item);


        }
        return true;

    }
}
