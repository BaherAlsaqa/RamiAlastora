package com.ramialastora.ramialastora.RamiActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.interfaces.Constants;

import static com.ramialastora.ramialastora.RamiActivities.RamiMain.hideStatus;

public class WebViewVideo extends AppCompatActivity {

    private WebView webView;
    private View view;
    private String liveURL;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    @SuppressLint({"SetJavaScriptEnabled", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_video);

        appBarLayout = findViewById(R.id.appbarlayout);
        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.viewvideo);
        Intent intent = getIntent();
        if (intent != null){
            liveURL = intent.getStringExtra(Constants.link);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        hideStatus(WebViewVideo.this);

//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);

//        MyWebViewClient webViewClient = new MyWebViewClient(getActivity());
        webView.setWebViewClient(new MyWebViewClient(WebViewVideo.this));

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(liveURL);

        /*//TODO ///// Start back one fragment ////////
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.d(Constants.Log+"back", "if( keyCode == KeyEvent.KEYCODE_BACK ) || details match details");
                    ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
                    ((RamiMain) getActivity()).changeToolbarBackground(R.color.white);
                    ((RamiMain) getActivity()).setLightStatusBar(view, getActivity());
                    ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu_dark);
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);//back on main
                    assert getFragmentManager() != null;
                    getFragmentManager().popBackStack();//back one fragment
                    return true;
                }
                return false;
            }
        });
        //TODO ///////////End back/////////////*/
    }

    private class MyWebViewClient extends WebViewClient {

        private Activity activity = null;

        public MyWebViewClient(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
            /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);*/
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_back, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(this, getString(R.string.search), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_back:
                Log.d(Constants.Log+"back", "case R.id.action_back: || Main");
                onBackPressed();
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.toolbar_search);
                // to deselected on background item in sort participating teams
//                TEAM_ID = 0;
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
