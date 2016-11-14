package com.example.codecaboose.testing1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//Can simulate button click if sensor wont work by changing the "variable"
public class MainActivity extends AppCompatActivity implements SensorEventListener{

    Button thing;
    int temp = 0;
    Button start;
    ImageView img;
    int reset = 0;
    int successCatch = 0;
    int running = 0; //Flag for if the cast has been set

    //Sensor related variables
    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInitialized = false;
        start = (Button) findViewById(R.id.button);
        img = (ImageView) findViewById(R.id.imageView);
        thing = (Button) findViewById(R.id.button2);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this,
                mAccelerometer ,  SensorManager.SENSOR_DELAY_NORMAL);
        //This is where a rod cast forward should be detected
        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (running == 0) {
                    running = 1;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //This will be the first promt to move the rod to the left or right
                            if (temp == 1) {

                                img.setImageResource(R.drawable.rodCast);
                                temp = 0;
                                Handler handler = new Handler();

                                //Another prompt to move the rod
                                handler.postDelayed(new Runnable() {
                                    public void run() {

                                        if (temp == 1) {
                                            img.setImageResource(R.drawable.right);
                                            Handler handler = new Handler();
                                            temp = 0;
                                            //Another prompt to move the rod
                                            handler.postDelayed(new Runnable() {
                                                public void run() {

                                                    if (temp == 1) {
                                                        img.setImageResource(R.drawable.left);
                                                        temp = 0;
                                                        Handler handler = new Handler();
                                                        temp = 0;
                                                        //Prompt to pull rod in
                                                        handler.postDelayed(new Runnable() {
                                                            public void run() {

                                                                if (temp == 1) {

                                                                    img.setImageResource(R.drawable.eie);
                                                                    temp = 0;
                                                                    //This flag will enable a button for
                                                                    // the user to click in the case
                                                                    //of a successful catch
                                                                    successCatch = 1;

                                                                    Toast.makeText(MainActivity.this, "You won!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                                , 5000);

                                                    }
                                                }
                                            }
                                                    , 5000);


                                        }
                                    }
                                }
                                        , 5000);
                            } else
                                reset = 1;

                        }
                    }

                            , 5000);
                    thing.setOnClickListener(new View.OnClickListener()

                                             {
                                                 public void onClick(View v) {
                                                     temp = 1;

                                                 }

                                             }

                    );


                }
            }


            });





    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(successCatch == 1)
        {
            //Reset to fishless rod and ready the game on screen click
            img.setImageResource(R.drawable.imger);

        }

        return false;
    }


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,
                mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can be safely ignored for this demo
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            mInitialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE) deltaX = (float)0.0;
            if (deltaY < NOISE) deltaY = (float)0.0;
            if (deltaZ < NOISE) deltaZ = (float)0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;


            if(deltaZ!=0) //change to delta Y or X to check other axis
            {
                Log.i("something", "x: "+ Float.toString(deltaX));
                Log.i("something", "y: "+ Float.toString(deltaY));
                Log.i("something", "z: "+ Float.toString(deltaZ));

                if(deltaZ >= 10)
                    start.performClick();
            }

            if(deltaX>=5)
                thing.performClick();


            if (deltaX > deltaY) {
                Log.i("something", "horizontal");
                //  iv.setImageResource(R.drawable.horizontal);
            } else if (deltaY > deltaX) {
                Log.i("something", "vertical");
                // iv.setImageResource(R.drawable.vertical);
            } else {

            }
        }
    }



}
