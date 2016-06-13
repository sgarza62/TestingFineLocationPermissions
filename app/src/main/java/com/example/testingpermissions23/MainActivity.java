package com.example.testingpermissions23;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String TAG = "MainActivity";
    private TextView mTextView;
    private View mParentLayout;
    public static final int FINE_LOC_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textview1);
        mParentLayout = findViewById(R.id.root_view);
        checkPermissions();
    }

    private void requestFineLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(mParentLayout, "Fine Location Access is Required", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    FINE_LOC_REQUEST
                            );
                        }
                    })
                    .show();
        } else {
            // Permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOC_REQUEST
            );
        }

        return;
    }

    private void checkPermissions() {
        // Marshmallow+ requires we ask for unmet permissions one-by-one at runtime
        if (Build.VERSION.SDK_INT >= 23) {
            boolean hasFineLocPermission =
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED;

            if (!hasFineLocPermission) {
                Log.i(TAG, "Fine Location Permission Missing");
                requestFineLocationPermission();
            }
        }
    }

    public void recheckPermissions(View view) {
        mTextView = (TextView) view;
        mTextView.setText("");
        checkPermissions();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == FINE_LOC_REQUEST) {
            Log.i(TAG, "Received response for fine location permission request.");
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mTextView.setText("PERMISSION GRANTED");
            } else {
                mTextView.setText("PERMISSION DENIED (CLICK HERE TO TRY AGAIN)");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}