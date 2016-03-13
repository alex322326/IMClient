package com.example.administrator.imclient.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class BaseActivity extends Activity {
	public MyApplication myapp;
	public Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myapp = (MyApplication) getApplication();
	}
}
