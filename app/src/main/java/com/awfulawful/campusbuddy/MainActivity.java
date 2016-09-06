package com.awfulawful.campusbuddy;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{
    TextView countdownTxt;
    Button nineOneBut;
    int count = 0;
    int count2 = 0;
    boolean torchMode;
    CameraManager manager;
    torchCallback mtorchCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets the layout of the main activity screen based on the XML file it references
        setContentView(R.layout.activity_main);

        //initialized the toolbar on the top of the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar); //draws the toolbar

        torchMode = false; //initialize torchMode used for LED on or off to false

        //A system service manager for detecting, characterizing, and connecting to CameraDevices
        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        //A callback for camera flash torch modes becoming unavailable, disabled, or enabled.
        mtorchCallback = new torchCallback();

        //System service manager for detecting and connecting to phones vibrator
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //initializes the flashlight button as a button object.
        final Button FlashlightButton = (Button) findViewById(R.id.FlashlightButton);

        assert FlashlightButton != null; //make sure the button is properly initialized

        FlashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //set a listener to the button object

                vibe.vibrate(60); //phone vibrates for 60 milliseconds upon button press

                count++;
                switch (count) {
                    case 1: {
                        FlashlightButton.setText("Turn Off"); //changes the flashlight button on press
                        torchModeState(); //turns the phones LED on if torchmode is false
                        break;
                    }
                    case 2: {
                        torchModeState(); //turns the phones led off if torchmode is true

                        //reverts on second press to the original flashlight button
                        FlashlightButton.setText("Flash Light");
                        count = 0; //reset the button press count
                        break;
                    }
                }

            }
        });


        Button gpsBut = (Button) findViewById(R.id.gpsButton); //initializes the GPS button object
        assert gpsBut != null; //make sure button is initialized
        gpsBut.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                vibe.vibrate(60); //vibrate 60 ms upon press

                //on press initiates a new intent which constructs the list of buildings
                Intent intent = new Intent(getApplicationContext(), BuildingListActivity.class);

                startActivity(intent); //starts the intent once it is initialized
            }
        });

        //initialize campus directory button object
        Button directoryImage = (Button) findViewById(R.id.campusDirButton);
        assert directoryImage != null; // make sure button is properly initialized

        directoryImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vibe.vibrate(60); //vibrate upon button press for 60ms
                //initialize a directory list activity
                Intent intent = new Intent(getApplicationContext(), DirectoryActivity.class);
                startActivity(intent); //starts the activity
            }
        });

        //initializes a text box within the main activity, referenced in the content main XML
        final TextView countDownText = (TextView) findViewById(R.id.textView911);
        countdownTxt = (TextView) findViewById(R.id.textView911);
        countdownTxt.setText(""); //hides the text window when not needed
        nineOneBut = (Button) findViewById(R.id.button911); //initializes the 911 button object

        //initializes a countdown timer object
        final CountDownTimer Count = new CountDownTimer(10000, 1000) {

            //executes code on predetermined second tick
            public void onTick(long millisUntilFinished) {

                int seconds = (int) ((millisUntilFinished / 1000)); //10 seconds

                //vibrate for 60ms every second during the countdown
                vibe.vibrate(60);

                //redraws the textview every second during the countdown
                countDownText.setText(seconds + " seconds to auto-call 911! Otherwise, hit cancel. ");
                int colors[] = {0xff940c0c, 0xffffffff}; //colors of the textview background
                //initializes the gradient color background
                GradientDrawable gradientDrawable =
                        new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
                countdownTxt.setBackgroundDrawable(gradientDrawable); //draws the gradient background

            }

            /** dial911 creates a call intent, checks if it has the required permissions,
             * if true, the call intent goes through, if not an exception is thrown*/
            private void dial911(final String phoneNumber) {
                try {
                    Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + phoneNumber));
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(phoneIntent);
                } catch (Exception e) {
                }
            }

            /** when the 911 countdown reaches 0 the onfinish method sets a new textview,
            * checks for permissions to dial 911,
            * if permissions are true the dial911 method is activated,
            * otherwise exception is thrown */
            public void onFinish() {
                assert countDownText != null;
                countDownText.setText("DIALING 911!");
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    dial911("91");
                } catch (Exception e) {
                }
            }
        };


        nineOneBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vibe.vibrate(60); //phone vibrates for 60ms upon release of 911 button
                count2++;
                switch (count2) {
                    case 1: {
                        nineOneBut.setText("CANCEL!");//changes the 911 button to a cancel button upon press
                        Count.start(); //initiates the countdown on 911 button press
                        break;
                    }
                    case 2: {
                        Count.cancel(); //cancels the countdown
                        countDownText.setText(""); //nullifies the textview
                        nineOneBut.setText("911"); //reverts to original 911 button
                        count2 = 0; //resets the button press count
                        break;
                    }
                }
            }

        });
    }

    /** Determines which camera is the main camera. Will change the state off the main camera's LED */
    @TargetApi(Build.VERSION_CODES.M) //requires at least API build M/23
    private void torchModeState() {
        try {
            String[] cameraIdList = new String[0];
            //if the required build is available, determine the list of cameras in the phone (usually more than one)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                cameraIdList = manager.getCameraIdList();
            }
            CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(cameraIdList[0]);
            //if highest priority camera available turn on or off the LED on the phone
            if (cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                torchMode = !torchMode;
                manager.setTorchMode(cameraIdList[0], torchMode);

            } else {
                //if second camera available turn on or off LED on phone
                CameraCharacteristics cameraCharacteristics1 = manager.getCameraCharacteristics(cameraIdList[1]);
                if (cameraCharacteristics1.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                    torchMode = !torchMode;
                    manager.setTorchMode(cameraIdList[1], torchMode);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /**onPause make sure the phones camera hardware is no longer in use*/
    @TargetApi(Build.VERSION_CODES.M) //requires at least API build M/23
    @Override
    protected void onPause() {
        super.onPause();
        if ((manager != null) && (mtorchCallback != null)) {
            manager.unregisterTorchCallback(mtorchCallback);
        }
    }

    /**gets back control of phones camera hardware*/
    @TargetApi(Build.VERSION_CODES.M) //requires at least API build M/23
    @Override
    protected void onResume() {
        super.onResume();
        if ((manager != null) && (mtorchCallback != null)) {
            manager.registerTorchCallback(mtorchCallback, null);
        }
    }

    /** A callback for camera flash torch modes becoming unavailable, disabled, or enabled.*/
    @TargetApi(Build.VERSION_CODES.M)//requires at least API build M/23
    class torchCallback extends CameraManager.TorchCallback {
        @Override
        public void onTorchModeUnavailable(String cameraId) {
            super.onTorchModeUnavailable(cameraId);
            Log.e("onTorchModeUnavailable", "CameraID:" + cameraId);
        }

        @Override
        public void onTorchModeChanged(String cameraId, boolean enabled) {
            super.onTorchModeChanged(cameraId, enabled);
            Log.e("onTorchModeChanged", "CameraID:" + cameraId + " TorchMode : " + enabled);
        }
    }

    /** Inflate the menu; this adds items to the action bar if it is present.*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /** Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           // return true;

            Toast toast =Toast.makeText(this,"Developed by:\n Steven Sobanski \n David Morgan \n David Krajewski \n Beta V1.111111111",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

        return super.onOptionsItemSelected(item);
    }


}



