package com.example.android.bluetoothchat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends Activity {

    IntentIntegrator integrator = new IntentIntegrator(ScanActivity.this);
    String text = "" ;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        integrator.addExtra("SCAN_WIDTH", size.x*0.75);
        integrator.addExtra("SCAN_HEIGHT", size.y*0.75);
        integrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
        integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
        integrator.addExtra("SAVE_HISTORY", false);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            text = result.getContents();
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra("text",text);
//            setResult(RESULT_OK,returnIntent);
//            finish();

            Intent returnIntent = new Intent();
            returnIntent.putExtra(EXTRA_DEVICE_ADDRESS, text);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
