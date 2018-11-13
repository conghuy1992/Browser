package com.conghuy.MyBrowser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.conghuy.MyBrowser.common.CommonWebViewFragment;
import com.conghuy.MyBrowser.common.Utils;
import com.conghuy.MyBrowser.controller.browser.BrowserFragment;

public class MainActivity extends AppCompatActivity {
    private BrowserFragment browserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        browserFragment = new BrowserFragment();
        Utils.addFragment(getSupportFragmentManager(),
                "",
                R.id.root,
                browserFragment);
    }

    @Override
    public void onBackPressed() {
        if (browserFragment.canGoBack()) browserFragment.goBack();
        else super.onBackPressed();
    }


}
