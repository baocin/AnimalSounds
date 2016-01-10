package com.github.baocin.animalsounds;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import im.delight.android.baselib.Screen;
import im.delight.android.baselib.UI;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    static boolean lockedOrientation = false;
    public int numCorrect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        Intent intent = new Intent(this, AudioService.class);
//        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh_button) {
            UI.restartActivity(this);
            return true;
        }
        if (id == R.id.lock_orientation){
            if (lockedOrientation){
                Toast.makeText(this.getApplicationContext(), "Unlocked Orientation", Toast.LENGTH_SHORT).show();
                Screen.unlockOrientation(this);
            }else {
                Screen.lockOrientation(this);
                Log.d(TAG, "Orientation: " + getResources().getConfiguration().orientation);
                switch (getResources().getConfiguration().orientation) {
                    case Configuration.ORIENTATION_LANDSCAPE:
                        Toast.makeText(this.getApplicationContext(), "Locked in Landscape", Toast.LENGTH_SHORT).show();
                        break;
                    case Configuration.ORIENTATION_PORTRAIT:
                        Toast.makeText(this.getApplicationContext(), "Locked in Portrait", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            lockedOrientation = !lockedOrientation;
        }

        return super.onOptionsItemSelected(item);
    }
}
