package com.marakana.android.tricoder;

import java.util.List;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RadioFragment extends Fragment {
	private static TextView textConfiguredNetworks, textScanResults;
	private static Button buttonScan;
	private static WifiManager wifiManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_radio, null);

		// Find the views
		textConfiguredNetworks = (TextView) view
				.findViewById(R.id.text_configured_networks);
		textScanResults = (TextView) view.findViewById(R.id.text_scan_results);
		buttonScan = (Button) view.findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				wifiManager.startScan();
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get the wifi manager
		wifiManager = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);

		// update the configured networks
		updateConfiguredNetworks(wifiManager.getConfiguredNetworks());

	}

	private static final IntentFilter SCAN_FILTER = new IntentFilter(
			WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

	@Override
	public void onStart() {
		super.onStart();
		getActivity().registerReceiver(SCAN_RECEIVER, SCAN_FILTER);
	}

	@Override
	public void onStop() {
		super.onStop();
		getActivity().unregisterReceiver(SCAN_RECEIVER);
	}

	private static final int MAX_RESULTS = 3;

	private void updateConfiguredNetworks(
			List<WifiConfiguration> configuredNetworks) {
		if (configuredNetworks == null || configuredNetworks.size() == 0)
			return;
		
		textConfiguredNetworks.setText("Configured Networks ("
				+ configuredNetworks.size() + "): ");

		int count = 0;
		for (WifiConfiguration network : configuredNetworks) {
			if (++count > MAX_RESULTS) {
				textConfiguredNetworks.append("\n...");
				break;
			}
			textConfiguredNetworks.append("\n" + network.SSID);
		}
	}

	private static final BroadcastReceiver SCAN_RECEIVER = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			List<ScanResult> scanResults = wifiManager.getScanResults();
			textScanResults.setText("Scan Results (" + scanResults.size()
					+ "): ");

			int count = 0;
			for (ScanResult result : scanResults) {
				if (++count > MAX_RESULTS) {
					textScanResults.append("\n...");
					break;
				}
				textScanResults.append(String.format("\n%s @%dMHz (%d dBm)",
						result.SSID, result.frequency, result.level));
			}
		}
	};
}
