package com.marakana.android.tricoder;

import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraFragment extends Fragment {
	private FrameLayout container;
	private Camera camera;
	private CameraPreview cameraPreview;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_camera, null);

		this.container = (FrameLayout) view.findViewById(R.id.camera_preview);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	
	@Override
	public void onStart() {
		super.onStart();
		camera = Camera.open();
		camera = (camera==null) ? Camera.open(0) : camera;
		if (camera == null || container == null) {
			Toast.makeText(getActivity(), "Can't open the camera",
					Toast.LENGTH_LONG).show();
			return;
		}
		cameraPreview = new CameraPreview(getActivity(), camera);
		container.addView(cameraPreview, 0);
	}
	
	@Override
	public void onStop() {
		super.onStop();
        if (this.camera != null) {
            this.camera.lock(); // unnecessary in API >= 14
            this.camera.stopPreview();
            this.camera.release();
            this.camera = null;
            this.container.removeView(this.cameraPreview);
        }

	}
}
