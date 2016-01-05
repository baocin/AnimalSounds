package com.github.baocin.animalsounds;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import im.delight.android.baselib.Screen;

public class MainActivity extends AppCompatActivity {
    static boolean lockedOrientation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        if (id == R.id.lock_orientation){
            if (lockedOrientation){
                Toast.makeText(this.getApplicationContext(), "Unlocked Orientation", Toast.LENGTH_SHORT).show();
                Screen.unlockOrientation(this);
            }else {
                Screen.lockOrientation(this);
                switch (getResources().getConfiguration().orientation) {
                    case Screen.Orientation.LANDSCAPE:
                    case Screen.Orientation.REVERSE_LANDSCAPE:
                        Toast.makeText(this.getApplicationContext(), "Locked in Landscape", Toast.LENGTH_SHORT).show();
                        break;
                    case Screen.Orientation.PORTRAIT:
                    case Screen.Orientation.REVERSE_PORTRAIT:
                        Toast.makeText(this.getApplicationContext(), "Locked in Portrait", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            lockedOrientation = !lockedOrientation;

        }

        return super.onOptionsItemSelected(item);
    }
}
