package com.example.fridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ImageView myimageView = (ImageView) findViewById(R.id.imgview);
                    Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.img);
                    //myimageView.setImageBitmap(myBitmap);
                    BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                            .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                            .build();
                    TextView txtView = (TextView) findViewById(R.id.txtContent);
                    if (!barcodeDetector.isOperational()) {
                        txtView.setText("Could not set up the detector!");
                    }
                    Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                    SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);

                    Barcode thisCode = barcodes.valueAt(0);
                    txtView.setText(thisCode.rawValue);
                }catch (Exception e){
                    System.out.print("ERROR!!!!!!!!!!!!!!!!!!!!!");
                }
            }
        });
       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final int REQUEST_ID_READ_WRITE_PERMISSION = 1;
       int REQUEST_ID_IMAGE_CAPTURE = 100;

// Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);

        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have read/write permission
            // Kiểm tra quyền đọc/ghi dữ liệu vào thiết bị lưu trữ ngoài.
            int writePermission = ActivityCompat.checkSelfPermission(this,
                  Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (writePermission != PackageManager.PERMISSION_GRANTED ||
                readPermission != PackageManager.PERMISSION_GRANTED) {
             //If don't have permission so prompt the user.
           this.requestPermissions(
                   new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                           Manifest.permission.READ_EXTERNAL_STORAGE },
                    REQUEST_ID_READ_WRITE_PERMISSION

           );
        }
   }

    }
}