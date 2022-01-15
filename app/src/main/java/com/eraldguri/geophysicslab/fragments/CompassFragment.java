package com.eraldguri.geophysicslab.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eraldguri.geophysicslab.R;

public class CompassFragment extends Fragment implements SensorEventListener {

    private TextView azimuthAngle;
    private ImageView imageAzimuth;

    private SensorManager mSensorManager;
    private Sensor mRotationV;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    float[] rotationMatrix = new float[9];
    float[] orientation = new float[9];
    private int mAzimuth;
    private final float[] mLastAccelerometer = new float[3];
    private final float[] mLastMagnetometer = new float[3];
    private boolean haveSensor = false;
    private boolean haveSensor2 = false;
    private final boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_compass, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        azimuthAngle = view.findViewById(R.id.tvAzimuthAngle);
        imageAzimuth = view.findViewById(R.id.imgCompass);

        mSensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);

        start();
    }

    private void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null
                    || mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
                noSensor();
            } else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        } else {
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop() {
        if (haveSensor && haveSensor2) {
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        } else {
            if (haveSensor) {
                mSensorManager.unregisterListener(this, mRotationV);
            }
        }
    }

    private void noSensor() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setMessage(R.string.compass_not_supported)
                .setCancelable(false)
                .setNegativeButton("Close", (dialogInterface, i) -> requireActivity().finish());
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);
            mAzimuth = (int)((Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[0]) + 360) % 360);
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(sensorEvent.values, 0, mLastAccelerometer, 0, sensorEvent.values.length);
            mLastMagnetometerSet = true;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(sensorEvent.values, 0, mLastMagnetometer, 0, sensorEvent.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastMagnetometerSet && mLastAccelerometerSet) {
            SensorManager.getRotationMatrix(rotationMatrix, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rotationMatrix, orientation);
            mAzimuth = (int)((Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[0]) + 360) % 360);
        }

        mAzimuth = Math.round(mAzimuth);
        imageAzimuth.setRotation(-mAzimuth);

        String where = "NO";
        if (mAzimuth >= 350 || mAzimuth <= 10) {
            where = "N";
        }
        if (mAzimuth < 350 && mAzimuth > 280) {
            where = "NW";
        }
        if (mAzimuth <= 280 && mAzimuth > 260) {
            where = "W";
        }
        if (mAzimuth <= 260 && mAzimuth > 190) {
            where = "SW";
        }
        if (mAzimuth <= 190 && mAzimuth > 170) {
            where = "S";
        }
        if (mAzimuth <= 170 && mAzimuth > 100) {
            where = "SE";
        }
        if (mAzimuth <= 100 && mAzimuth > 80) {
            where = "E";
        }
        if (mAzimuth <= 80 && mAzimuth > 10) {
            where = "NE";
        }

        azimuthAngle.setText(mAzimuth + " " + where);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPause() {
        super.onPause();

        stop();
    }

    @Override
    public void onResume() {
        super.onResume();

        start();
    }
}