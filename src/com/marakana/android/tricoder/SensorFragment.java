package com.marakana.android.tricoder;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SensorFragment extends Fragment {
	private static TextView textGravity, textMagnetic, textSensors;
	private SensorManager sensorManager;
	private Sensor sensorGravity, sensorMagnetic;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sensor, null);
		textMagnetic = (TextView) view.findViewById(R.id.text_magnetic);
		textGravity = (TextView) view.findViewById(R.id.text_gravity);
		textSensors = (TextView) view.findViewById(R.id.text_sensors);
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		sensorManager = (SensorManager) getActivity().getSystemService(
				Context.SENSOR_SERVICE);

		int sensors = sensorManager.getSensorList(Sensor.TYPE_ALL).size();
		textSensors.setText(sensors+" sensors");

		sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		sensorMagnetic = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		updateView(null);
	}

	@Override
	public void onStart() {
		super.onStart();
		sensorManager.registerListener(LISTENER, sensorGravity,
				SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(LISTENER, sensorMagnetic,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onStop() {
		super.onStop();
		sensorManager.unregisterListener(LISTENER);
	}

	private static final SensorEventListener LISTENER = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			updateView(event);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	private static void updateView(SensorEvent event) {
		if (event == null) {
			textGravity.setText("Gravity: Unknown");
			textMagnetic.setText("Magnetic Field: Unknown");
			return;
		}

		switch (event.sensor.getType()) {
		case Sensor.TYPE_GRAVITY:
			textGravity.setText(String.format(
					"Gravity: X: %.3f   Y: %.3f   Z: %.3f", event.values[0],
					event.values[1], event.values[2]));
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			textMagnetic.setText(String.format("Magnetic Field: %.3f",
					event.values[0]));

		}

	}
}
