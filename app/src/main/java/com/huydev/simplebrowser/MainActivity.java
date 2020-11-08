package com.huydev.simplebrowser;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.huydev.simplebrowser.common.Utils;
import com.huydev.simplebrowser.controller.BrowserFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private BrowserFragment browserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        browserFragment = new BrowserFragment();
        Utils.addFragment(
                getSupportFragmentManager(),
                "",
                R.id.root,
                browserFragment
        );
    }

    @Override
    public void onBackPressed() {
        browserFragment.back();
    }
}