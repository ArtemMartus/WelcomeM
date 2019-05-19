package com.upsage.welcomem.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;
import com.upsage.welcomem.R;
import com.upsage.welcomem.data.Order;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;
import com.upsage.welcomem.utils.ThemeUtil;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, OnTaskCompleted {
    private ZXingScannerView mScannerView;
    private static final String TAG = "Chinese QR scanner";
    private static final int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!checkPermission())
                requestPermission();
        }
    }

    private boolean checkPermission() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0) {

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!cameraAccepted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(CAMERA)) {
                            showMessageOKCancel(getString(R.string.bothPermissionString),
                                    (dialog, which) -> requestPermissions(new String[]{CAMERA},
                                            REQUEST_CAMERA));
                        }
                    }
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(QRScannerActivity.this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.okString), okListener)
                .setNegativeButton(getString(R.string.cancelString), (dialog, which) -> finish())
                .create()
                .show();
    }

    @Override
    public void onResume() {
        SQLSingleton.startConnection();

        super.onResume();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(mScannerView == null) {
                    mScannerView = new ZXingScannerView(this);
                    setContentView(mScannerView);
                }
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        try {
            mScannerView.stopCamera();
            Toast.makeText(this, getString(R.string.processingString), Toast.LENGTH_SHORT).show();
            int id = Integer.parseInt(rawResult.getText());
            Order order = new Order(id);
//            if (order.getId() == -1)
            order.test(this);
//            else
//                onTaskCompleted(order);


        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.incorrectQRString), Toast.LENGTH_SHORT).show();
            // If you would like to resume scanning, call this method below:
            mScannerView.resumeCameraPreview(this);
        }
    }

    @Override
    public void onTaskCompleted(Object o) {
        if (o == null) {
            Toast.makeText(this, getString(R.string.incorrectQRString), Toast.LENGTH_SHORT).show();
            // If you would like to resume scanning, call this method below:
            mScannerView.resumeCameraPreview(this);
        } else {
            Order order = (Order)o;
            Intent intent = new Intent(QRScannerActivity.this,ShowOrderActivity.class);
            intent.putExtra("orderId",order.getId());
            startActivity(intent);
            finish();
        }
    }
}
