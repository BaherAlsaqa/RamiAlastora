package com.ramialastora.ramialastora.RamiActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ramialastora.ramialastora.CustomViews.CustomTypefaceSpan;
import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.RamiFragments.FavoriteFragment;
import com.ramialastora.ramialastora.RamiFragments.LatestNewsFragment;
import com.ramialastora.ramialastora.RamiFragments.LeaguesFragment;
import com.ramialastora.ramialastora.RamiFragments.MainFragment;
import com.ramialastora.ramialastora.RamiFragments.NotificationFragment;
import com.ramialastora.ramialastora.RamiFragments.SearchFragment;
import com.ramialastora.ramialastora.classes.responses.UserResp;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.App;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.ramialastora.ramialastora.utils.LocaleManager;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ramialastora.ramialastora.RamiFragments.MatchDetailsFragment.BACK_FRAGMENTS;

public class RamiMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navView;
    private ActionBarDrawerToggle mDrawerToggle;
    private AppBarLayout appBarLayout;
    private View bottomSheetView = null;
    private BottomSheetBehavior<View> sheetBehavior;
    private Calendar calendar = null;
    private CalendarView calendarView;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;
    public static int BACK_FROM_SCORERS = 0;
    public static String PLAYER_OR_TEAM_NAME;
    public static String FROM_BACK = "";

    // for the sake of simplicity. use DI in real apps instead
    public static LocaleManager localeManager;

    @Override
    protected void attachBaseContext(Context base) {
        localeManager = new LocaleManager(base);
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(RamiMain.this);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("ar"));
        res.updateConfiguration(conf, dm);

        try {
            setContentView(R.layout.activity_rami_main);
        } catch (Exception e) {
            e.printStackTrace();
            setNewLocale();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);
        appBarLayout = findViewById(R.id.appbarlayout);
        bottomSheetView = findViewById(R.id.bottom_sheet);
        calendarView = findViewById(R.id.calendarView);

        appSharedPreferences = new AppSharedPreferences(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(Constants.Log + "token", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            token = Objects.requireNonNull(task.getResult()).getToken();
                        } else {
                            token = (task.getResult()).getToken();
                        }
                        // Log
                        Log.d(Constants.Log, "token = " + token);
                        boolean firstopen = appSharedPreferences.readBoolean(Constants.firstopen);
                        if (!firstopen) {
                            Log.d(Constants.Log, "is first open");
                            createorUpdateUser(1, 0, token);
                            appSharedPreferences.writeBoolean(Constants.firstopen, true);
                            favoriteDialog(RamiMain.this);
                        } else {
                            Log.d(Constants.Log, "is not first open");
                            int id = appSharedPreferences.readInteger(Constants.userid);
                            Log.d(Constants.Log, "user id = " + id + "");
                            createorUpdateUser(2, id, token);
                        }
                    }
                });

        bottomSheet();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String day = null;
                switch (dayOfWeek) {
                    case 1:
                        day = "الاحد";
                        break;
                    case 2:
                        day = "الاثنين";
                        break;
                    case 3:
                        day = "الثلاثاء";
                        break;
                    case 4:
                        day = "الاربعاء";
                        break;
                    case 5:
                        day = "الخميس";
                        break;
                    case 6:
                        day = "الجمعة";
                        break;
                    case 7:
                        day = "السبت";
                        break;
                }
                String dmy = dayOfMonth + "-" + (month + 1) + "-" + year + " / " + day;
                MainFragment mainFragment = new MainFragment();
                FragmentTransaction fragmentTransaction;
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, mainFragment.newInstance(dmy));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                expandCloseSheet(0);
            }
        });

        hideStatus(RamiMain.this);
//        setLightStatusBar(drawerLayout, RamiMain.this);

        drawerIcon(R.drawable.ic_menu);

        //TODO ////// change on toolbar ////////////////
