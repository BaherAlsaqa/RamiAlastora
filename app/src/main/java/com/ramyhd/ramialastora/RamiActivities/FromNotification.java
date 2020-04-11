package com.ramyhd.ramialastora.RamiActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ramyhd.ramialastora.CustomViews.CustomTypefaceSpan;
import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.RamiFragments.FavoriteFragment;
import com.ramyhd.ramialastora.RamiFragments.LatestNewsFragment;
import com.ramyhd.ramialastora.RamiFragments.LeaguesFragment;
import com.ramyhd.ramialastora.RamiFragments.MainFragment;
import com.ramyhd.ramialastora.RamiFragments.NotificationFragment;
import com.ramyhd.ramialastora.RamiFragments.SearchFragment;
import com.ramyhd.ramialastora.classes.responses.UserResp;
import com.ramyhd.ramialastora.interfaces.Constants;
import com.ramyhd.ramialastora.retrofit.APIClient;
import com.ramyhd.ramialastora.retrofit.APIInterface;
import com.ramyhd.ramialastora.utils.App;
import com.ramyhd.ramialastora.utils.AppSharedPreferences;
import com.ramyhd.ramialastora.utils.LocaleManager;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ramyhd.ramialastora.RamiActivities.SplashScreen.setLanguage;
import static com.ramyhd.ramialastora.RamiFragments.MatchDetailsFragment.BACK_FRAGMENTS;

public class FromNotification extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navView;
    private AppBarLayout appBarLayout;
    private View bottomSheetView = null;
    private BottomSheetBehavior<View> sheetBehavior;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;
    public static int BACK_FROM_SCORERS = 0;
    public static String PLAYER_OR_TEAM_NAME;
    public static String FROM_BACK = "";
