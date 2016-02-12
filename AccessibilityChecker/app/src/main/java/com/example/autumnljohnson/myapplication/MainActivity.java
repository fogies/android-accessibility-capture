package com.example.autumnljohnson.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intentToAccessibility = new Intent(this, AccessibilityTestService.class); // this was me

        this.findViewById(R.id.setPackageButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                stopService(intentToAccessibility); // this was me
                EditText mEdit = (EditText)findViewById(R.id.packageName);
                String packageName = mEdit.getText().toString();
                intentToAccessibility.putExtra("packageName", packageName);
                startService(intentToAccessibility); // this was me
            }
        });

        this.findViewById(R.id.accessibilitySettingButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