//        toolbarLogo.setVisibility(View.GONE);
//        toolbarAppTitle.setText(R.string.menu_notification);

        //An another way to add notification_badge in drawer item
        /*Menu menuNav = navView.getMenu();
        MenuItem element = menuNav.findItem(R.id.nav_notification);
        MenuItem element1 = menuNav.findItem(R.id.nav_latest_news);
        String before = element.getTitle().toString();
        String before1 = element1.getTitle().toString();

        String counter = Integer.toString(3);
        String counter1 = Integer.toString(8);
        String s = before + "                         "+counter+"";
        String s1 = before1 + "                         "+counter1+"";
        SpannableString sColored = new SpannableString( s );
        SpannableString sColored1 = new SpannableString( s1 );

        sColored.setSpan(new ForegroundColorSpan(Color.parseColor("#E0BD55")), s.length()-3, s.length(), 0);
        element.setTitle(sColored);

        sColored1.setSpan(new ForegroundColorSpan(Color.parseColor("#00B8A9")), s1.length()-3, s1.length(), 0);
        element1.setTitle(sColored1);*/

        /*ConstraintLayout constraintLayout = view.findViewById(R.id.constraint_layout);
        ConstraintLayout constraintLayout1 = view1.findViewById(R.id.constraint_layout);*/

        /*{
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                Log.d("drawer", "close");
                getSupportActionBar().setTitle("closed");
                String badge = "99";
                String badge1 = "88";
                notificationBadge.setText(badge);
                latestnewsBadge.setText(badge1);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("drawer", "open");
                getSupportActionBar().setTitle("opened");
                String badge = "99";
                String badge1 = "88";
                notificationBadge.setText(badge);
                latestnewsBadge.setText(badge1);
            }
        };*/

        badgeandCheckedDrawer();
        navView.getMenu().getItem(0).setChecked(true);

        Menu m = navView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }

    private void setNewLocale() {
        App.localeManager.setNewLocale(this, "ar");

        Intent i = new Intent(this, RamiMain.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

//            System.exit(0);
    }

    private void favoriteDialog(Context context) {
        final Dialog dialog1 = new Dialog(context, R.style.AppTheme);
        dialog1.show();
        dialog1.setContentView(R.layout.favorite_dialog_style);
        dialog1.setCancelable(false);
        hideStatus((Activity) context);

        // with out background
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final ConstraintLayout constraintLayout = dialog1.findViewById(R.id.clayout);
        /*constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction;
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new FavoriteFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                drawerLayout.closeDrawers();
                dialog1.dismiss();
            }
        });*/

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_UP:
                        constraintLayout.setBackground(getResources().getDrawable(R.drawable.shape_background_favorite_start));
                        FragmentTransaction fragmentTransaction;
                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, new FavoriteFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        dialog1.dismiss();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        constraintLayout.setBackground(getResources().getDrawable(R.drawable.shape_background_favorite_start1));
                        break;
                }
                return true;
            }
        });
    }

    public void bottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                Log.d(Constants.Log + "sheet", "onStateChanged");
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                Log.d(Constants.Log + "sheet", "onSlide");
            }
        });
    }

    public void expandCloseSheet(int close) {
        if (close == 0) {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        } else if (close == 1) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void badgeandCheckedDrawer() {
        /*navView.getMenu().getItem(1).setActionView(R.layout.notification_badge);
        navView.getMenu().getItem(5).setActionView(R.layout.latest_news_badge);*/
//        navView.getMenu().getItem(0).setChecked(true);
        Log.d(Constants.Log + "menu", "badgeandCheckedDrawer");
    }

    public void drawerIcon(int icon) {

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(icon);
        navView.setNavigationItemSelectedListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(false);

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConstraintLayout constraintLayout;
                ConstraintLayout constraintLayout1;

                constraintLayout = (ConstraintLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.notification_badge, null);
                constraintLayout1 = (ConstraintLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.latest_news_badge, null);

                TextView notificationBadge = constraintLayout.findViewById(R.id.notificationbadge);
                TextView latestnewsBadge = constraintLayout1.findViewById(R.id.latestnewsbadge);

                Log.d("ff", notificationBadge.getText().toString() + " | " + latestnewsBadge.getText().toString());
                String badge = "9";
                String badge1 = "8";
                notificationBadge.setText(badge);
                latestnewsBadge.setText(badge1);
                Log.d("df", notificationBadge.getText().toString());

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_search, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                expandCloseSheet(1);
                BACK_FRAGMENTS = 1;
                getSupportFragmentManager().popBackStack(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);//Clear from back stack
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new SearchFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.action_back:
                Log.d(Constants.Log + "back", "case R.id.action_back: || Main");
                onBackPressed();
                if (BACK_FROM_SCORERS == 1) {
                    menuBackIcon(R.menu.toolbar_back, R.string.menu_scorer, "", 0);
                    navView.getMenu().getItem(3).setChecked(true);
                } else {
                    menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
                    checkMainFragmentOnDrawer();
                }
                badgeandCheckedDrawer();
                changeToolbarBackground(R.drawable.back_toolbar);
                drawerIcon(R.drawable.ic_menu);
                // to deselected on background item in sort participating teams
//                TEAM_ID = 0;
                break;
            case R.id.action_back_dark:
                Log.d(Constants.Log + "back", "case R.id.action_back_dark: || Main");
                String back = appSharedPreferences.readString(Constants.backFragmentCurrent);
                if (back.equalsIgnoreCase(Constants.mainF)) {
                    Log.d(Constants.Log + "menuteam", "(Constants.mainF)");
                    onBackPressed();
                    menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
                    badgeandCheckedDrawer();
                    changeToolbarBackground(R.drawable.back_toolbar);
                    drawerIcon(R.drawable.ic_menu);
                } else if (back.equalsIgnoreCase(Constants.leagueF)) {
                    Log.d(Constants.Log + "menuteam", "(Constants.leagueF)");
                    getSupportFragmentManager().popBackStack(null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE);//Clear from back stack
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, new MainFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
                    badgeandCheckedDrawer();
                    changeToolbarBackground(R.color.white);
                    drawerIcon(R.drawable.ic_menu_dark);
                } else if (back.equalsIgnoreCase(Constants.teamF)) {
                    Log.d(Constants.Log + "menuteam", "(Constants.teamF)");
                    onBackPressed();
                    menuBackIcon(R.menu.toolbar_back, 0, PLAYER_OR_TEAM_NAME, 3);
                    badgeandCheckedDrawer();
                    changeToolbarBackground(R.drawable.back_team_up);
                    drawerIcon(R.drawable.ic_menu);
                } else if (back.equalsIgnoreCase(Constants.playerF)) {
                    Log.d(Constants.Log + "menuteam", "(Constants.playerF)");
                    onBackPressed();
                    if (FROM_BACK.equals(Constants.search)) {
                        menuBackIcon(R.menu.toolbar_back, R.string.search, "", 0);
                        FROM_BACK = "";
                    } else {
                        menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
                    }
                    badgeandCheckedDrawer();
                    changeToolbarBackground(R.drawable.back_toolbar);
                    drawerIcon(R.drawable.ic_menu);
                } else {
                    Log.d(Constants.Log + "menuteam", "(else)");
                    onBackPressed();
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.toolbar_back);
                    drawerIcon(R.drawable.ic_menu);
                }
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void checkMainFragmentOnDrawer() {
        navView.getMenu().getItem(0).setChecked(true);
    }

    //change drawer font
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/thesans_bold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public static void setLightStatusBar(View view, Activity activity) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void hideStatus(Activity context) {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(context, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(context, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            context.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        expandCloseSheet(1);
        FragmentTransaction fragmentTransaction = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_main:
                getSupportFragmentManager().popBackStack(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);//Clear from back stack
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new MainFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_notification:
                Log.d(Constants.Log + "nav", "R.id.nav_notification");
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new NotificationFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                drawerLayout.closeDrawers();
                navView.getMenu().getItem(1).setChecked(true);
                BACK_FRAGMENTS = 1;
                break;
            case R.id.nav_favorite:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new FavoriteFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                drawerLayout.closeDrawers();
                navView.getMenu().getItem(2).setChecked(true);
                BACK_FRAGMENTS = 1;
                break;
            case R.id.nav_scorer:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new LeaguesFragment().newInstance(Constants.scorers));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                drawerLayout.closeDrawers();
                navView.getMenu().getItem(3).setChecked(true);
                BACK_FRAGMENTS = 1;
                break;
            case R.id.nav_league:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new LeaguesFragment().newInstance(Constants.pTeams));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                drawerLayout.closeDrawers();
                navView.getMenu().getItem(4).setChecked(true);
                BACK_FRAGMENTS = 1;
                break;
            case R.id.nav_latest_news:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new LatestNewsFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                drawerLayout.closeDrawers();
                navView.getMenu().getItem(5).setChecked(true);
                BACK_FRAGMENTS = 1;
                break;
            /*case R.id.nav_setting:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new SettingsFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                drawerLayout.closeDrawers();
                navView.getMenu().getItem(6).setChecked(true);
                break;*/
            case R.id.nav_facebook:
                Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_twitter:
                Toast.makeText(this, "Twitter", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_logout:
                finish();
                drawerLayout.closeDrawers();
                break;
        }

        return true;
    }

    public void menuBackIcon(int menu, int resStringId, String leagueName, int ramiMain) {
        ConstraintLayout constraintLayout = findViewById(R.id.cl);
        ConstraintLayout cLNewsDetails = findViewById(R.id.clnewsdetails);
        ConstraintLayout clshare = findViewById(R.id.clshare);
        TextView toolbarAppTitle = findViewById(R.id.apptitle);
        TextView toolbarAppTitle2 = findViewById(R.id.apptitle2);
        TextView toolbarCreatedDate = findViewById(R.id.createddatevalue);
        TextView toolbarshare = findViewById(R.id.share);
        Log.d("menu321", "icon : " + menu);
        try {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //////////////////////////
        if (ramiMain == 0) {
            constraintLayout.setVisibility(View.GONE);
            toolbarAppTitle2.setVisibility(View.VISIBLE);
            toolbarAppTitle2.setText(resStringId);
            cLNewsDetails.setVisibility(View.GONE);
            clshare.setVisibility(View.GONE);
        } else if (ramiMain == 1) {
            constraintLayout.setVisibility(View.VISIBLE);
            toolbarAppTitle.setText(resStringId);
            toolbarAppTitle2.setVisibility(View.GONE);
            cLNewsDetails.setVisibility(View.GONE);
            clshare.setVisibility(View.GONE);
        } else if (ramiMain == 2) {
            Log.d("asd", "}else if (ramiMain == 2){");
            toolbarAppTitle2.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.GONE);
            if (resStringId != 0) {
                toolbarCreatedDate.setText(resStringId);
                cLNewsDetails.setVisibility(View.VISIBLE);
                clshare.setVisibility(View.GONE);
            } else {
                cLNewsDetails.setVisibility(View.GONE);
                clshare.setVisibility(View.VISIBLE);
            }

        } else if (ramiMain == 3) {
            Log.d("asd", "}else if (ramiMain == 3){");
            constraintLayout.setVisibility(View.GONE);
            toolbarAppTitle2.setVisibility(View.VISIBLE);
            toolbarAppTitle2.setText(leagueName);
            cLNewsDetails.setVisibility(View.GONE);
            clshare.setVisibility(View.GONE);
        }
    }

    public void changeToolbarBackground(int b) {
        appBarLayout.setBackgroundResource(b);
    }

    private void createorUpdateUser(int type, int user_id, String token) {
        Log.d(Constants.Log, "createUser");
        Call<UserResp> call = null;
        if (type == 1) {
            call = apiInterface.createUser(
                    getString(R.string.api_key),
                    getString(R.string.api_username),
                    getString(R.string.api_password),
                    getString(R.string.content_type),
                    token);
        } else if (type == 2) {
            call = apiInterface.updateToken(
                    getString(R.string.api_key),
                    getString(R.string.api_username),
                    getString(R.string.api_password),
                    getString(R.string.content_type),
                    user_id,
                    token);
        }
        call.enqueue(new Callback<UserResp>() {
            @Override
            public void onResponse(Call<UserResp> call, Response<UserResp> response) {

                Log.d(Constants.Log, " | Code = " + response.body());
                UserResp resource = response.body();
                if (response.code() == 201) {
                    String code = response.code() + "";
                    int id = resource.getData().getId();
                    String token = resource.getData().getToken();

                    appSharedPreferences.writeInteger(Constants.userid, id);

                    Log.d(Constants.Log, "Status = " + resource.getStatus() + " | Code = " + code);
                    Log.d(Constants.Log, "id = " + id + " | Token = " + token);

                } else {
                    String code = response.code() + "";
                    Log.d(Constants.Log, "create user error");
                    Log.d(Constants.Log, "Code = " + code);
                }


            }

            @Override
            public void onFailure(Call<UserResp> call, Throwable t) {
                if (t != null) {
                    Log.d(Constants.Log, "onFailure = " + t.getMessage());
                }
                call.cancel();
            }
        });
    }

}
