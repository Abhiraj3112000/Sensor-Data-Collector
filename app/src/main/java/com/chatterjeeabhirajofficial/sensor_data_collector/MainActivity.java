package com.chatterjeeabhirajofficial.sensor_data_collector;


import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView textViewGyroscope;
    private TextView textViewAccelerometer;
    private TextView textViewMagnetometer;
    private Button activate;
    private SensorManager sensorManagerGyroscope;
    private SensorManager sensorManagerAccelerometer;
    private SensorManager sensorManagerMagnetometer;
    public String currentDateAndTime = "";
    private static final String GYROSCOPE_FILE = "gyroscope_data.txt";
    private static final String ACCELEROMETER_FILE = "accelerometer_data.txt";
    private static final String MAGNETOMETER_FILE = "magnetometer_data.txt";
    private Sensor accelerometer;
    private boolean isaccelerometerAvailable, Acc_isFirstTime = true;
    private float Acc_lastX;
    private float Acc_lastY;
    private float Acc_lastZ;
    private Sensor gyroscope;
    private boolean isgyroscopeAvailable, gyroscope_isFirstTime = true;
    private float gyroscope_lastX;
    private float gyroscope_lastY;
    private float gyroscope_lastZ;
    private boolean clickedOn = true;
    private Sensor magnetometer;
    private boolean ismagnetometerAvailable, magnetometer_isFirstTime = true;
    private float magnetometer_lastX;
    private float magnetometer_lastY;
    private float magnetometer_lastZ;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewGyroscope = findViewById(R.id.textView);
        textViewAccelerometer = findViewById(R.id.textView2);
        textViewMagnetometer =findViewById(R.id.textView3);
        activate = findViewById(R.id.activate);
        //can also be done by using a single system service - done as specified in the meeting 
        //i.e to use different services
        sensorManagerGyroscope = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManagerAccelerometer = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManagerMagnetometer = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //gyroscope-
        if (sensorManagerGyroscope.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            gyroscope = sensorManagerGyroscope.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            isgyroscopeAvailable = true;
        } else {
            textViewGyroscope.setText("Gyroscope is not available");
            isgyroscopeAvailable = false;
        }

        //accelerometer-
        if (sensorManagerAccelerometer.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManagerAccelerometer.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isaccelerometerAvailable = true;
        } else {
            textViewAccelerometer.setText("Accelerometer is not available");
            isaccelerometerAvailable = false;
        }

        //magnetometer-
        if (sensorManagerMagnetometer.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            magnetometer = sensorManagerMagnetometer.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            ismagnetometerAvailable = true;
        } else {
            textViewAccelerometer.setText("Magnetometer is not available");
            ismagnetometerAvailable = false;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy  \nHH:mm:ss z\n\n");
        currentDateAndTime = "Date: " + sdf.format(new Date());
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        FileOutputStream fos=null;
        switch(sensorEvent.sensor.getType())
        {
            case Sensor.TYPE_GYROSCOPE:
                float currentX = sensorEvent.values[0];
                float currentY = sensorEvent.values[1];
                float currentZ = sensorEvent.values[2];
                if (gyroscope_isFirstTime) {
                    textViewGyroscope.setText("Gyroscope is active.");
                    try {
                        fos = openFileOutput(GYROSCOPE_FILE, MODE_PRIVATE);
                        fos.write((currentDateAndTime + currentX + "," + currentY + "," + currentZ + "\n").getBytes());
                        Toast.makeText(this, "Saved to" + getFilesDir() + "/" + GYROSCOPE_FILE, Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
                    if (gyroscope_lastX != currentX || gyroscope_lastY != currentY || gyroscope_lastZ != currentZ) {
                        textViewGyroscope.setText("Gyroscope is active.");
                        try {
                            fos = openFileOutput(GYROSCOPE_FILE, MODE_APPEND);
                            fos.write((currentX + ", " + currentY + ", " + currentZ + "\n").getBytes());
                            Toast.makeText(this, "Saved to" + getFilesDir() + "/" + GYROSCOPE_FILE, Toast.LENGTH_LONG).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                gyroscope_lastX = currentX;
                gyroscope_lastY = currentY;
                gyroscope_lastZ = currentZ;
                gyroscope_isFirstTime = false;
                break;

            case Sensor.TYPE_ACCELEROMETER:
                currentX = sensorEvent.values[0];
                currentY = sensorEvent.values[1];
                currentZ = sensorEvent.values[2];
                if (Acc_isFirstTime) {
                    try {
                        fos = openFileOutput(ACCELEROMETER_FILE, MODE_PRIVATE);
                        fos.write((currentDateAndTime + currentX + "," + currentY + "," + currentZ + "\n").getBytes());
                        Toast.makeText(this, "Saved to" + getFilesDir() + "/" + ACCELEROMETER_FILE, Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
                    if (Acc_lastX != currentX || Acc_lastY != currentY || Acc_lastZ != currentZ) {
                        try {
                            fos = openFileOutput(ACCELEROMETER_FILE, MODE_APPEND);
                            fos.write((currentX + ", " + currentY + ", " + currentZ + "\n").getBytes());
                            Toast.makeText(this, "Saved to" + getFilesDir() + "/" + ACCELEROMETER_FILE, Toast.LENGTH_LONG).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                Acc_lastX = currentX;
                Acc_lastY = currentY;
                Acc_lastZ = currentZ;
                Acc_isFirstTime = false;
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                currentX = sensorEvent.values[0];
                currentY = sensorEvent.values[1];
                currentZ = sensorEvent.values[2];
                if (magnetometer_isFirstTime) {
                    try {
                        fos = openFileOutput(MAGNETOMETER_FILE, MODE_PRIVATE);
                        fos.write((currentDateAndTime + currentX + "," + currentY + "," + currentZ + "\n").getBytes());
                        Toast.makeText(this, "Saved to" + getFilesDir() + "/" + MAGNETOMETER_FILE, Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
                    if (magnetometer_lastX != currentX || magnetometer_lastY != currentY || magnetometer_lastZ != currentZ) {
                        try {
                            fos = openFileOutput(MAGNETOMETER_FILE, MODE_APPEND);
                            fos.write((currentX + ", " + currentY + ", " + currentZ + "\n").getBytes());
                            Toast.makeText(this, "Saved to" + getFilesDir() + "/" + MAGNETOMETER_FILE, Toast.LENGTH_LONG).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                magnetometer_lastX = currentX;
                magnetometer_lastY = currentY;
                magnetometer_lastZ = currentZ;
                magnetometer_isFirstTime = false;
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @SuppressLint("SetTextI18n")
    public void activateSensor(View view) {
        // Do something in response to button click
        if(clickedOn) {

            //accelerometer-
            if (isaccelerometerAvailable) {
                sensorManagerAccelerometer.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                clickedOn = false;
                Log.d("activated:","Accelerometer");
                textViewAccelerometer.setText("Accelerometer is active.");
                activate.setText("Deactivate");
            }
            //gyroscope-
            if (isgyroscopeAvailable) {
                sensorManagerGyroscope.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                clickedOn = false;
                Log.d("activated:","Gyroscope");
                textViewGyroscope.setText("Gyroscope is active.");
                activate.setText("Deactivate");
            }
            //magnetometer-
            if (ismagnetometerAvailable) {
                sensorManagerMagnetometer.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
                clickedOn = false;
                Log.d("activated:","Magnetometer");
                textViewMagnetometer.setText("Magnetometer is active.");
                activate.setText("Deactivate");
            }
        }
        else
        {

            if (isaccelerometerAvailable) {
                sensorManagerAccelerometer.unregisterListener(this);
                textViewAccelerometer.setText("Accelerometer Is Deactivated");
                clickedOn = true;
                activate.setText("  Activate  ");
            }
            if(isgyroscopeAvailable)
            {
                sensorManagerGyroscope.unregisterListener(this);
                textViewGyroscope.setText("Gyroscope Is Deactivated");
                clickedOn=true;
                activate.setText("  Activate  ");
            }
            if(ismagnetometerAvailable)
            {
                sensorManagerMagnetometer.unregisterListener(this);
                textViewMagnetometer.setText("Magnetometer Is Deactivated");
                clickedOn=true;
                activate.setText("  Activate  ");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isaccelerometerAvailable) {
            sensorManagerAccelerometer.unregisterListener(this);
        }
        if (isgyroscopeAvailable) {
            sensorManagerGyroscope.unregisterListener(this);
        }
        if (ismagnetometerAvailable) {
            sensorManagerMagnetometer.unregisterListener(this);
        }
    }
}

