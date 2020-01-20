package com.ramialastora.ramialastora.RamiActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.interfaces.Constants;

import static com.ramialastora.ramialastora.RamiActivities.RamiMain.hideStatus;

public class OutLink extends AppCompatActivity {

    private Button gotoVideo;
    private String liveURL;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_link);

        gotoVideo = findViewById(R.id.gotovideo);
        toolbar = findViewById(R.id.toolbar);

        Intent intent = getIntent();
        if (intent != null){
            liveURL = intent.getStringExtra(Constants.link);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        hideStatus(OutLink.this);


        gotoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OutLink.this, liveURL, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(liveURL));
                startActivity(intent);
            }
        });

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
