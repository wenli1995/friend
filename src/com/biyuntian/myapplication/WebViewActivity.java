package com.biyuntian.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

public class WebViewActivity extends Activity{

	private WebView webview;
	private Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		webview=(WebView)findViewById(R.id.webview);
		btn=(Button)findViewById(R.id.back3);
		Intent intent=getIntent();
		String url=intent.getStringExtra("url");
		webview.loadUrl(url);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
	}

}
