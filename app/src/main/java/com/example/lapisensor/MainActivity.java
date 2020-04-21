package com.example.lapisensor;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothHeadset bluetoothHeadset;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    MediaPlayer mp;

    TextView timer;
    int lux_value;

    private SensorManager sensorManager;
    private Sensor mLight;
    int c = 0;
    private FirebaseDatabase mFirebaseFirestore;
    private DatabaseReference mDatabaseReference;




    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = 0;
        timer = (TextView) findViewById(R.id.timer);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);



        mFirebaseFirestore = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseFirestore.getReference().child("lux");

        Timer tmr = new Timer();


        tmr.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                c++;
                System.out.println(c + " secs");
                setText(c);
                Lux l = new Lux(lux_value);
//                mDatabaseReference.push().setValue(l);
                mDatabaseReference.setValue(lux_value);

            }
        }, 100, 1000);



    }
    private void setText(final int p){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timer.setText(p+" secs");
            }
        });
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        float lux = event.values[0];
        TextView t = (TextView) findViewById(R.id.val);
//        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//        System.out.println(lux);
        t.setText(String.valueOf(lux));
        lux_value = Math.round(lux);
//        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {

        super.onResume();
        sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {

        super.onPause();
//        sensorManager.unregisterListener(this);
    }
}
