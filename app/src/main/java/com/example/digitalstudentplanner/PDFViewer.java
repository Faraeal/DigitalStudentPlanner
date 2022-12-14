package com.example.digitalstudentplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PDFViewer extends AppCompatActivity {

    public static ArrayList<File> mFiles = new ArrayList<>();
    RecyclerView recyclerView;
    public static final int REQUEST_PERMISSION = 1;
    File folder;
    String[] items;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        recyclerView = findViewById(R.id.rv_View_pdf);
        
        permission();



    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
         if (ActivityCompat.shouldShowRequestPermissionRationale(PDFViewer.this,
                 Manifest.permission.WRITE_EXTERNAL_STORAGE))
         {
             Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
         }else {
             ActivityCompat.requestPermissions(PDFViewer.this,
                     new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
         }

        }else {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            initVies();
        }
    }

    private void initVies() {
        folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        mFiles = getPdfFiles(folder);

        ArrayList<File> myPdf = getPdfFiles(Environment.getExternalStorageDirectory());
        items = new String[myPdf.size()];
        for (int i = 0; i < items.length; i++)
        {
            items[i] = myPdf.get(i).getName().replace(".pdf", "");
        }

        PDFAdapter pdfAdapter = new PDFAdapter(this,mFiles, items);
        recyclerView.setAdapter(pdfAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
    }

    private ArrayList<File> getPdfFiles(File folder) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files != null){
            for (File singleFile: files){
                if (singleFile.isDirectory() && !singleFile.isHidden()){
                    arrayList.addAll(getPdfFiles(singleFile));
                }else{
                    if (singleFile.getName().endsWith(".pdf")){
                        arrayList.add(singleFile);
                    }
                }
            }
        }
        return arrayList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                initVies();
            }else{
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();

            }
        }
    }
}