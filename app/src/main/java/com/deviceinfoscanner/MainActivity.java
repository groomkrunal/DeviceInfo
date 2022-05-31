package com.deviceinfoscanner;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;


import android.graphics.fonts.Font;

import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ProcessBuilder processBuilder;
    String Holder = "";
    String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    InputStream inputStream;
    Process process ;
    byte[] byteArry ;
    AppCompatTextView generatePDF,txtInfo;
    AppCompatImageView imgDownloadPDf;
    long i;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(checkPermission()){
            init();
            getProcessorValue();
            getTotalRAM();
            try {
                getCamerasMegaPixel();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            getConfigInfo();
        }else {
            requestPermission();
        }


    }

    /**
     * initialization of variables
     */
    private void init(){
        txtInfo = findViewById(R.id.txtInfo);
        imgDownloadPDf = findViewById(R.id.imgDownloadPDf);
        imgDownloadPDf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePDf();
            }
        });

      }

    /**
     * This method is used to get device configuration
     */
    public void getConfigInfo() {
        String Manufacturer_value = Build.MANUFACTURER;
        String Brand_value = Build.BRAND;
        String Model_value = Build.MODEL;
        String Board_value = Build.BOARD;
        String Hardware_value = Build.HARDWARE;
        String Serial_nO_value = Build.SERIAL;
        // String  UID_value = tManager.getDeviceId();
        String android_id =
                Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
        String ScreenResolution_value = Build.DISPLAY;
        String Build_ID = Build.ID;

        String Bootloader = Build.BOOTLOADER;

        String CPU_ABS = Build.CPU_ABI;
        String CPU_ABI2 = Build.CPU_ABI2;
        String DEVICE = Build.DEVICE;
        String DISPLAY = Build.DISPLAY;

        String Product = Build.PRODUCT;
        String RadioAvailable = Build.RADIO;
        String Serial = Build.SERIAL;
        String Support_32_Buit = String.valueOf(Build.SUPPORTED_32_BIT_ABIS);
        String Support_64 = String.valueOf(Build.SUPPORTED_64_BIT_ABIS);
        String Support_ABIS = String.valueOf(Build.SUPPORTED_ABIS);
        String Release_Key = Build.TAGS;
        String Time = String.valueOf(Build.TIME);
//You can check All the other values here
        Log.i("TAG", "=======================================================");
        Log.i("TAG", "Bootloader:" + Bootloader);
        Log.i("TAG", "CPU_ABS:" + CPU_ABS);
        Log.i("TAG", "CPU_ABI2:" + CPU_ABI2);
         Log.i("TAG", "DEVICE:" +DEVICE);
        Log.i("TAG", "DISPLAY:" + DISPLAY);
        Log.i("TAG", "Product:" + Product);
        Log.i("TAG", "RadioAvailable: " + RadioAvailable);
        Log.i("TAG", "Support_32_Buit: " + Support_32_Buit);
        Log.i("TAG", "Support_64: " +Support_64);
        Log.i("TAG", "Support_ABIS: " + Support_ABIS);
        Log.i("TAG", "Release_Key: " +Release_Key);
        Log.i("TAG", "Time: " + Time);

        Log.i("TAG", "=======================================================");
        Log.i("TAG", "Hardware_value:" + Hardware_value);
        Log.i("TAG", "Hardware_value:" + Hardware_value);
        Log.i("TAG", "ScreenResolution_value:" + ScreenResolution_value);
        Log.i("TAG", "API_level:" + Build.VERSION.SDK_INT + "");

        Log.i("TAG", "Build_ID:" + Build_ID);
        Log.i("TAG", "IDD:" + android_id);
        Log.i("TAG", "SERIAL: " + Build.SERIAL);

        Log.i("TAG", "MODEL: " + Build.MODEL);
        Log.i("TAG", "BUILD NUMBER: " + Build.ID + Build.VERSION.INCREMENTAL);
        Log.i("TAG", "Manufacture: " + Build.MANUFACTURER);
        Log.i("TAG", "brand: " + Build.BRAND);
        Log.i("TAG", "type: " + Build.TYPE);
        Log.i("TAG", "user: " + Build.USER);
        Log.i("TAG", "BASE: " + Build.VERSION_CODES.BASE);
        Log.i("TAG", "BASEBAND VERSION " + Build.VERSION.INCREMENTAL);
        Log.i("TAG", "SDK  " + Build.VERSION.SDK);
        Log.i("TAG", "BOARD: " + Build.BOARD);
        Log.i("TAG", "BRAND " + Build.BRAND);
        Log.i("TAG", "HOST " + Build.HOST);
        Log.i("TAG", " Build Number FINGERPRINT: " + Build.FINGERPRINT);
        Log.i("TAG", "Android Version Code: " + Build.VERSION.RELEASE);



        // cpu info
        txtInfo.setText("MODEL : "+Build.MODEL+"\n"+"Brand : "+Build.BRAND+"\n"+"Android Version : "+Build.VERSION.RELEASE+"\n"+
                "BaseBand Version : "+Bootloader+"\n"+"CPU_ABS : " + CPU_ABS+"\n"+"CPU_ABI2 : " + CPU_ABI2+"\n"+"API_level : " + Build.VERSION.SDK_INT + ""+
                "\n"+"DISPLAY : " + DISPLAY+"\n"+"Product : " + Product+"\n"+"Release_Key : " +Release_Key+"\n"+"PROCESSOR : " + Hardware_value+"\n"+
                "IDD : " + android_id+"\n"+"Manufacture : " + Build.MANUFACTURER+"\n"+"type : " + Build.TYPE+"\n"+"RAM : "+i+"GB"+"\n");

        byteArry = new byte[1024];

        try{
            processBuilder = new ProcessBuilder(DATA);

            process = processBuilder.start();

            inputStream = process.getInputStream();

            while(inputStream.read(byteArry) != -1){

                Holder = Holder + new String(byteArry);
            }

            inputStream.close();

        } catch(IOException ex){

            ex.printStackTrace();
        }

        Log.i("TAG","CPU PROCESSOR: "+Holder);


    }


    /**
     *
     * This method is used to calculate the ProcessorValue
     * @return
     */

    private String getProcessorValue() {
        byte[] byteArry = new byte[1024];
        String processor = " ";
        String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(DATA);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            String Holder = " ";
            while (inputStream.read(byteArry) != -1) {
                Holder = Holder + new String(byteArry);
            }
            inputStream.close();
            String replaceString=Holder.replace( "Hardware", "CPU").replace(":", " ").trim();
            // processor = Holder, "Hardware", "CPU").replace(":", " ").trim();
            Log.e("@@@", replaceString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return processor;
    }

    /**
     * Tis is used to calculate total Ram available in  device
     * @return
     */
    public String getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = " ";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();
            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = " ";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();
            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;
            double mb = totRam / 1024.0;
            double gb = mb / 1024.0;
            double tb = totRam / 1073741824.0;
            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb);
                double value1 = Double.parseDouble(lastValue);
                 i = Math.round(value1);

                Log.i("TAG", "characteristics: " + i);
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }
        return lastValue;
    }

    /**
     * This code is used to fetchdevice camrea pixel
     * @return
     * @throws CameraAccessException
     */
    public String getCamerasMegaPixel() throws CameraAccessException {
        String output = "";
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        String[] cameraIds = manager.getCameraIdList();
        Log.i("cameraIds", String.valueOf(cameraIds.length));

        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraIds[0]);
        Log.i("characteristics", String.valueOf(characteristics));

        output = "back camera mega pixel: " +  calculateMegaPixel(characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE).getWidth(),
                characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE).getHeight()) + "\n";
        Log.i("back camera mega pixel",output);


        characteristics = manager.getCameraCharacteristics(cameraIds[1]);
        Log.i("back camera mega pixel",characteristics.toString());
        output +=  "front camera mega pixel: " + calculateMegaPixel(characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE).getWidth(),characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE).getHeight()) + "\n";
        Log.i("front camera mega pixel",output);
        return output;
    }

    public int calculateMegaPixel(float width, float height) {
        return  Math.round((width * height) / 1024000);
    }

    /**
     * This method is used to check  runtime permission is given or not
     * @return
     */

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This method is used to  request Run time permission.
     */
    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                    init();
                    getConfigInfo();
                    getProcessorValue();
                    getTotalRAM();
                    try {
                        getCamerasMegaPixel();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    /**
     * This me is used to generate,open and Save the Pdf file
     */

    private void generatePDf(){
        PdfGenerator.getBuilder()
            .setContext(MainActivity.this)
            .fromViewIDSource()
            .fromViewID(MainActivity.this, R.id.txtInfo)
            .setFileName("Demo-Text")
            .setFolderNameOrPath("MyFolder/MyDemoText/")
            .openPDAfterGeneration(true)
            .build(new PdfGeneratorListener() {
                @Override
                public void onFailure(FailureResponse failureResponse) {
                    super.onFailure(failureResponse);

                }

                @Override
                public void onStartPDFGeneration() {


                }

                @Override
                public void onFinishPDFGeneration() {

                }

                @Override
                public void showLog(String log) {

                    super.showLog(log);
                }

                @Override
                public void onSuccess(SuccessResponse response) {
                    super.onSuccess(response);

                }
            });}

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