//    private Calendar calendar = null;

    // for the sake of simplicity. use DI in real apps instead
    public static LocaleManager localeManager;
    String language;
    private boolean langState;
    Intent intent;

    /*@Override
    protected void attachBaseContext(Context base) {
        localeManager = new LocaleManager(base);
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(RamiMain.this);
    }*/

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        try {
            if (intent != null&& ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0)){
                String data = intent.getStringExtra("data");
                String id = intent.getStringExtra("newsId");
                assert data != null;
                if (data.equals("from_notification")){
                    Log.d(Constants.Log+"newsId", "rami main newsId = "+id);
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, new LatestNewsFragment().newInstance(id));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    BACK_FRAGMENTS = 1;
                }else{
                    Log.d(Constants.Log+"newsId", "rami main newsId != from_notification");
                }
            }else{
                Log.d(Constants.Log+"newsId", "rami main newsId intent == null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constants.Log+"newsId", "rami main newsId errorrrrrrrr");
        }

        /*Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        language = appSharedPreferences.readString(Constants.applanguage);
        conf.setLocale(new Locale(language));
        res.updateConfiguration(conf, dm);*/
        String currentLanguage = Resources.getSystem().getConfiguration().locale.getLanguage();
        Log.d(Constants.Log, "current language = " + currentLanguage);
        if (!currentLanguage.equalsIgnoreCase("ar"))
            setLanguage(FromNotification.this);

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
        CalendarView calendarView = findViewById(R.id.calendarView);

        appSharedPreferences = new AppSharedPreferences(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        langState = appSharedPreferences.readBoolean(Constants.langState);
        if (!langState) {
            if (!currentLanguage.equalsIgnoreCase("ar")) {
                Log.d(Constants.Log + "langstate", "langState = " + langState);
                setLanguage(FromNotification.this);
                appSharedPreferences.writeBoolean(Constants.langState, true);
                Intent intent = new Intent(FromNotification.this, SplashScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                getToken();
            }
        } else {
            getToken();
        }
        bottomSheet();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
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
        });

        hideStatus(FromNotification.this);
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

    private void getToken() {
        Log.d(Constants.Log + "langstate", "langState = " + langState);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(Constants.Log + "token", "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token;
                    token = Objects.requireNonNull(task.getResult()).getToken();
                    // Log
                    Log.d(Constants.Log, "token = " + token);
                    boolean firstopen = appSharedPreferences.readBoolean(Constants.firstopen);
                    if (!firstopen) {
                        Log.d(Constants.Log, "is first open");
                        createorUpdateUser(1, 0, token);
                        appSharedPreferences.writeBoolean(Constants.firstopen, true);
                        favoriteDialog(FromNotification.this);
                    } else {
                        Log.d(Constants.Log, "is not first open");
                        int id = appSharedPreferences.readInteger(Constants.userid);
                        Log.d(Constants.Log, "user id = " + id + "");
                        createorUpdateUser(2, id, token);
                    }
                });
    }

    private void setNewLocale() {
        App.localeManager.setNewLocale(this, "ar");

        Intent i = new Intent(this, FromNotification.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

//            System.exit(0);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void favoriteDialog(Context context) {
        final Dialog dialog1 = new Dialog(context, R.style.AppTheme);
        dialog1.show();
        dialog1.setContentView(R.layout.favorite_dialog_style);
        dialog1.setCancelable(false);
        hideStatus((Activity) context);

        // with out background
        Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

        constraintLayout.setOnTouchListener((v, event) -> {
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
        });
    }

    public void bottomSheet() {
        try {
            sheetBehavior = BottomSheetBehavior.from(bottomSheetView);
            sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @SuppressLint("InflateParams")
    public void drawerIcon(int icon) {

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(icon);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(false);

        mDrawerToggle.setToolbarNavigationClickListener(v -> {

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

                    /*menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
                    badgeandCheckedDrawer();
                    changeToolbarBackground(R.color.white);
                    drawerIcon(R.drawable.ic_menu_dark);*/
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
//                        menuBackIcon(R.menu.toolbar_back, R.string.search, "", 0);
                        menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
                        FROM_BACK = "";
                    }
                    badgeandCheckedDrawer();
                    changeToolbarBackground(R.drawable.back_toolbar);
                    drawerIcon(R.drawable.ic_menu);
                } else if (back.equalsIgnoreCase(Constants.scorersF)) {
                    getSupportFragmentManager().popBackStack(null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE);//Clear from back stack
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, new MainFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
        FragmentTransaction fragmentTransaction;
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
//                newFacebookIntent(this.getPackageManager(), "https://www.facebook.com/ramyhdsport/");
                startActivity(getOpenFacebookIntent(FromNotification.this, 1));
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_twitter:
                startActivity(getOpenFacebookIntent(FromNotification.this, 2));
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_logout:
                finish();
                drawerLayout.closeDrawers();
                break;
        }

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    /*public static Intent newFacebookIntent(PackageManager pm, String url) {
            Uri uri = Uri.parse(url);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
                if (applicationInfo.enabled) {
                    // http://stackoverflow.com/a/24547437/1048340
                    uri = Uri.parse("fb://facewebmodal/f?href=" + url);
                }
            } catch (PackageManager.NameNotFoundException ignored) {
            }
            return new Intent(Intent.ACTION_VIEW, uri);
        }*/
    public Intent getOpenFacebookIntent(Context context, int type) {
        if (type == 1) {//facebook
            try {
                context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/105599864402019"));
            } catch (Exception e) {
                return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ramyhdsport/"));
            }
        } else {//type == 2 >>> twitter
            try {
                // get the Twitter app if possible
                context.getPackageManager().getPackageInfo("com.twitter.android", 0);
                return new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=1036944848765497345"))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                // no Twitter app, revert to browser
                return new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ramyhdspoort"));
            }
        }
    }

    public void newsCreatedAt(String newsCreatedAtValue){
        TextView toolbarCreatedDate = findViewById(R.id.createddatevalue);
        toolbarCreatedDate.setText(newsCreatedAtValue);
    }

    public void menuBackIcon(int menu, int resStringId, String leagueName, int ramiMain) {
        ConstraintLayout constraintLayout = findViewById(R.id.cl);
        ConstraintLayout cLNewsDetails = findViewById(R.id.clnewsdetails);
        ConstraintLayout clshare = findViewById(R.id.clshare);
        TextView toolbarAppTitle = findViewById(R.id.apptitle);
        TextView toolbarAppTitle2 = findViewById(R.id.apptitle2);
        TextView toolbarCreatedDate = findViewById(R.id.createddatevalue);
//        TextView toolbarshare = findViewById(R.id.share);
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
//                toolbarCreatedDate.setText(resStringId);
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
        assert call != null;
        call.enqueue(new Callback<UserResp>() {
            @Override
            public void onResponse(@NotNull Call<UserResp> call, @NotNull Response<UserResp> response) {

                Log.d(Constants.Log, " | Code = " + response.body());
                UserResp resource = response.body();
                if (response.code() == 201) {
                    String code = response.code() + "";
                    assert resource != null;
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
            public void onFailure(@NotNull Call<UserResp> call, @NotNull Throwable t) {
                Log.d(Constants.Log, "onFailure = " + t.getMessage());
                call.cancel();
            }
        });
    }

}
