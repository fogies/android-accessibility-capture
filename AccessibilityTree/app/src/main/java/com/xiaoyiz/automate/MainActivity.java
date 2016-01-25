package com.xiaoyiz.automate;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

	public static void openApp(Context context, String packageName) {
		PackageManager manager = context.getPackageManager();
		Intent i = manager.getLaunchIntentForPackage(packageName);
		if (i == null) return;
		i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.findViewById(R.id.activeButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent killIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(killIntent);
            }
        });

		final Context ctxt = this;
		this.findViewById(R.id.button_Close5).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MyAccessibilityService.appName = "Close5";
				MyAccessibilityService.startSell = true;
				MyAccessibilityService.step = -1;
				openApp(ctxt, "com.ecg.close5");
			}
		});

        this.findViewById(R.id.button_OfferUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MyAccessibilityService.appName = "OfferUp";
                MyAccessibilityService.startSell = true;
                MyAccessibilityService.step = -1;
                openApp(ctxt, "com.offerup");
            }
        });

		this.findViewById(R.id.button_connect).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Utility.sendJSON("http://10.0.2.2:8000", "Test");
			}
		});

	}
}
