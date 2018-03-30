package com.zhirova.task_2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 123;
    private final String TAG = "MAIN_ACTIVITY";

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

//        if (savedInstanceState == null) {
//            ContactsFragment curFragment = new ContactsFragment();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container, curFragment);
//            fragmentTransaction.commit();
//        }

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
//                != PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, "TROLOLOLO");
//        }


        requestPermissionsAfterLoading();
    }


    private void requestPermissionsAfterLoading() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
                        PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                Log.d(TAG, "shouldShowRequestPermissionRationale");

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                Log.d(TAG, "requestPermissions");
            }
        } else {
            Log.d(TAG, "Permission has already been granted");
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


}
