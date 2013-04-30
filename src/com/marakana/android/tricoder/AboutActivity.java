package com.marakana.android.tricoder;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends Activity {
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/about.html");
	}

}
